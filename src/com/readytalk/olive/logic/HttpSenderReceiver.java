package com.readytalk.olive.logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.readytalk.olive.model.Video;

// Modified from: http://www.exampledepot.com/egs/java.net/post.html
public class HttpSenderReceiver {
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
		try {
			String input = OliveDatabaseApi.getVideoUrl(videoId);
			String baseUrl = S3Api.AWS_URL_PREFIX;
			String[] filenames = new String[2]; // Will later be set before each Zencoder request.
			double minimumTimeInSeconds = 0; // ASSUME: No video is shorter than 0 seconds.
			double maximumTimeInSeconds = 14400; // ASSUME: No video is longer than 4 hours.
			String[] urls = new String[2]; // Will later be set after each Zencoder request.
			Video[] videos = new Video[2]; // Exists only to send multiple return values.

			// Get first half
			filenames[0] = S3Api.getNameFromUrlWithNewTimeStamp(input);
			urls[0] = S3Api.AWS_URL_PREFIX + filenames[0];
			sendReceive(
					getJson(input, baseUrl, filenames[0], minimumTimeInSeconds,
							splitTimeInSeconds), new URL(getZencoderUrl()));
			videos[0] = new Video(filenames[0], urls[0], "", -1, -1);

			// Get second half
			filenames[1] = S3Api.getNameFromUrlWithNewTimeStamp(input);
			urls[1] = S3Api.AWS_URL_PREFIX + filenames[1];
			sendReceive(
					getJson(input, baseUrl, filenames[1], splitTimeInSeconds,
							maximumTimeInSeconds), new URL(getZencoderUrl()));
			videos[1] = new Video(filenames[1], urls[1], "", -1, -1);

			return videos;
		} catch (MalformedURLException e) {
			System.err.println("Zencoder URL: " + getZencoderUrl());
			e.printStackTrace();
		}
		return null;
	}

	private static String getJson(String input, String baseUrl,
			String filename, double startClip, double clipLength) {
		// Hard-coded for now.
		String data = "{\"api_key\": \"" + OliveDatabaseApi.getZencoderApiKey()
				+ "\",\"input\": \"" + input + "\","
				+ "\"output\": [{\"base_url\": \"" + baseUrl + "\","
				+ "\"filename\": \"" + filename + "\"," + "\"start_clip\": "
				+ startClip + "," + "\"clip_length\": " + clipLength + "}]}";
		System.out.println(data);
		return data;
	}

	private static String getZencoderUrl() {
		return "https://app.zencoder.com/api/jobs";
	}
}
