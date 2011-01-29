package com.readytalk.olive.model;

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
}
