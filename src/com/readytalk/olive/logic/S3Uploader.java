package com.readytalk.olive.logic;

import java.io.ByteArrayInputStream;
import java.io.File;

import org.jets3t.service.Constants;
import org.jets3t.service.S3Service;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.S3Bucket;
import org.jets3t.service.model.S3Object;
import org.jets3t.service.security.AWSCredentials;
import org.jets3t.service.utils.ServiceUtils;

public class S3Uploader {
    private static final String BUCKET_NAME = "test-bucket-Olive";

    public static void upLoadVideo(File video) throws Exception{
        // Your Amazon Web Services (AWS) login credentials are required to manage S3 accounts.
        // These credentials are stored in an AWSCredentials object:

        AWSCredentials awsCredentials = S3Credentials.loadAWSCredentials();

        // To communicate with S3 use the RestS3Service.
        RestS3Service s3Service = new RestS3Service(awsCredentials);

        /*
         * Uploading data objects
         */

        // We use S3Object classes to represent data objects in S3. To store some information in our
        // new test bucket, we must first create an object with a key/name then tell our
        // S3Service to upload it to S3.

        // In the example below, we print out information about the S3Object before and after
        // uploading it to S3. These print-outs demonstrate that the S3Object returned by the
        // putObject method contains extra information provided by S3, such as the date the
        // object was last modified on an S3 server.



        // Create an S3Object based on a file, with Content-Length set automatically and
        // Content-Type set based on the file's extension (using the Mimetypes utility class)
       
        S3Object fileObject = new S3Object(video);


        // Upload the data objects.
        s3Service.putObject(BUCKET_NAME, fileObject);     
  
  
    }
}
