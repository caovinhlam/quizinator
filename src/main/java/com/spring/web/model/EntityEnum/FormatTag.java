package com.spring.web.model.EntityEnum;

/**
 * the enum for the formatting tag of a question
 * 
 * @author Matthew Veldhuizen
 *
 */
public enum FormatTag {

	/**
	 * the enum for a short answer question
	 */
	SHORT_ANSWER("Short Answer"), 
	
	/**
	 * the enum for a multiple choice question
	 */
	MULTI_CHOICE("Multiple Choice"), 
	
	/**
	 * the enum for a multiple answer quesiton
	 */
	MULTI_ANSWER("Multiple Answer");
	
	private final String toString;
	
	/**
	 * the constuctor method for the enum
	 * 
	 * @param toString - the value of the string for the enum
	 */
	private FormatTag(String toString) {
		this.toString = toString;
	}
	
	/**
	 * the to string method for the enum
	 */
	public String toString() {
		return toString;
	}
	
	/**
	 * this method is used to compare values of the enums back to the value to be displayed 
	 * 
	 * @param stringValue - incoming string to compare
	 * @return - returns the value if they match else returns null
	 */
	public static FormatTag fromStringValue(String stringValue) {
        try {
			for (FormatTag value : FormatTag.values()) {
	            if (value.toString.equals(stringValue)) {
	                return value;
	            }
	        } 
        } catch (IllegalArgumentException ex) {
            return null; // Return null for unknown values
        }
		return null; 
    }
}
