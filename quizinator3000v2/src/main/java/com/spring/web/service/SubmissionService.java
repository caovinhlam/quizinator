package com.spring.web.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.web.dal.SubmissionRepo;
import com.spring.web.model.Submission;
import com.spring.web.model.User;
import com.spring.web.model.EntityEnum.UserRole;

/**
 * this service class is the layer between the model and repo
 * 
 * @author Stanley Chilton
 *
 */

@Service
public class SubmissionService {
	@Autowired
	SubmissionRepo submissionRepo;
	
	/**
	 * used to get the submission entity stored in the DB
	 * 
	 * @param submissionId - the submission id you are trying to get
	 * @return - returns a submission object if there was one
	 */
	public Submission getSubmissionById(Integer submissionId) {
		Optional<Submission> Submission = submissionRepo.findById(submissionId);
		
		if (Submission.isPresent())
			return Submission.get();
		else
			return null;
	}
	
	
	/**
	 * Get all submissions from the database
	 * 
	 * @return List of submission objects
	 */
	public List<Submission> getAllSubmissions() {
		return submissionRepo.findAll();
	}
	
	/**
	 * Get all submissions from the database
	 * 
	 * @param id - this is used to pass in the submissons for a specific id
	 * @return List of submission objects
	 */
	public List<Submission> getAllCourseContentSubmissions(int id) {
		List<Submission> submissionsList = submissionRepo.getAllSubmissionsWithUserId(id);
		List<Submission> revisedList = new ArrayList<>();
		for(Submission submission : submissionsList) {
			if(submission.getQuiz().getQuizType().name() == "COURSE_CONTENT") {
				revisedList.add(submission);
			}
		}
		return revisedList;
	}
	
	
	/**
	 * Get all submissions from the database
	 * 
	 * @param id - this is used to pass in the submissons for a specific id
	 * @return List of submission objects
	 */
	public List<Submission> getAllInterviewSubmissions(int id) {
		List<Submission> submissionsList = submissionRepo.getAllSubmissionsWithUserId(id);
		List<Submission> revisedList = new ArrayList<>();
		for(Submission submission : submissionsList) {
			if(submission.getQuiz().getQuizType().name() == "INTERVIEW_PREP") {
				revisedList.add(submission);
			}
		}
		return revisedList;
	}
	
	/**
	 * Create a new submission entry into the database or Update an existing entry in the database with the inputed submission object
	 * 
	 * @param submission - submission Entity
	 */
	public void saveOrUpdate(Submission submission) {
		submissionRepo.save(submission);
	}
	
	// wow you are almost there goto the quiz model
	
	/**
	 * Delete submission in the database by their id
	 * 
	 * @param subID - Id to search and delete submissions with
	 */
	public void deleteById(int subID) {
		submissionRepo.deleteById(subID);
	}
	
	/**
	 * gets all the submissions in the database then iterates over these entries deleting them all
	 * 
	 * @return returns the count of how many were deleted
	 */
	public int deleteAll() {
		int count = 0;
		List<Submission> submissions = getAllSubmissions();
		
		for(Submission submission : submissions) {
			submissionRepo.delete(submission);
			count++;
		}
		
		return count;
	}
	
	
	/**
	 * Returns a list of all submission made by a 
	 * 
	 * @param id - int value for a user ID
	 * @return list of all submissions from with user ID
	 */
	public List<Submission> getAllSubmissionsFromUser(int id) {
	
		return submissionRepo.getAllSubmissionsWithUserId(id);
	}
	
}
