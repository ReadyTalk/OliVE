package com.readytalk.olive.logic;

import java.io.File;

import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.S3Object;
import org.jets3t.service.security.AWSCredentials;

public class S3Uploader {
	private static final String BUCKET_NAME = "test-bucket-Olive";

	public static void upLoadVideo(File video) throws Exception {
		AWSCredentials awsCredentials = S3Credentials.loadAWSCredentials();

		RestS3Service s3Service = new RestS3Service(awsCredentials);

		// Set Content-Length automatically based on the file's extension.
		S3Object fileObject = new S3Object(video);

		// Upload the data objects.
		s3Service.putObject(BUCKET_NAME, fileObject);

	}
}
