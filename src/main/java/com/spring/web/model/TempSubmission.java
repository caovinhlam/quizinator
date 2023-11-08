package com.spring.web.model;

import java.util.HashMap;
import java.util.List;

/**
 * this class is used to store the temp values for a quiz
 * this is then passed to the submission entity for saving to
 * the DB
 * 
 * @author Matthew Veldhuizen
 *
 */
public class TempSubmission {

	private HashMap<Integer, Boolean> userSelectionMap;
	
	private HashMap<Integer, String> userShortAnswerMap;
	
	private Quiz quiz;
	
	private List<QuestionAttempt> shortAnswerAttempts;
	
	private Submission submission;
	
	/**
	 * blank constructor
	 */
	public TempSubmission() {}


	/**
	 * the constructor for the temp submission
	 * 
	 * @param userSelections - a mashmap of the users selections on a quiz
	 * @param userShortAnswer - a hashmap of the users short answer questions
	 * @param quiz - a link to the quiz they were submitting
	 */
	public TempSubmission(HashMap<Integer, Boolean> userSelections, HashMap<Integer, String> userShortAnswer, Quiz quiz) {
		this.userSelectionMap = userSelections;
		this.userShortAnswerMap = userShortAnswer;
		this.quiz = quiz;
	}
	
	/**
	 * getter for the submission
	 * 
	 * @return - returns a link to the submission
	 */
	public Submission getSubmission() {
		return submission;
	}


	/**
	 * setter for the submission var
	 * 
	 * @param submission - the submission to link to the temp submission
	 */
	public void setSubmission(Submission submission) {
		this.submission = submission;
	}


	/**
	 * getter for the list of short answers
	 * 
	 * @return - this method returns a list of the short answers
	 */
	public List<QuestionAttempt> getShortAnswerAttempts() {
		return shortAnswerAttempts;
	}

	/**
	 * setter for the short answer attempts
	 * 
	 * @param shortAnswerAttempts - a list of all the short answer attempts
	 */
	public void setShortAnswerAttempts(List<QuestionAttempt> shortAnswerAttempts) {
		this.shortAnswerAttempts = shortAnswerAttempts;
	}

	/**
	 * getter for the user selectionmap
	 * 
	 * @return - returns a list of all the user selections
	 */
	public HashMap<Integer, Boolean> getUserSelectionMap() {
		return userSelectionMap;
	}

	/**
	 * stter for the user selection map
	 * 
	 * @param userSelections - a hashmap of the user selections
	 */
	public void setUserSelectionMap(HashMap<Integer, Boolean> userSelections) {
		this.userSelectionMap = userSelections;
	}

	/**
	 * getter for the short answer map
	 * 
	 * @return - returns the map containing all the answers to the short answer questions
	 */
	public HashMap<Integer, String> getUserShortAnswerMap() {
		return userShortAnswerMap;
	}

	/**
	 * setter for the short answer map
	 * 
	 * @param userShortAnswer - the hashmap to set the short answers too
	 */
	public void setUserShortAnswerMap(HashMap<Integer, String> userShortAnswer) {
		this.userShortAnswerMap = userShortAnswer;
	}
	
	/**
	 * getter for the quiz
	 * 
	 * @return returns the quiz that this submission is going to be linked too
	 */
	public Quiz getQuiz() {
		return quiz;
	}

	/**
	 * setter for the linked quiz
	 * 
	 * @param quiz - the quiz that this submission will be linked too
	 */
	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
	}

	/**
	 * the to stirng method is used to display the vars and also used for testing purposes
	 */
	@Override
	public String toString() {
		return "TempSubmission [userSelections=" + userSelectionMap + ", userShortAnswer=" + userShortAnswerMap + "]";
	}
	
}
