package com.spring.web.model.EntityEnum;

/**
 * the enum for the possible content of the question 
 * 
 * @author Matthew Veldhuizen
 *
 */
public enum ContentTypeTag {

	/**
	 * proskills content enum
	 */
	PRO_SKILLS("Pro Skills"),
	
	/**
	 * sql content enum
	 */
	SQL("SQL"),
	
	/**
	 * excel vba enum
	 */
	EXCEL("Excel VBA"),
	
	/**
	 * BA enum
	 */
	BUSINESS_ANALYSIS("Business Analysis"),
	
	/**
	 * intro to BI enum
	 */
	BI_WEEK_1("Intro to BI & Data Warehousing"),

	/**
	 * ETL enum
	 */
	BI_WEEK_2("Extract, Transform, Load (ETL"),
	
	/**
	 * UX/UI enum
	 */
	BI_WEEK_3("Data Visualisation & UX/UI"),
	
	/**
	 * unix enum 
	 */
	UNIX("UNIX"),
	
	/**
	 * os admin enum
	 */
	OS_ADMIN("OS Admin"),
	
	/**
	 * python enum
	 */
	PYTHON("Python"),
	
	/**
	 * itil enum
	 */
	ITIL("ITIL"), 
	
	/**
	 * cloud core enum
	 */
	CLOUD_WEEK_1("Cloud Core Services"),
	
	/**
	 * cloud management enum
	 */
	CLOUD_WEEK_2("Cloud Management Tools"),
	
	/**
	 * java OOD enum
	 */
	OOD("Java Object Oriented Development"),
	
	/**
	 * jdbc jpa enum
	 */
	DATA_ACCESS("JDBC and JPA"),
	
	/**
	 * java EE web enum
	 */
	JAVA_EE_WEB("Java EE Web"),
	
	/**
	 * kiaora dee dee - the next comment is in the question controller
	 */
	
	/**
	 * spring enum
	 */
	SPRING("Spring Framework"),
	
	/**
	 * agile PM enum
	 */
	APM("Agile Project Management"),
	
	/**
	 * linux enum
	 */
	LINUX_FUNDAMENTALS("Linux Fundamentals"),
	
	/**
	 * networking enum
	 */
	NETWORKING_FUNDAMENTALS("Networking Fundamentals"),
	
	/**
	 * AWS cloud enum
	 */
	AWS_CLOUD_FOUNDATIONS("AWS Cloud Foundations"),
	
	/**
	 * security enum
	 */
	SECURITY_FUNDAMENTALS("Security Fundamentals"),
	
	/**
	 * jumpstart AWS enum
	 */
	JUMPSTART_AWS("Jumpstart on AWS"),
	
	/**
	 * databases enum
	 */
	DATABASES("Databases");
	
	private final String toString;
    
	/**
	 * the constuctor method for the enum
	 * 
	 * @param toString - the value of the string for the enum
	 */
	private ContentTypeTag(String toString) {
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
	public static ContentTypeTag fromStringValue(String stringValue) {        
		try { 
			for (ContentTypeTag value : ContentTypeTag.values()) {
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
