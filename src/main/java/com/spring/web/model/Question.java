package com.spring.web.model;

import java.util.List;
import java.util.ArrayList;

import com.spring.web.model.EntityEnum.CategoryTag;
import com.spring.web.model.EntityEnum.ContentTypeTag;
import com.spring.web.model.EntityEnum.FormatTag;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;

/**
 * the class for the Question model
 * 
 * @author cao vinh lam
 *
 */
@Entity
@NamedQuery(name="Question.findAll", query = "SELECT q FROM Question q")
public class Question {

	@Id
	@SequenceGenerator(name = "seqQuestion")
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "seqQuestion")
	private Integer questionId;
	
	private String questionContent;
	
	private boolean approved;
	
	private boolean reviewed;

	@Enumerated(EnumType.STRING)
	private CategoryTag categoryTag;
	
	@Enumerated(EnumType.STRING)
	private FormatTag formatTag;
	
	@Enumerated(EnumType.STRING)
	private ContentTypeTag contentTypeTag;
	
	@OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
	private List<Answer> answers;
	
	@ManyToOne
	@JoinColumn(name = "userId", referencedColumnName = "userId")
	private User user;
	
	@OneToMany(mappedBy = "questionID", cascade = CascadeType.ALL)
	private List<Notification> notifications;
	
	/**
	 * Empty constructor
	 */
	public Question() {}

	/**
	 * Question constructor with populated fields
	 * 
	 * @param questionContent - String containing the question content
	 * @param approved - boolean status for if a question is approved to be visible to all
	 * @param reviewed - boolean status for if a question has been reviewed by a trainer or sales
	 * @param categoryTag - An enum value that corresponds to the category of the question
	 * @param formatTag - An enum value that corresponds to the format of the question
	 * @param contentTypeTag - An enum value that corresponds to the content type of the question
	 * @param user - The user that owns/created the question
	 */
	public Question(String questionContent, boolean approved, boolean reviewed, CategoryTag categoryTag, FormatTag formatTag,
			ContentTypeTag contentTypeTag, User user) {
		this.questionContent = questionContent;
		this.approved = approved;
		this.reviewed = reviewed;
		this.categoryTag = categoryTag;
		this.formatTag = formatTag;
		this.contentTypeTag = contentTypeTag;
		this.user = user;
	}
	
	/**
	 * Getter for the question ID
	 * 
	 * @return Integer value of questionID
	 */
	public Integer getQuestionId() {
		return questionId;
	}

	/**
	 * Setter for question ID
	 * 
	 * @param questionId - Integer value to set question ID to
	 */
	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}


	/**
	 * Getter for question content
	 * 
	 * @return String value of question content
	 */
	public String getQuestionContent() {
		return questionContent;
	}

	/**
	 * Setter for question content
	 * 
	 * @param questionContent - setter for a questions content
	 */
	public void setQuestionContent(String questionContent) {
		this.questionContent = questionContent;
	}
	
	/**
	 * Getter for approved status
	 * 
	 * @return boolean value
	 */
	public boolean isApproved() {
		return approved;
	}

	/**
	 * Setter for approved status
	 * 
	 * @param approved - boolean
	 */
	public void setApproved(boolean approved) {
		this.approved = approved;
	}
	
	/**
	 * Getter for Reviewed status

	 * @return boolean value
	 */
	public boolean isReviewed() {
		return reviewed;
	}

	/**
	 * Setter for reviewed status
	 * 
	 * @param reviewed - boolean
	 */
	public void setReviewed(boolean reviewed) {
		this.reviewed = reviewed;
	}

	/**
	 * Getter for category tag
	 * 
	 * @return enum CategoryTag
	 */
	public CategoryTag getCategoryTag() {
		return categoryTag;
	}

	/**
	 * Setter for categoryTag
	 * 
	 * @param categoryTag - enum CategoryTag
	 */
	public void setCategoryTag(CategoryTag categoryTag) {
		this.categoryTag = categoryTag;
	}

	/**
	 * Getter for formatTag
	 * 
	 * @return - enum FormatTag
	 */
	public FormatTag getFormatTag() {
		return formatTag;
	}

	/**
	 * Setter for formatTag
	 * 
	 * @param formatTag - Enum FormatTag
	 */
	public void setFormatTag(FormatTag formatTag) {
		this.formatTag = formatTag;
	}

	/**
	 * Getter for contentTypeTag
	 * 
	 * @return - Enum ContentTypeTag
	 */
	public ContentTypeTag getContentTypeTag() {
		return contentTypeTag;
	}

	/**
	 * Setter for contentTypeTag
	 * 
	 * @param contentTypeTag - Enum ContentTypeTag
	 */
	public void setContentTypeTag(ContentTypeTag contentTypeTag) {
		this.contentTypeTag = contentTypeTag;
	}

	/**
	 * Getter for linked user object
	 * 
	 * @return - User Object
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Setter for linked user object
	 * 
	 * @param user - User Object
	 */
	public void setUser(User user) {
		this.user = user;
	}
	
	/**
	 * @return the answers
	 */
	public List<Answer> getAnswers() {
		return answers;
	}

	/**
	 * @param answers the answers to set
	 */
	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}

	/**
	 * @return the notifications
	 */
	public List<Notification> getNotifications() {
		return notifications;
	}

	/**
	 * @param notifications the notifications to set
	 */
	public void setNotifications(List<Notification> notifications) {
		this.notifications = notifications;
	}

	/**
	 * toostring method
	 */
	@Override
	public String toString() {
		return "Question [questionId=" + questionId + ", questionContent=" + questionContent + ", approved=" + approved
				+ ", reviewed=" + reviewed + ", categoryTag=" + categoryTag + ", formatTag=" + formatTag
				+ ", contentTypeTag=" + contentTypeTag + ", user=" + user + "]";
	}
	
}

