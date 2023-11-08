package com.spring.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.web.model.User;
import com.spring.web.model.UserTemp;
import com.spring.web.service.NotificationService;
import com.spring.web.service.UserService;

import jakarta.servlet.http.HttpSession;

/**
 * this controller is used for the login of all users
 * 
 * @author stanley Chilton
 *
 */
@Controller
public class LoginController {
	Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	UserService userService;
	
	@Autowired
	NotificationService notificationService;
	
	
	/**
	 * this method either redirects to the index page if logged in, or it redirects to the login page if not logged in 
	 * 
	 * @param model - used to pass the object to the front end html, in this case a user object for the register field
	 * @param session - used to check if the user is logged in so they are redirected to the index page
	 * @return - returns the login page html code or redirects to the index page if the user is logged in
	 * 
	 */
	@GetMapping("/login")
	public String getLogin(Model model, HttpSession session) {
		String returnValue;
		
		if (session.getAttribute("id") != null) {
			logger.info("redirected to the index page as logged in");
			returnValue = "redirect:/index";
		}else {
			
			logger.error("redirected to the login page as not logged in");
			model.addAttribute("tempuser", new UserTemp());
			returnValue = "login";
		}
		
		return(returnValue);
	}	
	
	
	
	/**
	 * Post to process the email and password for the user and log the in if correct
	 * 
	 * @param loginuser - this is used to store the values of the user trying to login for secure transfer in an https setting
	 * @param model - used to store attributes that are being sent to the html code in this case the email and password
	 * @param session - stores the user session so the the email address of the client logging
	 * 					in can be stored in it
	 * 
	 * @return - returns the loginerror page or redirects to the index page
	 */
	@PostMapping("/checkLogin")
	public String postLogin(@ModelAttribute UserTemp loginuser, Model model,
			HttpSession session) {
		User user;
		String returnValue;
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		
		if((user = userService.getUserByEmail(loginuser.getEmail())) == null)
		{
			model.addAttribute("message", "Login Error: email or password incorrect");
			model.addAttribute("tempuser", new UserTemp());
			logger.error("User log in attempted - incorrect email");
			returnValue = "login";
			
		} else if(!passwordEncoder.matches(loginuser.getPassword(), user.getPassword()))
		{
			model.addAttribute("message", "Login Error: email or password incorrect");
			model.addAttribute("tempuser", new UserTemp());
			logger.error("User log in attempted - incorrect password");
			returnValue = "login";
		}
		else {
			if(user.getUserRole().name() == "ABSENT") {
				model.addAttribute("message", "Login Error: Absent users refused access");
				logger.error("User log in attempted - absent user " + user);
				returnValue = "login";
			}else {
				session.setAttribute("id", user.getUserId());
				session.setAttribute("favourites", user.getFavouriteQuizzes());
				session.setAttribute("notes", notificationService.getAllNotificaitonsWithUserId(user.getUserId()));
				if(passwordEncoder.matches(user.getEmail(), user.getPassword())){
					logger.info("User prompted to change password - userid - " + session.getAttribute("id").toString());
					returnValue = "redirect:/changePass";
				}else {
					  
					logger.info("User logged in" + session.getAttribute("id").toString());
					returnValue = "redirect:/index";
				
				}
			}
		}
		   
		return returnValue ;		// Login again
	}	
	
	
	/**
	 * 
	 * this method allows the user to logout
	 * 
	 * @param session - the variable storing the login status
	 * @return - redirects the admin back to the login page
	 * 
	 */
	@GetMapping("/exit")
	public String ExitSpring(HttpSession session) {
		logger.info("User logged out: " + session.getAttribute("id"));
		
		session.invalidate();
		
		return("redirect:/login");
		
	}
	
	
	/**
	 * used when redirecting the trainer or sales to the change password page on their first login
	 * this has a confirm password system that will reject the change if the passwords do not match
	 * 
	 * @param model - stores the values to pass to the html page
	 * @param session - holds the user id that is currently logged in, means that we can access this user within the method
	 * @return changepass - address to change pass html
	 */
	@GetMapping("/changePass")
	public String createUser(Model model, HttpSession session) {
		String returnstring;
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		
		if(session.getAttribute("id") != null) {
			User user = userService.getUserById((int) session.getAttribute("id"));
			if(passwordEncoder.matches(user.getEmail(), user.getPassword())) {
				
				UserTemp tempUser = new UserTemp();
				tempUser.setEmail(user.getEmail());
				tempUser.setName(user.getName());
				tempUser.setUserId(user.getUserId());
				
				model.addAttribute("usertemp", tempUser);
		
				logger.info("page for password changing displayed");
				
				returnstring = "changepass";
			}else {
				returnstring = "redirect:/index";
			}
		}else {
			
			logger.error("user not logged in tried to access the change pass page");
			
			returnstring = "redirect:/index";
			
		}
		return returnstring;
	}
	
	
	/**
	 * Updates sales or trainer account and returns to index page
	 * 
	 * @param tempuser - user to used to update pass, aswell as store the confirm password for checking in the backend
	 * @param model - stores the information coming back from the html page
	 * @param session - stores the Id of the user logging in, meaning that we can get their id when we need it
	 * @return index - address to index/home page
	 */
	@PostMapping("/updatepassword")
	public String updatePassword( @ModelAttribute UserTemp tempuser, Model model, HttpSession session) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		tempuser.setPassword(passwordEncoder.encode(tempuser.getPassword()));
		
		User user = userService.getUserById((int) session.getAttribute("id"));
		if(passwordEncoder.matches(tempuser.getConfirmPassword(), tempuser.getPassword())){
			user.setPassword(tempuser.getPassword());
	
			userService.saveOrUpdate(user);
			
			logger.info("editing own profile");
			return ("redirect:/index");
		}else {
			logger.error("passwords didnt match redirecting");
			model.addAttribute("message", "Passwords dont match");
			model.addAttribute("usertemp", tempuser);
			
			return("changepass");
		}
	}
	
	/**
	 * this method is used to return the used home if they try to input the post mapping without 
	 * posting anything
	 * 
	 * @return returns the used to the html method
	 */
	@GetMapping("/updatepassword")
	public String updatePasswordIndex() {
		logger.error("user tried to update a password without permissions");
		return ("redirect:/index");
	}
	
//	/**
//	 * this method is used to redirect the user to the index page if they try access a page that requires params
//	 * without inputting params
//	 * 
//	 * @param ex - this param is the error message that can be logged when the user doesnt type a param
//	 * @return returns the user back the index page
//	 */
//	@ExceptionHandler(MissingServletRequestParameterException.class)
//	public String handleMissingParams(MissingServletRequestParameterException ex) {
//		String returnstring;
//		logger.error(ex.toString());
//		returnstring = "redirect:/index";
//		return returnstring;
//	}
}
