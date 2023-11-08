package com.spring.web.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * 
 * notification class
 * 
 * @author Samantha Jermyn
 */
@Entity
public class Notification {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int notificationID;
	
	private String message;
	
	private String oldInfo;
	private String newInfo;

	@ManyToOne
	@JoinColumn(name = "QuestionID")
	private Question questionID;
	
	@ManyToOne
	@JoinColumn(name = "QuizID")
	private Quiz quizID;
	
	@ManyToOne
	@JoinColumn(name = "userId")
	private User user;
	
	
	private String editor;
	
	/**
	 * Notification constructor with no parameters
	 */
	public Notification() {
		super();	
	}
	
	/**
	 * Notification constructor with parameters
	 * 
	 * @param message - message to display
	 * @param questionID - ID of question related to notification
	 * @param quizID - ID of quiz related to notification
	 * @param userID - ID of user to receive notification
	 */
	public Notification(String message, Question questionID, Quiz quizID, User userID) {
		super();
		this.message = message;
		this.questionID = questionID;
		this.quizID = quizID;
		this.user = userID;
	}

	/**
	 * getter for notificationID
	 * 
	 * @return notificationID - id of notification
	 */
	public int getNotificationID() {
		return notificationID;
	}

	/**
	 * getter for message
	 * 
	 * @return message - message to display
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * getter for questionID
	 * 
	 * @return questionID - id of question related to question
	 */
	public Question getQuestionID() {
		return questionID;
	}

	/**
	 * getter for quizID
	 * 
	 * @return quizID - id of quiz related to question
	 */
	public Quiz getQuizID() {
		return quizID;
	}

	/**
	 * getter for userID
	 * 
	 * @return userID - id of user to receive notification
	 */
	public User getUser() {
		return user;
	}

	/**
	 * setter for notificationID
	 * 
	 * @param notificationID - id for notification
	 */
	public void setNotificationID(int notificationID) {
		this.notificationID = notificationID;
	}

	/**
	 * setter for message
	 * 
	 * @param message - message to display to user
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * setter for questionId
	 * 
	 * @param questionID - id of question related to notification
	 */
	public void setQuestionID(Question questionID) {
		this.questionID = questionID;
	}

	/**
	 * setter for quizID
	 * 
	 * @param quizID - id of quiz related to notification
	 */
	public void setQuizID(Quiz quizID) {
		this.quizID = quizID;
	}

	/**
	 * setter for userID
	 * 
	 * @param userID - id of user to receive notification
	 */
	public void setUserID(User userID) {
		this.user = userID;
	}
	
	/**
	 * g'day don - search the content type file 
	 */
	
	/**
	 * getter old info
	 * 
	 * @return returns the old info
	 */
	public String getOldInfo() {
		return oldInfo;
	}

	/**
	 * getters for new info
	 * 
	 * @return returns the new info 
	 */
	public String getNewInfo() {
		return newInfo;
	}

	/**
	 * getters for editor
	 * 
	 * @return returns the user who edited it
	 */
	public String getEditor() {
		return editor;
	}

	/**
	 * setters for old info
	 * 
	 * @param oldInfo - the old info var
	 */
	public void setOldInfo(String oldInfo) {
		this.oldInfo = oldInfo;
	}

	/**
	 * setter for the new info
	 * 
	 * @param newInfo - the new info var
	 */
	public void setNewInfo(String newInfo) {
		this.newInfo = newInfo;
	}

	/**
	 * setter in editor
	 * 
	 * @param editor - the editor to set
	 */
	public void setEditor(String editor) {
		this.editor = editor;
	}
	
	/**
	 * tostring method
	 */
	@Override
	public String toString() {
		return "Notification [notificationID=" + notificationID + ", message=" + message + ", questionID=" + questionID.getQuestionId()
				+ ", quizID=" + quizID.getQuizID() + ", userID=" + user.getUserId() + "]";
	}	

}
