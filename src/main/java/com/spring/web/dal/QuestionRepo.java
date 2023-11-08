package com.spring.web.dal;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spring.web.model.Question;
import com.spring.web.model.Quiz;
import com.spring.web.model.EntityEnum.CategoryTag;
import com.spring.web.model.EntityEnum.ContentTypeTag;
import com.spring.web.model.EntityEnum.FormatTag;

/**
 * Repository for interacting with Question entity
 * 
 * @author Matthew Veldhuizen and Cao Vinh Lam
 *
 */

@Repository
public interface QuestionRepo extends JpaRepository<Question, Integer> {


	/**
	 * Find By Id method searches the database for a record that matches the passed in ID value
	 * 
	 * @param questionId - Integer ID value corresponding to the record that will be retrieved from the database
	 * @return Returns an optional question object
	 */
	Optional<Question> findById(Integer questionId);
	
	/**
	 * Find By Approved method searches the database and finds all records with an approved status that matches the parameter value
	 * 
	 * @param approved - boolean value used as a comparator for finding records with matching approval status
	 * @return List of question objects
	 */
	List<Question> findByApproved(boolean approved);
	
	/**
	 * Find By Category Tag method searches the database and finds all records with a category tag that matches the parameter value
	 * 
	 * @param categoryTag - An enum value that corresponds to a preset category tag
	 * @return List of question objects
	 */
	List<Question> findByCategoryTag(CategoryTag categoryTag);
	
	/**
	 * Find By Format Tag method searches the database and finds all records with a format tag that matches the parameter value
	 * 
	 * @param formatTag - An enum value that corresponds to a preset format tag
	 * @return List of question objects
	 */
	List<Question> findByFormatTag(FormatTag formatTag);
	
	/**
	 * Find By Content Type Tag method searches the database and finds all records with a content type tag that matches the parameter value
	 * 
	 * @param contentTypeTag - An enum value that corresponds to a preset content type tag
	 * @return List of question objects
	 */
	List<Question> findByContentTypeTag(ContentTypeTag contentTypeTag);
	
	/**
	 * Find By userID method searches the database and finds all records with a userID that matches the parameter value
	 * 
	 * @param user - An int value that corresponds to a userID
	 * @return List of question objects
	 */
	@Query("FROM Question q WHERE q.user.userId = :user")
	List<Question> getAllQuestionsWithUserId(@Param("user") int user);
	
	/**
	 * Find By userID method searches the database and finds the most recently created record with a userID that matches the parameter value
	 * 
	 * @param user - An int value that corresponds to a userID
	 * @return A question object
	 */
	@Query("FROM Question q WHERE q.user.userId = :user ORDER BY q.questionId DESC LIMIT 1")
	Question getMostRecentQuestionCreatedWithUserId(@Param("user") int user);

	
	/**
	 * Gets all questions that have not yet be reviewed
	 * @return List of questions
	 */
	@Query("FROM Question q WHERE q.reviewed = false")
	List<Question> getUnreviewedQuestions();
	
	
	/**
	 * Gets all questions that match the format tag and are approved
	 * 
	 * @param formatTagSelection - the format tag to look for
	 * @return - returns a list of questions based on the format tag and only if they are approved
	 */
	@Query("FROM Question q WHERE q.formatTag = :formatTagSelection AND q.approved = true")
	List<Question> findByFormatTagApproved(@Param("formatTagSelection") FormatTag formatTagSelection);
	
	
	/**
	 * Gets all questions that match the format tag and belong to the user with userId
	 * @param formatTagSelection - the format selection tag to be found
	 * @param userId - the user id of the user to be looked for
	 * @return - list of questions
	 */
	@Query("FROM Question q WHERE q.formatTag = :formatTagSelection AND q.user.userId = :userId")
	List<Question> findByUserAndFormatTag(@Param("formatTagSelection") FormatTag formatTagSelection, @Param("userId") int userId);
		
	
	/**
	 * Gets all questions that match the content type tag and are approved
	 * @param contentTypeTagSelection - the content type tag to be looked for
	 * @return - list of questions
	 */
	@Query("FROM Question q WHERE q.contentTypeTag = :contentTypeTagSelection AND q.approved = true")
	List<Question> findByContentTypeTagApproved(@Param("contentTypeTagSelection") ContentTypeTag contentTypeTagSelection);
	
	
	/**
	 * Gets all questions that match the content type tag and belong to the user with userId
	 * @param contentTypeTagSelection - the content type tag to be looked for
	 * @param userId - the user id of the user to be looked for 
	 * @return - list of questions
	 */
	@Query("FROM Question q WHERE q.contentTypeTag = :contentTypeTagSelection AND q.user.userId = :userId")
	List<Question> findByUserAndContentTypeTag(@Param("contentTypeTagSelection") ContentTypeTag contentTypeTagSelection, @Param("userId") int userId);
	
	
	/**
	 * Gets all questions that match the category tag and are approved
	 * @param categoryTagSelection - the category tag to be looked for
	 * @return - list of questions
	 */
	@Query("FROM Question q WHERE q.categoryTag = :categoryTagSelection AND q.approved = true")
	List<Question> findByCategoryTagApproved(@Param("categoryTagSelection") CategoryTag categoryTagSelection);
	
	
	/**
	 * Gets all questions that match the category tag and belong to the user with userId
	 * @param categoryTagSelection - category tag to look for
	 * @param userId  - the user id of the user to be looked for 
	 * @return - list of questions
	 */
	@Query("FROM Question q WHERE q.categoryTag = :categoryTagSelection AND q.user.userId = :userId")
	List<Question> findByUserAndCategoryTag(@Param("categoryTagSelection") CategoryTag categoryTagSelection, @Param("userId") int userId);
	
	
	/**
	 * Gets all questions that match the format tag, content type and are approved
	 * @param formatTagSelection - format tag to be looked for 
	 * @param contentTypeTagSelect - content type tag to look for
	 * @return - list of question
	 */
	@Query("FROM Question q WHERE q.formatTag = :formatTagSelection AND q.contentTypeTag = :contentTypeTagSelection  AND q.approved = true")
	List<Question> getAllQuestionsWithMatchingFormatContentTagApproved(@Param("formatTagSelection") FormatTag formatTagSelection, @Param("contentTypeTagSelection") ContentTypeTag contentTypeTagSelect);
	
	
	/**
	 * Gets all questions that match the format tag, content type and belong to the user with userId
	 * @param formatTagSelection - format tag to be looked for 
	 * @param contentTypeTagSelect - content type tag to look for
	 * @param userId  - the user id of the user to be looked for 
	 * @return - list of questions
	 */
	@Query("FROM Question q WHERE q.formatTag = :formatTagSelection AND q.contentTypeTag = :contentTypeTagSelection  AND q.user.userId = :userId")
	List<Question> getAllUserQuestionsWithMatchingFormatContentTag(@Param("formatTagSelection") FormatTag formatTagSelection, @Param("contentTypeTagSelection") ContentTypeTag contentTypeTagSelect, @Param("userId") int userId);
	
	
	/**
	 * Gets all questions that match the category tag, content type tag and are approved
	 * @param contentTypeTagSelect - content type tag to look for
	 * @param categoryTagSelection - category tag to look for
	 * @return - list of questions
	 */
	@Query("FROM Question q WHERE q.contentTypeTag = :contentTypeTagSelection AND q.categoryTag = :categoryTagSelection AND q.approved = true")
	List<Question> getAllQuestionsWithMatchingCategoryContentTagApproved(@Param("contentTypeTagSelection") ContentTypeTag contentTypeTagSelect, @Param("categoryTagSelection") CategoryTag categoryTagSelection);
	
	
	/**
	 * Gets all questions that match the category tag, content type tag and belong to the user with userId
	 * @param contentTypeTagSelect - content type tag to look for
	 * @param categoryTagSelection - category tag to look for
	 * @param userId - the user id of the user to be looked for 
	 * @return - list of questions
	 */
	@Query("FROM Question q WHERE q.contentTypeTag = :contentTypeTagSelection AND q.categoryTag = :categoryTagSelection AND q.user.userId = :userId")
	List<Question> getAllUserQuestionsWithMatchingCategoryContentTag(@Param("contentTypeTagSelection") ContentTypeTag contentTypeTagSelect, @Param("categoryTagSelection") CategoryTag categoryTagSelection, @Param("userId") int userId);
	
	
	/**
	 * Gets all questions that match the category tag, format tag and are approved
	 * @param formatTagSelection - format tag to be looked for 
	 * @param categoryTagSelection - category tag to look for
	 * @return - list of questions
	 */
	@Query("FROM Question q WHERE q.formatTag = :formatTagSelection AND q.categoryTag = :categoryTagSelection AND q.approved = true")
	List<Question> getAllQuestionsWithMatchingCategoryFormatTagApproved(@Param("formatTagSelection") FormatTag formatTagSelection, @Param("categoryTagSelection") CategoryTag categoryTagSelection);
	
	
	/**
	 * Gets all questions that match the category tag, format tag and belong to the user with userId
	 * @param formatTagSelection - format tag to be looked for 
	 * @param categoryTagSelection - category tag to look for
	 * @param userId - the user id of the user to be looked for 
	 * @return - list of questions
	 */
	@Query("FROM Question q WHERE q.formatTag = :formatTagSelection AND q.categoryTag = :categoryTagSelection AND q.user.userId = :userId")
	List<Question> getAllUserQuestionsWithMatchingCategoryFormatTag(@Param("formatTagSelection") FormatTag formatTagSelection, @Param("categoryTagSelection") CategoryTag categoryTagSelection, @Param("userId") int userId);
	
	
	/**
	 * Gets all questions that match the category tag, format tag and content type tag
	 * @param formatTagSelection - format tag to be looked for 
	 * @param contentTypeTagSelect - content type tag to look for
	 * @param categoryTagSelection - category tag to look for
	 * @return - list of questions
	 */
	@Query("FROM Question q WHERE q.formatTag = :formatTagSelection AND q.contentTypeTag = :contentTypeTagSelection AND q.categoryTag = :categoryTagSelection")
	List<Question> getAllQuestionsWithMatchingFormatContentCatgoryTag(@Param("formatTagSelection") FormatTag formatTagSelection, @Param("contentTypeTagSelection") ContentTypeTag contentTypeTagSelect, @Param("categoryTagSelection") CategoryTag categoryTagSelection);
	
	
	/**
	 * Gets all questions that match the category tag, format tag, content type tag and are approved
	 * @param formatTagSelection - format tag to be looked for 
	 * @param contentTypeTagSelect - content type tag to look for
	 * @param categoryTagSelection - category tag to look for
	 * @return - list of questions
	 */
	@Query("FROM Question q WHERE q.formatTag = :formatTagSelection AND q.contentTypeTag = :contentTypeTagSelection AND q.categoryTag = :categoryTagSelection AND q.approved = true")
	List<Question> getAllQuestionsWithMatchingFormatContentCatgoryTagApproved(@Param("formatTagSelection") FormatTag formatTagSelection, @Param("contentTypeTagSelection") ContentTypeTag contentTypeTagSelect, @Param("categoryTagSelection") CategoryTag categoryTagSelection);
	
	
	/**
	 * Gets all questions that match the category tag, format tag, content type tag and and belong to the user with userId
	 * @param formatTagSelection - format tag to be looked for 
	 * @param contentTypeTagSelect - content type tag to look for
	 * @param categoryTagSelection - category tag to look for
	 * @param userId - the user id of the user to be looked for 
	 * @return - list of questions
	 */
	@Query("FROM Question q WHERE q.formatTag = :formatTagSelection AND q.contentTypeTag = :contentTypeTagSelection AND q.categoryTag = :categoryTagSelection AND q.user.userId = :userId")
	List<Question> getAllUserQuestionsWithMatchingFormatContentCatgoryTag(@Param("formatTagSelection") FormatTag formatTagSelection, @Param("contentTypeTagSelection") ContentTypeTag contentTypeTagSelect, @Param("categoryTagSelection") CategoryTag categoryTagSelection, @Param("userId") int userId);

}
