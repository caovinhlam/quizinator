package com.spring.web;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.ClassOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.ui.Model;

import com.spring.web.Quizinator3000v2Application;
import com.spring.web.model.Answer;
import com.spring.web.model.Notification;
import com.spring.web.model.Question;
import com.spring.web.model.Quiz;
import com.spring.web.model.User;
import com.spring.web.model.EntityEnum.CategoryTag;
import com.spring.web.model.EntityEnum.ContentTypeTag;
import com.spring.web.model.EntityEnum.FormatTag;
import com.spring.web.model.EntityEnum.UserRole;
import com.spring.web.service.AnswerService;
import com.spring.web.service.NotificationService;
import com.spring.web.service.QuestionAttemptService;
import com.spring.web.service.QuestionService;
import com.spring.web.service.QuizService;
import com.spring.web.service.SubmissionService;
import com.spring.web.service.UserService;

import jakarta.annotation.security.RunAs;
import jakarta.servlet.http.HttpSession;

@SpringBootTest
class QuizServiceTests {
	
	@Autowired
	private AnswerService answerService;
	
	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private QuizService quizService;
	
	@Autowired
	private SubmissionService submissionService;
	
	@Autowired
	private QuestionAttemptService questionAttemptService;
	
	@Autowired
	private UserService userService;
	
	
	Quiz quiz1;
	Quiz quiz2;
	Quiz quiz3;
	Quiz quiz4;
	User user = new User();
	List<Question> questions;
	
	
	/**
	 * To clear the database, uncomment the code, run the test and then recomment 
	 * the code and run the setup test to populate DB with dummy data 
	 */
	@Test
	void test_ClearDatabase() {
		
		questionAttemptService.deleteAll();
		notificationService.deleteAll();
		submissionService.deleteAll();
		quizService.deleteAll();
		answerService.deleteAllAnswers();
		questionService.deleteAllQuestions();
		userService.deleteAllUsers();
		
	}
	
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Save Quizzes ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	
	@Test
	void save_blank_quiz() {
		quiz1 = new Quiz();
		quizService.saveQuiz(quiz1);
		
		Quiz fromDB = quizService.getQuizById(quiz1.getQuizID());
		assertEquals(fromDB.getQuizID(), quiz1.getQuizID());
		delete_quiz_by_id(fromDB.getQuizID());
		assertEquals(null, quizService.getQuizById(quiz1.getQuizID()));	
	}
	
	@Test
	void save_manually_filled_quiz() {
		userService.saveOrUpdate(user);
		questions = new ArrayList<>();
		quiz1 = new Quiz();

		
		quiz1.setAllowedTime(10);
		quiz1.setApproved(false);
		quiz1.setNumberOfQuestions(5);
		quiz1.setQuestions(questions);
		quiz1.setQuizName("name1");
		quiz1.setQuizType(CategoryTag.COURSE_CONTENT);
		quiz1.setReviewed(true);
		quiz1.setTotalposmarks(5);
		quiz1.setUser(user);
		quiz1.setUser(user);
		
		
		quizService.saveQuiz(quiz1);
		
		Quiz fromDB = quizService.getQuizById(quiz1.getQuizID());
		
		assertEquals(fromDB.getAllowedTime(), quiz1.getAllowedTime());
		assertEquals(fromDB.isApproved(), quiz1.isApproved());
		assertEquals(fromDB.getNumberOfQuestions(), quiz1.getNumberOfQuestions());
		assertEquals(fromDB.getQuestions().size(), quiz1.getQuestions().size());
		assertEquals(fromDB.getQuizName(), quiz1.getQuizName());
		assertEquals(fromDB.getQuizType(), quiz1.getQuizType());
		assertEquals(fromDB.isReviewed(), quiz1.isReviewed());
		assertEquals(fromDB.getTotalposmarks(), 0);
		assertEquals(fromDB.getUser().getUserId(), quiz1.getUser().getUserId());
		assertEquals(fromDB.getUser().getUserId(), quiz1.getUser().getUserId());
		assertEquals(fromDB.toString(), quiz1.toString());

		
		
		
		delete_quiz_by_id(fromDB.getQuizID());
		assertEquals(null, quizService.getQuizById(quiz1.getQuizID()));
		
	}
	
	
	
	@Test
	void save_auto_filled_quiz() {
		userService.saveOrUpdate(user);
		questions = new ArrayList<>();
		
		
		quiz1 = new Quiz("name2", CategoryTag.INTERVIEW_PREP, user,questions,6,true);
		quizService.saveQuiz(quiz1);
		
		Quiz fromDB = quizService.getQuizById(quiz1.getQuizID());
		assertEquals(fromDB.getQuizID(), quiz1.getQuizID());
		
		delete_quiz_by_id(fromDB.getQuizID());
		assertEquals(fromDB.getQuizName(), quiz1.getQuizName());
		assertEquals(fromDB.getQuizType(), quiz1.getQuizType());
		assertEquals(fromDB.isReviewed(), quiz1.isReviewed());
		assertEquals(fromDB.getUser().getUserId(), quiz1.getUser().getUserId());
		assertEquals(fromDB.getQuestions().size(), quiz1.getQuestions().size());
		assertEquals(fromDB.getAllowedTime(), quiz1.getAllowedTime());
		assertEquals(fromDB.isReviewed(), quiz1.isReviewed());
		
		
		assertEquals(null, quizService.getQuizById(quiz1.getQuizID()));
		
	}
	// ------------------------------- Update Quiz --------------------
	
	@Test
	void set_ID_test() {
		userService.saveOrUpdate(user);
		
		quiz1 = new Quiz();
		quizService.saveQuiz(quiz1);
		
		int original = quiz1.getQuizID();
		
		quiz1.setQuizID(original +1);
		
		quizService.updateQuiz(quiz1);

		Quiz fromData = quizService.getQuizById(original+1);
		assertEquals(fromData.getQuizID(), original+1);

	}
	
	
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Get Quizzes ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	@Test
	void get_single() {
		save_blank_quiz();
	}
	
	@Test
	void get_all() {
		delete_all_quiz();
		assertEquals(0, quizService.getAllQuiz().size());

		
		quiz1 = new Quiz();
		quizService.saveQuiz(quiz1);
		
		quiz2 = new Quiz();
		quizService.saveQuiz(quiz2);
		
		quiz3 = new Quiz();
		quizService.saveQuiz(quiz3);
		
		quiz4 = new Quiz();
		quizService.saveQuiz(quiz4);
		
		assertEquals(4, quizService.getAllQuiz().size());
		
		delete_all_quiz();
		assertEquals(0, quizService.getAllQuiz().size());
	}
	
	@Test
	void get_all_approved() {
		delete_all_quiz();
		assertEquals(0, quizService.getAllQuiz().size());

		
		quiz1 = new Quiz();
		quizService.saveQuiz(quiz1);
		
		quiz2 = new Quiz();
		quiz2.setApproved(true);
		quizService.saveQuiz(quiz2);
		
		quiz3 = new Quiz();
		quizService.saveQuiz(quiz3);
		
		quiz4 = new Quiz();
		quiz4.setApproved(true);
		quizService.saveQuiz(quiz4);
		
		assertEquals(2, quizService.getAllApprovedQuizzes().size());
		
		delete_quiz_by_id(quiz1.getQuizID());
		assertEquals(2, quizService.getAllApprovedQuizzes().size());
		
		delete_quiz_by_id(quiz4.getQuizID());
		assertEquals(1, quizService.getAllApprovedQuizzes().size());
		
		
	}
	
	@Test
	void get_different_reviewed_status () {
		delete_all_quiz();
		assertEquals(0, quizService.getreviewedQuizzes().size());
		assertEquals(0, quizService.getUnreviewedQuizzes().size());
		
		quiz1 = new Quiz();
		quiz1.setReviewed(false);
		quizService.saveQuiz(quiz1);
		
		quiz2 = new Quiz();
		quiz2.setReviewed(true);
		quizService.saveQuiz(quiz2);
		
		quiz3 = new Quiz();
		quiz3.setReviewed(false);
		quizService.saveQuiz(quiz3);
		
		quiz4 = new Quiz();
		quiz4.setReviewed(true);
		quizService.saveQuiz(quiz4);
		
		assertEquals(2, quizService.getreviewedQuizzes().size());
		assertEquals(2, quizService.getUnreviewedQuizzes().size());

		delete_quiz_by_id(quiz1.getQuizID());
		assertEquals(2, quizService.getreviewedQuizzes().size());
		assertEquals(1, quizService.getUnreviewedQuizzes().size());
		
		delete_quiz_by_id(quiz4.getQuizID());
		assertEquals(1, quizService.getreviewedQuizzes().size());
		assertEquals(1, quizService.getUnreviewedQuizzes().size());
		
		delete_all_quiz();
		assertEquals(0, quizService.getreviewedQuizzes().size());
		assertEquals(0, quizService.getUnreviewedQuizzes().size());		
	}
	
	
	@Test
	void get_all_with_userID() {
		userService.saveOrUpdate(user);
		delete_all_quiz();
		assertEquals(0, quizService.getAllQuizsWithUserId(user.getUserId()).size());

		
		quiz1 = new Quiz();
		quiz1.setUser(user);
		quizService.saveQuiz(quiz1);
		
		quiz2 = new Quiz();
		quizService.saveQuiz(quiz2);
		
		quiz3 = new Quiz();
		quiz3.setUser(user);
		quizService.saveQuiz(quiz3);
		
		quiz4 = new Quiz();
		quiz4.setUser(user);
		quizService.saveQuiz(quiz4);
		
		assertEquals(3, quizService.getAllQuizsWithUserId(user.getUserId()).size());
		delete_quiz_by_id(quiz2.getQuizID());
		delete_quiz_by_id(quiz1.getQuizID());
		assertEquals(2, quizService.getAllQuizsWithUserId(user.getUserId()).size());

	}
	
	
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Delete Quizzes ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	
	void delete_quiz_by_id(int id) {
		quizService.deleteById(id);
	}
	
	
	
	void delete_all_quiz() {
		quizService.deleteAll();
	}
	
	
}
