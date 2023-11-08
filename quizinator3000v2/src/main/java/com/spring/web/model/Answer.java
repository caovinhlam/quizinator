package com.spring.web.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.SequenceGenerator;

/**
 * this is the class for the answer model
 * 
 * @author Matthew Veldhuizen
 *
 */
@Entity
@NamedQuery(name = "Answer.findAll", query = "SELECT a FROM Answer a")
public class Answer {

	@Id
	@SequenceGenerator(name = "seqAnswer")
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "seqAnswer")
	private Integer answerId;
	
	private String answer;
	
	private boolean correct;
	
	@ManyToOne
	@JoinColumn(name = "questionId", referencedColumnName = "questionId")
	private Question question;
	
	/**
	 * Empty Constructor
	 */
	public Answer() {}

	/**
	 * Constructor for creating new Answer Object
	 * 
	 * @param answer - Value for the answer
	 * @param question - Linked question object
	 */
	public Answer(String answer, Question question) {
		super();
		this.answer = answer;
		this.question = question;
	}

	/**
	 * Constructor for creating new Answer Object
	 * 
	 * @param answer - Value for the answer
	 * @param correct - State of the answer
	 * @param question - Linked question object
	 */
	public Answer(String answer, boolean correct, Question question) {
		super();
		this.answer = answer;
		this.correct = correct;
		this.question = question;
	}
	
	/**
	 * Getter for ID
	 * 
	 * @return Integer answerId
	 */
	public Integer getAnswerId() {
		return answerId;
	}

	/**
	 * Set the objects answerId to parameter
	 * 
	 * @param answerId - Integer
	 */
	public void setAnswerId(Integer answerId) {
		this.answerId = answerId;
	}

	/**
	 * Get Answer value
	 * 
	 * @return - String answer
	 */
	public String getAnswer() {
		return answer;
	}

	/**
	 * Set answer value
	 * 
	 * @param answer - String
	 */
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	/**
	 * Get correct state
	 * 
	 * @return - Boolean
	 */
	public boolean isCorrect() {
		return correct;
	}

	/**
	 * Set correct state
	 * 
	 * @param correct - Boolean
	 */
	public void setCorrect(boolean correct) {
		this.correct = correct;
	}

	/**
	 * Get linked question object
	 * 
	 * @return - Question object
	 */
	public Question getQuestion() {
		return question;
	}

	/**
	 * Set linked question object
	 * 
	 * @param question - Question object
	 */
	public void setQuestion(Question question) {
		this.question = question;
	}

	/**
	 * Override toString method
	 */
	@Override
	public String toString() {
		return "Answer [answerId=" + answerId + ", answer=" + answer + ", correct=" + correct + ", question=" + question
				+ "]";
	}
	
}
