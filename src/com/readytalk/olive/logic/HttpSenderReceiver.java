package com.readytalk.olive.logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

// Modified from: http://www.exampledepot.com/egs/java.net/post.html
public class HttpSenderReceiver {
	public static String sendReceive(String data, URL url) {
		String response = "";
		try {
			// Send data
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
					conn.getOutputStream());
			outputStreamWriter.write(data);
			outputStreamWriter.flush();

			// Get the response
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				response += line + System.getProperty("line.separator");
			}
			outputStreamWriter.close();
			bufferedReader.close();
		} catch (IOException e) {
			// TODO WARNING: Readers and writers may not have been closed!
			System.err.println("Error communicating with URL: "
					+ url.toString());
			e.printStackTrace();
		}
		return response;
	}

	public static void split() {
		try {
			String response = sendReceive(getJson(), new URL(getZencoderUrl()));
			System.out.println(response); // Do this for now.
		} catch (MalformedURLException e) {
			System.err.println(getZencoderUrl());
			e.printStackTrace();
		}
	}

	private static String getJson() {
		// Hard-coded for now.
		String data = "{\"api_key\": \""
				+ ZencoderCredentials.getZencoderApiKey()
				+ "\",\"input\": \"https://s3.amazonaws.com/test-bucket-Olive/Wildlife.wmv\","
				+ "\"output\": [{\"base_url\": \"https://s3.amazonaws.com/test-bucket-Olive/\","
				+ "\"filename\": \"Wildlife0-3.wmv\"," + "\"start_clip\": 6,"
				+ "\"clip_length\": 2}]}";
		System.out.println(data); // Do this for now.
		return data;
	}

	private static String getZencoderUrl() {
		return "https://app.zencoder.com/api/jobs";
	}
}
