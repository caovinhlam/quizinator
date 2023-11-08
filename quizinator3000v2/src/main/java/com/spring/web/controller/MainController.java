package com.spring.web.controller;

import java.util.ArrayList;

import java.util.List;

import javax.management.relation.Role;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.type.classreading.ConcurrentReferenceCachingMetadataReaderFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.web.model.Answer;
import com.spring.web.model.Quiz;
import com.spring.web.model.User;
import com.spring.web.model.EntityEnum.UserRole;
import com.spring.web.service.AnswerService;
import com.spring.web.service.NotificationService;
import com.spring.web.service.QuestionService;
import com.spring.web.service.QuizService;
import com.spring.web.service.SubmissionService;
import com.spring.web.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * this class is used for the user to have access to the basic pages such as index
 * as well as for displaying and delete from parts of the site such as the sidebar
 * 
 * @author Stanley Chilton
 *
 */
@Controller
public class MainController {	
	
	Logger logger = LoggerFactory.getLogger(MainController.class);	
	
	@Autowired
	UserService userService;
	
	@Autowired
	NotificationService notificationService;
	

	@Autowired
	AnswerService answerService;
	
	@Autowired
	QuizService quizService;
	
	
	@Autowired
	QuestionService questionService;
	
	@Autowired
	SubmissionService submissionService;
	
	/**
	 * this is used for the default mapping and redirects the user to the index past
	 *  
	 * @param model - used to store all the objects needed for the displaying on the index page
	 * @param session - used to store the clients session status
	 * @return index - this redirects the user to the index page when the default mapping is input
	 */
	@GetMapping("/")
	public String toIndex(Model model, HttpSession session) {
		return ("redirect:/index");
	}
	

	
	
	/**
	 * this is the method that displays the original index page
	 * 
	 * @param model - holds the items to display on the index page
	 * @param session - holds the values of the logged in user
	 * @return - this will either return the index page and display it, or will redirect to the login page
	 * 
	 */
	@GetMapping("/index")
	public String index(Model model, HttpSession session) {
		
		String returnValue;
		
		
		if (session.getAttribute("id") != null) {
			logger.info("displaying the index page");
			User user = userService.getUserById((int) session.getAttribute("id"));
			model.addAttribute("user", user);
			session.setAttribute("notes", notificationService.getAllNotificaitonsWithUserId(user.getUserId()));
			
			model.addAttribute("blankuser",new User());
			if(user.getUserRole().name() == "TRAINING" || user.getUserRole().name() == "POND" || user.getUserRole().name() == "BEACHED") {
				logger.info("trainee directed to the index page");
				returnValue = "redirect:/personalProfile";
			} else {
				logger.info("displaying index to an admin user");
				returnValue = "index";
			}
		} 
		else
		{
			logger.error("index page access attempted while not logged in");
			model.addAttribute("message", "You must login to access this page");
			returnValue = "redirect:/login";
		}
		return (returnValue);
	}
	
	
	/**
	 * HI DONALD - check the profile controller for further instructions
	 */
	
	
	/**
	 * Provide autocomplete with list of content type tags
	 * 
	 * @param term - variable that the auto complete function uses to search the DB
	 * @return - returns the students that it has found match the term
	 */
	@GetMapping("/getAllStudents")
	@ResponseBody
	public List<String> getAllStudents(@RequestParam(value="term", required = false, defaultValue="")String term) {
	
		List<User> students = userService.getAllStudents();
		
		List<String> infoList  = new ArrayList<>();
		
		for (User user : students) {

			infoList.add(user.getName() +" : "+ user.getEmail());
		}
		
		logger.trace("students returned");
		return infoList;
	}
	
	/**
	 * returns a list of students to the index page for displaying
	 * 
	 * @param user - the user that is being searched for only used for the name to get a list of students
	 * @param model - holds the items to display on the index page
	 * @param session - holds the values of the logged in user
	 * @return - reutrns the index page with the list of users to display
	 */
	@PostMapping("/searchForStudents")
	public String searchForStudents(@ModelAttribute("forSearchStudents") User user, Model model, HttpSession session) {
		
		List<User> students = userService.getAllStudentswithNameorEmail(user.getName());
		model.addAttribute("students",students);
		User usermain = userService.getUserById((int) session.getAttribute("id"));
		model.addAttribute("user", usermain);
		model.addAttribute("blankuser",new User());
		
		logger.trace("users being returned");
		return ("/index");
	}
	
	/**
	 * this method is used to return someone to the login page if they tpye in /searchforstudents
	 * if this method doesnt exist then it will display an error to the user
	 * 
	 * @return this method returns the user to index page
	 */
	@GetMapping("/searchForStudents")
	public String searchForStudentIndexReturn() {
		logger.error("attempted to access a post with a get request");
		return "redirect:/index";
	}
	
	/**
	 * this method is used to delete notifications when the user clicks on them
	 * 
	 * @param id - the id of the notification to delete from the database
	 * @param model - holds the items to display on the index page
	 * @param session - holds the values of the logged in user
	 * @param request - this param is used to get the previous pages url so it can redirect you back to 
	 * @return returns the page that needs to redirected too, which in this case is the page that called this variable
	 */
	@GetMapping("/deleteNote")
	public String deleteNote(@RequestParam int id, Model model, HttpSession session, HttpServletRequest request) {
		String returnstring;
		String referer; 
		if(session.getAttribute("id") != null) {
			if(notificationService.getNotificationById(id).getUser().getUserId() == (int) session.getAttribute("id")) {
				User user = userService.getUserById((int) session.getAttribute("id"));
				
				if(notificationService.getNotificationById(id).getQuestionID() != null) {
					model.addAttribute("user", user);
					model.addAttribute("questionObject", notificationService.getNotificationById(id).getQuestionID());
					model.addAttribute("userCreated", notificationService.getNotificationById(id).getQuestionID().getUser().getName());
					List<Answer> answers = answerService.getAllAnswerByQuestionId(notificationService.getNotificationById(id).getQuestionID().getQuestionId());
					model.addAttribute("answers", answers);

					logger.info("Display details of questionId: " + notificationService.getNotificationById(id).getQuestionID().getQuestionId());
					referer =  "/questionDetails"; 
				}else if (notificationService.getNotificationById(id).getQuizID() != null) {
					
					Quiz quiz = quizService.getQuizById(notificationService.getNotificationById(id).getQuizID().getQuizID());
					model.addAttribute("quiz", quiz);
					int amount = quiz.getQuestions().size();
					model.addAttribute("numQuestions", amount);
					logger.info("displaying the quiz details");
					referer = "/quizDetails"; 

				}else {
					
					model.addAttribute("user", userService.getUserById((int) session.getAttribute("id")));
					model.addAttribute("QuizzesCreated", quizService.getAllQuizsWithUserId((int) session.getAttribute("id")));
					model.addAttribute("QuestionsCreated", questionService.getAllQuestionsWithUserId((int) session.getAttribute("id")));
					
					model.addAttribute("CoursePassed", submissionService.getSubmissionById((int) session.getAttribute("id"))  );
					model.addAttribute("InterviewPassed", submissionService.getSubmissionById((int) session.getAttribute("id"))  );
			
					logger.info("showing user their profile");
					
					referer = "/profile";
				}
				
				notificationService.deleteById(id);
				logger.info("notification " + id + " deleted by -" + session.getAttribute("id"));
				
				model.addAttribute("user", user);
				session.setAttribute("notes", notificationService.getAllNotificaitonsWithUserId(user.getUserId()));
				model.addAttribute("blankuser",new User());
				logger.info(referer);
				
				returnstring = referer;
			}else {
				logger.error("user attempted to delete another users notification, attempted delete account id: " + session.getAttribute("id"));
				returnstring = "redirect:/index";
			}
		}else {
			logger.error("user attempted to delete a notification while not logged in");
			returnstring = "redirect:/login";
		}
		return returnstring;
	}
	
	
	/**
	 * this method is used for a trainer or sales person to edit a student and load the editing student page
	 * 
	 * @param id - user id of student to update
	 * @param model - holds the items to display on the index page
	 * @param session - holds the values of the logged in user
	 * @return returns the edit student html page
	 */
	@GetMapping("/editStudent")
	public String editStudent(@RequestParam int id, Model model, HttpSession session) {
		String returnstring;
		
		if(session.getAttribute("id") != null) {
			
			UserRole role = userService.getUserById((int) session.getAttribute("id")).getUserRole();
			if(role == UserRole.SALES || role == UserRole.TRAINER) {
				
				model.addAttribute("userStudent", userService.getUserById(id));
				List<UserRole> roles = new ArrayList<UserRole>();
					
				roles.add(UserRole.BEACHED);
				roles.add(UserRole.ABSENT);
				
				roles.add(UserRole.POND);
				roles.add(UserRole.TRAINING);
				
				model.addAttribute("roles",roles );
				
				logger.info("displaying the student edit page");
				
				returnstring = "/editStudent";
			}else {
				logger.error("trainee user attempted to edit account - id that attempted: " + session.getAttribute("id"));
				returnstring = "redirect:/index";
			}
		}else {
			logger.error("non-logged in user tried to edit an account");
			returnstring = "redirect:/index";
		}
		return returnstring;
	}
	
	/**
	 * this method is used to redirect the user to the index page if they try access a page that requires params
	 * without inputting params
	 * 
	 * @param ex - this param is the error message that can be logged when the user doesnt type a param
	 * @return returns the user back the index page
	 */
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public String handleMissingParams(MissingServletRequestParameterException ex) {
		String returnstring;
		logger.error(ex.toString());
		returnstring = "redirect:/index";
		return returnstring;
	}
}
