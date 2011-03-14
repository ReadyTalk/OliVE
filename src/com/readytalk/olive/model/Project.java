package com.readytalk.olive.model;

public class Project {

	private String name;
	private int accountId;
	private String icon;
	private int poolPosition;

	public Project(String name, int accountId, String icon, int poolPosition) {
		this.name = name;
		this.accountId = accountId;
		this.icon = icon;
		this.poolPosition = poolPosition;
	}

	@Override
	public String toString() {
		return name;
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
	
	public void setPoolPosition(int poolPosition) {
		this.poolPosition = poolPosition;
	}

	public int getPoolPosition() {
		return poolPosition;
	}
}
