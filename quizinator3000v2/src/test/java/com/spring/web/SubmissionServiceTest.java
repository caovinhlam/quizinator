package com.spring.web;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.spring.web.model.Quiz;
import com.spring.web.model.Submission;
import com.spring.web.model.User;
import com.spring.web.model.EntityEnum.CategoryTag;
import com.spring.web.service.QuizService;
import com.spring.web.service.SubmissionService;
import com.spring.web.service.UserService;

@SpringBootTest
class SubmissionServiceTest {

	@Autowired
	SubmissionService submissionService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	QuizService quizService;
	
	@MockBean
	Submission submission;
	
	
	@BeforeEach
	void setup(){
		submissionService.deleteAll();
		quizService.deleteAll();
		userService.deleteAllUsers();
	}
	
	@Test
	void test_submission_created() {
		submissionService.saveOrUpdate(submission);

		List<Submission> submissions =  submissionService.getAllSubmissions();
		assertEquals(1, submissions.size());
		
	}
	
	@Test
	void test_submission_getById_return_null() {
		assertNull(submissionService.getSubmissionById(999));
	}
	
	@Test
	void test_submission_getById_return_submission() {
		submissionService.saveOrUpdate(submission);
		List<Submission> submissions =  submissionService.getAllSubmissions();
		assertEquals(1, submissions.size());
		Submission submissionTest = submissions.get(0); 
		assertNotNull(submissionService.getSubmissionById(submissionTest.getSubmissionID()));
	}

	@Test
	void test_submission_deleteById() {
		submissionService.saveOrUpdate(submission);
	
		List<Submission> submissions = submissionService.getAllSubmissions();
		assertEquals(1, submissions.size());
		Submission submissionTest = submissions.get(0);

		submissionService.deleteById(submissionTest.getSubmissionID());
		submissions = submissionService.getAllSubmissions();

		assertEquals(0, submissions.size());
	}

	
	@Test
	void test_submission_get_course_and_interview_content() {
		User user = new User();
		userService.saveOrUpdate(user);
		
		assertEquals(0, submissionService.getAllCourseContentSubmissions(user.getUserId()).size());
		assertEquals(0, submissionService.getAllInterviewSubmissions(user.getUserId()).size());

		
		Submission submission1 = new Submission();
		Submission submission2 = new Submission();
		Submission submission3 = new Submission();
		Submission submission4 = new Submission();
		
		Quiz quizInterview = new Quiz();
		quizInterview.setQuizType(CategoryTag.INTERVIEW_PREP);
		Quiz quizCourse = new Quiz();
		quizCourse.setQuizType(CategoryTag.COURSE_CONTENT);
		
		quizService.saveQuiz(quizCourse);
		quizService.saveQuiz(quizInterview);
		
		submission1.setQuiz(quizCourse);
		submission2.setQuiz(quizCourse);
		submission3.setQuiz(quizInterview);
		submission4.setQuiz(quizInterview);
		
		submission1.setUserId(user);
		submission2.setUserId(user);
		submission3.setUserId(user);
		submission4.setUserId(user);

		submissionService.saveOrUpdate(submission1);
		submissionService.saveOrUpdate(submission2);
		submissionService.saveOrUpdate(submission3);
		submissionService.saveOrUpdate(submission4);
		
		
		
		assertEquals(2, submissionService.getAllCourseContentSubmissions(user.getUserId()).size());
		assertEquals(2, submissionService.getAllInterviewSubmissions(user.getUserId()).size());
	}
	
	@Test
	void test_submission_get_by_userId() {
		User user = new User();
		userService.saveOrUpdate(user);
		
		assertEquals(0, submissionService.getAllCourseContentSubmissions(user.getUserId()).size());
		
		Submission submission1 = new Submission();
		Submission submission2 = new Submission();
		Submission submission3 = new Submission();
		Submission submission4 = new Submission();
		
		submission1.setUserId(user);
		submission2.setUserId(user);
		submission4.setUserId(user);

		submissionService.saveOrUpdate(submission1);
		submissionService.saveOrUpdate(submission2);
		submissionService.saveOrUpdate(submission3);
		submissionService.saveOrUpdate(submission4);
			
		assertEquals(3, submissionService.getAllSubmissionsFromUser(user.getUserId()).size());
		submissionService.deleteById(submission1.getSubmissionID());
		assertEquals(2, submissionService.getAllSubmissionsFromUser(user.getUserId()).size());
		submissionService.deleteAll();
		assertEquals(0, submissionService.getAllSubmissionsFromUser(user.getUserId()).size());
	}
	
}
