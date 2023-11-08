package com.spring.web.dal;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spring.web.model.Question;
import com.spring.web.model.Quiz;

/**
 * Repository for Quiz class
 * 
 * @author samantha jermyn
 */

@Repository
public interface QuizRepo extends JpaRepository<Quiz, Integer>{

	/**
	 * Gets all quizes created by userID parameter
	 * 
	 * @param user - id of user
	 * @return List of quizes with userId matches parameter
	 */
	@Query("FROM Quiz q  WHERE q.user.userId = :user")
	List<Quiz> getAllQuizsWithUserId(@Param("user") int user);

	/**
	 * gets a list of all the quizzes that are unreviewed
	 * 
	 * @return - returns a list of quizzes
	 */
	@Query("FROM Quiz q WHERE q.reviewed = false")
	List<Quiz> getUnreviewedQuizzes();
	
	//the end isnt here i was just kidding, goto the main method that runs this program theres a surprise on line 1111
	// also take that as a hint to unlock your surprise
	
	/**
	 * gets quizzes that have been reviewed
	 * 
	 * @return - returns a list of quizzes
	 */
	@Query("FROM Quiz q WHERE q.reviewed = true")
	List<Quiz> getreviewedQuizzes();
	
	/**
	 * gets a list of all the approved quizzes
	 * 
	 * @return - returns a list of quizzes
	 */
	@Query("FROM Quiz q WHERE q.approved = true")
	List<Quiz> getAllApprovedQuizzes();
}
