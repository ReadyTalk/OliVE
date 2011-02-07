package com.readytalk.olive.model;

public class Project {

	private int projectId;
	private String name;
	private int accountId;
	private String icon;

	public Project(int projectId, String name, int accountId, String icon) {
		this.projectId = projectId;
		this.name = name;
		this.accountId = accountId;
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

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
}
