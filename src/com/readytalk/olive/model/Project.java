package com.readytalk.olive.model;

public class Project {

	private int projectId;
	private String name;
	private User user;
	private String icon;

	public Project(int projectId, String name, User user, String icon) {
		this.projectId = projectId;
		this.name = name;
		this.user = user;
		this.icon = icon;
	}

	@Override
	public String toString() {
		return name;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
}
