package com.spring.web.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.web.dal.UserRepo;
import com.spring.web.model.Question;
import com.spring.web.model.User;
import com.spring.web.model.EntityEnum.UserRole;

/**
 * Service to interact with the user repository
 * 
 * @author Cao Vinh Lam
 *
 */
@Service
public class UserService {

	@Autowired
	UserRepo userRepo;
	
	/**
	 * Search the database for a user with the corresponding id
	 * 
	 * @param userId - id to search the user with
	 * @return User object, null if none is found
	 */
	public User getUserById(int userId) {
		Optional<User> opt = userRepo.findById(userId);
		
		if(opt.isPresent())
			return opt.get();
		else
			return null;
	}
	
	/**
	 * Get all users from the database
	 * 
	 * @return List of User objects
	 */
	public List<User> getAllUsers() {
		return userRepo.findAll();
	}
	
	/**
	 * Get all users with the corresponding user role
	 * 
	 * @param userRole - An enum value that corresponds to a preset user role
	 * @return List of User objects matched with the user role
	 */
	public List<User> getAllByUserRole(UserRole userRole) {
		return userRepo.findByUserRole(userRole);
	}
	
	/**
	 * Create a new user entry into the database or Update an existing entry in the database with the inputed User object
	 * 
	 * @param user - User Entity
	 */
	public void saveOrUpdate(User user) {
		userRepo.save(user);
	}
	
	/**
	 * Delete user in the database by their id
	 * 
	 * @param userID - Id to search and delete user with
	 */
	public void deleteById(int userID) {
		userRepo.deleteById(userID);
	}
	
	/**
	 * Delete all users in the database
	 * 
	 * @return Count of how many users were deleted
	 */
	public int deleteAllUsers() {
		int count = 0;
		List<User> users = getAllUsers();
		
		for(User user : users) {
			userRepo.delete(user);
			count++;
		}
		
		return count;
	}

	/**
	 * sends the email of a client to the repo to retrieve that specific client object
	 * 
	 * @param email - the email of the client being looked for
	 * @return - returns a client object retrieved from the Database
	 */
	public User getUserByEmail(String email) {
		Optional<User> opt = userRepo.findByEmail(email);
		if(opt.isPresent())
			return opt.get();
		else
			return null;
	}

	/**
	 * this method is used ot get a list of students
	 * 
	 * @return returns a list of users 
	 */
	public List<User> getAllStudents() {
//		return userRepo.getStudents();
		return null;
	}
	
	/**
	 * this method is used to get all students with a specific name or email
	 * 
	 * @param keyword - takes a keyword from the search bar and looks for those users
	 * @return returns a list of users/students
	 */
	public List<User> getAllStudentswithNameorEmail(String keyword) {
//		return userRepo.getAllStudentswithNameorEmail(keyword);
		return null;
	}

	
}
