package com.readytalk.olive.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ZencoderCredentials {

	public static String getZencoderApiKey() {
		String zencoderApiKey = "";
		try {
			Connection conn = OliveDataApi.getDBConnection();
			Statement st = conn.createStatement();
			String s = "USE OliveData;";
			st.executeUpdate(s);
			s = "SELECT * FROM ZencoderCredentials;";
			ResultSet r = st.executeQuery(s);
			if (r.first()) {
				zencoderApiKey = r.getString("ZencoderAPIKey");
				OliveDataApi.closeConnection(conn);
			} else {
				// TODO Add error for rare case that it can't find the data
				OliveDataApi.closeConnection(conn);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return zencoderApiKey;
	}
}
