package com.spring.web;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.spring.web.model.Question;
import com.spring.web.model.Answer;
import com.spring.web.service.AnswerService;
import com.spring.web.service.QuestionService;
import com.spring.web.service.UserService;

@SpringBootTest
class AnswerServiceTests {

	@Autowired
	private UserService userService;
	
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private AnswerService answerService;
	
	@BeforeEach
	void setUp() throws Exception {
		
		answerService.deleteAllAnswers();
		userService.getAllStudents();
				
	}
	
	//NOTE: (Set in application.properties before running tests) spring.jpa.hibernate.ddl-auto=create
		//////////////////////TEST Answer Service///////////////////////
		
		@Test
		public void create_an_answer() {
			
			Answer answer = new Answer();
			answerService.saveOrUpdateAnswer(answer);
			
			Answer answer1 = answerService.getAnswerById(answer.getAnswerId());
			
			assertEquals(answer.toString(), answer1.toString());
		
		}
		
		@Test
		public void get_answer_return_id() {
			
			Answer answer = new Answer();
			answerService.saveOrUpdateAnswer(answer);
			
			assertEquals(answer.toString(), answerService.getAnswerById(answer.getAnswerId()).toString());
		
		}
		
		@Test
		public void get_all_answers_return_list_of_answers() {
			
			Answer answer = new Answer();
			answerService.saveOrUpdateAnswer(answer);
			
			assertEquals(1, answerService.getAllAnswers().size());
		
		}
		
		@Test
		public void delete_by_id() {
			
			Answer answer = new Answer();
			answerService.saveOrUpdateAnswer(answer);
			
			answerService.deleteById(answer.getAnswerId());
			
			assertEquals(0, answerService.getAllAnswers().size());
		
		}

		@Test
		public void delete_by_list_of_answers() {
			
			Answer answer = new Answer();
			Answer answer1 = new Answer();
			
			answerService.saveOrUpdateAnswer(answer);
			answerService.saveOrUpdateAnswer(answer1);
			
			List<Answer> answers = new ArrayList<>();
			answers.add(answer);
			answers.add(answer1);
		
			answerService.deleteAnswerList(answers);
			
			assertEquals(0, answerService.getAllAnswers().size());
		
		}
		
		@Test
		public void delete_all_answers() {
			
			Answer answer = new Answer();
			Answer answer1 = new Answer();
			
			answerService.saveOrUpdateAnswer(answer);
			answerService.saveOrUpdateAnswer(answer1);
			
			List<Answer> answers = new ArrayList<>();
			answers.add(answer);
			answers.add(answer1);
		
			answerService.deleteAllAnswers();
			
			assertEquals(0, answerService.getAllAnswers().size());
		
		}
		
		@Test
		public void get_all_answers_by_question_id() {
			
			Question question = new Question();
			questionService.saveOrUpdateQuestion(question);
			Answer answer = new Answer();
			answer.setQuestion(question);
			
			answerService.saveOrUpdateAnswer(answer);
			
			List<Answer> answers = new ArrayList<>();
			answers.add(answer);
			
			assertEquals(answers.get(0).toString(), answerService.getAllAnswerByQuestionId(question.getQuestionId()).get(0).toString());
		
		}
}
