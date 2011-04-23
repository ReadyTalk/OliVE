package com.readytalk.olive.logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;

import org.jets3t.service.ServiceException;

import com.readytalk.olive.util.InvalidFileSizeException;

public class Combiner {
	public static String convertTo(String format, File video, File tempDir)
			throws IOException {
		String cmd = "ffmpeg -i ";
		if (isWindows()) {
			cmd = "cmd /c " + cmd;
		}
		String name = video.getName();
		String newName = name.substring(0, name.length() - 4) + "-converted."
				+ format;
		cmd = cmd + name + " -sameq " + newName;
		Runtime r = Runtime.getRuntime();
		Process p = r.exec(cmd, null, tempDir);
		BufferedReader in = new BufferedReader(new InputStreamReader(
				p.getErrorStream()));
		String s;
		while ((s = in.readLine()) != null) {
			s.trim();
		}
		video.delete();
		return newName;
	}

	public static String combineVideos(String[] videoURLs, String[] videos,
			File tempDir) throws IOException, NoSuchAlgorithmException,
			InvalidFileSizeException, ServiceException, InterruptedException {
		Runtime r = Runtime.getRuntime();
		boolean isWindows = isWindows();
		// boolean isLinux = isLinux();
		String cmd = "mencoder -ovc lavc -oac mp3lame ";
		if (isWindows) {
			cmd = "cmd /c " + cmd;
		}
		File[] temp = new File[videoURLs.length];
		File[] tempToDel = new File[videoURLs.length];

		String tempName;
		int[] biggestDims = getBiggestDimensions(videoURLs, tempDir);
		for (int i = 0; i < videoURLs.length; i++) {
			temp[i] = new File(videoURLs[i]);
			temp[i] = new File(tempDir + "/" + temp[i].getName());
			tempName = adjustDimensions(temp[i], biggestDims, tempDir);
			tempToDel[i] = new File(tempDir + "/" + tempName);
			cmd = cmd + tempName + " ";
		}
		String combinedName = S3Api.getTime() + "-combined.ogv";
		cmd = cmd + "-o " + combinedName;
		final Process p = r.exec(cmd, null, tempDir);
		new Thread() {
			public void run() {
				BufferedReader in = new BufferedReader(new InputStreamReader(
						p.getInputStream()));

				String s;
				try {
					while ((s = in.readLine()) != null) {
						s.trim();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
		String s;
		BufferedReader in = new BufferedReader(new InputStreamReader(
				p.getErrorStream()));
		while ((s = in.readLine()) != null) {
			s.trim();
		}
		File combined = new File(tempDir + "/" + combinedName);
		for (int j = 0; j < tempToDel.length; j++) {
			tempToDel[j].delete();
		}
		return combined.getAbsolutePath();
	}

	public static int[] getBiggestDimensions(String[] videos, File tempDir)
			throws IOException {
		int[] biggestDimensions = { 0, 0 };
		File temp;
		int[] dimensions;
		for (int i = 0; i < videos.length; i++) {
			S3Api.downloadVideosToTemp(videos[i]);
			temp = new File(videos[i]);
			dimensions = getDimensions(temp, tempDir);
			if (dimensions[0] > biggestDimensions[0]
					&& dimensions[1] > biggestDimensions[1]) {
				biggestDimensions = dimensions;
			}
		}
		return biggestDimensions;
	}

	public static int[] getDimensions(File video, File tempDir)
			throws IOException {
		Runtime r = Runtime.getRuntime();
		String[] arrV = new String[1];
		String cmd = "ffmpeg -i ";
		if (isWindows()) {
			cmd = "cmd /c " + cmd;
		}
		cmd = cmd + video.getName();
		Process p = r.exec(cmd, null, tempDir);
		BufferedReader in = new BufferedReader(new InputStreamReader(
				p.getErrorStream()));
		String s;
		while ((s = in.readLine()) != null) {
			if (s.contains("Video:")) {
				arrV = s.split(",");
			}
		}
		String tempDim = arrV[2].trim();
		if (tempDim.indexOf(" ") != -1) {
			tempDim = tempDim.substring(0, tempDim.indexOf(" "));
		}
		String[] dimsArr = tempDim.split("x");
		int width = (new Integer(dimsArr[0])).intValue();
		int height = (new Integer(dimsArr[1])).intValue();
		int[] ret = { width, height };
		return ret;
	}

	public static String adjustDimensions(File video, int[] biggestDimensions,
			File tempDir) throws IOException {
		int[] dimensions = getDimensions(video, tempDir);
		int width = dimensions[0];
		int height = dimensions[1];
		int bigWidth = biggestDimensions[0];
		int bigHeight = biggestDimensions[1];
		if (bigWidth == width && bigHeight == height) {
			return video.getName();
		}
		int padw1 = (bigWidth - width) / 2;
		int padh1 = (bigHeight - height) / 2;
		String cmdPre = "ffmpeg -i ";
		if (isWindows()) {
			cmdPre = "cmd /c " + cmdPre;
		}
		Runtime r = Runtime.getRuntime();
		String videoName = video.getName();
		String newName1 = videoName.substring(0, videoName.length() - 4)
				+ "-fixed.ogv";
		String cmd = cmdPre + videoName + " -vf pad=" + bigWidth + ":"
				+ bigHeight + ":" + padw1 + ":" + padh1 + ":black " + newName1;
		Process p = r.exec(cmd, null, tempDir);
		// String s;
		BufferedReader in = new BufferedReader(new InputStreamReader(
				p.getErrorStream()));
		String s;
		// This is where the code seems to get hung up
		while ((s = in.readLine()) != null) {
			s.trim();
		}
		video.delete();
		return newName1;
	}

	// http://www.mkyong.com/java/how-to-detect-os-in-java-systemgetpropertyosname/
	private static Boolean isWindows() {
		String os = System.getProperty("os.name").toLowerCase();
		// windows
		return (os.indexOf("win") >= 0);
	}

	// private static Boolean isLinux() {
	// String os = System.getProperty("os.name").toLowerCase();
	// // linux or unix
	// return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0);
	// }

}