package com.spring.web.dal;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spring.web.model.Question;
import com.spring.web.model.QuestionAttempt;
import com.spring.web.model.Quiz;

/**
 * this is the repo for the question attempt used for custom communication between the db and program
 * 
 * @author Matthew Veldhuizen
 *
 */
@Repository
public interface QuestionAttemptRepo extends JpaRepository<QuestionAttempt, Integer> {
	
	/**
	 * this method gets the question attempts backed on the question id and the submission id
	 * 
	 * @param questionId - the question id to look fo 
	 * @param submissionID - the submission id to look for 
	 * @return - a list of all the question attempts that match these values
	 */
	@Query("FROM QuestionAttempt qa WHERE qa.question.questionId = :questionId AND qa.submission.submissionID = :submissionID")
	List<QuestionAttempt> getQuestionAttemptsByQuestionAndSubmission(@Param("questionId") Integer questionId, @Param("submissionID") Integer submissionID);
	
}
