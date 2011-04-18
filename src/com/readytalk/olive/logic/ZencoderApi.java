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
import com.readytalk.olive.json.ZencoderInitialResponse;
import com.readytalk.olive.model.Video;

/**
 * Provides tool to use Zencoder from Olive
 * 
 * @author Team Olive
 * 
 */
// Modified from: http://www.exampledepot.com/egs/java.net/post.html
public class ZencoderApi {
	private static final String ZENCODER_API_JOBS_URL = "https://app.zencoder.com/api/jobs/";
	private static final String ZENCODER_API_OUTPUTS_URL_PREFIX = "https://app.zencoder.com/api/outputs/";
	private static final String NEW_EXTENSION = ".ogv";
	private static final String NEW_VIDEO_CODEC = "theora";
	private static final String NEW_AUDIO_CODEC = "vorbis";
	private static final String THUMB_FORMAT = "jpg";
	private static final String THUMB_SUFFIX_WITH_DOT = "_0000.";
	private static final int NUMBER_OF_THUMBS = 1;
	private static final int FRAMES_PER_SECOND = 30;
	private static final int AUDIO_CHANNELS = 2; // stereo
	private static final int THUMB_PUBLICITY = 1; // true; public
	private static final int VIDEO_PUBLICITY = 1; // true; public

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
			String filename, int numberOfThumbs, double startClip,
			double clipLength, String thumbBaseUrl, String thumbPrefix,
			String thumbFormat, int thumbPublicity, int videoPublicity) {
		String data = "{\"api_key\":\"" + DatabaseApi.getZencoderApiKey()
				+ "\",\"input\":\"" + input + "\","
				+ "\"output\":[{\"base_url\":\"" + baseUrl + "\","
				+ "\"filename\":\"" + filename + "\"," + "\"thumbnails\":{"
				+ "\"number\":" + numberOfThumbs + "," + "\"base_url\":\""
				+ thumbBaseUrl + "\"," + "\"prefix\":\"" + thumbPrefix + "\","
				+ "\"format\":\"" + thumbFormat + "\"," + "\"public\":"
				+ thumbPublicity + "}," + "\"public\":" + videoPublicity + ","
				+ "\"start_clip\":" + startClip + "," + "\"clip_length\":"
				+ clipLength + "}]}";
		return data;
	}

	private static String getJsonForConvertToOgg(String input,
			String videoBaseUrl, String filename, String videoCodec,
			String audioCodec, int numberOfThumbs, String thumbBaseUrl,
			String thumbPrefix, String thumbFormat, int thumbPublicity,
			int framesPerSecond, int audioChannels, int videoPublicity) {
		// Codec and file extension must match.
		String data = "{\"api_key\":\"" + DatabaseApi.getZencoderApiKey()
				+ "\",\"input\":\"" + input + "\","
				+ "\"output\":[{\"base_url\":\"" + videoBaseUrl + "\","
				+ "\"filename\":\"" + filename + "\",\"video_codec\":\""
				+ videoCodec + "\",\"audio_codec\":\"" + audioCodec + "\","
				+ "\"thumbnails\":{" + "\"number\":" + numberOfThumbs + ","
				+ "\"base_url\":\"" + thumbBaseUrl + "\"," + "\"prefix\":\""
				+ thumbPrefix + "\"," + "\"format\":\"" + thumbFormat + "\","
				+ "\"public\":" + thumbPublicity + "" + "},"
				+ "\"frame_rate\":" + framesPerSecond + ","
				+ "\"audio_channels\":" + audioChannels + "," + "\"public\":"
				+ videoPublicity + "}]}";
		return data;
	}

	public static Video[] split(int videoId, double splitTimeInSeconds)
			throws IOException {
		String originalVideoUrl = DatabaseApi.getVideoUrl(videoId);
		String originalVideoName = DatabaseApi.getVideoName(videoId);
		double maximumStartTimeInSeconds = Security.MIN_SPLIT_TIME_IN_SECONDS;
		double minimumEndTimeInSeconds = Security.MAX_SPLIT_TIME_IN_SECONDS;
		double[] splitStartInSeconds = { 0, splitTimeInSeconds };
		double[] clipLengthInSeconds = {
				splitTimeInSeconds - maximumStartTimeInSeconds,
				minimumEndTimeInSeconds - splitTimeInSeconds }; // Draw a picture to understand this.
		String thumbFormat = THUMB_FORMAT;
		Video[] videoFragments = new Video[2];
		String[] responses = new String[2];

		// Request the first and second halves of the original video.
		for (int i = 0; i < 2; ++i) {
			String videoFragmentFileName = S3Api
					.getNameFromUrlWithNewTimeStamp(originalVideoUrl);
			String thumbPrefix = S3Api.getTime(); // The video name can't be included because it has a ".".
			responses[i] = ZencoderApi.sendReceive(ZencoderApi.getJsonForSplit(
					originalVideoUrl, S3Api.AWS_URL_PREFIX,
					videoFragmentFileName, NUMBER_OF_THUMBS,
					splitStartInSeconds[i], clipLengthInSeconds[i],
					S3Api.AWS_URL_PREFIX, thumbPrefix, thumbFormat,
					THUMB_PUBLICITY, VIDEO_PUBLICITY), new URL(
					ZENCODER_API_JOBS_URL));
			String videoIcon = S3Api.AWS_URL_PREFIX + thumbPrefix
					+ THUMB_SUFFIX_WITH_DOT + thumbFormat;
			videoFragments[i] = new Video(originalVideoName,
					S3Api.AWS_URL_PREFIX + videoFragmentFileName, videoIcon,
					-1, -1, -1, false); // Name will be overwritten.
		}

		// Wait for the first and second halves of the original video.
		for (int i = 0; i < 2; ++i) {
			ZencoderInitialResponse zencoderInitialResponse = new Gson()
					.fromJson(responses[i], ZencoderInitialResponse.class);
			waitForJobToFinish(zencoderInitialResponse.outputs[0].id);
		}

		return videoFragments;
	}

	public static String[] convertToOgg(String videoUrl) throws IOException {
		String convertedVideoFileName = S3Api
				.getNameFromUrlWithNewTimeStamp(videoUrl) + NEW_EXTENSION;
		String thumbPrefix = S3Api.getTime(); // The video name can't be included because it has a ".".
		String response = ZencoderApi.sendReceive(
				getJsonForConvertToOgg(videoUrl, S3Api.AWS_URL_PREFIX,
						convertedVideoFileName, NEW_VIDEO_CODEC,
						NEW_AUDIO_CODEC, NUMBER_OF_THUMBS,
						S3Api.AWS_URL_PREFIX, thumbPrefix, THUMB_FORMAT,
						THUMB_PUBLICITY, FRAMES_PER_SECOND, AUDIO_CHANNELS,
						VIDEO_PUBLICITY), new URL(ZENCODER_API_JOBS_URL));
		ZencoderInitialResponse zencoderInitialResponse = new Gson().fromJson(
				response, ZencoderInitialResponse.class);
		waitForJobToFinish(zencoderInitialResponse.outputs[0].id);

		String[] videoUrlAndIcon = new String[] {
				S3Api.AWS_URL_PREFIX + convertedVideoFileName,
				S3Api.AWS_URL_PREFIX + thumbPrefix + THUMB_SUFFIX_WITH_DOT
						+ THUMB_FORMAT };
		return videoUrlAndIcon;
	}
}
