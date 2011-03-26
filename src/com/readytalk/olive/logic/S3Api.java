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
import org.jets3t.service.acl.AccessControlList;
import org.jets3t.service.acl.EmailAddressGrantee;
import org.jets3t.service.acl.GroupGrantee;
import org.jets3t.service.acl.Permission;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.S3Object;
import org.jets3t.service.security.AWSCredentials;

import com.google.gson.Gson;
import com.readytalk.olive.model.Project;
import com.readytalk.olive.model.Video;
import com.readytalk.olive.servlet.OliveServlet;
import com.readytalk.olive.util.InvalidFileSizeException;

/**
 * class S3Api provides tool to connect to S3 from Olive
 * 
 * @author Team Olive
 * 
 */
// JetS3t code samples (in Java): https://bitbucket.org/jmurty/jets3t/src/Release-0_8_0/src/org/jets3t/samples/CodeSamples.java
// JetS3t code samples (in HTML): http://jets3t.s3.amazonaws.com/toolkit/code-samples.html#downloading
// JetS3t JavaDocs: http://jets3t.s3.amazonaws.com/api/org/jets3t/service/model/StorageObject.html
public class S3Api {
	private static final String BUCKET_NAME = "test-bucket-Olive";
	public static final String AWS_URL_PREFIX = "https://s3.amazonaws.com/"
			+ BUCKET_NAME + "/";
	private static final String ZENCODER_AWS_EMAIL = "aws@zencoder.com";
	private static final long MAX_SIZE_IN_BYTES = 31457280L; // 30 MB
	private static final long MIN_SIZE_IN_BYTES = 1L; // ~0 MB
	private static final String DATE_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS";
	private static Logger log = Logger.getLogger(S3Api.class.getName());
	private static final int BUFFER_SIZE = 100000; // 100 KB
	private static final byte[] buffer = new byte[BUFFER_SIZE];

	/**
	 * Constructs the service and initializes the properties.
	 * 
	 * @return initialized properties
	 * @throws S3ServiceException
	 *             Exception for use by S3Services and related utilities. This exception can hold useful additional information about errors that occur when communicating with S3.
	 */
	private static RestS3Service getS3Service() throws S3ServiceException {
		AWSCredentials awsCredentials = DatabaseApi.getAwsCredentials();

		// RestS3Service is similar to HttpClient
		RestS3Service s3Service = new RestS3Service(awsCredentials);
		return s3Service;
	}

	/**
	 * Gets a calendar using the default time zone and locale
	 * 
	 * @return simple data format of the calendar
	 */
	// TODO Make this a hash function that uses the time
	public static String getTime() {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
		return simpleDateFormat.format(calendar.getTime());
	}

	/**
	 * Enables to upload a file
	 * 
	 * @param file
	 *            video file
	 * @return a converted file's location
	 * @throws InvalidFileSizeException
	 *             Exception for limiting the size of the video file
	 * @throws IOException
	 *             Signals that an I/O exception of some sort has occurred.
	 * @throws ServiceException
	 *             Exception for use by S3Services and related utilities. This exception can hold useful additional information about errors that occur when communicating with S3.
	 * @throws NoSuchAlgorithmException
	 *             This exception is thrown when a particular cryptographic algorithm is requested but is not available in the environment.
	 */
	public static String[] uploadFile(File file)
			throws InvalidFileSizeException, IOException, ServiceException,
			NoSuchAlgorithmException {
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

			String fileNameOnS3 = S3Api.getTime() + "-" + file.getName(); // TODO Make sure this isn't too long.

			fileAsS3Object.setName(fileNameOnS3);

			// Extend the permissions already set by the bucket.
			AccessControlList acl = s3Service.getBucketAcl(BUCKET_NAME);
			acl.grantPermission(new EmailAddressGrantee(ZENCODER_AWS_EMAIL),
					Permission.PERMISSION_FULL_CONTROL);
			acl.grantPermission(GroupGrantee.ALL_USERS,
					Permission.PERMISSION_READ);
			fileAsS3Object.setAcl(acl);

			// Upload the data object.
			s3Service.putObject(BUCKET_NAME, fileAsS3Object);

			String unconvertedVideoUrl = AWS_URL_PREFIX + fileNameOnS3;

			String[] videoUrlAndIcon = ZencoderApi
					.convertToOgg(unconvertedVideoUrl);

			return videoUrlAndIcon;
		} catch (IOException e) {
			log.severe("Error connecting with S3");
			e.printStackTrace();
			throw new IOException(e.getMessage());
		} catch (S3ServiceException e) {
			log.severe("Error creating RestS3Service object");
			e.printStackTrace();
			throw new S3ServiceException(e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			log.severe("Error creating S3Object object");
			e.printStackTrace();
			throw new NoSuchAlgorithmException(e.getMessage());
		} finally {
			fileAsS3Object.closeDataInputStream();
		}
	}

	/**
	 * Enables to delete the file in S3
	 * 
	 * @param fileName
	 *            name of the file
	 */
	public static void deleteFileInS3(String fileName) {
		log.severe("deleteFileInS3 has not yet been implemented.");
		// Do something like this:
		// RestS3Service s3Service = getS3Service();
		// s3Service.deleteObject(BUCKET_NAME, objectKey); // TODO Store objectKey in the database
	}

	/**
	 * gets the URL address of S3 where video is located
	 * 
	 * @param videoUrl
	 *            where video is located in S3
	 * @return
	 */
	public static String getNameFromUrl(String videoUrl) {
		return videoUrl.substring(AWS_URL_PREFIX.length());
	}

	/**
	 * Gets the time stamped on the URL where video is located
	 * 
	 * @param videoUrl
	 *            where video is located in S3
	 * @return the time stamped URL
	 */
	public static String getNameFromUrlWithNewTimeStamp(String videoUrl) {
		return S3Api.getTime()
				+ getNameFromUrl(videoUrl).substring(S3Api.getTime().length()); // TODO Fix fugly code
	}

	/**
	 * Gets the project properties
	 * 
	 * @param accountId
	 *            unique number given to an user
	 * @return project properties in forms of Json
	 */
	public static String getProjectInformation(int accountId) {
		int[] projectIds = DatabaseApi.getProjectIds(accountId);
		Project[] projects = new Project[projectIds.length];

		for (int projectIndex = 0; projectIndex < projectIds.length; ++projectIndex) {
			String projectName = DatabaseApi
					.getProjectName(projectIds[projectIndex]);
			String projectIcon = DatabaseApi
					.getProjectIcon(projectIds[projectIndex]);
			int poolPosition = DatabaseApi
					.getProjectPoolPosition(projectIds[projectIndex]);

			projects[projectIndex] = new Project(projectName, accountId,
					projectIcon, poolPosition);
		}

		return new Gson().toJson(projects);
	}

	/**
	 * Gets the video properties
	 * 
	 * @param projectId
	 *            unique number given to a project
	 * @return video properties in a Json form
	 */
	public static String getVideoInformation(int projectId) {
		int[] videoIds = DatabaseApi.getVideoIds(projectId);
		Video[] videos = new Video[videoIds.length];

		for (int videoIndex = 0; videoIndex < videoIds.length; ++videoIndex) {
			String videoName = DatabaseApi.getVideoName(videoIds[videoIndex]);
			String videoUrl = DatabaseApi.getVideoUrl(videoIds[videoIndex]);
			String videoIcon = DatabaseApi.getVideoIcon(videoIds[videoIndex]);
			int poolPosition = DatabaseApi
					.getVideoPoolPosition(videoIds[videoIndex]);
			int timelinePosition = DatabaseApi
					.getVideoTimelinePosition(videoIds[videoIndex]);
			boolean isSelected = DatabaseApi
					.getVideoIsSelected(videoIds[videoIndex]);

			videos[videoIndex] = new Video(videoName, videoUrl, videoIcon,
					projectId, poolPosition, timelinePosition, isSelected);
		}

		return new Gson().toJson(videos);
	}

	/**
	 * Gets a file from S3 using URL address given, and puts on the dataInputStream
	 * 
	 * @param videoUrl
	 *            where video is located in S3
	 * @return a video file
	 * @throws IOException
	 *             Signals that an I/O exception of some sort has occurred
	 */
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

	/**
	 * Gets file from input stream
	 * 
	 * @param inputStream
	 *            holds the video file that was in S3
	 * @param name
	 *            name of the video file
	 * @return video file
	 * @throws FileNotFoundException
	 *             Signals that an attempt to open the file denoted by a specified pathname has failed.
	 * @throws IOException
	 *             Signals that an I/O exception of some sort has occurred.
	 */
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

	/**
	 * 
	 * @param videoUrl
	 * @return
	 * @throws IOException
	 */
	// Modified from: http://msdn.microsoft.com/en-us/library/aa478985.aspx
	public static String downloadVideosToTemp(String videoUrl)
			throws IOException {
		File tempDir = OliveServlet.tempDir;
		File inFile = S3Api.getFileFromS3(videoUrl);
		File outFile = new File(tempDir, getNameFromUrl(videoUrl));
		outFile.deleteOnExit(); // Delete the file when the JVM exits (cannot be undone).
		S3Api.saveFileToDisk(inFile, outFile);
		return outFile.getName();
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
