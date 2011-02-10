package com.readytalk.olive.model;

public class User {

	private String username;
	private String password;
	private String name;
	private String email;
	private String securityQuestion;
	private String securityAnswer;
	
	
	public User(String username, String password, String name, String email) {
		this.username = username;
		this.password = password;
		this.name = name;
		this.email = email;
	}

	public User(String username, String password, String name,
			String email, String securityQuestion, String securityAnswer) {
		this.username = username;
		this.password = password;
		this.name = name;
		this.email = email;
		this.setSecurityQuestion(securityQuestion);
		this.setSecurityAnswer(securityAnswer);
	}

	@Override
	public String toString() {
		return username;
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

	public void setSecurityQuestion(String securityQuestion) {
		this.securityQuestion = securityQuestion;
	}

	public String getSecurityQuestion() {
		return securityQuestion;
	}

	public void setSecurityAnswer(String securityAnswer) {
		this.securityAnswer = securityAnswer;
	}

	public String getSecurityAnswer() {
		return securityAnswer;
	}
}
