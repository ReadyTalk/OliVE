package com.readytalk.olive.logic;

import java.io.File;
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

import com.readytalk.olive.util.InvalidFileSizeException;

// Java code samples: https://bitbucket.org/jmurty/jets3t/src/Release-0_8_0/src/org/jets3t/samples/CodeSamples.java
// HTML code samples: http://jets3t.s3.amazonaws.com/toolkit/code-samples.html#downloading
// JavaDocs: http://jets3t.s3.amazonaws.com/api/org/jets3t/service/model/StorageObject.html
public class S3Api {
	private static final String BUCKET_NAME = "test-bucket-Olive";
	private static final String AWS_URL_PREFIX = "https://s3.amazonaws.com/"
			+ BUCKET_NAME + "/";
	private static final long MAX_SIZE_IN_BYTES = 31457280L; // 30 MB
	private static final long MIN_SIZE_IN_BYTES = 1L; // ~0 MB
	private static final String DATE_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS";

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
			IOException {
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

			String fileNameOnS3 = S3Api.getTime();

			fileAsS3Object.setName(fileNameOnS3);

			// Upload the data objects.
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

	public static File downloadFile(String videoUrl) throws IOException {
		String fileName = videoUrl.substring(AWS_URL_PREFIX.length());
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
}
