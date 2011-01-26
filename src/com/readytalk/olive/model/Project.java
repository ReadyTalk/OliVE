package com.readytalk.olive.model;

import java.io.UnsupportedEncodingException;

public class Project {
	private String title;
	private String icon;
	private User user;
	private String ProjectID;

	public Project(String title, String icon, User user, String id) {
		this.title = title;
		this.icon = icon;
		this.user = user;
		this.ProjectID = id;
	}

	public Project(String title, User user) {
		this.title = title;
		this.user = user;
	}

	public String getProjectID() {
		return ProjectID;
	}

	public void setProjectID(String projectID) {
		ProjectID = projectID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return title;
	}

	public String sanitize(String input) throws UnsupportedEncodingException {
		String output = input;

		// Remove the *really* bad stuff (which cause XSS attacks and SQL
		// injections).
		String[] illegalStrings = { "<", ">", "(", ")", "\"", "'", ";" };
		for (int i = 0; i < illegalStrings.length; ++i) {
			output = output.replaceAll(illegalStrings[i], "");
		}

		// Let encoding convert the rest to safe strings (but not remove).
		String encoding = "UTF-8"; // This is recommended by the The World Wide Web Consortium Recommendation. See : http://download.oracle.com/javase/1.4.2/docs/api/java/net/URLEncoder.html#encode(java.lang.String, java.lang.String)
									// Other encodings: http://download.oracle.com/javase/1.4.2/docs/api/java/nio/charset/Charset.html
		try {
			output = java.net.URLEncoder.encode(output, encoding);
		} catch (UnsupportedEncodingException e) {
			throw new UnsupportedEncodingException("Choose a different encoding than \"" + encoding + "\". " + e.getMessage());
		}

		return output;
	}
}
