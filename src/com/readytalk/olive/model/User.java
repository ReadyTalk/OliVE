package com.readytalk.olive.model;

import java.io.UnsupportedEncodingException;

public class User {

	private String username;
	private String password;

	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return username;
	}
	
	public String sanitize(String input) throws UnsupportedEncodingException {
		String output = input;
		
		// Remove the *really* bad stuff (which cause XSS attacks and SQL
		// injections).
		String[] illegalStrings = {"!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "_", "+", "-", "=", "{", "}", "[", "]", "\\", "|", ";", "'", "\"", ":", ",", ".", "<", ">", "/", "?", "`", "~"};
		for (int i = 0; i < illegalStrings.length; ++i) {
			output = output.replaceAll(illegalStrings[i], "");
		}
		
		// Let encoding convert the rest to safe strings (but not remove).
		String encoding = "UTF-8";	// This is recommended by the The World Wide Web Consortium Recommendation. See : http://download.oracle.com/javase/1.4.2/docs/api/java/net/URLEncoder.html#encode(java.lang.String, java.lang.String)
									// Other encodings: http://download.oracle.com/javase/1.4.2/docs/api/java/nio/charset/Charset.html
		try {
			output = java.net.URLEncoder.encode(output, encoding);
		} catch (UnsupportedEncodingException e) {
			throw new UnsupportedEncodingException("Choose a different encoding than \"" + encoding + "\". " + e.getMessage());
		}
		
		return output;
	}
}
