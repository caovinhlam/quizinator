package com.spring.web.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.web.dal.QuizRepo;
import com.spring.web.model.Question;
import com.spring.web.model.Quiz;

/**
 * Service for Quiz class
 * 
 * @author samantha jermyn
 */

@Service
public class QuizService {

	@Autowired
	QuizRepo quizRepo;
	
	/**
	 * Returns the quiz with the corresponding ID, if none exit then return null
	 * 
	 * @param id - id of quiz to find
	 * @return Quiz - if none found then null
	 */
	public Quiz getQuizById(int id) {
		
		Optional<Quiz> quizs = quizRepo.findById(id);
		
		if(quizs.isPresent())			// process optional
			return quizs.get();
		else 
			return null;
	}
	
	
	
	/**
	 * Gets all quizes and returns the list of them all
	 * 
	 * @return Quiz list 
	 */
	public List<Quiz> getAllQuiz(){
		return quizRepo.findAll();
	}

	
	/**
	 * adds quiz parameter to database
	 * 
	 * @param quiz - quiz to save
	 */
	public void saveQuiz(Quiz quiz) {
		quizRepo.save(quiz);
	}
	
	/**
	 * updates quiz parameter in database
	 * 
	 * @param quiz - quiz to update
	 */
	public  void updateQuiz(Quiz quiz){
		saveQuiz(quiz);
	}
	
	
	/**
	 * deletes corresponding quiz with parameter ID
	 * 
	 * @param id - id of quiz to delete
	 */
	public void deleteById(int id){
		quizRepo.deleteById(id);
	}
	
	
	/**
	 * gets all quizes with the userID parameter
	 * 
	 * @param userID - id of user
	 * @return quizes list with same userID
	 */
	public List<Quiz> getAllQuizsWithUserId(int userID){
		return quizRepo.getAllQuizsWithUserId(userID);
	}

	

	/**
	 * deletes all quiz records in database
	 * 
	 * @return integer representing total amount deleted
	 */
	public int deleteAll() {
		int count = 0;
		
		List<Quiz> quizs = quizRepo.findAll();
			
		for (Quiz quiz : quizs) {
			deleteById(quiz.getQuizID());
			count++;
		}	
		return count;
	}

	/**
	 * gets a list of all unreviewed quizzes
	 * 
	 * @return - returns a list of quizzes
	 */
	public List<Quiz> getUnreviewedQuizzes() {
		return quizRepo.getUnreviewedQuizzes();
	}
	
	/**
	 * gets a list of all reviewed quizzes
	 * 
	 * @return - returns a list of quizzes
	 */
	public List<Quiz> getreviewedQuizzes() {
		return quizRepo.getreviewedQuizzes();
	}
	
	/**
	 * gets a list of all approved quizzes
	 * 
	 * @return - returns a list of quizzes
	 */
	public List<Quiz> getAllApprovedQuizzes() {
		return quizRepo.getAllApprovedQuizzes();
	}
}
