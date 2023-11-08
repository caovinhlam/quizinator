package com.spring.web.model.EntityEnum;

/**
 * the enum for the type of category the questions fall into
 * 
 * @author Matthew Veldhuizen
 *
 */
public enum CategoryTag {

	/**
	 * the enum for whether a question is course related content
	 */
	COURSE_CONTENT("Course Content"),
	
	/**
	 * the enum for whether a question is interview related content
	 */
	INTERVIEW_PREP("Interview Preparation");
	
	private final String toString;
	
	/**
	 * the constuctor method for the enum
	 * 
	 * @param toString - the value of the string for the enum
	 */
	private CategoryTag(String toString) {
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
	public static CategoryTag fromStringValue(String stringValue) {
        try {        
			for (CategoryTag value : CategoryTag.values()) {
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
