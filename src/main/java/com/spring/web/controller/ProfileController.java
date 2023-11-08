package com.spring.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.web.model.Answer;
import com.spring.web.model.Notification;
import com.spring.web.model.Question;
import com.spring.web.model.QuestionAttempt;
import com.spring.web.model.Submission;
import com.spring.web.model.User;
import com.spring.web.model.EntityEnum.UserRole;
import com.spring.web.service.NotificationService;
import com.spring.web.service.QuestionService;
import com.spring.web.service.QuizService;
import com.spring.web.service.SubmissionService;
import com.spring.web.service.UserService;

import jakarta.servlet.http.HttpSession;

/**
 * class is used to control the profile page and functions that come with it 
 * 
 * @author Samantha Jermyn, Stanley Chilton
 */
@Controller
public class ProfileController {
	Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	UserService userService;
	
	@Autowired
	NotificationService notificationService;
	
	@Autowired
	QuizService quizService;
	
	@Autowired
	QuestionService questionService;
	
	@Autowired
	SubmissionService submissionService;
	
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~  Profile  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Goes to the  profile screen and creates all the information to store
	 * 
	 * @param model - attribute to pass to HTML
	 * @param session - value of current logged in user
	 * @return profile - address to detailed profile
	 */
	@GetMapping("/personalProfile") 
	public String personalProfile(Model model, HttpSession session) {
		String returnString;
		
		if(session.getAttribute("id") != null) {
			model.addAttribute("user", userService.getUserById((int) session.getAttribute("id")));
			model.addAttribute("QuizzesCreated", quizService.getAllQuizsWithUserId((int) session.getAttribute("id")));
			model.addAttribute("QuestionsCreated", questionService.getAllQuestionsWithUserId((int) session.getAttribute("id")));
			model.addAttribute("CoursePassed", submissionService.getAllCourseContentSubmissions((int) session.getAttribute("id")));
			model.addAttribute("InterviewPassed", submissionService.getAllInterviewSubmissions((int) session.getAttribute("id")));
			session.setAttribute("notes", notificationService.getAllNotificaitonsWithUserId((int) session.getAttribute("id")));

			model.addAttribute("forGraphX", maxValueForX(userService.getUserById((int) session.getAttribute("id"))) );
			model.addAttribute("forGraphY", maxValueForY(userService.getUserById((int) session.getAttribute("id"))) );

			logger.info("showing user their profile");
			
			returnString = "/profile";
		}else {
			logger.error("user not logged in sending them home");
			returnString = "redirect:/index";
		}
		return returnString;
	}
	
	/**
	 * 
	 * @param user
	 * @return
	 */
	private List<String> maxValueForX(User user) {
		List<Submission> latest = submissionService.getAllSubmissionsFromUser(user.getUserId());
		List<Submission> maxValue = new ArrayList<Submission>();
		for (int i = latest.size()-1; i >=0; i--) {
			if (maxValue.size() <10) {
				maxValue.add(latest.get(i));
			}
		}
		
		List<String> gx = new ArrayList<>();
		List<Integer> gy = new ArrayList<>();
		for (Submission submission : maxValue) {
			gx.add(submission.getQuiz().getQuizName());
			gy.add(submission.getMark());
		}
		
		logger.info(""+gy);
		logger.info("" +gx);
		return gx;
	}
	
	/**
	 * 
	 * @param user
	 * @return
	 */
	private List<Integer> maxValueForY(User user) {
		List<Submission> latest = submissionService.getAllSubmissionsFromUser(user.getUserId());
		List<Submission> maxValue = new ArrayList<Submission>();
		for (int i = latest.size()-1; i >=0; i--) {
			if (maxValue.size() <10) {
				maxValue.add(latest.get(i));
			}
		}
		
		List<String> gx = new ArrayList<>();
		List<Integer> gy = new ArrayList<>();
		for (Submission submission : maxValue) {
			gx.add(submission.getQuiz().getQuizName());
			gy.add(submission.getMark());
		}
		
		logger.info(""+gy);
		logger.info("" +gx);
		return gy;
	}


	/**
	 * Goes to the  profile screen and creates all the information to store
	 * 
	 * @param id - the id of the user the trainer or sales manager is trying to view
	 * @param model - attribute to pass to HTML
	 * @param session - value of current logged in user
	 * @return profile - address to detailed profile
	 */
	@GetMapping("/Profile") 
	public String Profile(@RequestParam int id, Model model, HttpSession session) {
		String returnString;
		
		if(session.getAttribute("id") != null) {
			if(userService.getUserById((int) session.getAttribute("id")).getUserRole().name() == "TRAINER" 
				||  userService.getUserById((int) session.getAttribute("id")).getUserRole().name() == "SALES" ) {
			model.addAttribute("user", userService.getUserById(id));
			model.addAttribute("QuizzesCreated", quizService.getAllQuizsWithUserId(id));
			model.addAttribute("QuestionsCreated", questionService.getAllQuestionsWithUserId(id));
			
			model.addAttribute("CoursePassed", submissionService.getAllCourseContentSubmissions(id));
			model.addAttribute("InterviewPassed", submissionService.getAllInterviewSubmissions(id));
	
			logger.info("showing user their profile");
			
			model.addAttribute("forGraphX", maxValueForX(userService.getUserById(id)) );
			model.addAttribute("forGraphY", maxValueForY(userService.getUserById(id)) );
			
			logger.info("displaying the profile to the user");
			returnString = "/profile";
			} else {
				logger.error("user not sales or trainer sending them home");
				returnString = "redirect:/index";
			}
		}else {
			logger.error("user not logged in sending them home");
			returnString = "redirect:/index";
		}
		return returnString;
	}
	
	
	
	/**
	 * Goes to edit profile page
	 * 
	 * @param model - attribute to pass to HTML
	 * @param session - value of current logged in user 
	 * @return editProfile - address to editing profile page
	 */
	@GetMapping("/editOwnProfile")
	public String editOwnProfile(  Model model, HttpSession session) {
		String returnString;
		
		if(session.getAttribute("id") != null) {
			model.addAttribute("user", userService.getUserById((int) session.getAttribute("id")));
	
			logger.info("edit own profile");
			returnString = "/editProfile";
		}else {
			logger.error("user not logged in sending home");
			returnString = "redirect:/index";
		}
		return returnString;
	}
	
	/**
	 * Updates user and returns to index page
	 * 
	 * @param user - user to update
	 * @param model - attribute to pass to HTML
	 * @param session - value of current logged in user
	 * @return index - address to index/home page
	 */
	@PostMapping("/updateUser")
	public String updateUser( @ModelAttribute("user") User user, Model model, HttpSession session) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		userService.saveOrUpdate(user);
		model.addAttribute("user", userService.getUserById((int) session.getAttribute("id")));

		model.addAttribute("forGraphX", maxValueForX(userService.getUserById((int) session.getAttribute("id"))) );
		model.addAttribute("forGraphY", maxValueForY(userService.getUserById((int) session.getAttribute("id"))) );
		
		logger.info("edit own profile");
		return ("/profile");
	}
	
	/**
	 * sends the user home if they try access the update user page with a get request rather
	 * than displaying an error
	 * 
	 * @return returns the user back to the index page
	 */
	@GetMapping("/updateUser")
	public String updateUserIndex() {
		logger.error("attempted to access a post with a get request");
		return("redirect:/index");
	}
	
	/**
	 * hey donald - please carry on to the answer service for further instructions
	 */
	
	/**
	 * used to create a new trainer or sales user
	 * 
	 * @param model - stores the values to pass to the html page
	 * @param session - holds the user id that is currently logged in, means that they can be redirected if not logged in
	 * @return editProfile - address to editing profile page
	 */
	@GetMapping("/createUser")
	public String createUser(  Model model, HttpSession session) {
		String returnString;
		
		if(session.getAttribute("id") != null) {
			UserRole role = userService.getUserById((int) session.getAttribute("id")).getUserRole();
			if(role == UserRole.SALES || role == UserRole.TRAINER) {
				model.addAttribute("loggedinuser", userService.getUserById((int) session.getAttribute("id")));
				
				model.addAttribute("user", new User());
		
				logger.info("created a trainer user object");
				
				returnString = "/newUser";
			}else {
				logger.error("user attempted to access the create user page without correct permissions");
				returnString = "redirect:/index";
			}
		} else {
			logger.error("user attempted to access create user without being logged in");
			returnString = "redirect:/index";
		}
		return returnString;
	}
	
	/**
	 * this method is used to store the registering client in the database if the data is correct, or redirect them
	 * to the appropriate place the login error for example
	 * 
	 * @param user - takes the userr registration details, encrypts the password and then stores it
	 * @param model - used to store the instance variables such as messages to be passed back to the html code
	 * @param session - holds the user id that is currently logged in, means that they can be redirected if not logged in
	 * @return - either gives a login error or redirects the user to the logged in index page
	 */
	@PostMapping("/registerTrainerObject")
	public String addTrainerObject
	(
		@ModelAttribute("user") User user, Model model, HttpSession session
	)
	{
		String returnValue;
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		User checkUser = userService.getUserById((int) session.getAttribute("id"));
		
		if(!passwordEncoder.matches(user.getPassword(), checkUser.getPassword())) {
			
			logger.error("account creation attempted incorect Auth password");
			model.addAttribute("loggedinuser", userService.getUserById((int) session.getAttribute("id")));
			user.setPassword("");
			model.addAttribute("message", "Incorrect Auth password");
			model.addAttribute("user", user);
			
			return ("/newUser");
		}else {
			String password = user.getEmail();
			user.setPassword(passwordEncoder.encode(password));
			
			// Check email unique
			if(userService.getUserByEmail(user.getEmail()) != null) {
					logger.error("account creation attempted email already exists for this address: " + user.getEmail());
					model.addAttribute("message", "Email already exists");
					model.addAttribute("user", user);
					user.setPassword("");
					return ("/newUser");
			}
			else {
				logger.info("trainer added to the database: " + user);
				user.setUserRole(UserRole.TRAINER);
				logger.info("role trainer set");
				userService.saveOrUpdate(user);
				
				returnValue = "redirect:/index";
			}
		}
		return(returnValue);
	}
	
	/**
	 * takes the user back to the index page as this is a post mapping and will throw an error
	 * if the user hasnt submitted a form
	 * 
	 * @return returns the user back to the index page
	 */
	@GetMapping("/registerTrainerObject")
	public String addTrainerObjectIndex() {
		logger.error("attempted to access a post with a get request");
		return("redirect:/index");
	}
	
	/**
	 * Updates student  and returns to index page
	 * 
	 * @param user - student to update
	 * @param model - attribute to pass to HTML
	 * @param session - value of current logged in user
	 * @return index - address to index/home page
	 */
	@PostMapping("/updateStudent")
	public String updateStudent( @ModelAttribute("user") User user, Model model, HttpSession session) {
		User originalUser = userService.getUserById(user.getUserId());
		user.setPassword(userService.getUserById(user.getUserId()).getPassword());
		
		createNotification(originalUser, user, userService.getUserById((int) session.getAttribute("id")));
		userService.saveOrUpdate(user);
		
		model.addAttribute("user", userService.getUserById((int) session.getAttribute("id")));

		model.addAttribute("blankuser",new User());
		
		logger.info("edit student");
		
		return ("/index");
	}
	
	/**
	 * takes the user back to the index page as this is a post mapping and will throw an error
	 * if the user hasnt submitted a form
	 * 
	 * @return returns the user back to the index page
	 */
	@GetMapping("/updateStudent")
	public String updateStudentIndex() {
		logger.error("attempted to access a post with a get request");
		return("redirect:/index");
	}
	
	/**
	 * Creates a notification object when an admin account changes a student account
	 * 
	 * @param originalUser - user to be changed
	 * @param user - user with the updated values
	 * @param admin - admin account saving changes
	 */
	private void createNotification(User originalUser, User user, User admin) {
		
		Notification notification = new Notification();
		notification.setUserID(user);
		String messageString = "";
		
		String name = "";
		String role = "";
		
		if(!originalUser.getName().equals(user.getName())) {
			logger.info("name notification created");
			name = "Name";
			notification.setOldInfo(originalUser.getName());
			notification.setNewInfo(user.getName());
		}
		if(!originalUser.getUserRole().equals(user.getUserRole())) {
			if(name.equals("Name")) {
				role = " And ";
			}
			
			logger.info("role notification created");
			role = role+"Role";
			notification.setOldInfo(notification.getOldInfo() + originalUser.getUserRole());
			notification.setNewInfo(notification.getNewInfo() + user.getUserRole());
			
		}
		messageString = name + role + " changed by " + admin.getName();

		notification.setEditor(admin.getName());
		notification.setMessage(messageString);
		notificationService.saveNotification(notification);
	}



	/**
	 * this method is used to store the registering client in the database if the data is correct, or redirect them
	 * to the appropriate place the login error for example
	 * 
	 * @param user - takes the userr registration details, encrypts the password and then stores it
	 * @param model - used to store the instance variables such as messages to be passed back to the html code
	 * @param session - handles the values of the currently logged in user
	 * @return - either gives a login error or redirects the user to the logged in index page
	 * 
	 */
	@PostMapping("/registerSalesObject")
	public String addSalesObject
	(
		@ModelAttribute User user, Model model, HttpSession session
	)
	{
		String returnValue;
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		User checkUser = userService.getUserById((int) session.getAttribute("id"));
		
		if(!passwordEncoder.matches(user.getPassword(), checkUser.getPassword())) {
			
			logger.error("account creation attempted incorect Auth password");
			model.addAttribute("loggedinuser", userService.getUserById((int) session.getAttribute("id")));
			user.setPassword("");
			model.addAttribute("message", "incorrect Auth password");
			model.addAttribute("user", user);
			
			return ("/newUser");
		}else {
			String password = user.getEmail();
			user.setPassword(passwordEncoder.encode(password));
			
			// Check username unique
			if(userService.getUserByEmail(user.getEmail()) != null) {
				
				logger.error("account creation attempted email already exists for this address: " + user.getEmail());
				model.addAttribute("message", "Email already exists");
				model.addAttribute("user", user);
				user.setPassword("");
				return ("/newUser");
			}
			else {
				logger.info("sales added to the database: " + user);
				user.setUserRole(UserRole.SALES);
				logger.info("sales set");
				userService.saveOrUpdate(user);
	
				returnValue = "redirect:/index";
			}
		}
		
		return(returnValue);
	}	
	
	/**
	 * takes the user back to the index page as this is a post mapping and will throw an error
	 * if the user hasnt submitted a form
	 * 
	 * @return returns the user back to the index page
	 */
	@GetMapping("/registerSalesObject")
	public String addSalesObjectindex() {
		logger.error("attempted to access a post with a get request");
		return("redirect:/index");
	}
	
	/**
	 * this method I used to format the data back to the html so we can display the results of the 
	 * quizzes they have just completed
	 * 
	 * @param id - the id of the quiz the historical data is required for
	 * @param model - the model being passed to the frontend
	 * @param session - the session that stores the users data
	 * @return - returns a string that is the mapping for another method
	 */
	@GetMapping("/viewresults")
	public String viewresult(@RequestParam int id, Model model, HttpSession session) {
		String returnString;
		if(session.getAttribute("id") != null) {
			if(submissionService.getSubmissionById(id).getUserId().getUserId() == (int) session.getAttribute("id")
					|| userService.getUserById((int) session.getAttribute("id")).getUserRole().name() == "TRAINER"
					|| userService.getUserById((int) session.getAttribute("id")).getUserRole().name() == "SALES") {
				
				Submission submission = submissionService.getSubmissionById(id);
				
				model.addAttribute("results", submission);
					
				model.addAttribute("questions", submission.getQuiz().getQuestions());
				
				List<QuestionAttempt> questionAttempts = new ArrayList<>();
				
				for (Question question : submission.getQuiz().getQuestions()) {
					for (Answer answer : question.getAnswers()) {
						//because you can access the questionattempt from answer with bi-directional link
						for(QuestionAttempt questionAttempt : submission.getQuestionAttempt()) {
							if(answer.equals(questionAttempt.getAnswer()) && questionAttempt.getSubmission().equals(submission)) {
								questionAttempts.add(questionAttempt);
							}
						}
					}
				}
				model.addAttribute("attempts", questionAttempts);
				
				logger.info("displaying the results from a quiz attempt");
				returnString = "viewresults";
				
			} else {
				logger.error("user doesnt have permissions and is being redirected back to the index page");
				returnString = "redirect:/index";
			}
		}else {
			logger.error("user not logged in redirecting to login");
			returnString = "redirect:/index";
		}
		return returnString;
	}
}
	
