package com.readytalk.olive.model;

public class User {

	private String username;
	private String password;
	private String email;
	private String name;
	private int AccountID;

	// TODO change the way the user is handled by using ID's to distinguish one
	// user from another rather than using usernames

	public User(String username, String password, String email, String name) {
		super();
		this.username = username;
		this.password = password;
		this.email = email;
		this.name = name;
		this.AccountID = -1;
	}

	public User(String username, String password, String email, String name,
			int id) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.name = name;
		this.AccountID = id;
	}

	public User(String username, String password) {
		this.username = username;
		this.password = password;
		this.AccountID = -1;
	}

	public int getAccountID() {
		return AccountID;
	}

	public void setAccountID(int accountID) {
		AccountID = accountID;
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

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
