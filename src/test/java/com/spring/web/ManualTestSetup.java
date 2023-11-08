package com.spring.web;


import java.util.ArrayList;
import java.util.List;

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

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ManualTestSetup {

	@Value(value="${local.server.port}")
	private int port;
	
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
	
	@Test
	void test_ManualTestSetup() {
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		
		
		/***************************** SETUP USERS *******************************/
		
//		User user1 = new User("test1", "test1@gmail.com", passwordEncoder.encode("password1"), UserRole.TRAINING);
//		User user2 = new User("test2", "test2@gmail.com", passwordEncoder.encode("password2"), UserRole.POND);
//		User user3 = new User("test3", "test3@gmail.com", passwordEncoder.encode("password3"), UserRole.BEACHED);
//		User user4 = new User("test4", "test4@gmail.com", passwordEncoder.encode("password4"), UserRole.ABSENT);
//		
//		User user5 = new User("test5", "test5@gmail.com", passwordEncoder.encode("test5@gmail.com"), UserRole.TRAINER);
//		User user6 = new User("test6", "test6@gmail.com", passwordEncoder.encode("test6@gmail.com"), UserRole.SALES);
		
		
		//Power Rangers are students
		User redRanger = new User("Red Power Ranger", "redranger@mail.com", passwordEncoder.encode("red"), UserRole.TRAINING);
		User blueRanger = new User("Blue Power Ranger", "blueranger@mail.com", passwordEncoder.encode("blue"), UserRole.POND);
		User yellowRanger = new User("Yellow Power Ranger", "yellowranger@mail.com", passwordEncoder.encode("yellow"), UserRole.BEACHED);
		User greenRanger = new User("Green Power Ranger", "greenranger@mail.com", passwordEncoder.encode("green"), UserRole.ABSENT);
		
		//Transformers are trainers
		User optimusPrime = new User("Optimus Prime", "optimusprime@hotmail.com", passwordEncoder.encode("optimusprime@hotmail.com"), UserRole.TRAINER);
		User bumblebee = new User("BumbleBee", "bumblebee@hotmail.com", passwordEncoder.encode("bumblebee@hotmail.com"), UserRole.TRAINER);
		User megatron = new User("Megatron", "megatron@hotmail.com", passwordEncoder.encode("megatron@hotmail.com"), UserRole.TRAINER);
		
		//Dinosaurs are sales
		User trex = new User("T-Rex", "trex@outlook.com", passwordEncoder.encode("trex@outlook.com"), UserRole.SALES);
		User triceratops = new User("Triceratops", "triceratops@outlook.com", passwordEncoder.encode("triceratops@outlook.com"), UserRole.SALES);
		
		/***************************** END SETUP USERS ***************************/
		
		
		
		
		
		/**************************************** USER CREATED QUESTIONS ********************************/
		
		Question SQLquestion1 = new Question("What two SQL keywords do you use to designate what fields to retrieve and which table to retrieve them from?", true, true, CategoryTag.COURSE_CONTENT, FormatTag.MULTI_ANSWER, ContentTypeTag.SQL, redRanger);
		Answer SQLanswer1_1 = new Answer("SELECT", true, SQLquestion1);
		Answer SQLanswer1_2 = new Answer("FROM", true, SQLquestion1);
		Answer SQLanswer1_3 = new Answer("GROUP BY", false, SQLquestion1);
		Answer SQLanswer1_4 = new Answer("ORDER BY", false, SQLquestion1);
		
		Question SQLquestion2 = new Question("What SQL keyword do you use to specify which two fields match on a join?", true, true, CategoryTag.COURSE_CONTENT, FormatTag.MULTI_CHOICE, ContentTypeTag.SQL, redRanger);
		Answer SQLanswer2_1 = new Answer("WHERE", false, SQLquestion2);
		Answer SQLanswer2_2 = new Answer("FROM", false, SQLquestion2);
		Answer SQLanswer2_3 = new Answer("ON", true, SQLquestion2);
		Answer SQLanswer2_4 = new Answer("UNION", false, SQLquestion2);
		
		Question SQLquestion3 = new Question("What SQL query will fail to run?", true, true, CategoryTag.COURSE_CONTENT, FormatTag.MULTI_CHOICE, ContentTypeTag.SQL, redRanger);
		Answer SQLanswer3_1 = new Answer("SELECT * FROM users", false, SQLquestion3);
		Answer SQLanswer3_2 = new Answer("SELECT c.name FROM clients c", false, SQLquestion3);
		Answer SQLanswer3_3 = new Answer("SELECT * FROM users u INNER JOIN clients c ON c.userid = u.userid", false, SQLquestion3);
		Answer SQLanswer3_4 = new Answer("SELECT * FROM users u INNER JOIN clients c ON c.userid", true, SQLquestion3);
		
		Question SQLquestion4 = new Question("Select all valid join types?", true, true, CategoryTag.COURSE_CONTENT, FormatTag.MULTI_ANSWER, ContentTypeTag.SQL, redRanger);
		Answer SQLanswer4_1 = new Answer("Self Join", true, SQLquestion4);
		Answer SQLanswer4_2 = new Answer("Left Join", true, SQLquestion4);
		Answer SQLanswer4_3 = new Answer("Maximum Join", false, SQLquestion4);
		Answer SQLanswer4_4 = new Answer("Right Join", true, SQLquestion4);
		Answer SQLanswer4_5 = new Answer("Over Join", false, SQLquestion4);
		Answer SQLanswer4_6 = new Answer("Around Join", false, SQLquestion4);
		
		
		
		Question Javaquestion1 = new Question("Which collection type is sorted?", true, true, CategoryTag.COURSE_CONTENT, FormatTag.MULTI_CHOICE, ContentTypeTag.OOD, blueRanger);
		Answer Javaanswer1_1 = new Answer("HashSet", false, Javaquestion1);
		Answer Javaanswer1_2 = new Answer("TreeSet", true, Javaquestion1);
		
		Question Javaquestion2 = new Question("Select all valid Spring annotations", true, true, CategoryTag.COURSE_CONTENT, FormatTag.MULTI_ANSWER, ContentTypeTag.OOD, blueRanger);
		Answer Javaanswer2_1 = new Answer("@Bean", true, Javaquestion2);
		Answer Javaanswer2_2 = new Answer("@Component", true, Javaquestion2);
		Answer Javaanswer2_3 = new Answer("@Object", false, Javaquestion2);
		Answer Javaanswer2_4 = new Answer("@Service", true, Javaquestion2);
		Answer Javaanswer2_5 = new Answer("@Driver", false, Javaquestion2);
		Answer Javaanswer2_6 = new Answer("@Set", false, Javaquestion2);
		Answer Javaanswer2_7 = new Answer("@Autowired", true, Javaquestion2);
		
		Question Javaquestion3 = new Question("Which of the following classes would be the most appropriate for reading characters from a file?", false, false, CategoryTag.COURSE_CONTENT, FormatTag.MULTI_CHOICE, ContentTypeTag.OOD, blueRanger);
		Answer Javaanswer3_1 = new Answer("FileInputStream", false, Javaquestion3);
		Answer Javaanswer3_2 = new Answer("DataInputStream", false, Javaquestion3);
		Answer Javaanswer3_3 = new Answer("FileReader", true, Javaquestion3);
		Answer Javaanswer3_4 = new Answer("Reader", false, Javaquestion3);
		
		
		
		Question UNIXquestion1 = new Question("Select the invalid UNIX command", false, false, CategoryTag.COURSE_CONTENT, FormatTag.MULTI_CHOICE, ContentTypeTag.UNIX, yellowRanger);
		Answer UNIXanswer1_1 = new Answer("head", false, UNIXquestion1);
		Answer UNIXanswer1_2 = new Answer("cut", false, UNIXquestion1);
		Answer UNIXanswer1_3 = new Answer("repeat", true, UNIXquestion1);
		Answer UNIXanswer1_4 = new Answer("who", false, UNIXquestion1);
		
		Question UNIXquestion2 = new Question("What does this command do when run? echo $dirDateCut | tr \"/\" \"-\" > tmpfile", true, true, CategoryTag.COURSE_CONTENT, FormatTag.MULTI_CHOICE, ContentTypeTag.UNIX, yellowRanger);
		Answer UNIXanswer2_1 = new Answer("Print the contents of dirDateCut, replace any - with a / and write changes to tmpfile", false, UNIXquestion2);
		Answer UNIXanswer2_2 = new Answer("Pipe the value of the dirDateCut variable, change / to - and write to tmpfile", true, UNIXquestion2);		
		
		Question UNIXquestion3 = new Question("question3", true, true, CategoryTag.COURSE_CONTENT, FormatTag.MULTI_CHOICE, ContentTypeTag.UNIX, blueRanger);
		Answer UNIXanswer3_1 = new Answer("answer3_1", true, UNIXquestion3);
		Answer UNIXanswer3_2 = new Answer("answer3_2", false, UNIXquestion3);
		Answer UNIXanswer3_3 = new Answer("answer3_3", true, UNIXquestion3);
		
		Question Badquestion1 = new Question("This is a poorly written question", false, true, CategoryTag.COURSE_CONTENT, FormatTag.MULTI_CHOICE, ContentTypeTag.DATA_ACCESS, blueRanger);
		Answer Badanswer1_1 = new Answer("With poorly written answers", false, Badquestion1);
		Answer Badanswer1_2 = new Answer("That should not be approved", false, Badquestion1);
		
		/**************************************** END USER CREATED QUESTIONS ****************************/
		
		
		
		
		/**************************************** TRAINER CREATED QUESTIONS ********************************/
		
		Question Trainerquestion1 = new Question("Which option is not a key dependency of Spring Boot?", true, true, CategoryTag.COURSE_CONTENT, FormatTag.MULTI_CHOICE, ContentTypeTag.SPRING, optimusPrime);
		Answer Traineranswer1_1 = new Answer("Library", true, Trainerquestion1);
		Answer Traineranswer1_2 = new Answer("Security", false, Trainerquestion1);
		Answer Traineranswer1_3 = new Answer("Web", false, Trainerquestion1);
		Answer Traineranswer1_4 = new Answer("Plugin", false, Trainerquestion1);
		
		Question Trainerquestion2 = new Question("What does AWS S3 stand for?", true, true, CategoryTag.COURSE_CONTENT, FormatTag.MULTI_CHOICE, ContentTypeTag.AWS_CLOUD_FOUNDATIONS, optimusPrime);
		Answer Traineranswer2_1 = new Answer("Simple Storage Service", true, Trainerquestion2);
		Answer Traineranswer2_2 = new Answer("Standard Storage System", false, Trainerquestion2);
		
		
		Question Trainerquestion3 = new Question("Select all valid Spring annotations", true, true, CategoryTag.COURSE_CONTENT, FormatTag.MULTI_ANSWER, ContentTypeTag.SPRING, blueRanger);
		Answer Traineranswer3_1 = new Answer("@Bean", true, Trainerquestion3);
		Answer Traineranswer3_2 = new Answer("@Component", true, Trainerquestion3);
		Answer Traineranswer3_3 = new Answer("@Object", false, Trainerquestion3);
		Answer Traineranswer3_4 = new Answer("@Service", true, Trainerquestion3);
		Answer Traineranswer3_5 = new Answer("@Driver", false, Trainerquestion3);
		Answer Traineranswer3_6 = new Answer("@Set", false, Trainerquestion3);
		Answer Traineranswer3_7 = new Answer("@Autowired", true, Trainerquestion3);
		
		Question Trainerquestion4 = new Question("What are the 3 key scrum artifacts?", true, true, CategoryTag.COURSE_CONTENT, FormatTag.MULTI_ANSWER, ContentTypeTag.APM, bumblebee);
		Answer Traineranswer4_1 = new Answer("Product Backlog", true, Trainerquestion4);
		Answer Traineranswer4_2 = new Answer("Vision Box", false, Trainerquestion4);
		Answer Traineranswer4_3 = new Answer("User Story Estimates", false, Trainerquestion4);
		Answer Traineranswer4_4 = new Answer("Sprint Backlog", true, Trainerquestion4);
		Answer Traineranswer4_5 = new Answer("Increment", true, Trainerquestion4);
		
		
		
		Question Interviewquestion1 = new Question("Tell me about a time when you had to resolve conflict in a team", true, true, CategoryTag.INTERVIEW_PREP, FormatTag.SHORT_ANSWER, ContentTypeTag.PRO_SKILLS, megatron);
		Answer Interviewanswer1 = new Answer("STAR response", Interviewquestion1);
		
		
		
		Question Interviewquestion2 = new Question("Tell me about yourself?", true, true, CategoryTag.INTERVIEW_PREP, FormatTag.SHORT_ANSWER, ContentTypeTag.PRO_SKILLS, trex);
		Answer Interviewanswer2 = new Answer("Origin story, passion, education, work experience.", Interviewquestion2);
		
		Question Interviewquestion3 = new Question("What experience do you have working in agile team?", true, true, CategoryTag.INTERVIEW_PREP, FormatTag.SHORT_ANSWER, ContentTypeTag.PRO_SKILLS, trex);
		Answer Interviewanswer3 = new Answer("Multiple years experience across many projects in academic and professional space.", Interviewquestion3);
		
		
		
		Question Interviewquestion4 = new Question("What do you know about the Commonwealth Bank of Australia", true, true, CategoryTag.INTERVIEW_PREP, FormatTag.SHORT_ANSWER, ContentTypeTag.PRO_SKILLS, triceratops);
		Answer Interviewanswer4 = new Answer("Australia's biggest bank, operates in Australia, NZ, Asia, USA, UK", Interviewquestion4);
		
		
		Question Interviewquestion5 = new Question("Tell me about a time when you had to balance requirements meeting a deadline", true, true, CategoryTag.INTERVIEW_PREP, FormatTag.SHORT_ANSWER, ContentTypeTag.PRO_SKILLS, megatron);
		Answer Interviewanswer5 = new Answer("STAR response", Interviewquestion5);
		
		/**************************************** END TRAINER CREATED QUESTIONS ****************************/
		
		
		
		
		/**************************************** CREATE QUIZ AND SUBMISSIONS ********************************/
		
		List<Question> quiz1questions = new ArrayList<>();
		quiz1questions.add(SQLquestion3);
		quiz1questions.add(SQLquestion4);
		quiz1questions.add(Javaquestion1);
		quiz1questions.add(Javaquestion2);
		
		Quiz quiz1 = new Quiz("SQL and OOD Quiz", CategoryTag.COURSE_CONTENT, redRanger, quiz1questions, 10, true);
		quiz1.setApproved(true);
		
		List<Question> quiz2questions = new ArrayList<>();
		quiz2questions.add(UNIXquestion2);
		quiz2questions.add(UNIXquestion3);
		
		Quiz quiz2 = new Quiz("Unix Quiz", CategoryTag.COURSE_CONTENT, blueRanger, quiz2questions, 3, false);
		
		
		List<Question> quiz3questions = new ArrayList<>();
		quiz3questions.add(Interviewquestion2);
		quiz3questions.add(Interviewquestion1);
		quiz3questions.add(Interviewquestion5);
		quiz3questions.add(Interviewquestion4);
		
		Quiz quiz3 = new Quiz("Commbank Quiz", CategoryTag.INTERVIEW_PREP, triceratops, quiz3questions, 30, true);
		quiz3.setApproved(true);
		
		/**************************************** END CREATE QUIZ AND SUBMISSIONS ****************************/
		
		
		
		
		
		/**************************************** CREATE NOTIFICATIONS ********************************/
		
//		Notification notification1 = new Notification("test message1", question1, quiz1, user1);
//		Notification notification2 = new Notification("test message2", question2, quiz1, user1);
//		Notification notification3 = new Notification("test message3", question3, quiz1, user1);
//		Notification notification4 = new Notification("test message4", question4, quiz1, user1);
//		
//		Notification notification5 = new Notification("test message5", question5, quiz1, user1);
//		Notification notification6 = new Notification("test message6", question6, quiz1, user1);
//		
//		Notification notification7 = new Notification("test message7", question9, quiz1, user5);
//		Notification notification8 = new Notification("test message8", question10, quiz1, user5);
//		Notification notification9 = new Notification("test message9", question11, quiz1, user5);
//		Notification notification10 = new Notification("test message10", question12, quiz1, user5);
//		
//		Notification notification11 = new Notification("test message11", question13, quiz1, user5);
//		Notification notification12 = new Notification("test message12", question14, quiz1, user5);
		
		/**************************************** END CREATE NOTIFICATIONS ****************************/
		
		
		
		
		
		
		/**************************************** ADD ALL OBJECTS TO DB ********************************/
		
		userService.saveOrUpdate(redRanger);
		userService.saveOrUpdate(blueRanger);
		userService.saveOrUpdate(yellowRanger);
		userService.saveOrUpdate(greenRanger);
		
		userService.saveOrUpdate(optimusPrime);
		userService.saveOrUpdate(bumblebee);
		userService.saveOrUpdate(megatron);
		
		userService.saveOrUpdate(trex);
		userService.saveOrUpdate(triceratops);
		
		questionService.saveOrUpdateQuestion(SQLquestion1);
		questionService.saveOrUpdateQuestion(SQLquestion2);
		questionService.saveOrUpdateQuestion(SQLquestion3);
		questionService.saveOrUpdateQuestion(SQLquestion4);
		
		questionService.saveOrUpdateQuestion(Javaquestion1);
		questionService.saveOrUpdateQuestion(Javaquestion2);
		questionService.saveOrUpdateQuestion(Javaquestion3);
		
		questionService.saveOrUpdateQuestion(UNIXquestion1);
		questionService.saveOrUpdateQuestion(UNIXquestion2);
		questionService.saveOrUpdateQuestion(UNIXquestion3);
		
		
		questionService.saveOrUpdateQuestion(Trainerquestion1);
		questionService.saveOrUpdateQuestion(Trainerquestion2);
		questionService.saveOrUpdateQuestion(Trainerquestion3);
		questionService.saveOrUpdateQuestion(Trainerquestion4);
		
		
		questionService.saveOrUpdateQuestion(Interviewquestion1);
		questionService.saveOrUpdateQuestion(Interviewquestion2);
		questionService.saveOrUpdateQuestion(Interviewquestion3);
		questionService.saveOrUpdateQuestion(Interviewquestion4);
		questionService.saveOrUpdateQuestion(Interviewquestion5);
		
		
		answerService.saveOrUpdateAnswer(SQLanswer1_1);
		answerService.saveOrUpdateAnswer(SQLanswer1_2);
		answerService.saveOrUpdateAnswer(SQLanswer1_3);
		answerService.saveOrUpdateAnswer(SQLanswer1_4);
		
		answerService.saveOrUpdateAnswer(SQLanswer2_1);
		answerService.saveOrUpdateAnswer(SQLanswer2_2);
		answerService.saveOrUpdateAnswer(SQLanswer2_3);
		answerService.saveOrUpdateAnswer(SQLanswer2_4);
		
		answerService.saveOrUpdateAnswer(SQLanswer3_1);
		answerService.saveOrUpdateAnswer(SQLanswer3_2);
		answerService.saveOrUpdateAnswer(SQLanswer3_3);
		answerService.saveOrUpdateAnswer(SQLanswer3_4);
		
		answerService.saveOrUpdateAnswer(SQLanswer4_1);
		answerService.saveOrUpdateAnswer(SQLanswer4_2);
		answerService.saveOrUpdateAnswer(SQLanswer4_3);
		answerService.saveOrUpdateAnswer(SQLanswer4_4);
		answerService.saveOrUpdateAnswer(SQLanswer4_5);
		answerService.saveOrUpdateAnswer(SQLanswer4_6);
		
		
		answerService.saveOrUpdateAnswer(Javaanswer1_1);
		answerService.saveOrUpdateAnswer(Javaanswer1_2);
		
		answerService.saveOrUpdateAnswer(Javaanswer2_1);
		answerService.saveOrUpdateAnswer(Javaanswer2_2);
		answerService.saveOrUpdateAnswer(Javaanswer2_3);
		answerService.saveOrUpdateAnswer(Javaanswer2_4);
		answerService.saveOrUpdateAnswer(Javaanswer2_5);
		answerService.saveOrUpdateAnswer(Javaanswer2_6);
		answerService.saveOrUpdateAnswer(Javaanswer2_7);
		
		answerService.saveOrUpdateAnswer(Javaanswer3_1);
		answerService.saveOrUpdateAnswer(Javaanswer3_2);
		answerService.saveOrUpdateAnswer(Javaanswer3_3);
		answerService.saveOrUpdateAnswer(Javaanswer3_4);
		
		
		answerService.saveOrUpdateAnswer(UNIXanswer1_1);
		answerService.saveOrUpdateAnswer(UNIXanswer1_2);
		answerService.saveOrUpdateAnswer(UNIXanswer1_3);
		answerService.saveOrUpdateAnswer(UNIXanswer1_4);
		
		answerService.saveOrUpdateAnswer(UNIXanswer2_1);
		answerService.saveOrUpdateAnswer(UNIXanswer2_2);
		
		answerService.saveOrUpdateAnswer(UNIXanswer3_1);
		answerService.saveOrUpdateAnswer(UNIXanswer3_2);
		answerService.saveOrUpdateAnswer(UNIXanswer3_3);
		
		
		answerService.saveOrUpdateAnswer(Traineranswer1_1);
		answerService.saveOrUpdateAnswer(Traineranswer1_2);
		answerService.saveOrUpdateAnswer(Traineranswer1_3);
		answerService.saveOrUpdateAnswer(Traineranswer1_4);
		
		answerService.saveOrUpdateAnswer(Traineranswer2_1);
		answerService.saveOrUpdateAnswer(Traineranswer2_2);
		
		answerService.saveOrUpdateAnswer(Traineranswer3_1);
		answerService.saveOrUpdateAnswer(Traineranswer3_2);
		answerService.saveOrUpdateAnswer(Traineranswer3_3);
		answerService.saveOrUpdateAnswer(Traineranswer3_4);
		answerService.saveOrUpdateAnswer(Traineranswer3_5);
		answerService.saveOrUpdateAnswer(Traineranswer3_6);
		answerService.saveOrUpdateAnswer(Traineranswer3_7);
		
		answerService.saveOrUpdateAnswer(Traineranswer4_1);
		answerService.saveOrUpdateAnswer(Traineranswer4_2);
		answerService.saveOrUpdateAnswer(Traineranswer4_3);
		answerService.saveOrUpdateAnswer(Traineranswer4_4);
		answerService.saveOrUpdateAnswer(Traineranswer4_5);
		
		answerService.saveOrUpdateAnswer(Interviewanswer1);
		
		answerService.saveOrUpdateAnswer(Interviewanswer2);
		
		answerService.saveOrUpdateAnswer(Interviewanswer3);
		
		answerService.saveOrUpdateAnswer(Interviewanswer4);
		
		answerService.saveOrUpdateAnswer(Interviewanswer5);
		
		
		
		quizService.saveQuiz(quiz1);
		quizService.saveQuiz(quiz2);
		quizService.saveQuiz(quiz3);
		
		
//		notificationService.saveNotification(notification1);
//		notificationService.saveNotification(notification2);
//		notificationService.saveNotification(notification3);
//		notificationService.saveNotification(notification4);
//		notificationService.saveNotification(notification5);
//		notificationService.saveNotification(notification6);
		
//		notificationService.saveNotification(notification7);
//		notificationService.saveNotification(notification8);
//		notificationService.saveNotification(notification9);
//		notificationService.saveNotification(notification10);
//		notificationService.saveNotification(notification11);
//		notificationService.saveNotification(notification12);
		
		/**************************************** END ADD ALL OBJECTS TO DB ****************************/
		
		
	}

}
