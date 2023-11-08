package com.spring.web.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.web.dal.QuestionAttemptRepo;
import com.spring.web.model.Question;
import com.spring.web.model.QuestionAttempt;

/**
 * 
 * the question attempt server used for communication between the program and DB
 * 
 * @author Matthew Veldhuizen
 *
 */
@Service
public class QuestionAttemptService {

	@Autowired
	QuestionAttemptRepo questionAttemptRepo;
	
	/**
	 * gets the question attempt object based on the id of the question attempt being seeked
	 * 
	 * @param id - the id that needs to be checked for
	 * @return - returns the question attempt or null if none are found
	 */
	public QuestionAttempt getQuestionAttemptById(Integer id) {
		
		Optional<QuestionAttempt> questionAttempt = questionAttemptRepo.findById(id);
		
		if(questionAttempt.isPresent())			// process optional
			return questionAttempt.get();
		else 
			return null;
	}
	
	/**
	 * returns a list of every question attempt in the DB
	 * 
	 * @return - returns a list of all question attempts
	 */
	public List<QuestionAttempt> getAllQuestionAttempts() {
		return questionAttemptRepo.findAll();
	}
	
	/**
	 * returns a list of question attempts if they match the question and submission ids
	 * 
	 * @param questionId - the question id to match the attempt too 
	 * @param submissionID - the submission id to match the attempt too
	 * @return - returns a list of the questionattempts matching the 2 ids
	 */
	public List<QuestionAttempt> getQuestionAttemptsByQuestionAndSubmission(Integer questionId, Integer submissionID) {
		return questionAttemptRepo.getQuestionAttemptsByQuestionAndSubmission(questionId, submissionID);
	}
	
	/**
	 * saves or updates the question attempts in the DB
	 * 
	 * @param questionAttempt - takes the question attempt that needs to be saved to the DB
	 */
	public void saveOrUpdateQuestionAttempt(QuestionAttempt questionAttempt) {
		questionAttemptRepo.save(questionAttempt);
	}
	
	/**
	 * deletes the question attempt based on the id
	 * 
	 * @param id - the id of the question attempt to be deleted 
	 */
	public void deleteById(Integer id) {
		questionAttemptRepo.deleteById(id);
	}
	
	/**
	 * deletes all the question attempts stored in the DB
	 * 
	 * @return returns a count of all the quesiton attempts to delete
	 */
	public int deleteAll () {
		
		int count = 0;
		
		List<QuestionAttempt> questionAttempts = questionAttemptRepo.findAll();
		
		for (QuestionAttempt questionAttempt : questionAttempts) {
			deleteById(questionAttempt.getQuestionAttemptID());
			count++;
		}
		
		return count;
		
	}
	
}
