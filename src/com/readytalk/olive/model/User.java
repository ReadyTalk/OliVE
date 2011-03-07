package com.readytalk.olive.model;
/**
 * class User holds the information of an user to log in and some additional information for recovery.
 * 
 * @author Team Olive
 * 
 */

public class User {

	private String username;
	private String password;
	private String name;
	private String email;
	private String securityQuestion;
	private String securityAnswer;
	
	/**
	 * User Constructor for Log In and Sign Up.
	 * 
	 * @param username Identification key used for log in
	 * @param password Password key used for log in
	 * @param name	   Name of the user
	 * @param email    E-mail address used by an user
	 */
	public User(String username, String password, String name, String email) {
		this.username = username;
		this.password = password;
		this.name = name;
		this.email = email;
	}

	/**
	 * User Constructor for Edit Account Information
	 *
	 * @param username Identification key used for log in
	 * @param password Password key used for log in
	 * @param name	   Name of the user
	 * @param email    E-mail address used by an user
	 * @param securityQuestion Question used for recovering password
	 * @param securityAnswer   The answer for the security question
	 */
	public User(String username, String password, String name,
			String email, String securityQuestion, String securityAnswer) {
		this.username = username;
		this.password = password;
		this.name = name;
		this.email = email;
		this.setSecurityQuestion(securityQuestion);
		this.setSecurityAnswer(securityAnswer);
	}
	/**
	 * In use of displaying the username of a project for developers for debugging purpose
	 * 
	 * @return name of an user
	 */
	@Override
	public String toString() {
		return username;
	}
	/**
	 * calls the specific user name that is registered through sign up.
	 * 
	 * @return user name registered by an user
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Registers the name of an user
	 * 
	 * @param username user name registered by an user
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Brings the information for the password that has been set by an user 
	 * 
	 * @return password key stored in Olive database
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Registers user password that has been passed by user input
	 * 
	 * @param password Secret Key for an user that is stored in 
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Registers name of an user in the edit account information
	 * 
	 * @param name name of an user registered in the edit account information
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Brings the default name of an user listed in the edit account information
	 * 
	 * @return name of an user in the edit account information
	 */
	public String getName() {
		return name;
	}

	/**
	 * Registers or changes email of an user in the edit account information
	 * 
	 * @param email email address of an user
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * Brings the information regarding email address of an user
	 * @return email address registered by an user
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * Registers a security question for recovering purpose
	 * @param securityQuestion question asked later for recovering purpose
	 */
	public void setSecurityQuestion(String securityQuestion) {
		this.securityQuestion = securityQuestion;
	}
	/**
	 * Brings the information regarding security question stored by an user
	 * @return question asked later for recovering purpose
	 */
	public String getSecurityQuestion() {
		return securityQuestion;
	}
	/**
	 * Registers the answer for security question stored by an user
	 * @param securityAnswer an answer for security question
	 */
	public void setSecurityAnswer(String securityAnswer) {
		this.securityAnswer = securityAnswer;
	}
	/**
	 * Brings an information regarding an answer of a security question stored by an user
	 * @return security answer for security question
	 */
	public String getSecurityAnswer() {
		return securityAnswer;
	}
}
