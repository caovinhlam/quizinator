package com.spring.web.model;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.hibernate.engine.spi.SelfDirtinessTracker;
import org.hibernate.result.NoMoreReturnsException;

import com.spring.web.model.EntityEnum.CategoryTag;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;

/**
 *  
 * the quiz entity used for the storage of quizzes in the DB
 * 
 * @author Samantha Jermyn
 */

@Entity
public class Quiz {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int quizID;
	
	private String quizName;
	
	private CategoryTag quizType;
	
	@ManyToOne
	@JoinColumn(name = "userId", referencedColumnName = "userId")
	private User user;
	
	@ManyToMany 
	@JoinTable(
			name="Quiz_Question",
			joinColumns = {@JoinColumn(name="FK_QUIZ_ID")},
			inverseJoinColumns = {@JoinColumn(name="FK_QUESTION_ID")}
			)
	private List<Question> questions;
	
	private int allowedTime;
	
	private boolean reviewed;
	
	private boolean approved;
	
	@Transient
	private int numberOfQuestions;
	
	@Transient
	private int totalposmarks;

	/**
	 * Empty Quiz constructor
	 */
	public Quiz() {
		super();

	}
	
	/**
	 * Quiz constructor with parameters 
	 *
	 * @param quizName - the name of the quiz
	 * @param quizType - this param is the type of quiz being created
	 * @param user - the user id of the person creating the quiz
	 * @param questions -  the list of questions that are being stored in this quiz
	 * @param allowedTime - the allowed time for a quiz
	 * @param reviewed - whether the quiz is reviewed
	 */
	public Quiz(String quizName, CategoryTag quizType, User user, List<Question> questions, int allowedTime,
			boolean reviewed) {
		super();
		this.quizName = quizName;
		this.quizType = quizType;
		this.user = user;
		this.questions = questions;
		this.allowedTime = allowedTime;
		this.reviewed = reviewed;
	}

	/**
	 * getter for quizID
	 * 
	 * @return quizID - id of quiz
	 */
	public int getQuizID() {
		return quizID;
	}

	/**
	 * getter for questions
	 * 
	 * @return questions - list of all questions in quiz
	 */
	public List<Question> getQuestions() {
		return questions;
	}

	/**
	 * setter for quizID
	 * 
	 * @param quizID - ID of quiz
	 */
	public void setQuizID(int quizID) {
		this.quizID = quizID;
	}

	/**
	 * setter for questions
	 * 
	 * @param questions - list of questions inside quiz
	 */
	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	/**
	 * getter for reviewed
	 * 
	 * @return the reviewed
	 */
	public boolean isReviewed() {
		return reviewed;
	}

	/**
	 * setter for reviewed
	 * 
	 * @param reviewed the reviewed to set
	 */
	public void setReviewed(boolean reviewed) {
		this.reviewed = reviewed;
	}

	// i swear you are getting hotter the end is near - head over to the notification repo
	
	/**
	 * getter for user
	 * 
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * setter for user
	 * 
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * getter for quizname
	 * 
	 * @return the quizName
	 */
	public String getQuizName() {
		return quizName;
	}

	/**
	 * setter for quiz name
	 * 
	 * @param quizName the quizName to set
	 */
	public void setQuizName(String quizName) {
		this.quizName = quizName;
	}

	/**
	 * getter for allowed time
	 * 
	 * @return the allowedTime
	 */
	public int getAllowedTime() {
		return allowedTime;
	}


	/**
	 * setter for allowed time
	 * 
	 * @param allowedTime the allowedTime to set
	 */
	public void setAllowedTime(int allowedTime) {
		this.allowedTime = allowedTime;
	}

	/**
	 * setter for quiz type
	 * 
	 * @param quizType the quizType to set
	 */
	public void setQuizType(CategoryTag quizType) {
		this.quizType = quizType;
	}

	
	/**
	 * getter for number or questions
	 * 
	 * @return the numberOfQuestions
	 */
	public int getNumberOfQuestions() {
		return questions.size();
	}

	/**
	 * setter for number of questions
	 * 
	 * @param numberOfQuestions the numberOfQuestions to set
	 */
	public void setNumberOfQuestions(int numberOfQuestions) {
		this.numberOfQuestions = numberOfQuestions;
	}


	/**
	 * getter for quiz type
	 * 
	 * @return the quizType
	 */
	public CategoryTag getQuizType() {
		return quizType;
	}
	
	/**
	 * getter for approved
	 * 
	 * @return boolean value of approved
	 */
	public boolean isApproved() {
		return approved;
	}

	/**
	 * Sets the approved boolean
	 * 
	 * @param approved - boolean value
	 */
	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	/**
	 * getter for total possible marks
	 * 
	 * @return the totalposmarks
	 */
	public int getTotalposmarks() {
		int count = 0;
		for(Question question: questions) {
			for(Answer answer : question.getAnswers()) {
				if(answer.isCorrect() || question.getFormatTag().name() == "SHORT_ANSWER") {
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * setter for total possible marks
	 * 
	 * @param totalposmarks the totalposmarks to set
	 */
	public void setTotalposmarks(int totalposmarks) {
		this.totalposmarks = totalposmarks;
	}

	@Override
	public String toString() {
		return "Quiz [quizID=" + quizID + ", quizName=" + quizName + ", quizType=" + quizType + ", user=" + user
				+ ", questions=" + questions + ", allowedTime=" + allowedTime + ", reviewed=" + reviewed + "]";
	}

	
	
}
