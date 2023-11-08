package com.spring.web;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.spring.web.model.Answer;
import com.spring.web.model.Question;
import com.spring.web.model.User;
import com.spring.web.model.EntityEnum.CategoryTag;
import com.spring.web.model.EntityEnum.ContentTypeTag;
import com.spring.web.model.EntityEnum.FormatTag;
import com.spring.web.service.AnswerService;
import com.spring.web.service.QuestionService;
import com.spring.web.service.UserService;




@SpringBootTest
class QuestionServiceTest {

	
	
	@Autowired
	AnswerService answerService;
	
	@Autowired
	QuestionService questionService;
	
	@Autowired
	UserService userService;
	
	
	Question question1;
	Question question2;
	Question question3;

	
	@BeforeEach
	void before() {
		question1 = new Question();
		question2 = new Question();
		question3 = new Question();
	}
	
	@Test
	void save_blank_question_test() {
		questionService.saveOrUpdateQuestion(question1);
		
		Question fromData = questionService.getQuestionById(question1.getQuestionId());
		assertEquals(question1.getQuestionId(), fromData.getQuestionId());
	}
	
	@Test
	void save_filled_question_test() { 
		question1.setApproved(true);
		question1.setCategoryTag(CategoryTag.COURSE_CONTENT);
		question1.setContentTypeTag(ContentTypeTag.BI_WEEK_1);
		question1.setFormatTag(FormatTag.SHORT_ANSWER);
		question1.setQuestionContent("question");
		question1.setReviewed(true);
		question1.setUser(null);
		
		questionService.saveOrUpdateQuestion(question1);
		Question fromData = questionService.getQuestionById(question1.getQuestionId());
		
		assertEquals(question1.getQuestionId(), fromData.getQuestionId());
		assertEquals(question1.isApproved(), fromData.isApproved());
		assertEquals(question1.isReviewed(), fromData.isReviewed());
		assertEquals(question1.getUser(), fromData.getUser());
		
		assertEquals(question1.getCategoryTag(), fromData.getCategoryTag());
		assertEquals(question1.getContentTypeTag(), fromData.getContentTypeTag());
		assertEquals(question1.getFormatTag(), fromData.getFormatTag());
		
		assertEquals(question1.toString(), fromData.toString());




	}
	
	
	
	@Test
	void get_non_existing_question_test() {
		questionService.deleteById(1);
		
		assertEquals(null, questionService.getQuestionById(1));
	}
	
	@Test
	void get_all_approved_and_unreviewed_test() {
		questionService.deleteAllQuestions();
		
		User user = new User();
		userService.saveOrUpdate(user);
		
		question1.setApproved(false);
		question2.setApproved(true);
		question3.setApproved(true);
		
		question1.setReviewed(true);
		question2.setReviewed(true);
		question3.setReviewed(false);
		
		question1.setFormatTag(FormatTag.MULTI_ANSWER);
		question2.setFormatTag(FormatTag.MULTI_ANSWER);
		question3.setFormatTag(FormatTag.SHORT_ANSWER);
		
		question1.setCategoryTag(CategoryTag.COURSE_CONTENT);
		question2.setCategoryTag(CategoryTag.COURSE_CONTENT);
		question3.setCategoryTag(CategoryTag.INTERVIEW_PREP);
		
		question1.setContentTypeTag(ContentTypeTag.BI_WEEK_1);
		question2.setContentTypeTag(ContentTypeTag.BI_WEEK_1);
		question3.setContentTypeTag(ContentTypeTag.BI_WEEK_2);
		
		question2.setUser(user);
		question3.setUser(user);
		
		questionService.saveOrUpdateQuestion(question1);
		questionService.saveOrUpdateQuestion(question2);
		questionService.saveOrUpdateQuestion(question3);



		
		assertEquals(2, questionService.getAllByApproved(true).size());
		assertEquals(1, questionService.getAllByApproved(false).size());
		
		assertEquals(1, questionService.getAllByFormatTag(FormatTag.SHORT_ANSWER).size());
		assertEquals(2, questionService.getAllByFormatTag(FormatTag.MULTI_ANSWER).size());
		assertEquals(0, questionService.getAllByFormatTag(FormatTag.MULTI_CHOICE).size());

		assertEquals(2, questionService.getAllByCategoryTag(CategoryTag.COURSE_CONTENT).size());
		assertEquals(1, questionService.getAllByCategoryTag(CategoryTag.INTERVIEW_PREP).size());
		
		assertEquals(2, questionService.getAllByContentTypeTag(ContentTypeTag.BI_WEEK_1).size());
		assertEquals(1, questionService.getAllByContentTypeTag(ContentTypeTag.BI_WEEK_2).size());
		
		assertEquals(3, questionService.getAllQuestions().size());
		assertEquals(2, questionService.getAllQuestionsWithUserId(user.getUserId()).size());
		assertEquals(question3.toString(), questionService.getMostRecentQuestionCreatedWithUserId(user.getUserId()).toString());
		
		assertEquals(1, questionService.getUnreviewedQuestions().size());
		
		assertEquals(3, questionService.deleteQuestionList(questionService.getAllQuestions()));
	}
	
	
	@Test
	void set_ID_test() {
		User user = new User();
		userService.saveOrUpdate(user);
		
		Question question4 = new Question("?", false, false, CategoryTag.COURSE_CONTENT, FormatTag.MULTI_CHOICE, ContentTypeTag.AWS_CLOUD_FOUNDATIONS, user);
		questionService.saveOrUpdateQuestion(question4);

		int original = question4.getQuestionId();
		String newInfo = "new Stirng";
		
		question4.setQuestionContent(newInfo);
		question4.setQuestionId(original +1);
		
		questionService.saveOrUpdateQuestion(question4);

		Question fromData = questionService.getQuestionById(question4.getQuestionId());
		assertEquals(fromData.getQuestionId(), original+1);
		assertEquals(fromData.getQuestionContent(), newInfo);

	}
	
	@Test
	void get_all_by_category_tag() {
		questionService.deleteAllQuestions();
		
		User user = new User();
		userService.saveOrUpdate(user);
		
		question1.setApproved(true);
		question2.setApproved(true);
		question3.setApproved(true);
		
		question1.setReviewed(true);
		question2.setReviewed(true);
		question3.setReviewed(true);
		
		question1.setFormatTag(FormatTag.MULTI_ANSWER);
		question2.setFormatTag(FormatTag.MULTI_ANSWER);
		question3.setFormatTag(FormatTag.SHORT_ANSWER);
		
		question1.setCategoryTag(CategoryTag.COURSE_CONTENT);
		question2.setCategoryTag(CategoryTag.COURSE_CONTENT);
		question3.setCategoryTag(CategoryTag.INTERVIEW_PREP);
		
		question1.setContentTypeTag(ContentTypeTag.BI_WEEK_1);
		question2.setContentTypeTag(ContentTypeTag.BI_WEEK_1);
		question3.setContentTypeTag(ContentTypeTag.BI_WEEK_2);
		
		question2.setUser(user);
		question3.setUser(user);
		
		questionService.saveOrUpdateQuestion(question1);
		questionService.saveOrUpdateQuestion(question2);
		questionService.saveOrUpdateQuestion(question3);



		
		assertEquals(2, questionService.getAllByCategoryTagApproved(CategoryTag.COURSE_CONTENT).size());
		assertEquals(1, questionService.getAllByCategoryTagApproved(CategoryTag.INTERVIEW_PREP).size());
	}
	
	@Test
	void get_all_by_format_tag() {
		questionService.deleteAllQuestions();
		
		User user = new User();
		userService.saveOrUpdate(user);
		
		question1.setApproved(true);
		question2.setApproved(true);
		question3.setApproved(true);
		
		question1.setReviewed(true);
		question2.setReviewed(true);
		question3.setReviewed(true);
		
		question1.setFormatTag(FormatTag.MULTI_ANSWER);
		question2.setFormatTag(FormatTag.MULTI_ANSWER);
		question3.setFormatTag(FormatTag.SHORT_ANSWER);
		
		question1.setCategoryTag(CategoryTag.COURSE_CONTENT);
		question2.setCategoryTag(CategoryTag.COURSE_CONTENT);
		question3.setCategoryTag(CategoryTag.INTERVIEW_PREP);
		
		question1.setContentTypeTag(ContentTypeTag.BI_WEEK_1);
		question2.setContentTypeTag(ContentTypeTag.BI_WEEK_1);
		question3.setContentTypeTag(ContentTypeTag.BI_WEEK_2);
		
		question2.setUser(user);
		question3.setUser(user);
		
		questionService.saveOrUpdateQuestion(question1);
		questionService.saveOrUpdateQuestion(question2);
		questionService.saveOrUpdateQuestion(question3);



		
		assertEquals(2, questionService.getAllByFormatTagApproved(FormatTag.MULTI_ANSWER).size());
		assertEquals(1, questionService.getAllByFormatTagApproved(FormatTag.SHORT_ANSWER).size());
	}
	
	

}
