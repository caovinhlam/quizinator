package com.spring.web.dal;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spring.web.model.User;
import com.spring.web.model.EntityEnum.UserRole;

/**
 * Repository for interacting with the User entity
 * 
 * @author Cao Vinh Lam
 *
 */

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {

	/**
	 * Search the database for all users matched with the user role
	 * 
	 * @param userRole - An enum value that corresponds to a preset user role
	 * @return List of users that matched with the user role
	 */
	List<User> findByUserRole(UserRole userRole);

	/**
	 * gets a user from the DB where their email matches the incoming param
	 * 
	 * @param email - the email of a student that needs to be looked for
	 * @return - returns a student if one is found 
	 */
	Optional<User> findByEmail(String email);

	
	/**
	 * gets a list of all the students
	 * 
	 * @return - returns a list of the user objects
	 */
//	@Query ("FROM User u WHERE u.userRole LIKE 'POND' OR u.userRole LIKE 'BEACHED' OR u.userRole LIKE'TRAINING' OR u.userRole LIKE 'ABSENT' ")
//	List<User> getStudents();

	/**
	 * gets all the students based on their name or emails
	 * 
	 * @param keyword - the name/sub string of a name or email that is being looked for
	 * @return - returns a list of the user objects
	 */
//	@Query ("FROM User u WHERE (u.userRole LIKE 'POND' OR u.userRole LIKE 'BEACHED' OR u.userRole LIKE'TRAINING' OR u.userRole LIKE 'ABSENT') AND (u.name LIKE %:keyword% OR u.email LIKE %:keyword%) ")
//	List<User> getAllStudentswithNameorEmail(@Param("keyword") String keyword);

	
	
}
