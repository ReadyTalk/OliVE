package com.readytalk.olive.logic;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.jets3t.service.S3ServiceException;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.S3Object;
import org.jets3t.service.security.AWSCredentials;

import com.readytalk.olive.util.InvalidFileSizeException;

// https://bitbucket.org/jmurty/jets3t/src/Release-0_8_0/src/org/jets3t/samples/CodeSamples.java
// http://jets3t.s3.amazonaws.com/toolkit/code-samples.html#downloading
public class S3Uploader {
	private static final String BUCKET_NAME = "test-bucket-Olive";
	private static final long MAX_SIZE_IN_BYTES = 31457280L;	// 30 MB
	private static final long MIN_SIZE_IN_BYTES = 1L; 			// ~0 MB

	public static void upLoadVideo(File video) throws InvalidFileSizeException {
		if (video.length() > MAX_SIZE_IN_BYTES) {
			throw new InvalidFileSizeException("Video larger than "
					+ MAX_SIZE_IN_BYTES + " bytes");
		}
		if (video.length() < MIN_SIZE_IN_BYTES) {
			throw new InvalidFileSizeException("Video smaller than "
					+ MIN_SIZE_IN_BYTES + " bytes");
		}
		
		try {
			AWSCredentials awsCredentials = OliveDataApi.loadAWSCredentials();
			
			// RestS3Service is similar to HttpClient
			RestS3Service s3Service = new RestS3Service(awsCredentials);
			
			// Set Content-Length automatically based on the file's extension.
			S3Object fileObject = new S3Object(video);
			
			// Upload the data objects.
			s3Service.putObject(BUCKET_NAME, fileObject);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (S3ServiceException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
}
