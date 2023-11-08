package com.spring.web.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;

/**
 * entity for the question attempts
 * 
 * 
 * @author Matthew Veldhuizen
 *
 */

@Entity
public class QuestionAttempt {

	@Id
	@SequenceGenerator(name = "seqQuestionAttempt")
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "seqQuestionAttempt")
	private Integer questionAttemptID;
	
	@ManyToOne
	@JoinColumn(name = "submissionID", referencedColumnName = "submissionID")
	private Submission submission;
	
	@ManyToOne
	@JoinColumn(name = "questionId", referencedColumnName = "questionId")
	private Question question;
	
	@ManyToOne
	@JoinColumn(name = "answerId", referencedColumnName = "answerId")
	private Answer answer;
	
	private boolean selection;
	
	private String shortAnswerAttempt;
	
	//True if selected answer is correct, false if incorrect, null if short answer that needs to be marked
	private Boolean correct;
	
	/**
	 * blank constructor for question attempt
	 */
	public QuestionAttempt () {}
	
	/**
	 * constructor for the question attempt entity
	 * 
	 * @param submission - a link to the submission
	 * @param question - a link to the question this attempt is for 
	 * @param answer - a link to the answer this attempt is for 
	 * @param selection - the selection the user made
	 * @param shortAnswerAttempt - the short answer they input if the question was a short answer
	 * @param correct - whether they got the answer correct or if its pending marking
	 */
	public QuestionAttempt(Submission submission, Question question, Answer answer, boolean selection, String shortAnswerAttempt, Boolean correct) {
		this.submission = submission;
		this.question = question;
		this.answer = answer;
		this.selection = selection;
		this.shortAnswerAttempt = shortAnswerAttempt;
		this.correct = correct;
	}

	/**
	 * getter for question id
	 * 
	 * @return returns the question id
	 */
	public Integer getQuestionAttemptID() {
		return questionAttemptID;
	}

	/**
	 * setter for question id
	 * 
	 * @param questionAttemptID - sets the question attempt id
	 */
	public void setQuestionAttemptID(Integer questionAttemptID) {
		this.questionAttemptID = questionAttemptID;
	}

	/**
	 * getter for submissions
	 * 
	 * @return - returns the submission object this is attached too
	 */
	public Submission getSubmission() {
		return submission;
	}

	/**
	 * setter for submission
	 * 
	 * @param submission - the submission this attempt needs to be linked too
	 */
	public void setSubmission(Submission submission) {
		this.submission = submission;
	}

	/**
	 * getter for the question this is attached too
	 * 
	 * @return - returns the question this attempt is attached too
	 */
	public Question getQuestion() {
		return question;
	}

	/**
	 * setter for questions
	 * 
	 * @param question - the question object to set it too
	 */
	public void setQuestion(Question question) {
		this.question = question;
	}

	/**
	 * getter for the answer for this quesiton
	 * 
	 * @return - returns the answer object
	 */
	public Answer getAnswer() {
		return answer;
	}

	/**
	 * setter for the answer object
	 * 
	 * @param answer - the answer object to set the value too
	 */
	public void setAnswer(Answer answer) {
		this.answer = answer;
	}

	/**
	 * the selection method
	 * 
	 * @return - returns the user selection
	 */
	public boolean isSelection() {
		return selection;
	}

	/**
	 * setter for the user selection
	 * 
	 * @param selection - the value of whether the user selected this or not
	 */
	public void setSelection(boolean selection) {
		this.selection = selection;
	}

	/**
	 * getter for short answer attempt
	 * 
	 * @return - returns the string of the short answer the user made
	 */
	public String getShortAnswerAttempt() {
		return shortAnswerAttempt;
	}

	/**
	 * sets the short answer value of the attempt
	 * 
	 * @param shortAnswerAttempt - the value to set the short answer value too
	 */
	public void setShortAnswerAttempt(String shortAnswerAttempt) {
		this.shortAnswerAttempt = shortAnswerAttempt;
	}

	/**
	 * the getter for whether the attempt is correct or not
	 * 
	 * @return - returns the true or false value based on if they got this attempt correct or not
	 */
	public Boolean isCorrect() {
		if (correct == null) {
			return null;
		} else {
			return correct;
		}
	}

	/**
	 * setter for the correct value
	 * 
	 * @param correct - the true or false value to set the var too
	 */
	public void setCorrect(Boolean correct) {
		this.correct = correct;
	}

	/**
	 * the to String method used for testing
	 */
	@Override
	public String toString() {
		return "QuestionAttempt [questionAttemptID=" + questionAttemptID + ", submission=" + submission + ", question="
				+ question + ", answer=" + answer + ", selection=" + selection + ", shortAnswerAttempt="
				+ shortAnswerAttempt + "]";
	}
	
}
