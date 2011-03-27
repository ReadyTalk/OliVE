package com.readytalk.olive.json;

public class GetAccountInformationResponse {

	public GetAccountInformationResponse(String name, String email,
			String password, String securityQuestion, String securityAnswer) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.securityQuestion = securityQuestion;
		this.securityAnswer = securityAnswer;
	}

	public String name;
	public String email;
	public String password;
	public String securityQuestion;
	public String securityAnswer;
}
