package com.readytalk.olive.logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.google.gson.Gson;
import com.readytalk.olive.json.GeneralRequest;
import com.readytalk.olive.json.ZencoderInitialResponse;
import com.readytalk.olive.model.Video;
/**
 * Provides tool to use Zencoder from Olive 
 * @author Team Olive
 *
 */
// Modified from: http://www.exampledepot.com/egs/java.net/post.html
public class ZencoderApi {
	private static final String ZENCODER_API_JOBS_URL = "https://app.zencoder.com/api/jobs/";
	private static final String ZENCODER_API_OUTPUTS_URL_PREFIX = "https://app.zencoder.com/api/outputs/";
	/**
	 * 
	 * @param outputId
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	// Don't use id. See: http://zencoder.com/docs/api/#status
	private static void waitForJobToFinish(int outputId)
			throws MalformedURLException, IOException {
		String zencoderOutputsUrl = ZENCODER_API_OUTPUTS_URL_PREFIX + outputId
				+ "/progress?api_key=" + DatabaseApi.getZencoderApiKey();

		// Example responses (in order):
		// {"current_event":"Transcoding","progress":"100.0","state":"processing"}
		// {"current_event":"Uploading","progress":"100.0","state":"processing"}
		// {"current_event":"Uploading","state":"finished"}

		while (!doGet(zencoderOutputsUrl).contains("\"state\":\"finished\"")) {
			System.out.println("Job " + outputId + " not done yet.");
			try {
				Thread.sleep(1000); // 1 second
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// Modified from: http://download.oracle.com/javase/tutorial/networking/urls/readingWriting.html
	private static String getResponse(InputStream inputStream)
			throws IOException {
		// Get the response
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line;
		String response = "";
		while ((line = bufferedReader.readLine()) != null) {
			response += line;
		}
		bufferedReader.close();
		System.out.println(response);
		return response;
	}

	public static String doGet(String url) throws IOException {
		return getResponse((new URL(url)).openConnection().getInputStream());
	}

	// Even though this method lives in the ZencoderApi class, it is generalized
	// for any type of POST request.
	// Modified from: http://download.oracle.com/javase/tutorial/networking/urls/readingWriting.html
	public static String sendReceive(String data, URL url) throws IOException {
		// Send data
		System.out.println("Sending data to Zencoder...");
		URLConnection conn = url.openConnection();
		conn.setDoOutput(true);
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
				conn.getOutputStream());
		outputStreamWriter.write(data);
		outputStreamWriter.flush();
		System.out.println("Data sent to Zencoder.");
		outputStreamWriter.close();
		
		return getResponse(conn.getInputStream());
	}

	private static String getJsonForSplit(String input, String baseUrl,
			String filename, double startClip, double clipLength) {
		String data = "{\"api_key\":\"" + DatabaseApi.getZencoderApiKey()
				+ "\",\"input\":\"" + input + "\","
				+ "\"output\":[{\"base_url\":\"" + baseUrl + "\","
				+ "\"filename\":\"" + filename + "\",\"public\":1,"
				+ "\"start_clip\":" + startClip + "," + "\"clip_length\":"
				+ clipLength + "}]}";
		System.out.println(data);
		return data;
	}

	private static String getJsonForConvertToOgg(String input, String baseUrl,
			String filename, String videoCodec, String audioCodec) {
		// Codec and file extension must match.
		String data = "{\"api_key\":\"" + DatabaseApi.getZencoderApiKey()
				+ "\",\"input\":\"" + input + "\","
				+ "\"output\":[{\"base_url\":\"" + baseUrl + "\","
				+ "\"filename\":\"" + filename + "\",\"video_codec\":\""
				+ videoCodec + "\",\"audio_codec\":\"" + audioCodec
				+ "\",\"public\":1" + "}]}";
		System.out.println(data);
		return data;
	}

	public static Video[] split(int videoId, double splitTimeInSeconds)
			throws IOException {
		String originalVideoUrl = DatabaseApi.getVideoUrl(videoId);
		String awsBaseUrl = S3Api.AWS_URL_PREFIX;
		double maximumStartTimeInSeconds = Security.MIN_SPLIT_TIME_IN_SECONDS;
		double minimumEndTimeInSeconds = Security.MAX_SPLIT_TIME_IN_SECONDS;
		double[] splitStartInSeconds = { 0, splitTimeInSeconds };
		double[] clipLengthInSeconds = {
				splitTimeInSeconds - maximumStartTimeInSeconds,
				minimumEndTimeInSeconds - splitTimeInSeconds }; // Draw a picture to understand this.
		Video[] videoFragments = new Video[2];
		String[] responses = new String[2];
		
		// Request the first and second halves of the original video.
		for (int i = 0; i < 2; ++i) {
			String videoFragmentFileName = S3Api
					.getNameFromUrlWithNewTimeStamp(originalVideoUrl);
			responses[i] = ZencoderApi.sendReceive(ZencoderApi.getJsonForSplit(
					originalVideoUrl, awsBaseUrl, videoFragmentFileName,
					splitStartInSeconds[i], clipLengthInSeconds[i]), new URL(
					ZENCODER_API_JOBS_URL));
			videoFragments[i] = new Video(
					DatabaseApi.getVideoName(videoId) + i, S3Api.AWS_URL_PREFIX
							+ videoFragmentFileName,
					"/olive/images/bbb480.jpg", -1, -1, -1, false); // TODO Get icon from Zencoder
		}
		
		// Wait for the first and second halves of the original video.
		for (int i = 0; i < 2; ++i) {
			ZencoderInitialResponse zencoderInitialResponse = new Gson().fromJson(
					responses[i], ZencoderInitialResponse.class);
			waitForJobToFinish(zencoderInitialResponse.outputs[0].id);
		}

		return videoFragments;
	}

	public static String convertToOgg(String videoUrl) throws IOException {
		String awsBaseUrl = S3Api.AWS_URL_PREFIX;
		String newExtension = ".ogv";
		String convertedVideoFileName = S3Api
				.getNameFromUrlWithNewTimeStamp(videoUrl) + newExtension;
		String newVideoCodec = "theora";
		String newAudioCodec = "vorbis";

		String response = ZencoderApi.sendReceive(
				getJsonForConvertToOgg(videoUrl, awsBaseUrl,
						convertedVideoFileName, newVideoCodec, newAudioCodec),
				new URL(ZENCODER_API_JOBS_URL));
		ZencoderInitialResponse zencoderInitialResponse = new Gson().fromJson(
				response, ZencoderInitialResponse.class);
		waitForJobToFinish(zencoderInitialResponse.outputs[0].id);
		
		return S3Api.AWS_URL_PREFIX + convertedVideoFileName;
	}
}
