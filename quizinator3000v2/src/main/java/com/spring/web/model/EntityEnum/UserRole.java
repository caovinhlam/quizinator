package com.spring.web.model.EntityEnum;

/**
 * this is the user role enum used to set the type of user a specific user is
 * 
 * @author Cao vinh lam 
 *
 */
public enum UserRole {

	/**
	 * the enum for a student in training
	 */
	TRAINING("Student in Training"),
	
	/**
	 * the enum for a student who is back from a placement
	 */
	BEACHED("Beached Student"), 
	
	/**
	 * the enum for a student who has completed training but hasnt been placed yet
	 */
	POND("Pond Student"), 
	
	/**
	 * the enum for a student who has left the company
	 */
	ABSENT("Absent Student"), 
	
	/**
	 * the enum for a trainer of the courses
	 */
	TRAINER("Trainer"), 
	
	/**
	 * the enum for a sales account
	 */
	SALES("Sales");
	
	private final String toString;
	
	/**
	 * the constuctor method for the enum
	 * 
	 * @param toString - the value of the string for the enum
	 */
	private UserRole(String toString) {
		this.toString = toString;
	}
	
	
	/**
	 * the to string method for the enum
	 */
	public String toString() {
		return toString;
	}
}
