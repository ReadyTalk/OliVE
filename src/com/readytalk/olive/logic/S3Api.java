package com.readytalk.olive.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Logger;

import org.jets3t.service.S3ServiceException;
import org.jets3t.service.ServiceException;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.S3Object;
import org.jets3t.service.security.AWSCredentials;

import com.google.gson.Gson;
import com.readytalk.olive.model.Video;
import com.readytalk.olive.servlet.OliveServlet;
import com.readytalk.olive.util.InvalidFileSizeException;

// Java code samples: https://bitbucket.org/jmurty/jets3t/src/Release-0_8_0/src/org/jets3t/samples/CodeSamples.java
// HTML code samples: http://jets3t.s3.amazonaws.com/toolkit/code-samples.html#downloading
// JavaDocs: http://jets3t.s3.amazonaws.com/api/org/jets3t/service/model/StorageObject.html
public class S3Api {
	private static final String BUCKET_NAME = "test-bucket-Olive";
	public static final String AWS_URL_PREFIX = "https://s3.amazonaws.com/"
			+ BUCKET_NAME + "/";
	private static final long MAX_SIZE_IN_BYTES = 31457280L; // 30 MB
	private static final long MIN_SIZE_IN_BYTES = 1L; // ~0 MB
	private static final String DATE_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS";
	private static final int BUFFER_SIZE = 100000; // 100 KB
	private static final byte[] buffer = new byte[BUFFER_SIZE];
	private static Logger log = Logger.getLogger(S3Api.class.getName());

	private static RestS3Service getS3Service() throws S3ServiceException {
		AWSCredentials awsCredentials = OliveDatabaseApi.getAwsCredentials();

		// RestS3Service is similar to HttpClient
		RestS3Service s3Service = new RestS3Service(awsCredentials);
		return s3Service;
	}

	public static String getTime() {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
		return simpleDateFormat.format(calendar.getTime());
	}

	public static String uploadFile(File file) throws InvalidFileSizeException,
			IOException, ServiceException {
		if (file.length() > MAX_SIZE_IN_BYTES) {
			throw new InvalidFileSizeException("File larger than "
					+ MAX_SIZE_IN_BYTES + " bytes");
		}
		if (file.length() < MIN_SIZE_IN_BYTES) {
			throw new InvalidFileSizeException("File smaller than "
					+ MIN_SIZE_IN_BYTES + " bytes");
		}

		S3Object fileAsS3Object = null;
		try {
			RestS3Service s3Service = getS3Service();

			// Set Content-Length automatically based on the file's extension.
			fileAsS3Object = new S3Object(file);

			String fileNameOnS3 = S3Api.getTime() + "-" + file.getName(); // TODO Make sure this isn't too big.

			fileAsS3Object.setName(fileNameOnS3);

			// Grant Zencoder the same permissions on this object as in the
			// bucket the object will be placed in.
			fileAsS3Object.setAcl(s3Service.getBucketAcl(BUCKET_NAME));

			// Upload the data object.
			s3Service.putObject(BUCKET_NAME, fileAsS3Object);

			String videoUrl = AWS_URL_PREFIX + fileNameOnS3;

			return videoUrl;
		} catch (IOException e) {
			log.severe("Error connecting with S3");
			e.printStackTrace();
		} catch (S3ServiceException e) {
			log.severe("Error creating RestS3Service object");
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			log.severe("Error creating S3Object object");
			e.printStackTrace();
		} finally {
			fileAsS3Object.closeDataInputStream();
		}
		return null; // Error
	}

	public static File getFileFromS3(String videoUrl) throws IOException {
		String fileName = getNameFromUrl(videoUrl);
		S3Object s3Object = null;
		try {
			RestS3Service s3Service = getS3Service();

			s3Object = s3Service.getObject(BUCKET_NAME, fileName);

			return getFileFromInputStream(s3Object.getDataInputStream(),
					fileName);
		} catch (S3ServiceException e) {
			log.severe("Error accessing object \"" + fileName
					+ "\" in S3 bucket \"" + BUCKET_NAME + "\"");
			e.printStackTrace();
		} catch (ServiceException e) {
			log.severe("Error accessing S3Object input stream for file \""
					+ fileName + "\" in S3 bucket \"" + BUCKET_NAME + "\"");
			e.printStackTrace();
		} finally {
			s3Object.closeDataInputStream();
		}
		log.severe("The function downloadFile returned null instead of a file");
		return null; // Error
	}

	public static String getNameFromUrl(String videoUrl) {
		return videoUrl.substring(AWS_URL_PREFIX.length());
	}

	public static String getNameFromUrlWithNewTimeStamp(String videoUrl) {
		return S3Api.getTime()
				+ getNameFromUrl(videoUrl).substring(S3Api.getTime().length()); // TODO Fix fugly code
	}

	private static File getFileFromInputStream(InputStream inputStream,
			String name) throws FileNotFoundException, IOException {
		File file = new File(name);
		OutputStream out = new FileOutputStream(file);
		byte buffer[] = new byte[1024];
		int bufferLength;
		while ((bufferLength = inputStream.read(buffer)) > 0) {
			out.write(buffer, 0, bufferLength);
		}
		out.close();
		inputStream.close();
		return file;
	}

	// Modified from: http://msdn.microsoft.com/en-us/library/aa478985.aspx
	public static String downloadVideosToTemp(int projectId) throws IOException {
		int[] videoIds = OliveDatabaseApi.getVideoIds(projectId);
		File tempDir = OliveServlet.tempDir;
		Video[] videos = new Video[videoIds.length];
		log.info("Downloading " + videoIds.length
				+ " file(s) from S3 (may take a while)...");
		log.info("Download directory: " + tempDir);
		for (int videoIndex = 0; videoIndex < videoIds.length; ++videoIndex) {
			String videoUrl = OliveDatabaseApi
					.getVideoUrl(videoIds[videoIndex]);
			File inFile = S3Api.getFileFromS3(videoUrl);
			File outFile = new File(tempDir, getNameFromUrl(videoUrl));
			outFile.deleteOnExit(); // Delete the file when the JVM exits (cannot be undone).
			S3Api.saveFileToDisk(inFile, outFile);

			String videoName = OliveDatabaseApi
					.getVideoName(videoIds[videoIndex]);
			String videoTempUrl = "/olive" + OliveServlet.TEMP_DIR_PATH
					+ getNameFromUrl(videoUrl);
			String videoTempIcon = OliveDatabaseApi
					.getVideoIcon(videoIds[videoIndex]);
			int startTimeStoryboard = OliveDatabaseApi
					.getVideoStartTimeStoryboard(videoIds[videoIndex]);
			videos[videoIndex] = new Video(videoName, videoTempUrl,
					videoTempIcon, projectId, startTimeStoryboard); // TODO Distinguish these urls from the S3 urls!

			log.info((videoIds.length - 1 - videoIndex)
					+ " file(s) remaining)...");
		}
		log.info("Downloaded " + videoIds.length + " file(s) from S3.");

		return new Gson().toJson(videos);
	}

	// Modified from: http://java.sun.com/docs/books/performance/1st_edition/html/JPIOPerformance.fm.html#11078
	public static void saveFileToDisk(File from, File to) throws IOException {
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(from);
			out = new FileOutputStream(to);
			while (true) {
				synchronized (buffer) {
					int amountRead = in.read(buffer);
					if (amountRead == -1) {
						break;
					}
					out.write(buffer, 0, amountRead);
				}
			}
		} finally {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
		}
	}
}
