package com.readytalk.olive.logic;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import org.jets3t.service.security.AWSCredentials;


public class S3Credentials {
    public static String AWS_ACCESS_KEY_PROPERTY_NAME = "";
    public static String AWS_SECRET_KEY_PROPERTY_NAME = "";

    public static AWSCredentials loadAWSCredentials() throws IOException {
        /*InputStream propertiesIS =
            ClassLoader.class.getResourceAsStream(SAMPLES_PROPERTIES_NAME);

        if (propertiesIS == null) {
            throw new RuntimeException("Unable to load test properties file from classpath: "
                + SAMPLES_PROPERTIES_NAME);
        }

        Properties testProperties = new Properties();
        testProperties.load(propertiesIS);

        if (!testProperties.containsKey(AWS_ACCESS_KEY_PROPERTY_NAME)) {
            throw new RuntimeException(
                "Properties file 'test.properties' does not contain required property: " + AWS_ACCESS_KEY_PROPERTY_NAME);
        }
        if (!testProperties.containsKey(AWS_SECRET_KEY_PROPERTY_NAME)) {
            throw new RuntimeException(
                "Properties file 'test.properties' does not contain required property: " + AWS_SECRET_KEY_PROPERTY_NAME);
        }*/
    	
    	try{ 
			Connection conn = OliveLogic.getDBConnection();	
			Statement st = conn.createStatement();
			String s = "USE OliveData;";
			st.executeUpdate(s);
			s = "SELECT * FROM s3credentials;";
			ResultSet r = st.executeQuery(s);
			if(r.first()){
				AWS_ACCESS_KEY_PROPERTY_NAME = r.getString("AWSAccessKey");
				AWS_SECRET_KEY_PROPERTY_NAME = r.getString("AWSSecretKey");
				OliveLogic.closeConnection(conn);
			}
			else{
				//TODO Add error for rare case that it can't find the data
				OliveLogic.closeConnection(conn);
			}
			
		}catch (Exception e) { e.printStackTrace(); }

        AWSCredentials awsCredentials = new AWSCredentials(
            AWS_ACCESS_KEY_PROPERTY_NAME, AWS_SECRET_KEY_PROPERTY_NAME);

        return awsCredentials;
    }

}