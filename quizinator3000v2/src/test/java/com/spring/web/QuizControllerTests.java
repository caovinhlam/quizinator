package com.spring.web;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.spring.web.service.AnswerService;
import com.spring.web.service.NotificationService;
import com.spring.web.service.QuestionService;
import com.spring.web.service.QuizService;
import com.spring.web.service.UserService;

import com.spring.web.controller.QuizController;

import com.spring.web.model.Quiz;
import com.spring.web.model.Answer;
import com.spring.web.model.TempQuestion;
import com.spring.web.model.TempQuiz;
import com.spring.web.model.Question;
import com.spring.web.model.User;
import com.spring.web.model.Notification;
import com.spring.web.model.EntityEnum.CategoryTag;
import com.spring.web.model.EntityEnum.ContentTypeTag;
import com.spring.web.model.EntityEnum.FormatTag;
import com.spring.web.model.EntityEnum.UserRole;

@WebMvcTest(QuizController.class)
@AutoConfigureMockMvc(addFilters = false)
class QuizControllerTests {
	
	@MockBean
	private UserService userService;
	
	@MockBean
	private QuestionService questionService;
	
	@MockBean
	private QuizService quizService;
	
	@MockBean
	private AnswerService answerService;
	
	@MockBean
	private NotificationService notificationService;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Mock
	Question question;
	
	@BeforeEach
	void setup() {
		
	}
	
	@Test
	void mock_mvc_not_null() {
		assertNotNull(mockMvc);
	}

	//////////////////////TEST Quiz Controller///////////////////////
	
	@Test
	void test_quizList_get() throws Exception {
		
		User user = new User("test name", "test email", "test password", UserRole.TRAINING);
		user.setUserId(1);
	
		Question question = new Question("test question", false, true, CategoryTag.COURSE_CONTENT, FormatTag.SHORT_ANSWER, ContentTypeTag.PRO_SKILLS, user);
		
		List<Question> questions = new ArrayList<>();
		questions.add(question);
		Quiz quiz = new Quiz("test name", CategoryTag.COURSE_CONTENT, user, questions, 10, false);
		
		Notification notification = new Notification("test message", question, quiz, user);
		List<Notification> notifications = new ArrayList<>();
		notifications.add(notification);
		
		List<Quiz> quizzes = new ArrayList<>();
		quizzes.add(quiz);
		
		user.setFavouriteQuizzes(quizzes);
		
		when(userService.getUserById(1)).thenReturn(user);
		when(quizService.getAllApprovedQuizzes()).thenReturn(quizzes);
		when(quizService.getAllQuizsWithUserId(1)).thenReturn(quizzes);
		when(notificationService.getAllNotificaitonsWithUserId(1)).thenReturn(notifications);
				
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/quizList").sessionAttr("id", 1))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attribute("quizzes", quizzes))
		.andExpect(MockMvcResultMatchers.model().attribute("myQuizzes", quizzes))
		.andExpect(MockMvcResultMatchers.model().attribute("favourites", quizzes));
				
	}
	
	@Test
	void test_createQuizManual_get() throws Exception {
		
		User user = new User("test name", "test email", "test password", UserRole.TRAINING);
		user.setUserId(1);
	
		Question question = new Question("test question", false, true, CategoryTag.COURSE_CONTENT, FormatTag.SHORT_ANSWER, ContentTypeTag.PRO_SKILLS, user);
		
		List<Question> questions = new ArrayList<>();
		questions.add(question);
			
		user.setLockedQuestions(questions);
		
		when(userService.getUserById(1)).thenReturn(user);
		when(questionService.getAllByApproved(true)).thenReturn(questions);
				
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/createQuizManual").sessionAttr("id", 1))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeExists("quizObject"))
		.andExpect(MockMvcResultMatchers.model().attribute("questions", questions))
		.andExpect(MockMvcResultMatchers.model().attribute("lockedQuestions",questions));
				
	}
	
	@Test
	void test_createQuizManual_object_post() throws Exception {
		
		User user = new User("test name", "test email", "test password", UserRole.TRAINER);
		user.setUserId(1);
		
		Question question = new Question("test question", false, true, CategoryTag.COURSE_CONTENT, FormatTag.SHORT_ANSWER, ContentTypeTag.PRO_SKILLS, user);
		
		List<Question> questions = new ArrayList<>();
		questions.add(question);	
		
		TempQuiz tempQuiz = new TempQuiz();
		tempQuiz.setQuizName("test name");
		tempQuiz.setUser(user);
		tempQuiz.setQuestions(questions);
		tempQuiz.setAllowedTime(10);
		
		when(userService.getUserById(1)).thenReturn(user);
				
		this.mockMvc
		.perform(MockMvcRequestBuilders.post("/createQuizManualObject").sessionAttr("id", 1).flashAttr("quizObject", tempQuiz))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
				
	}
	
	@Test
	void test_createQuiz_get() throws Exception {
		
		User user = new User("test name", "test email", "test password", UserRole.TRAINING);
		
		when(userService.getUserById(1)).thenReturn(user);
				
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/createQuiz").sessionAttr("id", 1))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeExists("tempQuiz"))
		.andExpect(MockMvcResultMatchers.model().attribute("user", user))
		.andExpect(MockMvcResultMatchers.model().attribute("message","yes"));
				
	}
	
	@Test
	void test_createQuizAuto_get() throws Exception {
		
		User user = new User("test name", "test email", "test password", UserRole.TRAINING);
		
		Question question = new Question("test question", false, true, CategoryTag.COURSE_CONTENT, FormatTag.SHORT_ANSWER, ContentTypeTag.PRO_SKILLS, user);
		
		List<Question> questions = new ArrayList<>();
		questions.add(question);
		
		user.setLockedQuestions(questions);
		
		List<ContentTypeTag> contentTypeTags = new ArrayList<>();
		contentTypeTags.add(ContentTypeTag.PRO_SKILLS);
		
		List<FormatTag> formatTags = new ArrayList<>();
		formatTags.add(FormatTag.SHORT_ANSWER);
		
		TempQuiz tempQuiz = new TempQuiz();
		tempQuiz.setAllowedTime(10);
		tempQuiz.setSelectContents(contentTypeTags);
		tempQuiz.setSelectFormats(formatTags);
		
		when(userService.getUserById(1)).thenReturn(user);
		when(questionService.getAllQuestionsWithMatchingFormatContentCatgoryTagApproved(FormatTag.SHORT_ANSWER, ContentTypeTag.PRO_SKILLS, CategoryTag.COURSE_CONTENT)).thenReturn(questions);
				
		this.mockMvc
		.perform(MockMvcRequestBuilders.post("/createQuizAuto").sessionAttr("id", 1).flashAttr("tempQuiz", tempQuiz).param("quizName", "test name").param("categoryTag", "COURSE_CONTENT").param("numberOfQuestions", "4"))
		.andExpect(MockMvcResultMatchers.status().isOk());
				
	}
	
	@Test
	void test1_createQuizAuto_get() throws Exception {
		
		User user = new User("test name", "test email", "test password", UserRole.TRAINING);
		
		TempQuiz tempQuiz = new TempQuiz();
		
		when(userService.getUserById(1)).thenReturn(user);
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.post("/createQuizAuto").sessionAttr("id", 1).flashAttr("tempQuiz", tempQuiz).param("quizName", "test name").param("numberOfQuestions", "4"))
		.andExpect(MockMvcResultMatchers.status().isOk());
				
	}
	
	@Test
	void test2_createQuizAuto_get() throws Exception {
		
		User user = new User("test name", "test email", "test password", UserRole.TRAINING);
		
		TempQuiz tempQuiz = new TempQuiz();
		
		when(userService.getUserById(1)).thenReturn(user);
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.post("/createQuizAuto").sessionAttr("id", 1).flashAttr("tempQuiz", tempQuiz).param("quizName", "test name").param("categoryTag", "COURSE_CONTENT").param("numberOfQuestions", "4"))
		.andExpect(MockMvcResultMatchers.status().isOk());
				
	}
	
	@Test
	void test3_createQuizAuto_get() throws Exception {
		
		User user = new User("test name", "test email", "test password", UserRole.TRAINING);
		
		TempQuiz tempQuiz = new TempQuiz();
		
		List<ContentTypeTag> contentTypeTags = new ArrayList<>();
		contentTypeTags.add(ContentTypeTag.PRO_SKILLS);
		
		tempQuiz.setSelectContents(contentTypeTags);
		
		when(userService.getUserById(1)).thenReturn(user);
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.post("/createQuizAuto").sessionAttr("id", 1).flashAttr("tempQuiz", tempQuiz).param("quizName", "test name").param("categoryTag", "COURSE_CONTENT").param("numberOfQuestions", "4"))
		.andExpect(MockMvcResultMatchers.status().isOk());
				
	}
	
	@Test
	void test4_createQuizAuto_get() throws Exception {
		
		User user = new User("test name", "test email", "test password", UserRole.TRAINING);
		
		Question question = new Question("test question", false, true, CategoryTag.COURSE_CONTENT, FormatTag.SHORT_ANSWER, ContentTypeTag.PRO_SKILLS, user);
		
		List<Question> questions = new ArrayList<>();
		questions.add(question);
		
		user.setLockedQuestions(questions);
		
		List<ContentTypeTag> contentTypeTags = new ArrayList<>();
		contentTypeTags.add(ContentTypeTag.PRO_SKILLS);
		
		List<FormatTag> formatTags = new ArrayList<>();
		formatTags.add(FormatTag.SHORT_ANSWER);
		
		TempQuiz tempQuiz = new TempQuiz();
		tempQuiz.setAllowedTime(10);
		tempQuiz.setSelectContents(contentTypeTags);
		tempQuiz.setSelectFormats(formatTags);
		
		when(userService.getUserById(1)).thenReturn(user);
		when(questionService.getAllQuestionsWithMatchingFormatContentCatgoryTagApproved(FormatTag.SHORT_ANSWER, ContentTypeTag.PRO_SKILLS, CategoryTag.COURSE_CONTENT)).thenReturn(questions);
				
		this.mockMvc
		.perform(MockMvcRequestBuilders.post("/createQuizAuto").sessionAttr("id", 1).flashAttr("tempQuiz", tempQuiz).param("quizName", "test name").param("categoryTag", "COURSE_CONTENT").param("numberOfQuestions", "0"))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
				
	}
	
	@Test
	void test_quiz_details_get() throws Exception {
		
		User user = new User("test name", "test email", "test password", UserRole.TRAINING);
		
		Question question = new Question("test question", false, true, CategoryTag.COURSE_CONTENT, FormatTag.SHORT_ANSWER, ContentTypeTag.PRO_SKILLS, user);
		
		List<Question> questions = new ArrayList<>();
		questions.add(question);
		Quiz quiz = new Quiz("test name", CategoryTag.COURSE_CONTENT, user, questions, 10, false);
		
		when(userService.getUserById(1)).thenReturn(user);
		when(quizService.getQuizById(1)).thenReturn(quiz);
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/quizDetails?quizId=1").sessionAttr("id", 1))
		.andExpect(MockMvcResultMatchers.status().isOk());
				
	}
	
	@Test
	void test_edit_quiz_get() throws Exception {
		
		User user = new User("test name", "test email", "test password", UserRole.TRAINER);
		
		Question question = new Question("test question", false, true, CategoryTag.COURSE_CONTENT, FormatTag.SHORT_ANSWER, ContentTypeTag.PRO_SKILLS, user);
		
		List<Question> questions = new ArrayList<>();
		questions.add(question);
		Quiz quiz = new Quiz("test name", CategoryTag.COURSE_CONTENT, user, questions, 10, false);
		
		when(userService.getUserById(1)).thenReturn(user);
		when(quizService.getQuizById(1)).thenReturn(quiz);
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/editQuiz?id=1").sessionAttr("id", 1))
		.andExpect(MockMvcResultMatchers.status().isOk());
				
	}
	
	@Test
	void test_edit_quiz_object_post() throws Exception {
		
		User user = new User("test name", "test email", "test password", UserRole.TRAINER);
		List<User> users = new ArrayList<>();
		users.add(user);
		
		Question question = new Question("test question", false, true, CategoryTag.COURSE_CONTENT, FormatTag.SHORT_ANSWER, ContentTypeTag.PRO_SKILLS, user);
		
		List<Question> questions = new ArrayList<>();
		questions.add(question);
		
		TempQuiz tempQuiz = new TempQuiz();
		tempQuiz.setQuizID(1);
		tempQuiz.setQuestions(questions);
		tempQuiz.setQuizName("test name");
		tempQuiz.setAllowedTime(10);
		
		Quiz quiz = new Quiz(); 
		
		List<Quiz> quizzesList = new ArrayList<>();
		quizzesList.add(quiz);
		user.setFavouriteQuizzes(quizzesList);
		
		when(userService.getUserById(1)).thenReturn(user);
		when(quizService.getQuizById(1)).thenReturn(quiz);
		when(userService.getAllUsers()).thenReturn(users);
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.post("/editQuizObject").sessionAttr("id", 1).flashAttr("tempQuiz", tempQuiz))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
				
	}
	
	@Test
	void test_delete_quiz_get() throws Exception {
		
		User user = new User("test name", "test email", "test password", UserRole.TRAINER);
		List<User> users = new ArrayList<>();
		users.add(user);
		
		Question question = new Question("test question", false, true, CategoryTag.COURSE_CONTENT, FormatTag.SHORT_ANSWER, ContentTypeTag.PRO_SKILLS, user);
		
		Quiz quiz = new Quiz(); 
		quiz.setUser(user);
		List<Quiz> quizzesList = new ArrayList<>();
		quizzesList.add(quiz);
		
		user.setFavouriteQuizzes(quizzesList);
		
		Notification notification = new Notification("test message", question, quiz, user);
		List<Notification> notifications = new ArrayList<>();
		notifications.add(notification);
		
		when(userService.getUserById(1)).thenReturn(user);
		when(quizService.getQuizById(1)).thenReturn(quiz);
		when(quizService.getreviewedQuizzes()).thenReturn(quizzesList);
		when(userService.getAllUsers()).thenReturn(users);
		when(notificationService.getAllNotifications()).thenReturn(notifications);	
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/deleteQuiz?id=1").sessionAttr("id", 1))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
				
	}
	
	@Test
	void test_view_unreviewed_quizzes_get() throws Exception {
		
		User user = new User("test name", "test email", "test password", UserRole.TRAINER);
		
		Quiz quiz = new Quiz(); 
		quiz.setUser(user);
		List<Quiz> quizzesList = new ArrayList<>();
		quizzesList.add(quiz);
		
		when(userService.getUserById(1)).thenReturn(user);
		when(quizService.getUnreviewedQuizzes()).thenReturn(quizzesList);
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/viewUnreviewedQuizzes").sessionAttr("id", 1))
		.andExpect(MockMvcResultMatchers.status().isOk());
				
	}
	
	@Test
	void test_finish_quiz_review_get() throws Exception {
		
		User student = new User("test name", "test email", "test password", UserRole.TRAINING);
		student.setUserId(2);
		User user = new User("test name", "test email", "test password", UserRole.TRAINER);
		
		Quiz quiz = new Quiz(); 
		quiz.setUser(student);
		
		when(userService.getUserById(1)).thenReturn(user);
		when(userService.getUserById(2)).thenReturn(student);
		when(quizService.getQuizById(1)).thenReturn(quiz);
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/finishQuizReview?id=1&value=true").sessionAttr("id", 1))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
				
	}
	
	@Test
	void test1_finish_quiz_review_get() throws Exception {
		
		User student = new User("test name", "test email", "test password", UserRole.TRAINING);
		student.setUserId(2);
		User user = new User("test name", "test email", "test password", UserRole.TRAINER);
		
		Quiz quiz = new Quiz(); 
		quiz.setUser(student);
		
		when(userService.getUserById(1)).thenReturn(user);
		when(userService.getUserById(2)).thenReturn(student);
		when(quizService.getQuizById(1)).thenReturn(quiz);
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/finishQuizReview?id=1&value=false").sessionAttr("id", 1))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
				
	}
	
	@Test
	void test_favourite_quiz_get() throws Exception {
		
		User user = new User("test name", "test email", "test password", UserRole.TRAINER);
		
		Quiz quiz = new Quiz(); 
		List<Quiz> quizzesList = new ArrayList<>();
		quizzesList.add(quiz);
		
		user.setFavouriteQuizzes(quizzesList);
		
		when(userService.getUserById(1)).thenReturn(user);
		when(quizService.getQuizById(1)).thenReturn(quiz);
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/favouriteQuiz?quizid=1").sessionAttr("id", 1))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
				
	}
	
	@Test
	void test_view_created_quizzes_get() throws Exception {
		
		User user = new User("test name", "test email", "test password", UserRole.TRAINER);
		Question question = new Question("test question", false, true, CategoryTag.COURSE_CONTENT, FormatTag.SHORT_ANSWER, ContentTypeTag.PRO_SKILLS, user);
		
		List<Question> questions = new ArrayList<>();
		questions.add(question);
		
		Quiz quiz = new Quiz(); 
		quiz.setQuestions(questions);
		quiz.setUser(user);
		quiz.setNumberOfQuestions(1);
		
		List<Quiz> quizzesList = new ArrayList<>();
		quizzesList.add(quiz);
		user.setFavouriteQuizzes(quizzesList);
		
		when(userService.getUserById(1)).thenReturn(user);
		when(quizService.getAllQuizsWithUserId(1)).thenReturn(quizzesList);
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/viewQuizesCreated?id=1").sessionAttr("id", 1))
		.andExpect(MockMvcResultMatchers.status().isOk());
				
	}
	
	@Test
	void test_favourite_quiz_from_student_get() throws Exception {
		
		User user = new User("test name", "test email", "test password", UserRole.TRAINER);
		Question question = new Question("test question", false, true, CategoryTag.COURSE_CONTENT, FormatTag.SHORT_ANSWER, ContentTypeTag.PRO_SKILLS, user);
		
		List<Question> questions = new ArrayList<>();
		questions.add(question);
		
		Quiz quiz = new Quiz(); 
		quiz.setQuestions(questions);
		quiz.setUser(user);
		quiz.setNumberOfQuestions(1);
		
		List<Quiz> quizzesList = new ArrayList<>();
		quizzesList.add(quiz);
		user.setFavouriteQuizzes(quizzesList);
		
		when(userService.getUserById(1)).thenReturn(user);
		when(quizService.getQuizById(1)).thenReturn(quiz);
		when(quizService.getAllQuizsWithUserId(1)).thenReturn(quizzesList);
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/favouriteQuizFromStudent?quizid=1").sessionAttr("id", 1))
		.andExpect(MockMvcResultMatchers.status().isOk());
				
	}
	
	
}
