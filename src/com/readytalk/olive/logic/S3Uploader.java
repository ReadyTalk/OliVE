package com.readytalk.olive.logic;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.jets3t.service.S3ServiceException;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.S3Object;
import org.jets3t.service.security.AWSCredentials;

import com.readytalk.olive.util.FileTooLargeException;

// https://bitbucket.org/jmurty/jets3t/src/Release-0_8_0/src/org/jets3t/samples/CodeSamples.java
// http://jets3t.s3.amazonaws.com/toolkit/code-samples.html#downloading
public class S3Uploader {
	private static final String BUCKET_NAME = "test-bucket-Olive";
	private static final long MAX_SIZE_IN_BYTES = 31457280L; // 30 MB

	public static void upLoadVideo(File video) throws FileTooLargeException {
		if (video.length() > MAX_SIZE_IN_BYTES) {
			throw new FileTooLargeException("Video larger than "
					+ MAX_SIZE_IN_BYTES + " bytes");
		}
		AWSCredentials awsCredentials = null;
		try {
			awsCredentials = OliveDataApi.loadAWSCredentials();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// RestS3Service is similar to HttpClient
		RestS3Service s3Service = null;
		try {
			s3Service = new RestS3Service(awsCredentials);
		} catch (S3ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Set Content-Length automatically based on the file's extension.
		S3Object fileObject = null;
		try {
			fileObject = new S3Object(video);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Upload the data objects.
		try {
			s3Service.putObject(BUCKET_NAME, fileObject);
		} catch (S3ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
