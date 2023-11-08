package com.spring.web;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.spring.web.model.Answer;
import com.spring.web.model.Notification;
import com.spring.web.model.Question;
import com.spring.web.service.AnswerService;
import com.spring.web.service.QuestionService;
import com.spring.web.service.QuizService;

@SpringBootTest
class AnswerServiceTest {


	@Autowired
	AnswerService answerService;
	
	@Autowired
	QuestionService questionService;
	
	Answer answer1;
	Answer answer2;
	Answer answer3;
	
	Question question;
	
	@BeforeEach
	void before() {
		answer1 = new Answer();
		answer2 = new Answer();
		answer3 = new Answer();
		
		question = new Question();
	}
	

	@Test
	void save_blank_answer_test() {
		answerService.saveOrUpdateAnswer(answer1);
		
		Answer fromDatabase = answerService.getAnswerById(answer1.getAnswerId());
		assertEquals(answer1.getAnswerId(), fromDatabase.getAnswerId());
		
		answerService.deleteById(answer1.getAnswerId());
	}
	
	@Test
	void save_filled_answer_test() {
		
		answer1.setAnswer("1");
		answer1.setCorrect(true);
		answer1.setQuestion(null);
		answerService.saveOrUpdateAnswer(answer1);
		
		Answer fromDatabase = answerService.getAnswerById(answer1.getAnswerId());
		
		assertEquals(answer1.getAnswerId(), fromDatabase.getAnswerId());
		assertEquals(answer1.getAnswer(), fromDatabase.getAnswer());
		assertEquals(answer1.isCorrect(), fromDatabase.isCorrect());
		assertEquals(answer1.getQuestion(), fromDatabase.getQuestion());
		assertEquals(answer1.toString(), fromDatabase.toString());

		answerService.deleteById(answer1.getAnswerId());
	}
	
	@Test
	void get_no_answer_with_id_test() {
		answerService.deleteById(1);
		
		assertEquals(null, answerService.getAnswerById(1));
	}
	
	@Test
	void get_all_3_answers_test() {
		
		answerService.saveOrUpdateAnswer(answer1);
		answerService.saveOrUpdateAnswer(answer2);
		answerService.saveOrUpdateAnswer(answer3);

		assertEquals(3, answerService.getAllAnswers().size());
	}
	
	@Test
	void get_no_answer_test() {
		answerService.deleteById(1);
		
		assertEquals(null, answerService.getAnswerById(1));
	}
	
	@Test
	void delete_0_test() {
		answerService.deleteAllAnswers();
		
		assertEquals(0, answerService.deleteAllAnswers());
	}
	
	@Test
	void delete_all_3_test() {
		answerService.deleteAllAnswers();
		
		answerService.saveOrUpdateAnswer(answer1);
		answerService.saveOrUpdateAnswer(answer2);
		answerService.saveOrUpdateAnswer(answer3);
		
		assertEquals(3, answerService.deleteAllAnswers());
		answerService.deleteAllAnswers();

	}
	
	@Test
	void delete_3_from_list_test() {
		answerService.deleteAllAnswers();
		
		answerService.saveOrUpdateAnswer(answer1);
		answerService.saveOrUpdateAnswer(answer2);
		answerService.saveOrUpdateAnswer(answer3);
		
		assertEquals(3, answerService.deleteAnswerList(answerService.getAllAnswers()));

	}
	
	@Test
	void alternative_constructor_test() {
		questionService.saveOrUpdateQuestion(question);

		Answer answer4 = new Answer("1", question);
		Answer answer5 = new Answer("1", true, question);
		
		answerService.saveOrUpdateAnswer(answer5);
		answerService.saveOrUpdateAnswer(answer4);
		answerService.saveOrUpdateAnswer(answer3);
		answerService.saveOrUpdateAnswer(answer2);
		answerService.saveOrUpdateAnswer(answer1);
		
		assertEquals(2, answerService.getAllAnswerByQuestionId(question.getQuestionId()).size());
	}
	
	@Test
	void SetID_test() {
		answerService.saveOrUpdateAnswer(answer1);

		int original = answer1.getAnswerId();
		answer1.setAnswerId(original +1);
		
		answerService.saveOrUpdateAnswer(answer1);

		Answer fromData = answerService.getAnswerById(answer1.getAnswerId());
		assertEquals(fromData.getAnswerId(), original+1);
	}
	
	
	
	
	
	

}
