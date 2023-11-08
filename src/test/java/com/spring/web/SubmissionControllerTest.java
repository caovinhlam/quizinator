package com.spring.web;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.ui.Model;

import com.spring.web.controller.SubmissionController;
import com.spring.web.model.Answer;
import com.spring.web.model.Question;
import com.spring.web.model.QuestionAttempt;
import com.spring.web.model.Quiz;
import com.spring.web.model.Submission;
import com.spring.web.model.TempSubmission;
import com.spring.web.model.User;
import com.spring.web.model.EntityEnum.CategoryTag;
import com.spring.web.model.EntityEnum.FormatTag;
import com.spring.web.model.EntityEnum.UserRole;
import com.spring.web.service.AnswerService;
import com.spring.web.service.QuestionAttemptService;
import com.spring.web.service.QuestionService;
import com.spring.web.service.QuizService;
import com.spring.web.service.SubmissionService;
import com.spring.web.service.UserService;

import jakarta.servlet.http.HttpSession;

@WebMvcTest(SubmissionController.class)
@AutoConfigureMockMvc(addFilters = false)
class SubmissionControllerTest {
	
	
	@MockBean
	private QuestionAttemptService questionAttemptService;
	
	@MockBean
	private UserService userService;
	
	@MockBean
	private SubmissionService submissionService;
	
	@MockBean
	private QuizService quizService;
	
	@MockBean
	private QuestionService questionService;
	
	@MockBean
	private AnswerService answerService;
	
	@Autowired
	private MockMvc mockMvc;
		
	
	@Mock
	TempSubmission tempSubmission;
	
	@Mock
    Model model;
	
	@Mock
    HttpSession session;
    
    @Mock
    HttpSession mockSession;
    
    Answer answer1 = new Answer();
	Answer answer2 = new Answer();
	Answer answer3 = new Answer();
	Answer answer4 = new Answer();
	
	Question question1 = new Question();
	Question question2 = new Question();
	Question question3 = new Question();
	Question question4 = new Question();
    
	Submission submission = new Submission();
	
	@BeforeEach
	void setup() {
		answerService.deleteAllAnswers();
		questionService.deleteAllQuestions();
		submissionService.deleteAll();
		quizService.deleteAll();
		
		userService.deleteAllUsers();
		
		question1.setFormatTag(FormatTag.SHORT_ANSWER);
		question2.setFormatTag(FormatTag.MULTI_ANSWER);
		question3.setFormatTag(FormatTag.MULTI_CHOICE);
		question4.setFormatTag(FormatTag.SHORT_ANSWER);

		
		question1.setAnswers(Arrays.asList(answer1));
		question2.setAnswers(Arrays.asList(answer2));
		question3.setAnswers(Arrays.asList(answer3));
		question4.setAnswers(Arrays.asList(answer4));
		
		answer1.setQuestion(question1);
		answer2.setQuestion(question2);
		answer3.setQuestion(question3);
		answer4.setQuestion(question4);
		
		answer1.setAnswerId(1);
		answer2.setAnswerId(2);
		answer3.setAnswerId(3);
		answer3.setAnswerId(4);
		
		
		
		
	}
	
	@Test
	void mock_mvc_not_null() {
		assertNotNull(mockMvc);
	}
	
	
	// ----------------- TAKE Quiz METHOD ------------------
	
	@Test
	void take_quiz_failed_id() throws Exception {

		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/quizattempt?id=1"))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection());

	}
	
	@Test
	void take_quiz_success() throws Exception {
		
		
		
		Quiz quiz = new Quiz();
		List<Question> questions = new ArrayList<>();
		questions.add(question1);
		questions.add(question2);
		questions.add(question3);
		quiz.setQuestions(questions);
		
		when(quizService.getQuizById(101)).thenReturn(quiz);
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/quizattempt?id=101").sessionAttr("id", 101))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attribute("quiz", quiz))
		.andExpect(MockMvcResultMatchers.model().attribute("questions", quiz.getQuestions()))
		.andExpect(MockMvcResultMatchers.model().attributeExists("tempSubmission"));
	}
	
	
	// ----------------- REiderct to index method ------------------
	
	@Test
	void quizAttemptObject_test() throws Exception {

		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/quizAttemptObject"))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection());

	}
	
	
	// ----------------- Finish MApping method ------------------
	
		@Test
		void finishMarking_test() throws Exception {
						
			when(submissionService.getSubmissionById(101)).thenReturn(submission);
			
			this.mockMvc
			.perform(MockMvcRequestBuilders.get("/finishMarking?submissionID=101"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
			
			assertEquals(0, submission.getVisability());

		}
		
		
	// ----------------- Mark Question Attempt method ------------------
		
		@Test
		void markQuestionAttempt_with_true_correct_test() throws Exception {
			QuestionAttempt attempt = new QuestionAttempt();	
			attempt.setSubmission(submission);
			
			submission.setSubmissionID(100);
			attempt.setCorrect(true);
			submission.setMark(5);
			
			when(questionAttemptService.getQuestionAttemptById(101)).thenReturn(attempt);
			when(submissionService.getSubmissionById(100)).thenReturn(submission);
			
			this.mockMvc
			.perform(MockMvcRequestBuilders.get("/markQuestionAttempt?questionAttemptID=101"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
			
			assertEquals(4, submission.getMark());
			assertEquals(false, attempt.isCorrect());
			
		}
		
		@Test
		void markQuestionAttempt_with_false_correct_test() throws Exception {
			QuestionAttempt attempt = new QuestionAttempt();	
			attempt.setSubmission(submission);
			
			submission.setSubmissionID(100);
			attempt.setCorrect(false);
			submission.setMark(5);
		
			when(questionAttemptService.getQuestionAttemptById(101)).thenReturn(attempt);
			when(submissionService.getSubmissionById(100)).thenReturn(submission);
			
			this.mockMvc
			.perform(MockMvcRequestBuilders.get("/markQuestionAttempt?questionAttemptID=101"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
			
			assertEquals(6, submission.getMark());
			assertEquals(true, attempt.isCorrect());
		}
			
		@Test
		void markQuestionAttempt_with_null_correct_test() throws Exception {
			QuestionAttempt attempt = new QuestionAttempt();	
			attempt.setSubmission(submission);
			
			submission.setSubmissionID(100);
			attempt.setCorrect(null);
			submission.setMark(5);
			
			
			when(questionAttemptService.getQuestionAttemptById(101)).thenReturn(attempt);
			when(submissionService.getSubmissionById(100)).thenReturn(submission);
			
			this.mockMvc
			.perform(MockMvcRequestBuilders.get("/markQuestionAttempt?questionAttemptID=101"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
			
			assertEquals(6, submission.getMark());
			assertEquals(true, attempt.isCorrect());
		}
		
		
		// ----------------- Mark Submission method ------------------
		
		@Test
		void markSubmission_no_session_id() throws Exception {
			
			this.mockMvc
			.perform(MockMvcRequestBuilders.get("/markSubmission?submissionID=101"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
		}
			
				
		@Test
		void markSubmission_with_session_id_and_multiple_short_answer() throws Exception {
			QuestionAttempt attempt1 = new QuestionAttempt();
			QuestionAttempt attempt2 = new QuestionAttempt();
			
			attempt1.setShortAnswerAttempt("hello");
			
			question1.setQuestionContent("hello1");
			question2.setQuestionContent("hello2");
			
			answer1.setAnswer("wahaa");
			attempt1.setQuestion(question1);
			attempt2.setQuestion(question2);
			attempt1.setAnswer(answer1);
			attempt2.setAnswer(answer2);
			
			
			List<QuestionAttempt> attempts = new ArrayList<>();
			
			attempts.add(attempt2);
			attempts.add(attempt1);
			
			submission.setQuestionAttempt(attempts);
			submission.setSubmissionID(100);
			submission.setMark(5);
			
			when(submissionService.getSubmissionById(101)).thenReturn(submission);
			
			this.mockMvc
			.perform(MockMvcRequestBuilders.get("/markSubmission?submissionID=101").sessionAttr("id", 101))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("tempSubmissionObject"));
		}
				
				
				
		// ----------------- Mark Submission method ------------------
			
		@Test
		void markSubmissionList_no_session_id() throws Exception {
		
			this.mockMvc
			.perform(MockMvcRequestBuilders.get("/markSubmissionList"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection());

		}
			
		@Test
		void markSubmissionList_with_Sale_user() throws Exception {
			User user = new User();
			user.setUserRole(UserRole.SALES);
			
			Submission submission2 = new Submission();
			Submission submission3 = new Submission();
			
			Quiz quiz1 = new Quiz();
			Quiz quiz2 = new Quiz();
			Quiz quiz3 = new Quiz();
			
			user.setName("hello");
			
			quiz1.setQuizType(CategoryTag.INTERVIEW_PREP);
			quiz2.setQuizType(CategoryTag.INTERVIEW_PREP);
			quiz3.setQuizType(CategoryTag.COURSE_CONTENT);
			
			submission2.setVisability(1);
			submission3.setVisability(1);

			submission.setQuiz(quiz1);
			submission2.setQuiz(quiz2);
			submission3.setQuiz(quiz3);
			
			submission.setUserId(user);
			submission2.setUserId(user);
			submission3.setUserId(user);
			
			List<Submission> submissions = new ArrayList<>();
			submissions.add(submission);
			submissions.add(submission2);
			submissions.add(submission3);
			
			when(userService.getUserById(101)).thenReturn(user);
			when(submissionService.getAllSubmissions()).thenReturn(submissions);
			
			this.mockMvc
			.perform(MockMvcRequestBuilders.get("/markSubmissionList").sessionAttr("id", 101))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("submissionsToMark"));
		}
		
		@Test
		void markSubmissionList_with_Trainer_user() throws Exception {
			User user = new User();
			user.setUserRole(UserRole.TRAINER);
			
			Submission submission2 = new Submission();
			Submission submission3 = new Submission();
			
			Quiz quiz1 = new Quiz();
			Quiz quiz2 = new Quiz();
			Quiz quiz3 = new Quiz();
			
			user.setName("hello");
			
			quiz1.setQuizType(CategoryTag.INTERVIEW_PREP);
			quiz2.setQuizType(CategoryTag.INTERVIEW_PREP);
			quiz3.setQuizType(CategoryTag.COURSE_CONTENT);
			
			submission2.setVisability(1);
			submission3.setVisability(1);

			submission.setQuiz(quiz1);
			submission2.setQuiz(quiz2);
			submission3.setQuiz(quiz3);
			
			submission.setUserId(user);
			submission2.setUserId(user);
			submission3.setUserId(user);
			
			List<Submission> submissions = new ArrayList<>();
			submissions.add(submission);
			submissions.add(submission2);
			submissions.add(submission3);
			
			when(userService.getUserById(101)).thenReturn(user);
			when(submissionService.getAllSubmissions()).thenReturn(submissions);
			
			this.mockMvc
			.perform(MockMvcRequestBuilders.get("/markSubmissionList").sessionAttr("id", 101))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("submissionsToMark"));
		}
			
		
		@Test
		void markSubmissionList_with_POND_user() throws Exception {
			User user = new User();
			user.setUserRole(UserRole.POND);
			
			Submission submission2 = new Submission();
			Submission submission3 = new Submission();
			
			Quiz quiz1 = new Quiz();
			Quiz quiz2 = new Quiz();
			Quiz quiz3 = new Quiz();
			
			user.setName("hello");
			
			quiz1.setQuizType(CategoryTag.INTERVIEW_PREP);
			quiz2.setQuizType(CategoryTag.INTERVIEW_PREP);
			quiz3.setQuizType(CategoryTag.COURSE_CONTENT);
			
			submission2.setVisability(1);
			submission3.setVisability(1);

			submission.setQuiz(quiz1);
			submission2.setQuiz(quiz2);
			submission3.setQuiz(quiz3);
			
			submission.setUserId(user);
			submission2.setUserId(user);
			submission3.setUserId(user);
			
			List<Submission> submissions = new ArrayList<>();
			submissions.add(submission);
			submissions.add(submission2);
			submissions.add(submission3);
			
			when(userService.getUserById(101)).thenReturn(user);
			when(submissionService.getAllSubmissions()).thenReturn(submissions);
			
			this.mockMvc
			.perform(MockMvcRequestBuilders.get("/markSubmissionList").sessionAttr("id", 101))
			.andExpect(MockMvcResultMatchers.status().isOk());
		}
			
		
		
		
		
		
		// -----------------Quiz Attempt method ------------------
			
		@Test
		void quizAttemptObject_full_coverage() throws Exception {
		
			User user = new User();
			Quiz quiz = new Quiz();
			
			
			List<Question> questions = new ArrayList<>();
			questions.add(question1);
			questions.add(question2);
			questions.add(question3);
			quiz.setQuestions(questions);
			
			HashMap<Integer, Boolean> userSelectionMap = new HashMap<>();
			HashMap<Integer, String> userShortAnswerMap = new HashMap<>();
			for(Question question : quiz.getQuestions()) {
				for (Answer answer : question.getAnswers()) {
					if (answer.getQuestion().getFormatTag() != FormatTag.SHORT_ANSWER)
						userSelectionMap.put(answer.getAnswerId(), false);
					else
						userShortAnswerMap.put(answer.getAnswerId(), "");
				}
			}
			
			
			TempSubmission tempSubmission = new TempSubmission(userSelectionMap, userShortAnswerMap, quiz);
			
			
			when(quizService.getQuizById(101)).thenReturn(quiz);
			when(userService.getUserById(1)).thenReturn(user);
			
			
			this.mockMvc
			.perform(MockMvcRequestBuilders.post("/quizAttemptObject?id=101").sessionAttr("id", 1).flashAttr("tempSubmission", tempSubmission))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection());

		}
		
		

			
			
			
			
			
			
}
