package com.spring.web.dal;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spring.web.model.Question;
import com.spring.web.model.Quiz;
import com.spring.web.model.Submission;
import com.spring.web.model.EntityEnum.CategoryTag;
import com.spring.web.model.EntityEnum.ContentTypeTag;
import com.spring.web.model.EntityEnum.FormatTag;

/**
 * this repo deals directly with the database to grab entries
 * 
 * @author Stanley Chilton
 *
 */
@Repository
public interface SubmissionRepo extends JpaRepository<Submission, Integer> {
	
	/**
	 * this method just prompts the database to return a object
	 * 
	 * @param id - this is the id of the object to get from the database
	 */
	public Optional<Submission> findById(Integer id);
	
	/**
	 * Gets all submissions by userID parameter
	 * 
	 * @param user - id of user
	 * @return List of quizes with userId matches parameter
	 */
	@Query("FROM Submission q  WHERE q.userId.userId = :user")
	List<Submission> getAllSubmissionsWithUserId(@Param("user") int user);

}

