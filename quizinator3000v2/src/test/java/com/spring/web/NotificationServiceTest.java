package com.spring.web;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.spring.web.model.Notification;
import com.spring.web.model.Question;
import com.spring.web.model.Quiz;
import com.spring.web.model.User;
import com.spring.web.service.NotificationService;
import com.spring.web.service.QuestionService;
import com.spring.web.service.QuizService;
import com.spring.web.service.UserService;


@SpringBootTest
class NotificationServiceTest {

	@Autowired
	NotificationService notificationService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	QuizService quizService;
	
	@Autowired
	QuestionService questionService;
	
	Notification notification1;
	Notification notification2;
	Notification notification3;
	
	User user = new User();
	Quiz quiz = new Quiz();
	Question question = new Question();
	
	

	
	@BeforeEach
	void before() {
		notification1 = new Notification();
		notification2 = new Notification();
		notification3 = new Notification();
		
	}
	
	@Test
	void save_blank_notification_test() {
		notificationService.saveNotification(notification1);
		
		Notification fromDatabaseNotification = notificationService.getNotificationById(notification1.getNotificationID());
		assertEquals(notification1.getNotificationID(), fromDatabaseNotification.getNotificationID());
		
		notificationService.deleteById(notification1.getNotificationID());

	}
	
	@Test
	void save_filled_notification_test() {
		
		userService.saveOrUpdate(user);
		quizService.saveQuiz(quiz);
		questionService.saveOrUpdateQuestion(question);
		
		notification1.setMessage("1");
		notification1.setOldInfo("1");
		notification1.setNewInfo("1");
		
		notification1.setQuizID(quiz);
		notification1.setQuestionID(question);
		notification1.setUserID(user);
		notification1.setEditor("1");
		
		notificationService.saveNotification(notification1);
		Notification fromDatabase = notificationService.getNotificationById(notification1.getNotificationID());
		
		assertEquals(notification1.getNotificationID(), fromDatabase.getNotificationID());
		assertEquals(notification1.getMessage(), fromDatabase.getMessage());
		assertEquals(notification1.getOldInfo(), fromDatabase.getOldInfo());
		assertEquals(notification1.getNewInfo(), fromDatabase.getNewInfo());
		
		assertEquals(notification1.getQuizID().getQuizID(), fromDatabase.getQuizID().getQuizID());
		assertEquals(notification1.getQuestionID().getQuestionId(), fromDatabase.getQuestionID().getQuestionId());
		assertEquals(notification1.getUser().getUserId(), fromDatabase.getUser().getUserId());
		assertEquals(notification1.getEditor(), fromDatabase.getEditor());
		assertEquals(notification1.toString(), fromDatabase.toString());

		
		notificationService.deleteById(notification1.getNotificationID());
		userService.deleteById(user.getUserId());
		quizService.deleteById(quiz.getQuizID());
		questionService.deleteById(question.getQuestionId());
	}
	
	@Test
	void get_no_notification_with_ID_test() {
		notificationService.deleteById(1);
		
		Notification fromDatabase = notificationService.getNotificationById(1);
		assertEquals(null, fromDatabase);

	}
	
	
	@Test
	void delete_all_0_test() {
		notificationService.deleteAll();
		
		assertEquals(0,notificationService.deleteAll());
	}
	
	@Test
	void delete_all_3_test() {
		notificationService.deleteAll();
		
		notificationService.saveNotification(notification1);
		notificationService.saveNotification(notification2);
		notificationService.saveNotification(notification3);

		
		assertEquals(3,notificationService.deleteAll());

	}
	
	
	@Test
	void get_all_with_same_userID_0_test() {
		delete_all_0_test();
		
		userService.saveOrUpdate(user);
		assertEquals(0,notificationService.getAllNotificaitonsWithUserId(user.getUserId()).size());
		
		userService.deleteById(user.getUserId());

	}
	
	@Test
	void get_all_with_same_userID_2_test() {
		notificationService.deleteAll();
		userService.saveOrUpdate(user);
		
		notificationService.saveNotification(notification1);
		notificationService.saveNotification(notification2);
		notificationService.saveNotification(notification3);
		
		notification1.setUserID(user);
		notification3.setUserID(user);
		
		notificationService.updateNotification(notification1);
		notificationService.updateNotification(notification3);
		
		assertEquals(2,notificationService.getAllNotificaitonsWithUserId(user.getUserId()).size());
		
		notificationService.deleteAll();
		userService.deleteById(user.getUserId());
		

	}
	
	@Test
	void get_all_0_test() {
		delete_all_0_test();
		
		assertEquals(0,notificationService.getAllNotifications().size());
	}
	
	@Test
	void get_all_3_test() {
		delete_all_0_test();
		notificationService.saveNotification(notification1);
		notificationService.saveNotification(notification2);
		notificationService.saveNotification(notification3);
		
		assertEquals(3,notificationService.getAllNotifications().size());
		delete_all_0_test();

	}
	
	
	@Test
	void notification_parameter_constructor_with_updates_test() {		
		Notification notification4 = new Notification("1", question, quiz, user);
		
		notification4.setNotificationID(1);
		
		assertEquals(1,notification4.getNotificationID());

		assertEquals(quiz.getQuizID(),notification4.getQuizID().getQuizID());
		assertEquals(question.getQuestionId(),notification4.getQuestionID().getQuestionId());
		assertEquals(user.getUserId(),notification4.getUser().getUserId());
		
		
	}
	
	
	
	
	
	

}
