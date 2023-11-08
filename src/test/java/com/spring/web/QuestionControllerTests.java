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
import com.spring.web.service.UserService;

import com.spring.web.controller.QuestionController;

import com.spring.web.model.Answer;
import com.spring.web.model.TempQuestion;
import com.spring.web.model.Question;
import com.spring.web.model.User;
import com.spring.web.model.EntityEnum.CategoryTag;
import com.spring.web.model.EntityEnum.ContentTypeTag;
import com.spring.web.model.EntityEnum.FormatTag;
import com.spring.web.model.EntityEnum.UserRole;

@WebMvcTest(QuestionController.class)
@AutoConfigureMockMvc(addFilters = false)
class QuestionControllerTests {
	
	@MockBean
	private UserService userService;
	
	@MockBean
	private QuestionService questionService;
	
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

	//////////////////////TEST Question Controller///////////////////////
	
	@Test
	void test_questionList_get() throws Exception {
		
		User user = new User("test name", "test email", "test password", UserRole.TRAINING);
		Question question = new Question("test question", false, true, CategoryTag.COURSE_CONTENT, FormatTag.SHORT_ANSWER, ContentTypeTag.PRO_SKILLS, user);
		
		when(userService.getUserById(1)).thenReturn(user);
		
		List<String> contentTagsStrings = new ArrayList<>();
		List<Question> lockedQuestions = new ArrayList<>();		
		
		user.setLockedQuestions(lockedQuestions);
		
		for (ContentTypeTag value : ContentTypeTag.values()) {
			contentTagsStrings.add(value.toString());
		}
		
		List<Question> questions = new ArrayList<>();
		questions.add(question);
		
		when(questionService.getAllByApproved(true)).thenReturn(questions);
		
		Optional<List<Question>> questionsOpt = Optional.of(questions);
		
		if (questionsOpt.isPresent()){
			
			questions = questionsOpt.get();
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/questionList?id=1").sessionAttr("id", 1))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attribute("questions", questions))
		.andExpect(MockMvcResultMatchers.model().attribute("contentTags", contentTagsStrings));
		
		}
	}
	
	@Test
	void test_questionDetails_get() throws Exception {
		
		User user = new User("test name", "test email", "test password", UserRole.TRAINING);
		Question question = new Question("test question", false, true, CategoryTag.COURSE_CONTENT, FormatTag.SHORT_ANSWER, ContentTypeTag.PRO_SKILLS, user);
		
		when(userService.getUserById(1)).thenReturn(user);
		when(questionService.getQuestionById(1)).thenReturn(question);
		
		Optional<Question> questionOpt = Optional.of(questionService.getQuestionById(1));
		
		if (questionOpt.isPresent()){
			
			question = questionOpt.get();
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/questionDetails?questionId=1").sessionAttr("id", 1))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attribute("questionObject", question))
		.andExpect(MockMvcResultMatchers.model().attribute("userCreated", user.getName()));
		
		}
	}
	
	@Test 
	void test_post_question_filter_return_filtered_list() throws Exception {
			 
		User user = new User("test name", "test email", "test password", UserRole.TRAINING);
		Question question = new Question("test question", false, true, CategoryTag.COURSE_CONTENT, FormatTag.SHORT_ANSWER, ContentTypeTag.PRO_SKILLS, user);
		
		ContentTypeTag contentTag = ContentTypeTag.APM;
		CategoryTag categoryTag = CategoryTag.COURSE_CONTENT;
		FormatTag formatTag = FormatTag.MULTI_ANSWER;
		
		List<String> contentTypesStr = new ArrayList<>();
		List<Question> filteredQuestions = new ArrayList<>();
		filteredQuestions.add(question);
		
		List<Question> filteredUserQuestions = new ArrayList<>();
		filteredUserQuestions.add(question);
		
		user.setLockedQuestions(filteredUserQuestions);
		
		for (ContentTypeTag value : ContentTypeTag.values()) {
			contentTypesStr.add(value.toString());
		}
		
		when(userService.getUserById(1)).thenReturn(user);
		when(questionService.getAllByContentTypeTag(contentTag)).thenReturn(filteredQuestions);
		when(questionService.getAllByCategoryTag(categoryTag)).thenReturn(filteredQuestions);
		when(questionService.getAllByFormatTag(formatTag)).thenReturn(filteredQuestions);
		when(questionService.getAllUserQuestionsWithMatchingCategoryFormatTag(categoryTag, formatTag, 1)).thenReturn(filteredUserQuestions);
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.post("/questionListSorted?id=1").sessionAttr("id", 1).param("contentType", "Pro Skills").param("categoryType", "Course Content").param("formatType", "Multiple Answer"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attribute("contentTags", contentTypesStr))
		.andExpect(MockMvcResultMatchers.model().attributeExists("myQuestions"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("questions"));
				
	}
	
	@Test 
	void test_post_question_filter_return_filtered_list_null_content() throws Exception {
			 
		User user = new User("test name", "test email", "test password", UserRole.TRAINING);
		Question question = new Question("test question", false, true, CategoryTag.COURSE_CONTENT, FormatTag.SHORT_ANSWER, ContentTypeTag.PRO_SKILLS, user);
		
		ContentTypeTag contentTag = ContentTypeTag.APM;
		CategoryTag categoryTag = CategoryTag.COURSE_CONTENT;
		FormatTag formatTag = FormatTag.MULTI_ANSWER;
		
		List<String> contentTypesStr = new ArrayList<>();
		List<Question> filteredQuestions = new ArrayList<>();
		filteredQuestions.add(question);
		
		List<Question> filteredUserQuestions = new ArrayList<>();
		filteredUserQuestions.add(question);
		
		user.setLockedQuestions(filteredUserQuestions);
		
		for (ContentTypeTag value : ContentTypeTag.values()) {
			contentTypesStr.add(value.toString());
		}
		
		when(userService.getUserById(1)).thenReturn(user);
		when(questionService.getAllByContentTypeTag(contentTag)).thenReturn(filteredQuestions);
		when(questionService.getAllByCategoryTag(categoryTag)).thenReturn(filteredQuestions);
		when(questionService.getAllByFormatTag(formatTag)).thenReturn(filteredQuestions);
		when(questionService.getAllUserQuestionsWithMatchingCategoryFormatTag(categoryTag, formatTag, 1)).thenReturn(filteredUserQuestions);
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.post("/questionListSorted?id=1").sessionAttr("id", 1).param("contentType", "null").param("categoryType", "Course Content").param("formatType", "Multiple Answer"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attribute("contentTags", contentTypesStr))
		.andExpect(MockMvcResultMatchers.model().attributeExists("myQuestions"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("questions"));
				
	}
	
	@Test 
	void test_post_question_filter_return_filtered_list_null_category() throws Exception {
			 
		User user = new User("test name", "test email", "test password", UserRole.TRAINING);
		Question question = new Question("test question", false, true, CategoryTag.COURSE_CONTENT, FormatTag.SHORT_ANSWER, ContentTypeTag.PRO_SKILLS, user);
		
		ContentTypeTag contentTag = ContentTypeTag.APM;
		CategoryTag categoryTag = CategoryTag.COURSE_CONTENT;
		FormatTag formatTag = FormatTag.MULTI_ANSWER;
		
		List<String> contentTypesStr = new ArrayList<>();
		List<Question> filteredQuestions = new ArrayList<>();
		filteredQuestions.add(question);
		
		List<Question> filteredUserQuestions = new ArrayList<>();
		filteredUserQuestions.add(question);
		
		user.setLockedQuestions(filteredUserQuestions);
		
		for (ContentTypeTag value : ContentTypeTag.values()) {
			contentTypesStr.add(value.toString());
		}
		
		when(userService.getUserById(1)).thenReturn(user);
		when(questionService.getAllByContentTypeTag(contentTag)).thenReturn(filteredQuestions);
		when(questionService.getAllByCategoryTag(categoryTag)).thenReturn(filteredQuestions);
		when(questionService.getAllByFormatTag(formatTag)).thenReturn(filteredQuestions);
		when(questionService.getAllUserQuestionsWithMatchingCategoryFormatTag(categoryTag, formatTag, 1)).thenReturn(filteredUserQuestions);
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.post("/questionListSorted?id=1").sessionAttr("id", 1).param("contentType", "Pro Skills").param("categoryType", "null").param("formatType", "Multiple Answer"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attribute("contentTags", contentTypesStr))
		.andExpect(MockMvcResultMatchers.model().attributeExists("myQuestions"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("questions"));
				
	}
	
	@Test 
	void test_post_question_filter_return_filtered_list_null_format() throws Exception {
			 
		User user = new User("test name", "test email", "test password", UserRole.TRAINING);
		Question question = new Question("test question", false, true, CategoryTag.COURSE_CONTENT, FormatTag.SHORT_ANSWER, ContentTypeTag.PRO_SKILLS, user);
		
		ContentTypeTag contentTag = ContentTypeTag.APM;
		CategoryTag categoryTag = CategoryTag.COURSE_CONTENT;
		FormatTag formatTag = FormatTag.MULTI_ANSWER;
		
		List<String> contentTypesStr = new ArrayList<>();
		List<Question> filteredQuestions = new ArrayList<>();
		filteredQuestions.add(question);
		
		List<Question> filteredUserQuestions = new ArrayList<>();
		filteredUserQuestions.add(question);
		
		user.setLockedQuestions(filteredUserQuestions);
		
		for (ContentTypeTag value : ContentTypeTag.values()) {
			contentTypesStr.add(value.toString());
		}
		
		when(userService.getUserById(1)).thenReturn(user);
		when(questionService.getAllByContentTypeTag(contentTag)).thenReturn(filteredQuestions);
		when(questionService.getAllByCategoryTag(categoryTag)).thenReturn(filteredQuestions);
		when(questionService.getAllByFormatTag(formatTag)).thenReturn(filteredQuestions);
		when(questionService.getAllUserQuestionsWithMatchingCategoryFormatTag(categoryTag, formatTag, 1)).thenReturn(filteredUserQuestions);
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.post("/questionListSorted?id=1").sessionAttr("id", 1).param("contentType", "Pro Skills").param("categoryType", "Course Content").param("formatType", "null"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attribute("contentTags", contentTypesStr))
		.andExpect(MockMvcResultMatchers.model().attributeExists("myQuestions"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("questions"));
				
	}
	
	@Test 
	void test_post_question_filter_return_filtered_list_null_allTags() throws Exception {
			 
		User user = new User("test name", "test email", "test password", UserRole.TRAINING);
		Question question = new Question("test question", false, true, CategoryTag.COURSE_CONTENT, FormatTag.SHORT_ANSWER, ContentTypeTag.PRO_SKILLS, user);
		
		ContentTypeTag contentTag = ContentTypeTag.APM;
		CategoryTag categoryTag = CategoryTag.COURSE_CONTENT;
		FormatTag formatTag = FormatTag.MULTI_ANSWER;
		
		List<String> contentTypesStr = new ArrayList<>();
		List<Question> filteredQuestions = new ArrayList<>();
		filteredQuestions.add(question);
		
		List<Question> filteredUserQuestions = new ArrayList<>();
		filteredUserQuestions.add(question);
		
		user.setLockedQuestions(filteredUserQuestions);
		
		for (ContentTypeTag value : ContentTypeTag.values()) {
			contentTypesStr.add(value.toString());
		}
		
		when(userService.getUserById(1)).thenReturn(user);
		when(questionService.getAllByContentTypeTag(contentTag)).thenReturn(filteredQuestions);
		when(questionService.getAllByCategoryTag(categoryTag)).thenReturn(filteredQuestions);
		when(questionService.getAllByFormatTag(formatTag)).thenReturn(filteredQuestions);
		when(questionService.getAllUserQuestionsWithMatchingCategoryFormatTag(categoryTag, formatTag, 1)).thenReturn(filteredUserQuestions);
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.post("/questionListSorted?id=1").sessionAttr("id", 1).param("contentType", "null").param("categoryType", "null").param("formatType", "null"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attribute("contentTags", contentTypesStr))
		.andExpect(MockMvcResultMatchers.model().attributeExists("myQuestions"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("questions"));
				
	}
	
	@Test 
	void test_post_question_filter_return_filtered_list_null_content_category() throws Exception {
			 
		User user = new User("test name", "test email", "test password", UserRole.TRAINING);
		Question question = new Question("test question", false, true, CategoryTag.COURSE_CONTENT, FormatTag.SHORT_ANSWER, ContentTypeTag.PRO_SKILLS, user);
		
		ContentTypeTag contentTag = ContentTypeTag.APM;
		CategoryTag categoryTag = CategoryTag.COURSE_CONTENT;
		FormatTag formatTag = FormatTag.MULTI_ANSWER;
		
		List<String> contentTypesStr = new ArrayList<>();
		List<Question> filteredQuestions = new ArrayList<>();
		filteredQuestions.add(question);
		
		List<Question> filteredUserQuestions = new ArrayList<>();
		filteredUserQuestions.add(question);
		
		user.setLockedQuestions(filteredUserQuestions);
		
		for (ContentTypeTag value : ContentTypeTag.values()) {
			contentTypesStr.add(value.toString());
		}
		
		when(userService.getUserById(1)).thenReturn(user);
		when(questionService.getAllByContentTypeTag(contentTag)).thenReturn(filteredQuestions);
		when(questionService.getAllByCategoryTag(categoryTag)).thenReturn(filteredQuestions);
		when(questionService.getAllByFormatTag(formatTag)).thenReturn(filteredQuestions);
		when(questionService.getAllUserQuestionsWithMatchingCategoryFormatTag(categoryTag, formatTag, 1)).thenReturn(filteredUserQuestions);
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.post("/questionListSorted?id=1").sessionAttr("id", 1).param("contentType", "null").param("categoryType", "null").param("formatType", "Multiple Answer"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attribute("contentTags", contentTypesStr))
		.andExpect(MockMvcResultMatchers.model().attributeExists("myQuestions"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("questions"));
				
	}
	
	@Test 
	void test_post_question_filter_return_filtered_list_null_content_format() throws Exception {
			 
		User user = new User("test name", "test email", "test password", UserRole.TRAINING);
		Question question = new Question("test question", false, true, CategoryTag.COURSE_CONTENT, FormatTag.SHORT_ANSWER, ContentTypeTag.PRO_SKILLS, user);
		
		ContentTypeTag contentTag = ContentTypeTag.APM;
		CategoryTag categoryTag = CategoryTag.COURSE_CONTENT;
		FormatTag formatTag = FormatTag.MULTI_ANSWER;
		
		List<String> contentTypesStr = new ArrayList<>();
		List<Question> filteredQuestions = new ArrayList<>();
		filteredQuestions.add(question);
		
		List<Question> filteredUserQuestions = new ArrayList<>();
		filteredUserQuestions.add(question);
		
		user.setLockedQuestions(filteredUserQuestions);
		
		for (ContentTypeTag value : ContentTypeTag.values()) {
			contentTypesStr.add(value.toString());
		}
		
		when(userService.getUserById(1)).thenReturn(user);
		when(questionService.getAllByContentTypeTag(contentTag)).thenReturn(filteredQuestions);
		when(questionService.getAllByCategoryTag(categoryTag)).thenReturn(filteredQuestions);
		when(questionService.getAllByFormatTag(formatTag)).thenReturn(filteredQuestions);
		when(questionService.getAllUserQuestionsWithMatchingCategoryFormatTag(categoryTag, formatTag, 1)).thenReturn(filteredUserQuestions);
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.post("/questionListSorted?id=1").sessionAttr("id", 1).param("contentType", "null").param("categoryType", "Course Content").param("formatType", "null"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attribute("contentTags", contentTypesStr))
		.andExpect(MockMvcResultMatchers.model().attributeExists("myQuestions"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("questions"));
				
	}
	
	@Test 
	void test_post_question_filter_return_filtered_list_null_category_format() throws Exception {
			 
		User user = new User("test name", "test email", "test password", UserRole.TRAINING);
		Question question = new Question("test question", false, true, CategoryTag.COURSE_CONTENT, FormatTag.SHORT_ANSWER, ContentTypeTag.PRO_SKILLS, user);
		
		ContentTypeTag contentTag = ContentTypeTag.APM;
		CategoryTag categoryTag = CategoryTag.COURSE_CONTENT;
		FormatTag formatTag = FormatTag.MULTI_ANSWER;
		
		List<String> contentTypesStr = new ArrayList<>();
		List<Question> filteredQuestions = new ArrayList<>();
		filteredQuestions.add(question);
		
		List<Question> filteredUserQuestions = new ArrayList<>();
		filteredUserQuestions.add(question);
		
		user.setLockedQuestions(filteredUserQuestions);
		
		for (ContentTypeTag value : ContentTypeTag.values()) {
			contentTypesStr.add(value.toString());
		}
		
		when(userService.getUserById(1)).thenReturn(user);
		when(questionService.getAllByContentTypeTag(contentTag)).thenReturn(filteredQuestions);
		when(questionService.getAllByCategoryTag(categoryTag)).thenReturn(filteredQuestions);
		when(questionService.getAllByFormatTag(formatTag)).thenReturn(filteredQuestions);
		when(questionService.getAllUserQuestionsWithMatchingCategoryFormatTag(categoryTag, formatTag, 1)).thenReturn(filteredUserQuestions);
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.post("/questionListSorted?id=1").sessionAttr("id", 1).param("contentType", "Pro Skills").param("categoryType", "null").param("formatType", "null"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attribute("contentTags", contentTypesStr))
		.andExpect(MockMvcResultMatchers.model().attributeExists("myQuestions"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("questions"));
				
	}
	
	@Test
	void test_autocomplete_responsebody() throws Exception {
		
		this.mockMvc.perform(MockMvcRequestBuilders.get("/autocompleteContentTags"))
	            .andExpect(MockMvcResultMatchers.status().isOk())
	            .andExpect(MockMvcResultMatchers.content().json("[\"Pro Skills\",\"SQL\",\"Excel VBA\",\"Business Analysis\",\"Intro to BI & Data Warehousing\",\"Extract, Transform, Load (ETL\",\"Data Visualisation & UX/UI\",\"UNIX\",\"OS Admin\",\"Python\",\"ITIL\", \r\n"
	            		+ "		\"Cloud Core Services\",\"Cloud Management Tools\", \"Java Object Oriented Development\", \"JDBC and JPA\",\r\n"
	            		+ "		\"Java EE Web\",\"Spring Framework\", \"Agile Project Management\", \"Linux Fundamentals\",\"Networking Fundamentals\",\"AWS Cloud Foundations\", \"Security Fundamentals\",\r\n"
	            		+ "		\"Jumpstart on AWS\", \"Databases\"]"));
		
	}
	
	@Test
	void test_createQuestion_form() throws Exception {
		
		User user = new User();
		user.setUserRole(UserRole.TRAINER);
		
		when(userService.getUserById((1))).thenReturn(user);
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/createQuestion").sessionAttr("id", 1))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attribute("user", user))
		.andExpect(MockMvcResultMatchers.model().attributeExists("questionObject"));
		
	}
	
	@Test
	void test_edit_question_object() throws Exception {
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/editQuestionObject"))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
		
	}
	
	@Test
	void test_editQuestion_get() throws Exception {
		
		User user = new User();
		user.setUserId(1);
		
		Question question = new Question();
		question.setUser(user);
		
		List<Answer> answers = new ArrayList<>();
		
		when(userService.getUserById(1)).thenReturn(user);
		when(questionService.getQuestionById(1)).thenReturn(question);
		when(answerService.getAllAnswerByQuestionId(1)).thenReturn(answers);
		
		TempQuestion tempQuestion = new TempQuestion(question.getQuestionId(), question.getQuestionContent(), question.isApproved(), 
				question.isReviewed(), question.getCategoryTag(), question.getFormatTag(), question.getContentTypeTag(), null);
		tempQuestion.setNewAnswers(answers);
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/editQuestion?questionId=1").sessionAttr("id", 1))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeExists("questionObject"));
//		.andExpect(MockMvcResultMatchers.model().attribute("questionObject", tempQuestion.toString()));
		
	}
	
	@Test
	void test_createQuestion_object_post() throws Exception {
		
		Answer answer = new Answer();
		List<Answer> newAnswers = new ArrayList<>();
		newAnswers.add(answer);
		
		List<String> answers = new ArrayList<>();
		answers.add("1");
		
		User user = new User();
		user.setUserRole(UserRole.TRAINER);
		
		TempQuestion questionObject = new TempQuestion();
		questionObject.setFormatTag(FormatTag.MULTI_ANSWER);
		questionObject.setAnswers(answers);
		questionObject.setUser(user);
		questionObject.setCorrectAnswers(answers);
		questionObject.setNewAnswers(newAnswers);
		
		when(userService.getUserById(1)).thenReturn(user);
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.post("/createQuestionObject").sessionAttr("id", 1).flashAttr("questionObject", questionObject))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
		
	}
	
	@Test
	void test_createQuestion_object_trainersales_multi_answer_post() throws Exception {
		
		List<String> answers = new ArrayList<>();
		answers.add("test answer");
		
		User user = new User();
		user.setUserRole(UserRole.TRAINER);
		
		TempQuestion questionObject = new TempQuestion();
		questionObject.setFormatTag(FormatTag.MULTI_ANSWER);
		questionObject.setAnswers(answers);
		questionObject.setUser(user);
		
		when(userService.getUserById(1)).thenReturn(user);
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.post("/createQuestionObject").sessionAttr("id", 1).flashAttr("questionObject", questionObject))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
		
	}
	
	@Test
	void test_editQuestion_object_post() throws Exception {
		
		User user = new User();
		User user1 = new User();
		user.setName("test name");
		user1.setName("test name");
		user.setUserId(1);
		user1.setUserId(2);
		Answer answer = new Answer();
		
		List<Answer> answers = new ArrayList<>();
		answers.add(answer);
		
		TempQuestion questionObject = new TempQuestion();
		questionObject.setUser(user1);
		questionObject.setNewAnswers(answers);
		
		when(userService.getUserById(1)).thenReturn(user);
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.post("/editQuestionObject").flashAttr("questionObject", questionObject).sessionAttr("id", 1))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
		
	}
	
	@Test
	void test_view_unreviewed_get() throws Exception {
		
		User user = new User();
		user.setName("test name");
		user.setUserRole(UserRole.TRAINER);
		
		Question question = new Question();
		question.setUser(user);
		List<Question> questions = new ArrayList<>();
		questions.add(question);
		
		when(userService.getUserById(1)).thenReturn(user);
		when(questionService.getUnreviewedQuestions()).thenReturn(questions);
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/viewUnreviewed").sessionAttr("id", 1))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attribute("questions", questions))
		.andExpect(MockMvcResultMatchers.model().attribute("user", user));
		
	}
	
	@Test
	void test_delete_question_object_get() throws Exception {
		
		User user = new User();
		user.setUserId(1);
		user.setName("test name");
		user.setUserRole(UserRole.TRAINER);
		
		User user1 = new User();
		user1.setName("test name");
		user1.setUserId(2);
		
		Question question = new Question();
		question.setQuestionContent("queston content");
		question.setUser(user1);
		
		when(questionService.getQuestionById(1)).thenReturn(question);
		when(userService.getUserById(1)).thenReturn(user);
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/deleteQuestionObject?questionId=1").sessionAttr("id", 1))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
		
	}
	
	@Test
	void test_finish_review_get() throws Exception {
		
		User student = new User("test name", "test email", "test password", UserRole.TRAINER);
		student.setUserId(1);
		
		Question question = new Question();
		question.setApproved(true);
		question.setReviewed(true);
		question.setUser(student);
		
		List<Question> questions = new ArrayList<>();
		
		when(questionService.getQuestionById(1)).thenReturn(question);
		when(userService.getUserById(1)).thenReturn(student);
		when(questionService.getUnreviewedQuestions()).thenReturn(questions);
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/finishReview?id=1&value=true").sessionAttr("id", 1))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
//		.andExpect(MockMvcResultMatchers.model().attribute(null, null);
//		.andExpect(MockMvcResultMatchers.model().attribute("questions", questions));
		
	}
	
	@Test 
	void test_remove_lock_question_in_questionList() throws Exception {
			 
		User user = new User("test name", "test email", "test password", UserRole.TRAINING);
		Question question = new Question("test question", false, true, CategoryTag.COURSE_CONTENT, FormatTag.SHORT_ANSWER, ContentTypeTag.PRO_SKILLS, user);
				
		List<Question> lockedQuestions = new ArrayList<>();
		lockedQuestions.add(question);
		
		user.setLockedQuestions(lockedQuestions);
		
		when(userService.getUserById(1)).thenReturn(user);
		when(questionService.getQuestionById(1)).thenReturn(question);
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/lockQuestion?questionId=1&page=questionlist").sessionAttr("id", 1))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
				
	}
	
	@Test 
	void test_add_lock_question_in_lockedQuestionDetails() throws Exception {
			 
		User user = new User("test name", "test email", "test password", UserRole.TRAINING);
		Question question = new Question("test question", false, true, CategoryTag.COURSE_CONTENT, FormatTag.SHORT_ANSWER, ContentTypeTag.PRO_SKILLS, user);
				
		List<Question> lockedQuestions = new ArrayList<>();
		
		user.setLockedQuestions(lockedQuestions);
		
		when(userService.getUserById(1)).thenReturn(user);
		when(questionService.getQuestionById(1)).thenReturn(question);
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/lockQuestion?questionId=1&page=lockedQuestionsDetails").sessionAttr("id", 1))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
				
	}
	
	@Test
	void test_get_locked_question_details() throws Exception {
		
		User user = new User("test name", "test email", "test password", UserRole.TRAINING);
		Question question = new Question("test question", false, true, CategoryTag.COURSE_CONTENT, FormatTag.SHORT_ANSWER, ContentTypeTag.PRO_SKILLS, user);
				
		List<Question> lockedQuestions = new ArrayList<>();
		lockedQuestions.add(question);
		
		user.setLockedQuestions(lockedQuestions);
		
		when(userService.getUserById(1)).thenReturn(user);
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/lockedQuestionsDetails").sessionAttr("id", 1))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attribute("lockedQuestions", lockedQuestions));
		
	}
	
	
}
