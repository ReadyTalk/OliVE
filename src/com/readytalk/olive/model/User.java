package com.readytalk.olive.model;

public class User {

	private int accountId;
	private String username;
	private String password;
	private String name;
	private String email;

	// TODO change the way the user is handled by using ID's to distinguish one
	// user from another rather than using usernames

	public User(String username, String password, String email, String name) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.name = name;
	}

	public User(int accountId, String username, String password, String name,
			String email) {
		this.accountId = accountId;
		this.username = username;
		this.password = password;
		this.name = name;
		this.email = email;
	}

	@Override
	public String toString() {
		return username;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
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

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}
}
