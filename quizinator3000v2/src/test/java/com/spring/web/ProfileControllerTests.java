package com.spring.web;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.ArrayList;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.support.BeanDefinitionDsl.Role;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.spring.web.controller.ProfileController;
import com.spring.web.model.Answer;
import com.spring.web.model.Question;
import com.spring.web.model.QuestionAttempt;
import com.spring.web.model.User;
import com.spring.web.model.EntityEnum.UserRole;
import com.spring.web.model.Submission;
import com.spring.web.model.Quiz;
import com.spring.web.service.AnswerService;
import com.spring.web.service.NotificationService;
import com.spring.web.service.QuestionService;
import com.spring.web.service.QuizService;
import com.spring.web.service.SubmissionService;
import com.spring.web.service.UserService;

import jakarta.servlet.http.HttpSession;

@WebMvcTest(value = ProfileController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProfileControllerTests {

	@MockBean
	private UserService userService;
	
	@MockBean
	private QuizService quizService;
	
	@MockBean
	private QuestionService questionService;
	
	@MockBean
	private AnswerService answerService;
	
	@MockBean
	private NotificationService notificationService;
	
	@MockBean
	private SubmissionService submissionService;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Mock
	HttpSession session;
    
	@Mock
	Question question;
	
	@BeforeEach
	void setup() {
		session = new MockHttpSession();
		session.setAttribute("id", 2);
	}
			
	@Test
	void mock_mvc_not_null() {
		assertNotNull(mockMvc);

	}
	
	@Test
	void test_personal_profile_get() throws Exception {
		
		User user = new User();
		user.setUserRole(UserRole.TRAINER);
		Quiz quiz = new Quiz();
		List<Quiz> quizzes = new ArrayList<>();
		quizzes.add(quiz);
		Question question = new Question();
		List<Question> questions = new ArrayList<>();
		questions.add(question);
 		
		Submission submission = new Submission();
		
		
		when(userService.getUserById(1)).thenReturn(user);
		when(quizService.getAllQuizsWithUserId(1)).thenReturn(quizzes);
		when(questionService.getAllQuestionsWithUserId(1)).thenReturn(questions);
		when(submissionService.getSubmissionById(1)).thenReturn(submission);
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/personalProfile").sessionAttr("id", 1))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
	}
	
	
	@Test
	void test_personal_profile_get_no_session() throws Exception {
		
		User user = new User();
		user.setUserRole(UserRole.TRAINER);
		Quiz quiz = new Quiz();
		List<Quiz> quizzes = new ArrayList<>();
		quizzes.add(quiz);
		Question question = new Question();
		List<Question> questions = new ArrayList<>();
		questions.add(question);
 		
		Submission submission = new Submission();
		
		
		when(userService.getUserById(1)).thenReturn(user);
		when(quizService.getAllQuizsWithUserId(1)).thenReturn(quizzes);
		when(questionService.getAllQuestionsWithUserId(1)).thenReturn(questions);
		when(submissionService.getSubmissionById(1)).thenReturn(submission);
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/personalProfile"))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
		
	}
	
	@Test
	void test_profile_get_trainer() throws Exception {
		
		User user = new User();
		user.setUserRole(UserRole.POND);
		User user1 = new User();
		user1.setUserRole(UserRole.TRAINER);
		Quiz quiz = new Quiz();
		List<Quiz> quizzes = new ArrayList<>();
		quizzes.add(quiz);
		Question question = new Question();
		List<Question> questions = new ArrayList<>();
		questions.add(question);
 		
		Submission submission = new Submission();
		
		when(userService.getUserById(2)).thenReturn(user1);
		when(userService.getUserById(1)).thenReturn(user);
		when(quizService.getAllQuizsWithUserId(1)).thenReturn(quizzes);
		when(questionService.getAllQuestionsWithUserId(1)).thenReturn(questions);
		when(submissionService.getSubmissionById(1)).thenReturn(submission);
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/Profile?id=1").sessionAttr("id", 2))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
	}
	
	@Test
	void test_profile_get_sales() throws Exception {
		
		User user = new User();
		user.setUserRole(UserRole.POND);
		User user1 = new User();
		user1.setUserRole(UserRole.SALES);
		Quiz quiz = new Quiz();
		List<Quiz> quizzes = new ArrayList<>();
		quizzes.add(quiz);
		Question question = new Question();
		List<Question> questions = new ArrayList<>();
		questions.add(question);
 		
		Submission submission = new Submission();
		
		when(userService.getUserById(2)).thenReturn(user1);
		when(userService.getUserById(1)).thenReturn(user);
		when(quizService.getAllQuizsWithUserId(1)).thenReturn(quizzes);
		when(questionService.getAllQuestionsWithUserId(1)).thenReturn(questions);
		when(submissionService.getSubmissionById(1)).thenReturn(submission);
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/Profile?id=1").sessionAttr("id", 2))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
	}
	
	
	@Test
	void test_profile_get_no_session() throws Exception {
		
		User user = new User();
		user.setUserRole(UserRole.POND);
		User user1 = new User();
		user1.setUserRole(UserRole.TRAINER);
		Quiz quiz = new Quiz();
		List<Quiz> quizzes = new ArrayList<>();
		quizzes.add(quiz);
		Question question = new Question();
		List<Question> questions = new ArrayList<>();
		questions.add(question);
 		
		Submission submission = new Submission();
		
		when(userService.getUserById(2)).thenReturn(user1);
		when(userService.getUserById(1)).thenReturn(user);
		when(quizService.getAllQuizsWithUserId(1)).thenReturn(quizzes);
		when(questionService.getAllQuestionsWithUserId(1)).thenReturn(questions);
		when(submissionService.getSubmissionById(1)).thenReturn(submission);
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/Profile?id=1"))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
		
	}
	
	@Test
	void test_profile_get_pond_user() throws Exception {
		
		User user = new User();
		user.setUserRole(UserRole.POND);
		User user1 = new User();
		user1.setUserRole(UserRole.POND);
		Quiz quiz = new Quiz();
		List<Quiz> quizzes = new ArrayList<>();
		quizzes.add(quiz);
		Question question = new Question();
		List<Question> questions = new ArrayList<>();
		questions.add(question);
 		
		Submission submission = new Submission();
		
		when(userService.getUserById(2)).thenReturn(user1);
		when(userService.getUserById(1)).thenReturn(user);
		when(quizService.getAllQuizsWithUserId(1)).thenReturn(quizzes);
		when(questionService.getAllQuestionsWithUserId(1)).thenReturn(questions);
		when(submissionService.getSubmissionById(1)).thenReturn(submission);
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/Profile?id=1").sessionAttr("id", 2))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
		
	}
	
	
	
	@Test
	void test_edit_own_profile_get() throws Exception {
		
		User user = new User();
		
		when(userService.getUserById(1)).thenReturn(user);
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/editOwnProfile").sessionAttr("id", 1))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attribute("user", user));
		
	}
	
	@Test
	void test_update_user_post() throws Exception {
		
		User user = new User("test name", "test email", "test password", UserRole.TRAINING);
		
		when(userService.getUserById(1)).thenReturn(user);
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.post("/updateUser").sessionAttr("id", 1).flashAttr("user", user))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attribute("user", user));
		
	}
	
	@Test
	void test_update_user_get() throws Exception {
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/updateUser"))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
		
	}
	
	@Test
	void test_create_user_get() throws Exception {
		
		User user = new User();
		
		when(userService.getUserById(1)).thenReturn(user);
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/createUser").sessionAttr("id", 1))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
		
	}
	
	@Test
	void test_create_trainersales_get() throws Exception {
		
		User user = new User();
		user.setUserRole(UserRole.SALES);
		
		when(userService.getUserById(1)).thenReturn(user);
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/createUser").sessionAttr("id", 1))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attribute("loggedinuser", user))
		.andExpect(MockMvcResultMatchers.model().attributeExists("user"));
		
	}
	
	@Test
	void test_register_trainer_object_successful_post() throws Exception {
		
		User user = new User("test name", "test email", "test password", UserRole.TRAINING);
		
		when(userService.getUserById(1)).thenReturn(user);
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.post("/registerTrainerObject").sessionAttr("id", 1).flashAttr("user", user))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attribute("message", "Incorrect Auth password"))
		.andExpect(MockMvcResultMatchers.model().attribute("user", user));
		
	}
	
	@Test
	void test_register_trainer_object_get() throws Exception {
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/registerTrainerObject"))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
		
	}
	
	@Test
	void test_register_trainer_object_duplicateEmail_post() throws Exception {
		
		User user = new User("test name", "test email", "test password", UserRole.TRAINER);
		User user1 = new User("test name", "test email", "test password", UserRole.TRAINER);
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		when(userService.getUserById(1)).thenReturn(user);
		when(userService.getUserByEmail(user1.getEmail())).thenReturn(user1);
		user1.setUserRole(UserRole.TRAINER);
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.post("/registerTrainerObject").sessionAttr("id", 1).flashAttr("user", user1))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attribute("message", "Email already exists"))
		.andExpect(MockMvcResultMatchers.model().attribute("user", user1));
		
	}
	
	@Test
	void test_get_catch_for_post() throws Exception {
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/updateStudent"))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
		
	}
	
	@Test
	void test_update_student_post() throws Exception {
		
		User originalUser = new User("test name", "test email", "test password", UserRole.TRAINING);
		User user = new User("test name1", "test email1", "test password1", UserRole.TRAINING);
		
		when(userService.getUserById(1)).thenReturn(originalUser);
		when(userService.getUserById(user.getUserId())).thenReturn(user);
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.post("/updateStudent").sessionAttr("id", 1).flashAttr("user", user))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeExists("blankuser"))
		.andExpect(MockMvcResultMatchers.model().attribute("user", originalUser));
		
	}
	
	@Test
	void test_register_sales_object_successful_post() throws Exception {
		
		User user = new User("test name", "test email", "test password", UserRole.TRAINING);
		
		when(userService.getUserById(1)).thenReturn(user);
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.post("/registerSalesObject").sessionAttr("id", 1).flashAttr("user", user))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attribute("loggedinuser", user))
		.andExpect(MockMvcResultMatchers.model().attribute("message", "incorrect Auth password"))
		.andExpect(MockMvcResultMatchers.model().attribute("user", user));
		
	}
	
	@Test
	void test_register_sales_object_missmatched_passwords() throws Exception {
		
		User user = new User("test name", "test email", "test password", UserRole.TRAINING);
		User checkuser = new User();
		
		
		when(userService.getUserById(1)).thenReturn(user);
		when(checkuser.getPassword()).thenReturn("test");
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.post("/registerSalesObject").sessionAttr("id", 1).flashAttr("user", user))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.model().attribute("message", "incorrect Auth password"));

		
	}
	
	@Test
	void test_register_sales_object_get() throws Exception {
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/registerSalesObject"))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
		
	}
	
	
	@Test
	void test_view_results_user_allowed() throws Exception {
	    Submission submission = new Submission();
	    User user = new User();
	    Quiz quiz = new Quiz();
	    user.setUserRole(UserRole.POND);
	    user.setUserId(1);
	    submission.setUserId(user);
	    QuestionAttempt questionAttempt = new QuestionAttempt();
	    List<QuestionAttempt> questionAttempts = new ArrayList<>();
	    questionAttempts.add(questionAttempt);
	    Answer answer = new Answer();
	    submission.setQuestionAttempt(questionAttempts);
		List<Answer> answers = new ArrayList<>();
		answers.add(answer);
		Question question = new Question();
		question.setAnswers(answers);
		List<Question> questions = new ArrayList<>();
		questions.add(question);
		quiz.setTotalposmarks(5);
		
		
		quiz.setQuestions(questions);
	    submission.setQuiz(quiz);
	    
	    when(submissionService.getSubmissionById(1)).thenReturn(submission);
	    when(userService.getUserById(1)).thenReturn(user);
	    
	    mockMvc.perform(MockMvcRequestBuilders.get("/viewresults?id=1").sessionAttr("id", 1))
	            .andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void test_view_results_user_not_allowed() throws Exception {
	    User user = new User();
	    user.setUserRole(UserRole.POND);
	    user.setUserId(2);
	    
	    when(session.getAttribute("id")).thenReturn(2);
	    when(userService.getUserById(2)).thenReturn(user);
	    
	    mockMvc.perform(MockMvcRequestBuilders.get("/viewresults?id=1").sessionAttr("id", 1))
	            .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@Test
	void test_view_results_user_not_logged_in() throws Exception {

	    mockMvc.perform(MockMvcRequestBuilders.get("/viewresults?id=1"))
	            .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

}
