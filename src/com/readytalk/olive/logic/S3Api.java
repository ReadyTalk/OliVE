package com.readytalk.olive.logic;

import java.io.File;
import java.io.IOException;
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
import com.readytalk.olive.model.Video;
import com.readytalk.olive.util.InvalidFileSizeException;

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

	private static RestS3Service getS3Service() throws S3ServiceException {
		AWSCredentials awsCredentials = DatabaseApi.getAwsCredentials();

		// RestS3Service is similar to HttpClient
		RestS3Service s3Service = new RestS3Service(awsCredentials);
		return s3Service;
	}

	// TODO Make this a hash function that uses the time
	public static String getTime() {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
		return simpleDateFormat.format(calendar.getTime());
	}

	public static String uploadFile(File file) throws InvalidFileSizeException,
			IOException, ServiceException, NoSuchAlgorithmException {
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

			String convertedVideoUrl = ZencoderApi
					.convertToOgg(unconvertedVideoUrl);

			return convertedVideoUrl;
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

	public static void deleteFileInS3(String fileName) {
		log.severe("deleteFileInS3 has not yet been implemented.");
		// Do something like this:
		// RestS3Service s3Service = getS3Service();
		// s3Service.deleteObject(BUCKET_NAME, objectKey); // TODO Store objectKey in the database
	}

	public static String getNameFromUrl(String videoUrl) {
		return videoUrl.substring(AWS_URL_PREFIX.length());
	}

	public static String getNameFromUrlWithNewTimeStamp(String videoUrl) {
		return S3Api.getTime()
				+ getNameFromUrl(videoUrl).substring(S3Api.getTime().length()); // TODO Fix fugly code
	}

	public static String getVideoInformation(int projectId) {
		int[] videoIds = DatabaseApi.getVideoIds(projectId);
		Video[] videos = new Video[videoIds.length];

		for (int videoIndex = 0; videoIndex < videoIds.length; ++videoIndex) {
			String videoName = DatabaseApi.getVideoName(videoIds[videoIndex]);
			String videoUrl = DatabaseApi.getVideoUrl(videoIds[videoIndex]);
			String videoIcon = DatabaseApi.getVideoIcon(videoIds[videoIndex]);
			int startTimeStoryboard = DatabaseApi
					.getVideoTimelinePosition(videoIds[videoIndex]);
			boolean isSelected = DatabaseApi
					.getVideoIsSelected(videoIds[videoIndex]);

			videos[videoIndex] = new Video(videoName, videoUrl, videoIcon,
					projectId, startTimeStoryboard, isSelected);
		}

		return new Gson().toJson(videos);
	}
}
