package com.spring.web.controller;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.web.model.Notification;
import com.spring.web.model.Question;
import com.spring.web.model.TempQuiz;
import com.spring.web.model.Quiz;
import com.spring.web.model.User;
import com.spring.web.model.EntityEnum.CategoryTag;
import com.spring.web.model.EntityEnum.ContentTypeTag;
import com.spring.web.model.EntityEnum.FormatTag;
import com.spring.web.model.EntityEnum.UserRole;
import com.spring.web.service.QuestionService;
import com.spring.web.service.QuizService;
import com.spring.web.service.UserService;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import com.spring.web.service.NotificationService;

/**
 * the controller for the quiz for all the specific methods that relate to quizzes
 * 
 * @author Samantha Jermyn, caovinh lam, christian lee 
 *
 */
@Controller
public class QuizController {
	
Logger logger = LoggerFactory.getLogger(QuestionController.class);
	
	@Autowired
	QuestionService questionService;
	
	@Autowired
	QuizService quizService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	NotificationService notificationService;
	
	/**
	 * Will take user to view quizzes or index 
	 * 
	 * @param model - used to pass the object to the front end html, in this case a user object for the register field
	 * @param session - used to check if the user is logged in so they are redirected to the index page
	 * @return quizlist page for user to view quizzes OR index if error
	 */
	@GetMapping("/quizList")
	public String getQuizList(Model model, HttpSession session) {
		String returnString;
		
		if (session.getAttribute("id") != null) {
//			List<Quiz> quiz = quizService.getreviewedQuizzes();
			User user = userService.getUserById((int) session.getAttribute("id")); 
			List<Quiz> quiz = quizService.getAllApprovedQuizzes();
			List<Quiz> myQuizzes = quizService.getAllQuizsWithUserId(user.getUserId());
			model.addAttribute("quizzes", quiz);
			model.addAttribute("myQuizzes", myQuizzes);
			
			logger.info("" + quiz);
			returnString = "quizList";
			session.setAttribute("notes", notificationService.getAllNotificaitonsWithUserId(user.getUserId()));

			model.addAttribute("favourites", user.getFavouriteQuizzes());
			
		}else {
			logger.error("user attempted to access create user without being logged in");
			returnString = "redirect:/index";
		}
		return returnString;
	}
	
	/**
	 * 
	 * @param model - used to pass the object to the front end html, in this case a user object for the register field
	 * @param session - used to check if the user is logged in so they are redirected to the index page
	 * @return returns page to manually create quiz OR index
	 */
	@GetMapping("/createQuizManual")
	public String getCreateQuizManual(HttpSession session, Model model) {
		String returnString;
		if (session.getAttribute("id") != null) {
			User user = userService.getUserById((int) session.getAttribute("id"));
			model.addAttribute("user",user);
			
			TempQuiz quiz = new TempQuiz();
			model.addAttribute("quizObject",quiz);
			
			List<Question> questions = questionService.getAllByApproved(true);
			List<Question> lockedQuestions = user.getLockedQuestions();

			model.addAttribute("questions", questions);
			model.addAttribute("lockedQuestions",lockedQuestions);
			
			logger.info("displaying the manual quiz creation form");
			
			return "createManualQuizForm";
		} else {
			logger.error("user attempted to access create user without being logged in");
			returnString = "redirect:/index";
		}
		return returnString;
	}
	
	/**
	 * Updates a quiz that corresponds to the tempQuiz objects id and saves the updates to database.
	 * 
	 * @param quizObject - TempQuiz object that stores changes to quiz object
	 * @param model - used to pass the object to the front end html, in this case a user object for the register field
	 * @param session - used to check if the user is logged in so they are redirected to the index page
	 * @return page back to quiz list display OR index if error
	 */
	@PostMapping("/createQuizManualObject")
	public String postCreateQuizObject(@ModelAttribute("quizObject") TempQuiz quizObject, Model model ,HttpSession session) {
		String returnString;
		if (session.getAttribute("id") != null) {
			
			Quiz quiz = new Quiz();
			User user = userService.getUserById((int) session.getAttribute("id"));
			
			quiz.setQuizName(quizObject.getQuizName());
			quiz.setUser(user);
			
			quiz.setQuizType(quizObject.getQuestions().get(0).getCategoryTag());
			quiz.setQuestions(quizObject.getQuestions());
			
			if(!quizObject.isNoTimeLimit())
				quiz.setAllowedTime(quizObject.getAllowedTime());
			else {
				quiz.setAllowedTime(Integer.MAX_VALUE);
			}
			// Quizzes created by Trainers and Sales are already pre-approved and reviewed
			if (user.getUserRole() == UserRole.TRAINER || user.getUserRole() == UserRole.SALES) {
				quiz.setApproved(true);
				quiz.setReviewed(true);
			}
			quizService.saveQuiz(quiz);
			
			logger.info("quiz created");
			
			return "redirect:/quizList";
			
		} else {
			logger.error("user attempted to access create user without being logged in");
			returnString = "redirect:/index";
		}
		return returnString;
		
	}
	
	/**
	 * this method is used to prevent an error if the user tries to access the post method and direct them home
	 * 
	 * @return - this method returns the user back the index page
	 */
	@GetMapping("/createQuizManualObject")
	public String postCreateQuizObjectindex() {
		return "redirect:/index";
	}
	 	
	/**
	 * Create empty temporary quiz object
	 * @param session - used to check if the user is logged in and allows user object to be added to model
	 * @param model - stores attributes that can be accessed on the front-end, in this case objects and a flag message
	 * @return String Object - takes user to createQuizForm
	 */
	@GetMapping("/createQuiz")
	public String getCreateQuiz(HttpSession session, Model model) {
		String returnString;
		if (session.getAttribute("id") != null) {
			User user = userService.getUserById((int) session.getAttribute("id"));
		
			TempQuiz quiz = new TempQuiz();
			
			model.addAttribute("user", user);
			model.addAttribute("message", "yes");
			model.addAttribute("tempQuiz", quiz);
		
			logger.info("displaying the quiz creation form");
			
			returnString = "/createQuizForm";

		} else {
			logger.error("user not logged in redirecting to login");
			returnString = "redirect:/index";
		}
		return returnString;
	}
	
	/**
	 * Post auto generate quizzes based on inputs passed by user
	 * @param tempQuiz - stores the list of content type tags and format tags passed in by the user
	 * @param request - passes inputs from the front-end to the back-end
	 * @param model - stores model attributes that can be accessed in the front-end, in this case a list of 
	 * question objects, user object and flag messages
	 * @param session - used to check if the user is logged in and allows user object to be added to model
	 * @return String Object - takes user to quizList if successful creation
	 */
	@PostMapping("/createQuizAuto")
	public String getCreateQuizAuto(@ModelAttribute("tempQuiz") TempQuiz tempQuiz, HttpServletRequest request, Model model, HttpSession session) {
		
		String returnString;
		if (session.getAttribute("id") != null) {
			Quiz quizObject = new Quiz();
			
			User user = userService.getUserById((int) session.getAttribute("id"));
			
			//Initialise lists
			List <Question> matchingQuestions = new ArrayList<>();
			List <Question> selectedQuestions = new ArrayList<>();
			List<ContentTypeTag> contentTypeTags = new ArrayList<>();
			List<FormatTag> formatTypeTags = new ArrayList<>();
			List<Question> lockedQuestions = user.getLockedQuestions();
			List <Question> lockedQuestionsMatching = new ArrayList<>();
			
			//set created quiz object to user inputs from front-end
			quizObject.setUser(user);
			quizObject.setQuizName(request.getParameter("quizName"));
			//quizObject.setAllowedTime(Integer.parseInt(request.getParameter("timeLimit")));
			if(!tempQuiz.isNoTimeLimit())
				quizObject.setAllowedTime(tempQuiz.getAllowedTime());
			else {
				quizObject.setAllowedTime(Integer.MAX_VALUE);
			}
			
			//check if category tag is null and return page with appropriate error message, else set quiz type
			if (request.getParameter("categoryTag") == null) {
				logger.warn("No category type specified by user");
				
				TempQuiz quiz = new TempQuiz();
				
				model.addAttribute("user", user);
				model.addAttribute("message", "nope3");
				model.addAttribute("tempQuiz", quiz);
				return "/createQuizForm";
			} else {
				quizObject.setQuizType(CategoryTag.valueOf(request.getParameter("categoryTag")));
			}
			
			//check if logged in user is a trainer or sales level user then approve quiz, else leave quiz as not reviewed and not approved
			if (user.getUserRole() == UserRole.TRAINER || user.getUserRole() == UserRole.SALES) {
				quizObject.setReviewed(true);
				quizObject.setApproved(true);
			} else {
				quizObject.setReviewed(false);
				quizObject.setApproved(false);
			}
			
			int numberOfQuestions = Integer.parseInt(request.getParameter("numberOfQuestions"));
			
			//check that user has ticked at least one content type checkbox and format type checkbox
			if (tempQuiz.getSelectContents() != null && tempQuiz.getSelectFormats() != null) {
				//populate contentTypeTags list with all the content type tags that had ticked checkboxes 
				for (ContentTypeTag content : ContentTypeTag.values()) {
					if (tempQuiz.getSelectContents().contains(content)) {
						contentTypeTags.add(content);
			    	}     
				}
				//populate contentTypeTags list with all the content type tags that had ticked checkboxes 
				for(FormatTag format : FormatTag.values()) {
					if (tempQuiz.getSelectFormats().contains(format)) {
						formatTypeTags.add(format);
					}
				}
			//direct user by displaying appropriate response if no content type checkbox is ticked	
			} else if (tempQuiz.getSelectContents() == null) {
				logger.warn("No content type specified by user");
				TempQuiz quiz = new TempQuiz();
				
				model.addAttribute("user", user);
				model.addAttribute("message", "nope1");
				model.addAttribute("tempQuiz", quiz);
				return "/createQuizForm";
				
			//direct user by displaying appropriate response if no format type checkbox is ticked		
			} else if (tempQuiz.getSelectFormats() == null) {
				logger.warn("No format type specified by user");
				
				TempQuiz quiz = new TempQuiz();
				
				model.addAttribute("user", user);
				model.addAttribute("message", "nope2");
				model.addAttribute("tempQuiz", quiz);
				return "/createQuizForm";
			}
			
			//populate matchingQuestions list with all questions that match the content type tags, format type tags and the category tag
			for (int i = 0; i < contentTypeTags.size(); i++) {
				for (int j = 0; j < formatTypeTags.size(); j++) {
					matchingQuestions.addAll(questionService.getAllQuestionsWithMatchingFormatContentCatgoryTagApproved(formatTypeTags.get(j), contentTypeTags.get(i), quizObject.getQuizType()));
					
					//populate lockedQuestionsMatching with all locked questions that match the content type tags, format type tags and the category tag
					for (int k = 0; k < lockedQuestions.size(); k++) {
						if (contentTypeTags.get(i) == lockedQuestions.get(k).getContentTypeTag() && formatTypeTags.get(j) == lockedQuestions.get(k).getFormatTag() && quizObject.getQuizType() == lockedQuestions.get(k).getCategoryTag()) {
							lockedQuestionsMatching.add(lockedQuestions.get(k));
						}	
					}					
				}
			}
			
			/*process locked questions so that locked questions are added to the generated quiz first
			 * if number of questions is less than matching locked questions create quiz with only locked questions
			 * else generate quiz with all locked questions and then auto generate the rest
			*/ 
			if (numberOfQuestions <= lockedQuestionsMatching.size()) {
				for (int i = 0; i < lockedQuestionsMatching.size(); i++) {
					if (selectedQuestions.size() < numberOfQuestions) {			
						selectedQuestions.add(lockedQuestionsMatching.get(i));
					} else {
						numberOfQuestions -= selectedQuestions.size();
						break;
					}
				}
				numberOfQuestions -= selectedQuestions.size();
			} else {
				for (int i = 0; i < lockedQuestionsMatching.size(); i++) {
					if (selectedQuestions.size() < numberOfQuestions) {			
						selectedQuestions.add(lockedQuestionsMatching.get(i));
					} 
				}
				numberOfQuestions -= selectedQuestions.size();		
			}
			
			//remove matching locked questions from matching questions list to avoid generating duplicates
			for(int i = 0; i < lockedQuestionsMatching.size(); i++) {
				if (matchingQuestions.contains(lockedQuestionsMatching.get(i))) {
					matchingQuestions.remove(lockedQuestionsMatching.get(i));
				}	
			}
			
			/* populate selected questions randomly based on the size of matching questions 
			 * if not enough matching questions exist then give user appropriate response 
			 */
			if (matchingQuestions.size() >= numberOfQuestions) {
				if (numberOfQuestions > 0) {
					for (int i = 0; i < numberOfQuestions; i++) {
						
						int randomNum = ThreadLocalRandom.current().nextInt(0, matchingQuestions.size());
						
						selectedQuestions.add(matchingQuestions.get(randomNum));
						matchingQuestions.remove(randomNum);	
					}
				}		
			} else {
				logger.warn("Not enough matching questions to auto generate quiz");
				
				TempQuiz quiz = new TempQuiz();
				
				model.addAttribute("matchingQuestions", matchingQuestions);
				model.addAttribute("matchingLockedQuestions", lockedQuestionsMatching);
				model.addAttribute("user", user);
				model.addAttribute("message", "nope");
				model.addAttribute("tempQuiz", quiz);
				return "/createQuizForm";
			}
			logger.info("successfully auto generated quiz");
			
			quizObject.setQuestions(selectedQuestions);
			quizService.saveQuiz(quizObject);
			
			returnString = "redirect:/quizList";
		} else {
			logger.error("user attempted to access create user without being logged in");
			returnString = "redirect:/index";
		}
		return returnString;
	}
	
	/**
	 * this method is used to prevent an error if the user tries to access the post method and direct them home
	 * 
	 * @return - this method returns the user back the index page
	 */
	@GetMapping("/createQuizAuto")
	public String getCreateQuizAutoindex() {
		return "redirect:/index";
	}
	
	
	// take the hint and goto submission service
	
	/**
	 * Retrieve selected question from the database and display its information and answers
	 * 
	 * @param quizId - int value for a quiz ID
	 * @param model - used to pass the object to the front end html, in this case a user object for the register field
	 * @param session - used to check if the user is logged in so they are redirected to the index page
	 * @return takes user to quiz detials page OR index page if error with authentication
	 */
	@GetMapping("/quizDetails")
	public String quizDetails(@RequestParam int quizId, Model model, HttpSession session) {
		String returnString;
		if (session.getAttribute("id") != null) {
			User user = userService.getUserById((int) session.getAttribute("id"));
			Quiz quiz = quizService.getQuizById(quizId);
			model.addAttribute("user", user);
			model.addAttribute("quiz", quiz);
			if( user.getUserRole() == UserRole.TRAINER || user.getUserRole() == UserRole.SALES || user.getUserId() == quiz.getUser().getUserId() ) {
				model.addAttribute("canDelete", true);
			}else {
				model.addAttribute("canDelete", false);

			}
			logger.info("displaying quiz details");
			returnString = "quizDetails";
		} else {
			logger.error("user attempted to access create user without being logged in");
			returnString = "redirect:/index";
		}
		return returnString;
	}
	
	/**
	 * Takes user to a page to edit quiz
	 * 
	 * @param id - int value of quiz
	 * @param model - used to pass the object to the front end html, in this case a user object for the register field
	 * @param session - used to check if the user is logged in so they are redirected to the index page
	 * @return page to edit quiz OR back to home page
	 */
	@GetMapping("/editQuiz")
	public String editQuiz(@RequestParam int id, Model model, HttpSession session) {
		String returnString;
		if (session.getAttribute("id") != null) {
			User user = userService.getUserById((int) session.getAttribute("id"));
			Quiz quiz =  quizService.getQuizById(id);
			if(user.getUserRole() == UserRole.TRAINER || user.getUserRole() == UserRole.SALES || user.getUserId() == quiz.getUser().getUserId()) {
				TempQuiz tempQuiz = new TempQuiz();
				tempQuiz.setQuizID(id);
				tempQuiz.setQuizName(quiz.getQuizName());
				tempQuiz.setQuizType(quiz.getQuizType());
//				tempQuiz.setAllowedTime(quiz.getAllowedTime());
				if(quiz.getAllowedTime() != 2147483647) {
					tempQuiz.setAllowedTime(quiz.getAllowedTime());
					tempQuiz.setNoTimeLimit(false);
				}
				else {
					tempQuiz.setAllowedTime(0);
					tempQuiz.setNoTimeLimit(true);
				}
				
				model.addAttribute("category", quiz.getQuizType());
				
				model.addAttribute("quiz", tempQuiz);
				List<Question> allQuestions = questionService.getAllByCategoryTag(quiz.getQuizType());
				List<Question> addedQuestions = quiz.getQuestions();
				model.addAttribute("allQuestions", allQuestions);
				model.addAttribute("addedQuestions", addedQuestions);
				
				logger.info("displaying the edit quiz form");
				
				returnString = "editQuiz";
			}else {
				logger.error("userid " + user.getUserId()+ "attempted to access the edit quiz page without correct permissions");
				returnString = "redirect:/index";
			}
		} else {
			logger.error("user attempted to access create user without being logged in");
			returnString = "redirect:/index";
		}
		return returnString;
	}
	
	/**
	 * 
	 * @param quizObject - TempQuiz object that stores new quiz data to update 
	 * @param model - used to pass the object to the front end html, in this case a user object for the register field
	 * @param session - used to check if the user is logged in so they are redirected to the index page
	 * @return page to view edited quiz details OR back to index
	 */
	@PostMapping("/editQuizObject")
	public String editQuizObject(@ModelAttribute("tempQuiz") TempQuiz quizObject, Model model, HttpSession session) {
		String returnString;
		if (session.getAttribute("id") != null) {
			User user = userService.getUserById((int) session.getAttribute("id"));
			Quiz quiz = quizService.getQuizById(quizObject.getQuizID());
			if( user.getUserRole() == UserRole.TRAINER || user.getUserRole() == UserRole.SALES || user.getUserId() == quiz.getUser().getUserId() ) {
				
				quiz.setQuestions(quizObject.getQuestions());
				quiz.setQuizName(quizObject.getQuizName());
				if(!quizObject.isNoTimeLimit())
					quiz.setAllowedTime(quizObject.getAllowedTime());
				else {
					quiz.setAllowedTime(Integer.MAX_VALUE);
				}
//				quiz.setAllowedTime(quizObject.getAllowedTime());
				
				if (user.getUserRole() == UserRole.BEACHED || user.getUserRole() == UserRole.POND || user.getUserRole() == UserRole.TRAINING) {
					quiz.setReviewed(false);
				}else {
					quiz.setReviewed(true);
				}
				
				quizService.updateQuiz(quiz);
				
				model.addAttribute("canDelete", true);
				model.addAttribute("quiz", quiz);
				checkIfFavourited(quiz, user);
				
				logger.info("edited the quiz");
				
				returnString = "redirect:/quizDetails?quizId=" + quiz.getQuizID();
			}else {
				logger.error("userid " + user.getUserId()+ "attempted to access the edit quiz page without correct permissions");
				returnString = "redirect:/index";
			}
		}else {
			logger.error("user attempted to access create user without being logged in");
			returnString = "redirect:/index";
		}
		return returnString;
	}
	
	
	/**
	 * this method is used to prevent an error if the user tries to access the post method and direct them home
	 * 
	 * @return - this method returns the user back the index page
	 */
	@GetMapping("/editQuizObject")
	public String editQuizObject() {
		return "redirect:/index";
	}
	
	/**
	 * Deletes a quiz from the quizList page and returns the admin to the quizList page
	 * 
	 * @param id - int value for quiz id
	 * @param model - used to pass the object to the front end html, in this case a user object for the register field
	 * @param session - used to check if the user is logged in so they are redirected to the index page
	 * @return page back to quizList page OR index page
	 */
	@GetMapping("/deleteQuiz")
	public String deleteQuiz(@RequestParam int id, Model model, HttpSession session) {
		String returnString;
		if (session.getAttribute("id") != null) {
			User user = userService.getUserById((int) session.getAttribute("id"));
			Quiz quiz = quizService.getQuizById(id);
			if( user.getUserRole() == UserRole.TRAINER || user.getUserRole() == UserRole.SALES || user.getUserId() == quiz.getUser().getUserId() ) {
				removeFromFavourites(quiz);
				removeFromNotifications(quiz);
				quizService.deleteById(id);
			}else {
				
				logger.error("userid " + user.getUserId()+ "attempted to access the delete quiz page without correct permissions");
				model.addAttribute("canDelete", false);
			}

			List<Quiz> quizzes = quizService.getreviewedQuizzes();
			model.addAttribute("quizzes", quizzes);
			session.setAttribute("favourites", user.getFavouriteQuizzes());
			model.addAttribute("favourites", user.getFavouriteQuizzes());
			
			logger.info("quiz " + id + " deleted");
			
			returnString = "redirect:/quizList";			
		} else {
			logger.error("user attempted to access create user without being logged in");
			returnString = "redirect:/index";
		}
		return returnString;
	}
	
	/**
	 * gets all the unreviewed quizzes and saves to model before opening the page
	 * 
	 * @param model - used to pass the object to the front end html, in this case a user object for the register field
	 * @param session - used to check if the user is logged in so they are redirected to the index page
	 * @return page to see all unreviewd quizzes
	 */
	@GetMapping("/viewUnreviewedQuizzes")
	public String viewUnreviewedQuizzes(Model model, HttpSession session) {
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
				logger.error("userid " + user.getUserId()+ " attempted to access the view unreviewd quiz page without correct permissions");
				returnString = "redirect:/index";
			}
		} else {
			logger.error("user attempted to access create user without being logged in");
			returnString = "redirect:/index";
		}
		return returnString;
	}
	
	/**
	 * this method will either set a quizes approved value to true or false and set the reviewed value to true before redirecting to viewUnreviewedQuizzes
	 * 
	 * @param id - integer id value of a quiz
	 * @param value - boolean value for approved
	 * @param model - used to pass the object to the front end html, in this case a user object for the register field
	 * @param session - used to check if the user is logged in so they are redirected to the index page
	 * @return page to view unreviewed question OR index page if error occurs
	 */
	@GetMapping("/finishQuizReview")
	public String finishQuizReview(@RequestParam Integer id, @RequestParam boolean value,Model model, HttpSession session) {
		String returnString;
		if (session.getAttribute("id") != null) {
			User user1 = userService.getUserById((int) session.getAttribute("id"));
			if(user1.getUserRole() == UserRole.TRAINER || user1.getUserRole() == UserRole.SALES) {
				Quiz quiz = quizService.getQuizById(id);
				quiz.setApproved(value);
				quiz.setReviewed(true);
				
				User student = userService.getUserById(quiz.getUser().getUserId());
	
				int aID = (int) session.getAttribute("id");
				createApprovedQuizNotification(quiz,value,student,aID);
				
				quizService.updateQuiz(quiz);
				User user = userService.getUserById(aID);
				
	
				if(value) {
					logger.info(user.getUserRole() + " userId: " + user.getUserId() + "(" + user.getName() + ")"
							+ "has reviewed and approved questionId: " + quiz.getQuizID());
				}
				else {
					logger.info(user.getUserRole() + " userId: " + user.getUserId() + "(" + user.getName() + ")"
							+ "has reviewed and rejected questionId: " + quiz.getQuizID());
				}
	
				returnString = "redirect:/viewUnreviewedQuizzes";
			}else {
				logger.error("userid " + user1.getUserId()+ "attempted to access the review quiz page without correct permissions");
				returnString = "redirect:/index";
			}
		} else {
			logger.error("user attempted to access create user without being logged in");
			returnString = "redirect:/index";
		}
		return returnString;
	}
	
	
	/**
	 * will favourite/unfavaourite a quiz depending on if its already been favourited by the user
	 * 
	 * @param quizid - int value of a quiz id
	 * @param model - used to pass the object to the front end html, in this case a user object for the register field
	 * @param session - used to check if the user is logged in so they are redirected to the index page
	 * @return page to view all approved quizzes
	 */
	@GetMapping("/favouriteQuiz")
	public String favouriteQuiz(@RequestParam int quizid, Model model, HttpSession session) {
		String returnString;
		if (session.getAttribute("id") != null) {
			User user = userService.getUserById((int) session.getAttribute("id"));
			List<Quiz> quizes= user.getFavouriteQuizzes();
			
			if(quizes.contains(quizService.getQuizById(quizid))) {
				quizes.remove(quizService.getQuizById(quizid));
			}else {
				quizes.add(quizService.getQuizById(quizid));
			}
			user.setFavouriteQuizzes(quizes);
			userService.saveOrUpdate(user);
			
			session.setAttribute("favourites", user.getFavouriteQuizzes());
			
			logger.info("displaying quizlist");
			
			returnString = "redirect:/quizList";
		} else {
			logger.error("user attempted to access create user without being logged in");
			returnString = "redirect:/index";
		}
		return returnString;
	}
	
	/**
	 * Gets all quizzes created by user with ID id and displays it
	 * 
	 * @param id - int value of a user id
	 * @param model - used to pass the object to the front end html, in this case a user object for the register field
	 * @param session - used to check if the user is logged in so they are redirected to the index page
	 * @return page to view all quizzes created by userID
	 */
	@GetMapping("/viewQuizesCreated")
	public String viewQuizesCreated(@RequestParam int id, Model model, HttpSession session) {
		String returnString;
		if (session.getAttribute("id") != null) {
			User user = userService.getUserById((int) session.getAttribute("id"));

			model.addAttribute("quizzes", quizService.getAllQuizsWithUserId(id));
			session.setAttribute("favourites", user.getFavouriteQuizzes());
			model.addAttribute("favourites", user.getFavouriteQuizzes());

			returnString = "createdQuizzes";
		} else {
			logger.error("user attempted to access create user without being logged in");
			returnString = "redirect:/index";
		}
		return returnString;
	}

	
	/**
	 * lets user favourite a quiz from student and returns them back to view quizzes created by student
	 * @param quizid - int value of a quiz
	 * @param model - used to pass the object to the front end html, in this case a user object for the register field
	 * @param session - used to check if the user is logged in so they are redirected to the index page
	 * @return returns admin back to page to view all quizzes created by a student
	 */
	@GetMapping("/favouriteQuizFromStudent")
	public String favouriteQuizFromStudent(@RequestParam int quizid, Model model, HttpSession session) {
		String returnString;
		if (session.getAttribute("id") != null) {
			User user = userService.getUserById((int) session.getAttribute("id"));
			List<Quiz> quizes= user.getFavouriteQuizzes();
			
			if(quizes.contains(quizService.getQuizById(quizid))) {
				quizes.remove(quizService.getQuizById(quizid));
			}else {
				quizes.add(quizService.getQuizById(quizid));
			}
			user.setFavouriteQuizzes(quizes);
			userService.saveOrUpdate(user);
			
			User student = (quizService.getQuizById(quizid)).getUser();
			
			model.addAttribute("quizzes", quizService.getAllQuizsWithUserId(student.getUserId()));
			session.setAttribute("favourites", user.getFavouriteQuizzes());
			model.addAttribute("favourites", user.getFavouriteQuizzes());
			
			returnString = "createdQuizzes";
		} else {
			
			logger.error("user attempted to access create user without being logged in");
			returnString = "redirect:/index";
		}
		return returnString;
	}

	/**
	 * Will create the notification to alert the student when their quiz gets approved/rejected
	 * 
	 * @param quiz - quiz object that has been approved/rejected
	 * @param value - boolean value weather it has been approved/rejected
	 * @param student - student object that created the quiz
	 * @param aID - int value of admin user id that approved/rejected the quiz
	 */
	private void createApprovedQuizNotification(Quiz quiz, boolean value, User student, int aID) {
		Notification notification = new Notification();
		User user = userService.getUserById(aID);
		
		String message ="";
		if(value) {
			message = user.getName() + " has approved your quiz : " + quiz.getQuizName();
			logger.info("User userId: " + student.getUserId() + "(" + student.getName() + ")" + "has been notified that "
					+ user.getUserRole() + " userId: " + user.getUserId() + "(" + user.getName() + ")" 
					+ "has reviewed and approved their quizId: " + quiz.getQuizID());
		} else {
			message = user.getName() + " has rejected your quiz : " + quiz.getQuizName();
			logger.info("User userId: " + student.getUserId() + "(" + student.getName() + ")" + "has been notified that "
					+ user.getUserRole() + " userId: " + user.getUserId() + "(" + user.getName() + ")" 
					+ "has reviewed and rejected their quizId: " + quiz.getQuizID());
		}
		
		notification.setMessage(message);
		notification.setQuizID(quiz);
		notification.setUserID(student);
		notification.setEditor(user.getName());
		
		logger.info("notification set");
		
		notificationService.saveNotification(notification);
	}
	
	/**
	 * removes all mentions of quiz from user favourites and deletes it from database
	 * 
	 * @param quiz - quiz object to remove from database
	 */
	private void removeFromFavourites(Quiz quiz) {
		List<User> users = userService.getAllUsers();
		for (User user : users) {
			List<Quiz> favs = user.getFavouriteQuizzes();
			
			if(favs.contains(quiz)) {
				favs.remove(quiz);
				user.setFavouriteQuizzes(favs);
				userService.saveOrUpdate(user);	
				createBasicNotification("The quiz :" + quiz.getQuizName() +" has been deleted",user);
				logger.info("favourite removed");
			}
			
		}
	}
	
	/**
	 * removes all mentions of quiz from notifications by deleting them
	 * 
	 * @param quiz - quiz object to remove from database
	 */
	private void removeFromNotifications(Quiz quiz) {
		List<Notification> notesList = notificationService.getAllNotifications();
		for (Notification notification : notesList) {
			if(notification.getQuizID() != null) {
				if(notification.getQuizID().getQuizID() == quiz.getQuizID()) {
					notificationService.deleteById(notification.getNotificationID());
					logger.info("notification deleted");
				}
			}
		}
		
	}
	
	/**
	 * Creates a basic notification with a message and a userID
	 * 
	 * @param string - string message to set for notification
	 * @param user - user to be sent notification
	 */
	private void createBasicNotification(String string, User user) {
		Notification notification = new Notification();
		notification.setMessage(string);
		notification.setUserID(user);
		notificationService.saveNotification(notification);
		logger.info("notification set");
		
	}
	
	/**
	 * checks if a quiz has been favourited by ANY user and will create notification to let them know quiz has been changed
	 * 
	 * @param quiz - quiz to check if favourited
	 * @param user - user account making changes
	 */
	private void checkIfFavourited(Quiz quiz, User user) {
		List<User> users = userService.getAllUsers();
		for (User user1 : users) {
			List<Quiz> favs = user1.getFavouriteQuizzes();
			
			if(favs.contains(quiz) || quiz.getUser().getUserId() == user1.getUserId()) {
				creatEditedQuizNotification(quiz, user1, user);
			}	
		}
	}

	/**
	 * crates a notification for user if a quiz has been edited
	 * 
	 * @param quiz - quiz that has been edited
	 * @param user1 - user to recieve notification
	 * @param user - user account to make change
	 */
	private void creatEditedQuizNotification(Quiz quiz, User user1, User user) {
		Notification notification = new Notification();
		notification.setMessage("Quiz "+quiz.getQuizName() + " has been edited by " + user.getName());
		notification.setUserID(user1);
		notification.setQuizID(quiz);
		notificationService.saveNotification(notification);
		logger.info("notification set");
	}
	
}
