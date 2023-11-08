package com.spring.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsPasswordService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.spring.web.model.User;
import com.spring.web.model.UserTemp;
import com.spring.web.model.EntityEnum.UserRole;
import com.spring.web.service.UserService;

import jakarta.servlet.http.HttpSession;

/**
 * this controller is used for the registering of a user
 * 
 * @author Stanley Chilton
 *
 */
@Controller
public class RegisterController {
	Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	UserService userService;
	
	/**
	 * this method is used to store the registering client in the database if the data is correct, or redirect them
	 * to the appropriate place the login error for example
	 * 
	 * @param tempuser - takes the userr registration details, encrypts the password and then stores it
	 * @param model - used to store the instance variables such as messages to be passed back to the html code
	 * @param session - this holds the information about the user in this case its used to create a login session
	 * for a user when they register
	 * @return - either gives a login error or redirects the user to the logged in index page
	 * 
	 * 
	 */
	@PostMapping("/registerUserObject")
	public String addUserObject
	(
		@ModelAttribute("tempuser") UserTemp tempuser, Model model, HttpSession session
	)
	{
		String returnValue;
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String password = tempuser.getPassword();
		User user = new User();
		tempuser.setPassword(passwordEncoder.encode(password));
		
		// Check username unique
		if(userService.getUserByEmail(tempuser.getEmail()) != null) {
			
			model.addAttribute("message1", "email already exists");
			model.addAttribute("tempuser", tempuser);
			returnValue = "login";
		}
		else {
			if(passwordEncoder.matches(tempuser.getConfirmPassword(), tempuser.getPassword())){
				logger.info("client added to the database: " + tempuser);
				user.setEmail(tempuser.getEmail());
				user.setName(tempuser.getName());
				user.setPassword(tempuser.getPassword());
				user.setUserRole(UserRole.TRAINING);
				logger.info("trainee set");
				userService.saveOrUpdate(user);
				session.setAttribute("id", user.getUserId());
	
				returnValue = "redirect:/index";
			} else {
				model.addAttribute("message1", "Passwords dont match");
				
				model.addAttribute("tempuser", tempuser);
				
				logger.error("user not logged in redirecting to login");
				returnValue = "login";
			}
		}
		
		return(returnValue);
	}	
	
	/**
	 * this method is used to return the used home if they try to input the post mapping without 
	 * posting anything
	 * 
	 * @return returns the used to the html method
	 */
	@GetMapping("/registerUserObject")
	public String addUserObject() {
		logger.error("attempted to access a post with a get request");
		return "redirect:/index";
	}
}
