package com.spring.web.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.web.model.Quiz;
import com.spring.web.model.User;
import com.spring.web.model.EntityEnum.UserRole;
import com.spring.web.service.AnswerService;
import com.spring.web.service.NotificationService;
import com.spring.web.service.QuestionService;
import com.spring.web.service.QuizService;
import com.spring.web.service.UserService;

import jakarta.servlet.http.HttpSession;

/**
 * the reviewing controller that handles the specific reviewing methods
 * 
 * @author Matthew Veldhuizen
 *
 */
@Controller
public class ReviewingController {
	Logger logger = LoggerFactory.getLogger(ReviewingController.class);
	
	@Autowired
	UserService userService;
	
	@Autowired
	AnswerService answerService;
	
	@Autowired
	NotificationService notificationService;
	
	@Autowired
	QuestionService questionService;
	
	@Autowired
	QuizService quizService;
	
	/**
	 * Gets all unreviewed questions
	 * 
	 * @param model - attribute to pass to HTML
	 * @param session - holds the session information of the logged in user
	 * @return page to see all unreviewed questions
	 */
	@GetMapping("/viewUnreviewedQuiz")
	public String viewUnreviewed(Model model, HttpSession session) {
		String returnString;
		if (session.getAttribute("id") != null) {
			User user = userService.getUserById((int) session.getAttribute("id"));
			if( user.getUserRole() == UserRole.TRAINER || user.getUserRole() == UserRole.SALES) {
			
				model.addAttribute("user", user);
				
				List<Quiz> quizes = quizService.getUnreviewedQuizzes();
				model.addAttribute("quizes", quizes);
	
				logger.info("Displaying unreviewed quizess page by " + user.getUserRole() + " userId: " + user.getUserId() + "(" + user.getName() + ")");
				
				returnString = "/quizzesToReivew";
			}else {
				logger.error("index page access attempted while not logged in");
				returnString = "redirect:/index";
			}
		} else {
			logger.error("index page access attempted while not logged in");
			returnString = "redirect:/index";
		}
		return returnString;
	}
	
}
