package com.spring.web.model;

import java.util.List;

import com.spring.web.model.EntityEnum.CategoryTag;
import com.spring.web.model.EntityEnum.ContentTypeTag;
import com.spring.web.model.EntityEnum.FormatTag;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

/**
 * 
 * @author The ARGONAUT
 *
 */
public class TempQuestion {

	private Integer questionId;
	
	private String questionContent;
	
	private boolean approved;
	
	private boolean reviewed;
	
	private CategoryTag categoryTag;
	
	private FormatTag formatTag;
	
	private ContentTypeTag contentTypeTag;
	
	private User user;
		
	private List<String> correctAnswers;
	
	private List<String> answers;
	
	private List<Answer> newAnswers;

	/**
	 * blank constructor for temp questions
	 */
	public TempQuestion() {};
	
	/**
	 * constructor for tempquestions
	 * 
	 * @param questionId - the id of a question
	 * @param questionContent - the content of the question
	 * @param approved - whether the question has been approved, if made by a trainer then it will be
	 * @param reviewed - whether that question has been reviewed, if made by a trainer then it will be
	 * @param categoryTag - the category the question is made in - course/interview
	 * @param formatTag - the format of the question - multi choice/long short answer etc 
	 * @param contentTypeTag - the content type of the question, java/sql/interview specific
	 * @param user - the user that is creating the question
	 */
	public TempQuestion(Integer questionId, String questionContent, boolean approved, boolean reviewed, CategoryTag categoryTag,
			FormatTag formatTag, ContentTypeTag contentTypeTag, User user) {
		super();
		this.questionId = questionId;
		this.questionContent = questionContent;
		this.approved = approved;
		this.categoryTag = categoryTag;
		this.formatTag = formatTag;
		this.contentTypeTag = contentTypeTag;
		this.user = user;
	}

	/**
	 * getter for question content
	 * 
	 * @return returns the question content
	 */
	public String getQuestionContent() {
		return questionContent;
	}
	
	/**
	 * getter for question id
	 * 
	 * @return - returns the quesiton id
	 */
	public Integer getQuestionId() {
		return questionId;
	}

	/**
	 * setter for quesiton id 
	 * 
	 * @param questionId - the question id to set it quesiton id too
	 */
	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}

	/**
	 * setter for question content 
	 * 
	 * @param questionContent - the question content to set the question content too
	 */
	public void setQuestionContent(String questionContent) {
		this.questionContent = questionContent;
	}

	/**
	 * getter for approved
	 * 
	 * @return - reutns whether the question is approved
	 */
	public boolean isApproved() {
		return approved;
	}

	/**
	 * setter for approved
	 * 
	 * @param approved - the value to set the question too
	 */
	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	/**
	 * getter for reviewed
	 * 
	 * @return - returns whether the question has been reviewed
	 */
	public boolean isReviewed() {
		return reviewed;
	}

	/**
	 * setter for reviewed
	 * 
	 * @param reviewed - the value to set the reviewed status too
	 */
	public void setReviewed(boolean reviewed) {
		this.reviewed = reviewed;
	}

	/**
	 * getter for category tag
	 * 
	 * @return - returns the category tag
	 */
	public CategoryTag getCategoryTag() {
		return categoryTag;
	}

	/**
	 * setter for category tag
	 * 
	 * @param categoryTag - the category tag to set the question too
	 */
	public void setCategoryTag(CategoryTag categoryTag) {
		this.categoryTag = categoryTag;
	}

	/**
	 * getter for format tag
	 * 
	 * @return - returns the format of the question
	 */
	public FormatTag getFormatTag() {
		return formatTag;
	}

	/**
	 * setter for format tag
	 * 
	 * @param formatTag - the format to set the question too
	 */
	public void setFormatTag(FormatTag formatTag) {
		this.formatTag = formatTag;
	}

	/**
	 * getter for content type
	 * 
	 * @return - returns the content type of the question
	 */
	public ContentTypeTag getContentTypeTag() {
		return contentTypeTag;
	}

	/**
	 * setter for type page
	 * 
	 * @param contentTypeTag - the type of content to set the question too
	 */
	public void setContentTypeTag(ContentTypeTag contentTypeTag) {
		this.contentTypeTag = contentTypeTag;
	}

	/**
	 * getter for user
	 * 
	 * @return - returns the user who created the question
	 */
	public User getUser() {
		return user;
	}

	/**
	 * setter for user
	 * 
	 * @param user - sets the user who is making the question
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * getter for correct answers
	 * 
	 * @return - returns a list of the correct answers
	 */
	public List<String> getCorrectAnswers() {
		return correctAnswers;
	}

	/**
	 * setter for correct answers
	 * 
	 * @param correctAnswers - used to set the list of correct answers
	 */
	public void setCorrectAnswers(List<String> correctAnswers) {
		this.correctAnswers = correctAnswers;
	}

	/**
	 * getter for answers
	 * 
	 * @return - used to get all the answers
	 */
	public List<String> getAnswers() {
		return answers;
	}

	/**
	 * setter for answers
	 * 
	 * @param answers - used to set all the answers
	 */
	public void setAnswers(List<String> answers) {
		this.answers = answers;
	}

	/**
	 * getter for new answers
	 * 
	 * @return - returns a list of the new answers 
	 */
	public List<Answer> getNewAnswers() {
		return newAnswers;
	}

	/**
	 * hi doland - search in the notifications model
	 */
	
	/**
	 * setter for new answers
	 * 
	 * @param newAnswers - takes a list of answers to set the new answers too
	 */
	public void setNewAnswers(List<Answer> newAnswers) {
		this.newAnswers = newAnswers;
	}

	/**
	 * the method for tostring
	 */
	@Override
	public String toString() {
		return "TempQuestion [questionId=" + questionId + ", questionContent=" + questionContent + ", approved="
				+ approved + ", reviewed=" + reviewed + ", categoryTag=" + categoryTag + ", formatTag=" + formatTag
				+ ", contentTypeTag=" + contentTypeTag + ", user=" + user + ", correctAnswers=" + correctAnswers
				+ ", answers=" + answers + ", newAnswers=" + newAnswers + "]";
	}
	
}
