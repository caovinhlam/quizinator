package com.spring.web.model;

import com.spring.web.model.EntityEnum.UserRole;

/**
 * this is the class for the user model
 * 
 * @author StanleyChilton
 *
 */
public class UserTemp {

	private int userId;
	
	private String name;
	private String email;
	private String password;
	private String confirmPassword;

	/**
	 * blank user contructor
	 */
	public UserTemp() {
		super();
	}

	/**
	 * the constructor for a user object
	 * 
	 * @param name - the name of a user 
	 * @param email - the email for a user
	 * @param password - the password for a user
	 */
	public UserTemp(String name, String email, String password) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
	}

	/**
	 * getter for user id
	 * 
	 * @return returns the user id
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * setter for user id
	 * 
	 * @param userId - the id of a user to set
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	/**
	 * getter for the name
	 * 
	 * @return reutns the name of a user
	 */
	public String getName() {
		return name;
	}

	/**
	 * setter for name
	 * 
	 * @param name - the name of a user to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * getter for email
	 * 
	 * @return - the email address of a user
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * setter for email
	 * 
	 * @param email - the email to set the users too
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * getter for password
	 * 
	 * @return - the password of a user entity
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * setter for password
	 * 
	 * @param password - the password of a user to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	/**
	 * getter for confirm password
	 * 
	 * @return the confirmPassword
	 */
	public String getConfirmPassword() {
		return confirmPassword;
	}

	/**
	 * setter from confirm password
	 * 
	 * @param confirmPassword the confirmPassword to set
	 */
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	/**
	 * the to string method
	 */
	@Override
	public String toString() {
		return "User [userId=" + userId + ", name=" + name + ", email=" + email + ", password=" + password + "]";
	}
	
}
