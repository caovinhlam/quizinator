package com.spring.web;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.spring.web.dal.QuestionRepo;
import com.spring.web.dal.UserRepo;
import com.spring.web.model.Question;
import com.spring.web.model.User;
import com.spring.web.model.EntityEnum.CategoryTag;
import com.spring.web.model.EntityEnum.ContentTypeTag;
import com.spring.web.model.EntityEnum.FormatTag;
import com.spring.web.model.EntityEnum.UserRole;
import com.spring.web.service.QuestionService;
import com.spring.web.service.UserService;

@SpringBootTest
class QuestionRepoTests {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private QuestionRepo questionRepo;
	
	@BeforeEach
	void setUp() throws Exception {
		
		questionService.deleteAllQuestions();
		userService.deleteAllUsers();
		
	}
	
	//////////////////////TEST Question Service///////////////////////
	
	@Test
	public void find_by_id_return_optQuestion() {
		
		Question question = new Question();	
		questionService.saveOrUpdateQuestion(question);
		
		Optional<Question> quesId = questionRepo.findById(question.getQuestionId());
		Question questionTest = quesId.get();
		
		assertEquals(question.getQuestionId(), questionTest.getQuestionId());
	
	}
	
	@Test
	public void find_by_approved_return_question_list() {
		
		Question question = new Question("test question", true, false, CategoryTag.COURSE_CONTENT, FormatTag.SHORT_ANSWER, ContentTypeTag.PRO_SKILLS, null);
		questionService.saveOrUpdateQuestion(question);
		
		List<Question> approvedQuestions = questionRepo.findByApproved(true);
		
		assertTrue(approvedQuestions.size() == 1);
	
	}
	
	@Test
	public void find_by_categoryTag_return_question_list() {
		
		Question question = new Question("test question", true, false, CategoryTag.COURSE_CONTENT, FormatTag.SHORT_ANSWER, ContentTypeTag.PRO_SKILLS, null);
		questionService.saveOrUpdateQuestion(question);
		
		List<Question> questions = questionRepo.findByCategoryTag(CategoryTag.COURSE_CONTENT);
		
		assertTrue(questions.size() == 1);
	
	}
	
	@Test
	public void find_by_formatTag_return_question_list() {
		
		Question question = new Question("test question", true, false, CategoryTag.COURSE_CONTENT, FormatTag.SHORT_ANSWER, ContentTypeTag.PRO_SKILLS, null);
		questionService.saveOrUpdateQuestion(question);
		
		List<Question> questions = questionRepo.findByFormatTag(FormatTag.SHORT_ANSWER);
		
		assertTrue(questions.size() == 1);
	
	}
	
	@Test
	public void find_by_contentTypeTag_return_question_list() {
		
		Question question = new Question("test question", true, false, CategoryTag.COURSE_CONTENT, FormatTag.SHORT_ANSWER, ContentTypeTag.PRO_SKILLS, null);
		questionService.saveOrUpdateQuestion(question);
		
		List<Question> questions = questionRepo.findByContentTypeTag(ContentTypeTag.PRO_SKILLS);
		
		assertTrue(questions.size() == 1);
	
	}
	
	@Test
	public void find_by_userId_return_question_list() {
		
		User user = new User("test user", "test email", "test password", UserRole.TRAINING);
		userService.saveOrUpdate(user);
		
		Question question = new Question("test question", true, false, CategoryTag.COURSE_CONTENT, FormatTag.SHORT_ANSWER, ContentTypeTag.PRO_SKILLS, user);
		questionService.saveOrUpdateQuestion(question);
		
		List<Question> questions = questionRepo.getAllQuestionsWithUserId(user.getUserId());
		
		assertTrue(questions.size() == 1);
		
	}
	
	@Test
	public void get_most_recent_question_return_question() {
		
		User user = new User("test user", "test email", "test password", UserRole.TRAINING);
		userService.saveOrUpdate(user);
		
		Question question1 = new Question("test question1", true, false, CategoryTag.COURSE_CONTENT, FormatTag.SHORT_ANSWER, ContentTypeTag.PRO_SKILLS, user);
		Question question2 = new Question("test question2", true, false, CategoryTag.COURSE_CONTENT, FormatTag.SHORT_ANSWER, ContentTypeTag.PRO_SKILLS, user);
		
		questionService.saveOrUpdateQuestion(question1);
		questionService.saveOrUpdateQuestion(question2);
		
		Question mostRecentQuestion = questionRepo.getMostRecentQuestionCreatedWithUserId(user.getUserId());
		
		assertEquals(mostRecentQuestion.toString(), question2.toString());
		
	}

}
