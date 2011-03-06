package com.readytalk.olive.logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import com.readytalk.olive.model.Video;

// Modified from: http://www.exampledepot.com/egs/java.net/post.html
public class ZencoderApi {
	private static String getZencoderUrl() {
		return "https://app.zencoder.com/api/jobs";
	}

	// Even though this method lives in the ZencoderApi class, it is generalized
	// for any type of POST request.
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

		// Get the response
		System.out.println("Receiving data from Zencoder...");
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(conn.getInputStream()));
		String line;
		String response = "";
		while ((line = bufferedReader.readLine()) != null) {
			response += line + System.getProperty("line.separator");
		}
		outputStreamWriter.close();
		bufferedReader.close();
		System.out.println(response);
		System.out.println("Data received from Zencoder.");

		return response;
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

		// Request the first and second halves of the original video.
		for (int i = 0; i < 2; ++i) {
			String videoFragmentFileName = S3Api
					.getNameFromUrlWithNewTimeStamp(originalVideoUrl);
			ZencoderApi.sendReceive(ZencoderApi.getJsonForSplit(
					originalVideoUrl, awsBaseUrl, videoFragmentFileName,
					splitStartInSeconds[i], clipLengthInSeconds[i]), new URL(
					getZencoderUrl()));
			videoFragments[i] = new Video(
					DatabaseApi.getVideoName(videoId) + i, S3Api.AWS_URL_PREFIX
							+ videoFragmentFileName,
					"/olive/images/bbb480.jpg", -1, -1); // TODO Get icon from Zencoder
		}

		return videoFragments;
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

	public static String convertToOgg(String videoUrl) throws IOException {
		String awsBaseUrl = S3Api.AWS_URL_PREFIX;
		String newExtension = ".ogv";
		String convertedVideoFileName = S3Api
				.getNameFromUrlWithNewTimeStamp(videoUrl) + newExtension;
		String newVideoCodec = "theora";
		String newAudioCodec = "vorbis";

		ZencoderApi.sendReceive(
				getJsonForConvertToOgg(videoUrl, awsBaseUrl,
						convertedVideoFileName, newVideoCodec, newAudioCodec),
				new URL(getZencoderUrl()));
		return S3Api.AWS_URL_PREFIX + convertedVideoFileName;
	}
}
