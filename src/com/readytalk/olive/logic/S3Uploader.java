package com.readytalk.olive.logic;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

import org.jets3t.service.S3ServiceException;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.S3Object;
import org.jets3t.service.security.AWSCredentials;

import com.readytalk.olive.util.InvalidFileSizeException;

// Java code samples: https://bitbucket.org/jmurty/jets3t/src/Release-0_8_0/src/org/jets3t/samples/CodeSamples.java
// HTML code samples: http://jets3t.s3.amazonaws.com/toolkit/code-samples.html#downloading
// JavaDocs: http://jets3t.s3.amazonaws.com/api/org/jets3t/service/model/StorageObject.html
public class S3Uploader {
	private static final String BUCKET_NAME = "test-bucket-Olive";
	private static final long MAX_SIZE_IN_BYTES = 31457280L; // 30 MB
	private static final long MIN_SIZE_IN_BYTES = 1L; // ~0 MB

	private static Logger log = Logger.getLogger(S3Uploader.class.getName());

	public static void uploadFile(File file) throws InvalidFileSizeException {
		if (file.length() > MAX_SIZE_IN_BYTES) {
			throw new InvalidFileSizeException("File larger than "
					+ MAX_SIZE_IN_BYTES + " bytes");
		}
		if (file.length() < MIN_SIZE_IN_BYTES) {
			throw new InvalidFileSizeException("File smaller than "
					+ MIN_SIZE_IN_BYTES + " bytes");
		}

		try {
			AWSCredentials awsCredentials = OliveDatabaseApi.getAwsCredentials();

			// RestS3Service is similar to HttpClient
			RestS3Service s3Service = new RestS3Service(awsCredentials);

			// Set Content-Length automatically based on the file's extension.
			S3Object fileAsS3Object = new S3Object(file);

			// Upload the data objects.
			s3Service.putObject(BUCKET_NAME, fileAsS3Object);
		} catch (IOException e) {
			log.severe("Error connecting with S3");
			e.printStackTrace();
		} catch (S3ServiceException e) {
			log.severe("Error creating RestS3Service object");
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			log.severe("Error creating S3Object object");
			e.printStackTrace();
		}
	}

	public static File downloadFile(String fileName,
			RestS3Service s3Service) {
		S3Object objectComplete = null;
		try {
			objectComplete = s3Service.getObject(BUCKET_NAME, fileName);	// TODO Does this work?
			return objectComplete.getDataInputFile();
		} catch (S3ServiceException e) {
			log.severe("Error accessing object \"" + fileName
					+ "\" in S3 bucket \"" + BUCKET_NAME + "\"");
			e.printStackTrace();
		}
		
		log.severe("Returning null!");
		return null;
	}
}
