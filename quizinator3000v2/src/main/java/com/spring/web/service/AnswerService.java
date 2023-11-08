package com.spring.web.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.web.dal.AnswerRepo;
import com.spring.web.model.Answer;

/**
 * A service layer for Answer objects that provides methods for database interaction
 * 
 * @author Matthew Veldhuizen and Cao Vinh Lam
 *
 */

@Service
public class AnswerService {

	@Autowired
	AnswerRepo answerRepo;
	
	/**
	 * Finds an answer object if there is a record with a matching ID value
	 * 
	 * @param answerId - an Integer value used to find the corresponding record
	 * @return Answer object if a match exists
	 */
	public Answer getAnswerById(Integer answerId) {
		Optional<Answer> answer = answerRepo.findById(answerId);
		
		if (answer.isPresent())
			return answer.get();
		else
			return null;
	}
	
	/**
	 * Finds all answer objects that exist
	 * 
	 * @return A list of answer objects
	 */
	public List<Answer> getAllAnswers() {
		return answerRepo.findAll();
	}
	
	/**
	 * Saves the parameter answer object to the database, updates existing record if a record with that ID already exists
	 * 
	 * @param answer - Answer object to save
	 */
	public void saveOrUpdateAnswer(Answer answer) {
		answerRepo.save(answer);
	}
	
	/**
	 * Deletes a record from the database that matches the parameter answerId
	 * 
	 * @param answerId - Integer value corresponding to Primary Key value of record in database
	 */
	public void deleteById(Integer answerId) {
		answerRepo.deleteById(answerId);
	}
	
	/**
	 * Deletes all objects from the database that are within the question list parameter
	 * 
	 * @param answers - A list of answer objects that are scheduled for deletion
	 * @return int value corresponding to the number of records deleted
	 */
	public int deleteAnswerList(List<Answer> answers) {
		int count = 0;
		
		for (Answer answer : answers) {
			answerRepo.delete(answer);
			count++;
		}
		
		return count;
	}
	
	/**
	 * hey donald - goto temp question
	 */
	
	
	/**
	 * Deletes all answer objects from the database
	 * 
	 * @return int value corresponding to the number of records deleted
	 */
	public int deleteAllAnswers() {
		int count = 0;
		List<Answer> answers = getAllAnswers();
		
		for (Answer answer : answers) {
			answerRepo.delete(answer);
			count++;
		}
		
		return count;
	}

	
	/**
	 * Finds all answer objects with userId that matches the parameter
	 * 
	 * @param questionId - An Integer value used to find the corresponding record
	 * @return A list of answer objects
	 */
	public List<Answer> getAllAnswerByQuestionId(int questionId) {
		return answerRepo.getAllAnswersByQuestionId(questionId);
	}
}
