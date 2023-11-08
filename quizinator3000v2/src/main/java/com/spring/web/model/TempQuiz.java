package com.spring.web.model;

import java.util.List;

import com.spring.web.model.EntityEnum.CategoryTag;
import com.spring.web.model.EntityEnum.ContentTypeTag;
import com.spring.web.model.EntityEnum.FormatTag;

/**
 * this entity is used for the storage of temp quiz data
 * which is then passed to an actual quiz and other places required to be saved in the
 * DB
 * 
 * @author Caovinh lam, christian lee
 *
 */
public class TempQuiz {
	
	private int quizID;
	
	private String quizName;
	
	private CategoryTag quizType;
	
	private User user;
	
	private List<Question> questions;
	
	private boolean noTimeLimit;

	private int allowedTime;
	
	private boolean reviewed;
	
	private List<ContentTypeTag> selectContents;
	
	private List<FormatTag> selectFormats;
	
	private List<CategoryTag> quizCategories;

	/**
	 * Empty Quiz constructor
	 */
	public TempQuiz() {
		super();

	}
	
    /**
     * setter for QuizID
     * 
     * @return quizID - int value 
     */
	public int getQuizID() {
		return quizID;
	}

	/**
	 * getter for QuizId
	 * 
	 * @param quizID - int value for id
	 */
	public void setQuizID(int quizID) {
		this.quizID = quizID;
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
	 * setter for questions
	 * 
	 * @param questions - list of questions inside quiz
	 */
	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	/**
	 * getter for the reviewed variable
	 * 
	 * @return returns the value of reviewed, boolean
	 */
	public boolean isReviewed() {
		return reviewed;
	}

	/**
	 * setter for the reviewed variable
	 * 
	 * @param reviewed - the reviewed value to set the value to
	 */
	public void setReviewed(boolean reviewed) {
		this.reviewed = reviewed;
	}

	/**
	 * getter for the user who made the quiz
	 * 
	 * @return the user who created the quiz
	 */
	public User getUser() {
		return user;
	}

	/**
	 * setter for the user who created the quiz
	 * 
	 * @param user - the user to set the creator too 
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * getter for the quiz name
	 * 
	 * @return the name of the quiz
	 */
	public String getQuizName() {
		return quizName;
	}

	/**
	 * setter for the quiz name
	 * 
	 * @param quizName the quizName to set
	 */
	public void setQuizName(String quizName) {
		this.quizName = quizName;
	}
	
	/**
	 * getter for the quiz type
	 * 
	 * @return - returns the quiz type
	 */
	public CategoryTag getQuizType() {
		return quizType;
	}

	/**
	 * setter for the quiz type
	 * 
	 * @param quizType the quizType to set
	 */
	public void setQuizType(CategoryTag quizType) {
		this.quizType = quizType;
	}
	
	/**
	 * getter for boolean if there is no time limit
	 * 
	 * @return - whether a time limit was set or not
	 */
	public boolean isNoTimeLimit() {
		return noTimeLimit;
	}
	
	/**
	 * setter for the notimelimit var, sets to true or false if the user set a time or not
	 * 
	 * @param noTimeLimit - true or false whether the time was set
	 */
	public void setNoTimeLimit(boolean noTimeLimit) {
		this.noTimeLimit = noTimeLimit;
	}
	
	/**
	 * setter for the allowed time
	 * 
	 * @param allowedTime the time to set the allowed time too
	 */
	public void setAllowedTime(int allowedTime) {
		this.allowedTime = allowedTime;
	}

	/**
	 * getter for the allowed time
	 * 
	 * @return the Time allowed for the quiz
	 */
	public int getAllowedTime() {
		return allowedTime;
	}
	
	/**
	 * getter for the contents
	 * 
	 * @return returns a list of the contenttypes
	 */
    public List<ContentTypeTag> getSelectContents() {
        return selectContents;
    }

    /**
     * setter for the contents
     * 
     * @param selectContents - a list of the values to set the contents too
     */
    public void setSelectContents(List<ContentTypeTag> selectContents) {
        this.selectContents = selectContents;
    }

    /**
     * getter for the formats
     * 
     * @return - returns a list of formats
     */
	public List<FormatTag> getSelectFormats() {
		return selectFormats;
	}

	/**
	 * setter for the formats
	 * 
	 * @param selectFormats - a list of the formats to set the var too
	 */
	public void setSelectFormats(List<FormatTag> selectFormats) {
		this.selectFormats = selectFormats;
	}

	/**
	 * getter for the categories
	 * 
	 * @return - returns a list of the categoreies
	 */
	public List<CategoryTag> getQuizCategories() {
		return quizCategories;
	}

	/**
	 * setter for the categories
	 * 
	 * @param quizCategories - a list of the categories to set the var too
	 */
	public void setQuizCategories(List<CategoryTag> quizCategories) {
		this.quizCategories = quizCategories;
	}
	
	/**
	 * returns a string of all the variables, used for testing purposes
	 */
	@Override
	public String toString() {
		return "TempQuiz [quizName=" + quizName + ", quizType=" + quizType + ", user=" + user + ", questions="
				+ questions + ", allowedTime=" + allowedTime + ", reviewed=" + reviewed + ", selectContents="
				+ selectContents + ", selectFormats=" + selectFormats + ", quizCategories=" + quizCategories + "]";
	}
	
}
