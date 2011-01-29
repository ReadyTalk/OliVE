package com.readytalk.olive.logic;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.jets3t.service.security.AWSCredentials;

public class S3Credentials {
	public static String AWS_ACCESS_KEY_PROPERTY_NAME = "";
	public static String AWS_SECRET_KEY_PROPERTY_NAME = "";

	public static AWSCredentials loadAWSCredentials() throws IOException {
		try {
			Connection conn = OliveDataApi.getDBConnection();
			Statement st = conn.createStatement();
			String s = "USE OliveData;";
			st.executeUpdate(s);
			s = "SELECT * FROM s3credentials;";
			ResultSet r = st.executeQuery(s);
			if (r.first()) {
				AWS_ACCESS_KEY_PROPERTY_NAME = r.getString("AWSAccessKey");
				AWS_SECRET_KEY_PROPERTY_NAME = r.getString("AWSSecretKey");
				OliveDataApi.closeConnection(conn);
			} else {
				// TODO Add error for rare case that it can't find the data
				OliveDataApi.closeConnection(conn);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		AWSCredentials awsCredentials = new AWSCredentials(
				AWS_ACCESS_KEY_PROPERTY_NAME, AWS_SECRET_KEY_PROPERTY_NAME);

		return awsCredentials;
	}

}