package com.spring.web.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.web.dal.QuestionRepo;
import com.spring.web.model.Question;
import com.spring.web.model.EntityEnum.CategoryTag;
import com.spring.web.model.EntityEnum.ContentTypeTag;
import com.spring.web.model.EntityEnum.FormatTag;

/**
 * A service layer for Question objects that provides methods for database interaction
 * 
 * @author Matthew Veldhuizen and Cao Vinh Lam
 *
 */

@Service
public class QuestionService {

	@Autowired
	QuestionRepo questionRepo;
	
	/**
	 * Finds a question object if there is a record with a matching ID value
	 * 
	 * @param questionId - an Integer value used to find the corresponding record
	 * @return Question object if a match exists
	 */
	public Question getQuestionById(Integer questionId) {
		Optional<Question> question = questionRepo.findById(questionId);
		
		if (question.isPresent())
			return question.get();
		else
			return null;
	}
	
	/**
	 * Finds all question objects with an approval status that matches the parameter
	 * 
	 * @param approved - boolean value used for comparison to question objects
	 * @return A list of question objects
	 */
	public List<Question> getAllByApproved(boolean approved) {
		return questionRepo.findByApproved(approved);
	}
	
	/**
	 * Finds all question objects that have a category tag that matches the parameter
	 * 
	 * @param categoryTag - Enum value corresponding to preset category tags
	 * @return A list of question objects
	 */
	public List<Question> getAllByCategoryTag(CategoryTag categoryTag) {
		return questionRepo.findByCategoryTag(categoryTag);
	}

	/**
	 * Finds all question objects that have a format tag that matches the parameter
	 * 
	 * @param formatTag - Enum value corresponding to preset format tags
	 * @return A list of question objects
	 */
	public List<Question> getAllByFormatTag(FormatTag formatTag) {
		return questionRepo.findByFormatTag(formatTag);
	}

	/**
	 * Finds all question objects that have a content type tag that matches the parameter
	 * 
	 * @param contentTypeTag - Enum value corresponding to preset content type tags
	 * @return A list of question objects
	 */
	public List<Question> getAllByContentTypeTag(ContentTypeTag contentTypeTag) {
		return questionRepo.findByContentTypeTag(contentTypeTag);
	}
	
	/**
	 * Finds all question objects that exist
	 * 
	 * @return A list of question objects
	 */
	public List<Question> getAllQuestions() {
		return questionRepo.findAll();
	}
	
	/**
	 * Saves the parameter question object to the database, updates existing record if a record with that ID already exists
	 * 
	 * @param question - Question object to save
	 */
	public void saveOrUpdateQuestion(Question question) {
		questionRepo.save(question);
	}
	
	/**
	 * Deletes a record from the database that matches the parameter questionId
	 * @param questionId - Integer value corresponding to Primary Key value of record in database 
	 */
	public void deleteById(Integer questionId) {
		questionRepo.deleteById(questionId);
	}
	
	/**
	 * Deletes all objects from the database that are within the question list parameter
	 * @param questions - A list of question objects that are scheduled for deletion
	 * @return int value corresponding to the number of records deleted
	 */
	public int deleteQuestionList(List<Question> questions) {
		int count = 0;
		
		for (Question question : questions) {
			questionRepo.delete(question);
			count++;
		}
		
		return count;
	}
	
	/**
	 * Deletes all question objects from the database
	 * @return int value corresponding to the number of records deleted
	 */
	public int deleteAllQuestions() {
		int count = 0;
		List<Question> questions = getAllQuestions();
		
		for (Question question : questions) {
			questionRepo.delete(question);
			count++;
		}
		
		return count;
	}

	/**
	 * Finds all question objects with userId that matches the parameter
	 * @param user - An Integer value used to find the corresponding record
	 * @return A list of question objects
	 */
	public List<Question> getAllQuestionsWithUserId(int user) {
		return questionRepo.getAllQuestionsWithUserId(user);
	}
	
	/**
	 * Finds the most recently created question by the user 
	 * @param user - An Integer value used to find the corresponding record 
	 * @return A question object recently created by the user
	 */
	public Question getMostRecentQuestionCreatedWithUserId(int user) {
		return questionRepo.getMostRecentQuestionCreatedWithUserId(user);
	}
	
	/**
	 * finds all questions that have not been reviewed
	 * 
	 * @return List of question unreviewed
	 */
	public List<Question> getUnreviewedQuestions() {
		
		return questionRepo.getUnreviewedQuestions();
	}
		
	/**
	 * Gets all questions that match the category tag, format tag and content type tag
	 * @param formatTag - Enum value corresponding to preset format tags
	 * @param contentTypeTag - Enum value corresponding to preset content type tags
	 * @param categoryTag - Enum value corresponding to preset category tags
	 * @return A list of question objects
	 */
	public List<Question> getAllQuestionsWithMatchingFormatContentCatgoryTag(FormatTag formatTag, ContentTypeTag contentTypeTag, CategoryTag categoryTag){
		
		return questionRepo.getAllQuestionsWithMatchingFormatContentCatgoryTag(formatTag, contentTypeTag, categoryTag);
	}
	
	/**
	 * Gets all questions that match the category tag, format tag, content type tag and are approved
	 * @param formatTag - Enum value corresponding to preset format tags
	 * @param contentTypeTag - Enum value corresponding to preset content type tags
	 * @param categoryTag - Enum value corresponding to preset category tags
	 * @return A list of question objects
	 */
	public List<Question> getAllQuestionsWithMatchingFormatContentCatgoryTagApproved(FormatTag formatTag, ContentTypeTag contentTypeTag, CategoryTag categoryTag){
		
		return questionRepo.getAllQuestionsWithMatchingFormatContentCatgoryTagApproved(formatTag, contentTypeTag, categoryTag);
	}
	
	/**
	 * Gets all questions that match the category tag, format tag, content type tag and and belong to the user with userId
	 * @param formatTag - Enum value corresponding to preset format tags
	 * @param contentTypeTag - Enum value corresponding to preset content type tags
	 * @param categoryTag - Enum value corresponding to preset category tags
	 * @param userId - Id corresponding to user
	 * @return A list of question objects
	 */
	public List<Question> getAllUserQuestionsWithMatchingFormatContentCatgoryTag(FormatTag formatTag, ContentTypeTag contentTypeTag, CategoryTag categoryTag, int userId){
		
		return questionRepo.getAllUserQuestionsWithMatchingFormatContentCatgoryTag(formatTag, contentTypeTag, categoryTag, userId);
	}
	
	/**
	 * Finds all question objects that have a category tag that matches the parameter and are approved
	 * 
	 * @param categoryTag - Enum value corresponding to preset category tags
	 * @return A list of question objects
	 */
	public List<Question> getAllByCategoryTagApproved(CategoryTag categoryTag) {
		return questionRepo.findByCategoryTagApproved(categoryTag);
	}

	/**
	 * Finds all question objects that have a format tag that matches the parameter and are approved
	 * 
	 * @param formatTag - Enum value corresponding to preset format tags
	 * @return A list of question objects
	 */
	public List<Question> getAllByFormatTagApproved(FormatTag formatTag) {
		return questionRepo.findByFormatTagApproved(formatTag);
	}

	/**
	 * Finds all question objects that have a content type tag that matches the parameter and are approved
	 * 
	 * @param contentTypeTag - Enum value corresponding to preset content type tags
	 * @return A list of question objects
	 */
	public List<Question> getAllByContentTypeTagApproved(ContentTypeTag contentTypeTag) {
		return questionRepo.findByContentTypeTagApproved(contentTypeTag);
	}
	
	/**
	 * Finds all question objects that have a category tag that matches the parameter
	 * 
	 * @param categoryTag - Enum value corresponding to preset category tags
	 * @param userId - Id corresponding to user
	 * @return A list of question objects
	 */
	public List<Question> getAllByUserAndCategoryTag(CategoryTag categoryTag, int userId) {
		return questionRepo.findByUserAndCategoryTag(categoryTag, userId);
	}

	/**
	 * Finds all question objects that have a format tag that matches the parameter
	 * 
	 * @param formatTag - Enum value corresponding to preset format tags
	 * @param userId - Id corresponding to user
	 * @return A list of question objects
	 */
	public List<Question> getAllByUserAndFormatTag(FormatTag formatTag, int userId) {
		return questionRepo.findByUserAndFormatTag(formatTag, userId);
	}

	/**
	 * Finds all question objects that have a content type tag that matches the parameter
	 * 
	 * @param contentTypeTag - Enum value corresponding to preset content type tags
	 * @param userId - Id corresponding to user
	 * @return A list of question objects
	 */
	public List<Question> getAllByUserAndContentTypeTag(ContentTypeTag contentTypeTag, int userId) {
		return questionRepo.findByUserAndContentTypeTag(contentTypeTag, userId);
	}
	
	/**
	 * Gets all questions that match the format tag, content type tag and are approved
	 * @param formatTag - Enum value corresponding to preset format tags
	 * @param contentTypeTag - Enum value corresponding to preset content type tags
	 * @return A list of question objects
	 */
	public List<Question> getAllQuestionsWithMatchingFormatContentTagApproved(FormatTag formatTag, ContentTypeTag contentTypeTag){
		
		return questionRepo.getAllQuestionsWithMatchingFormatContentTagApproved(formatTag, contentTypeTag);
	}
	
	/**
	 * Gets all questions that match the category tag, content type tag and are approved
	 * @param contentTypeTag - Enum value corresponding to preset content type tags
	 * @param categoryTag - Enum value corresponding to preset category tags
	 * @return A list of question objects
	 */
	public List<Question> getAllQuestionsWithMatchingContentCatgoryTagApproved(ContentTypeTag contentTypeTag, CategoryTag categoryTag){
		
		return questionRepo.getAllQuestionsWithMatchingCategoryContentTagApproved(contentTypeTag, categoryTag);
	}
	
	/**
	 * Gets all questions that match the category tag, format tag and are approved
	 * @param formatTag - Enum value corresponding to preset format tags
	 * @param categoryTag - Enum value corresponding to preset category tags
	 * @return A list of question objects
	 */
	public List<Question> getAllQuestionsWithMatchingFormatCatgoryTagApproved(FormatTag formatTag, CategoryTag categoryTag){
		
		return questionRepo.getAllQuestionsWithMatchingCategoryFormatTagApproved(formatTag, categoryTag);
	}
	
	/**
	 * Gets all questions that match the content type tag, format tag and belong to the user with userId
	 * @param formatTag - Enum value corresponding to preset format tags
	 * @param contentTypeTag - Enum value corresponding to preset content type tags
	 * @param id - Id corresponding to user
	 * @return A list of question objects
	 */
	public List<Question> getAllUserQuestionsWithMatchingFormatContentTag(FormatTag formatTag, ContentTypeTag contentTypeTag, int id){
		
		return questionRepo.getAllUserQuestionsWithMatchingFormatContentTag(formatTag, contentTypeTag, id);
	}
	
	/**
	 * Gets all questions that match the content type tag, category tag and belong to the user with userId
	 * @param categoryTag - Enum value corresponding to preset category tags
	 * @param contentTypeTag - Enum value corresponding to preset content type tags
	 * @param id - Id corresponding to user
	 * @return A list of question objects
	 */
	public List<Question> getAllUserQuestionsWithMatchingCategoryContentTag(CategoryTag categoryTag, ContentTypeTag contentTypeTag, int id){
		
		return questionRepo.getAllUserQuestionsWithMatchingCategoryContentTag(contentTypeTag, categoryTag, id);
	}
	
	/**
	 * Gets all questions that match the category tag, format tag and belong to the user with userId
	 * @param categoryTag - Enum value corresponding to preset category tags
	 * @param formatTag - Enum value corresponding to preset format tags
	 * @param id - Id corresponding to user
	 * @return A list of question objects
	 */
	public List<Question> getAllUserQuestionsWithMatchingCategoryFormatTag(CategoryTag categoryTag, FormatTag formatTag, int id){
		
		return questionRepo.getAllUserQuestionsWithMatchingCategoryFormatTag(formatTag, categoryTag, id);
	}
}
