package com.spring.web.model;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;

/**
 * this class is the class that handles the submission database entity
 * 
 * @author Stanley chilton
 *
 */
@Entity
public class Submission {
	@Id
//	@SequenceGenerator(name = "seqSubmission")
//	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "seqSubmission")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer submissionID;
	
	@ManyToOne
	@JoinColumn(name = "quizID", referencedColumnName = "quizID")
	private Quiz quiz;
	
	@ManyToOne
	@JoinColumn(name = "userId", referencedColumnName = "userId")
	private User userId;
	
	private int visability;

	@OneToMany(mappedBy = "submission")
	private List<QuestionAttempt> questionAttempt;
	
	private int mark;
	
	/**
	 * this is the blank constructor for a submission
	 */
	public Submission() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

	/**
	 * this is the initial constructor for a submission
	 * 
	 * @param quiz - the link to a quiz entity
	 * @param user - the link to a student entity
	 * @param visability - whether the skill is archived or not
	 * @param questionAttempt - the link to all the question attempts
	 * @param mark - the total marks earned for a submission
	 */
	public Submission(Quiz quiz, User user, int visability, List<QuestionAttempt> questionAttempt, int mark) {
		this.quiz = quiz;
		this.userId = user;
		this.visability = 1;
		this.questionAttempt = questionAttempt;
		this.mark = mark;
	}



	/**
	 * id getter
	 * 
	 * @return the submissionID
	 */
	public Integer getSubmissionID() {
		return submissionID;
	}

	/**
	 * id setter 
	 * @param submissionID the submissionID to set
	 */
	public void setSubmissionID(Integer submissionID) {
		this.submissionID = submissionID;
	}

	/**
	 * quiz getter 
	 * 
	 * @return the quiz
	 */
	public Quiz getQuiz() {
		return quiz;
	}

	/**
	 * quiz setter
	 * 
	 * @param quiz the quiz to set
	 */
	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
	}



	/**
	 * getter for userid 
	 * 
	 * @return the userId
	 */
	public User getUserId() {
		return userId;
	}



	/**
	 * setter for userID
	 * 
	 * @param userId the userId to set
	 */
	public void setUserId(User userId) {
		this.userId = userId;
	}



	/**
	 * visability getter
	 * 
	 * @return the visability
	 */
	public int getVisability() {
		return visability;
	}



	/**
	 * viability setter
	 * 
	 * @param visability the visability to set
	 */
	public void setVisability(int visability) {
		this.visability = visability;
	}
	
	/**
	 * getter for getting the list of quesiton attempt
	 * 
	 * @return - returns the list of question attempts
	 */
	public List<QuestionAttempt> getQuestionAttempt() {
		return questionAttempt;
	}


	/**
	 * setter used for question attempt
	 * 
	 * @param questionAttempt - a list of the quesiton attempts to link to this submission
	 */
	public void setQuestionAttempt(List<QuestionAttempt> questionAttempt) {
		this.questionAttempt = questionAttempt;
	}
	
	
	/**
	 * returns the marks for the specific quiz entity
	 * 
	 * @return - returns the mark for that specific quiz attempt
	 */
	public int getMark() {
		return mark;
	}


	/**
	 * the setter for the mark value of the quiz submission
	 * 
	 * @param mark - the amount of marks to set the var too
	 */
	public void setMark(int mark) {
		this.mark = mark;
	}



	/**
	 * hash code getter
	 */
	@Override
	public int hashCode() {
		return Objects.hash(submissionID);
	}

	/**
	 * comparison between objects
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Submission other = (Submission) obj;
		return Objects.equals(submissionID, other.submissionID);
	}

	@Override
	public String toString() {
		return "Submission [submissionID=" + submissionID + ", quiz=" + quiz + ", userId=" + userId + ", visability="
				+ visability + ", questionAttempt=" + questionAttempt + ", mark=" + mark + "]";
	}
}
