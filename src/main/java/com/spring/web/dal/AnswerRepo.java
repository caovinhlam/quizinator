package com.spring.web.dal;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spring.web.model.Answer;

/**
 * Repository for interacting with Answer Entity
 * 
 * @author Matthew Veldhuizen and Cao Vinh Lam
 *
 */

@Repository
public interface AnswerRepo extends JpaRepository<Answer, Integer> {

	/**
	 * Method to find an optional answer object if it exists
	 * 
	 * @param answerId - Integer id value to use for searching for matching record in database
	 * @return Answer object if a matching object exists
	 */
	Optional<Answer> findById(Integer answerId);

	
	/**
	 * Find By questionId method searches the database and finds all records with a questionId that matches the parameter value
	 * 
	 * @param question - An int value that corresponds to a questionId
	 * @return List of answer objects
	 */
	@Query("FROM Answer a WHERE a.question.questionId = :question")
	List<Answer> getAllAnswersByQuestionId(@Param("question") int question);
}
