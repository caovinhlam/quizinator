package com.spring.web.model;

import java.util.List;

import com.spring.web.model.EntityEnum.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;

/**
 * this is the class for the user model
 * 
 * @author cao vinh lam
 *
 */
@Entity
public class User {

	@Id
	@SequenceGenerator(name = "seqUser")
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "seqUser")
	private int userId;
	
	private String name;
	private String email;
	private String password;
	@Enumerated(EnumType.STRING)
	private UserRole userRole;
	
	@OneToMany(mappedBy = "user")
	private List<Notification> notifications;
	
	@ManyToMany
	@JoinTable(
			name="locked_questions",
			joinColumns = {@JoinColumn(name="FK_USER_ID")},
			inverseJoinColumns = {@JoinColumn(name="FK_QUESTION_ID")}
			)
	private List<Question> lockedQuestions;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name="favourited_quizzes",
			joinColumns = {@JoinColumn(name="FK_USER_ID")},
			inverseJoinColumns = {@JoinColumn(name="FK_QUIZ_ID")}
			)
	private List<Quiz> favouriteQuizzes;
	
	
	/**
	 * blank user contructor
	 */
	public User() {
		super();
	}

	/**
	 * the constructor for a user object
	 * 
	 * @param name - the name of a user 
	 * @param email - the email for a user
	 * @param password - the password for a user
	 * @param userRole - the role of a user
	 */
	public User(String name, String email, String password, UserRole userRole) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		this.userRole = userRole;
	}

	/**
	 * getter for user id
	 * 
	 * @return returns the user id
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * setter for user id
	 * 
	 * @param userId - the id of a user to set
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	/**
	 * getter for the name
	 * 
	 * @return returns the name of a user
	 */
	public String getName() {
		return name;
	}

	/**
	 * setter for name
	 * 
	 * @param name - the name of a user to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * getter for email
	 * 
	 * @return - the email address of a user
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * setter for email
	 * 
	 * @param email - the email to set the users too
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * getter for password
	 * 
	 * @return - the password of a user entity
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * setter for password
	 * 
	 * @param password - the password of a user to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * getter for user role
	 * 
	 * @return returns the user role
	 */
	public UserRole getUserRole() {
		return userRole;
	}

	/**
	 * setter for user role
	 * 
	 * @param userRole - the userrole of a student 
	 */
	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}
	
	/**
	 * getter for notifications for user
	 * 
	 * @return returns list of Notifications
	 */
	public List<Notification> getNotifications() {
		return notifications;
	}

	/**
	 * setter for Notifications
	 * 
	 * @param notifications - list of notifications objects
	 */
	public void setNotifications(List<Notification> notifications) {
		this.notifications = notifications;
	}
	
	/**
	 * getter for the locked questions
	 * 
	 * @return - returns a list of all the locked questions a user has
	 */
	public List<Question> getLockedQuestions() {
		return lockedQuestions;
	}

	/**
	 * setter for the loccked questions a user has
	 * 
	 * @param lockedQuestions - a list of all the questions a user is going to lock
	 */
	public void setLockedQuestions(List<Question> lockedQuestions) {
		this.lockedQuestions = lockedQuestions;
	}
	
	/**
	 * Returns the list of all users favourited quizzes
	 * 
	 * @return list of favourite quizzes
	 */
	public List<Quiz> getFavouriteQuizzes() {
		return favouriteQuizzes;
	}

	/**
	 * Sets the list for all users favourited quizzes
	 * 
	 * @param favouriteQuizzes - List of Quiz
	 */
	public void setFavouriteQuizzes(List<Quiz> favouriteQuizzes) {
		this.favouriteQuizzes = favouriteQuizzes;
	}
	
	/**
	 * the to string method
	 */
	@Override
	public String toString() {
		return "User [userId=" + userId + ", name=" + name + ", email=" + email + ", userRole=" + userRole + "]";
	}
	
}
