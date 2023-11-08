package com.spring.web.controller;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import org.hibernate.result.NoMoreReturnsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.context.Theme;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.spring.web.model.TempQuestion;
import com.spring.web.model.Answer;
import com.spring.web.model.Notification;
import com.spring.web.model.Question;
import com.spring.web.model.User;
import com.spring.web.model.EntityEnum.CategoryTag;
import com.spring.web.model.EntityEnum.ContentTypeTag;
import com.spring.web.model.EntityEnum.FormatTag;
import com.spring.web.model.EntityEnum.UserRole;
import com.spring.web.service.QuestionService;
import com.spring.web.service.UserService;
import com.spring.web.service.AnswerService;
import com.spring.web.service.NotificationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * Controller used to handle all processes of Question Entity and Answer Entity
 * 
 * @author Cao Vinh Lam, Christian Lee, Samantha Jermyn
 *
 */
@Controller
public class QuestionController {
	
	Logger logger = LoggerFactory.getLogger(QuestionController.class);
	
	@Autowired
	QuestionService questionService;

	@Autowired
	UserService userService;
	
	@Autowired
	AnswerService answerService;
	
	@Autowired
	NotificationService notificationService;
	
	/**
	 * Retrieves all questions from the database and displays them
	 * 
	 * @param model - List of Question objects stored as an attribute so it can be displayed on the page
	 * @param session - Retrieve user id from session
	 * @return A HTML page listing all the questions currently stored in the database
	QuestionService questionService;
	*/
	@GetMapping("/questionList")
	public String getAllQuestions(Model model, HttpSession session) {
		String returnString;
		if (session.getAttribute("id") != null) {
			User user = userService.getUserById((int) session.getAttribute("id"));
			session.setAttribute("notes", notificationService.getAllNotificaitonsWithUserId(user.getUserId()));

			
			List<String> contentTagsStrings = new ArrayList<>();
			
			for (ContentTypeTag value : ContentTypeTag.values()) {				
				contentTagsStrings.add(value.toString());
			}
					
			List<Question> questions;
			Optional<List<Question>> questionsOpt = Optional.of(questionService.getAllByApproved(true));
			
			if (questionsOpt.isPresent()) {				
				questions = questionsOpt.get();			
				model.addAttribute("questions", questions);				
			}
			
			List<Question> myQuestions = questionService.getAllQuestionsWithUserId(user.getUserId());
			
			model.addAttribute("user", user);
			model.addAttribute("myQuestions", myQuestions);			
			model.addAttribute("contentTags", contentTagsStrings);			
			model.addAttribute("lockedQuestions", user.getLockedQuestions());
			
			logger.trace("Retrieving Questions List");
			
			returnString = "/questionList";
		}else {
			logger.error("user not logged in redirecting to login");
			returnString = "redirect:/index";
		}

		return returnString;

	}
	
	/**
	 * 
	 * filters the entire database of questions
	 * 
	 * @param id - the id of the user filtering the list
	 * @param model - handles the objects being passed from the backend to the html frontend
	 * @param request - gets the params from the form in the front end
	 * @param session - stores the current login session of the user
	 * @return sends the user to the displayed question list
	 */
	@PostMapping("/questionListSorted")
	public String getAllQuestionsForTag(@RequestParam int id, HttpServletRequest request, Model model, HttpSession session) {
		 
		List<Question> filteredQuestions = new ArrayList<>();
		List<Question> filteredUserQuestions = new ArrayList<>();
		
		//Request inputs from questionList.html and stores tags in variables
		ContentTypeTag contentTag = ContentTypeTag.fromStringValue(request.getParameter("contentType"));
			
		CategoryTag categoryTag = CategoryTag.fromStringValue(request.getParameter("categoryType"));
	
		FormatTag formatTag = FormatTag.fromStringValue(request.getParameter("formatType"));	
				
		//Needed for autocomplete (List of content type enum in string format)
		List<String> contentTypesStr = new ArrayList<>();
		
		for (ContentTypeTag value : ContentTypeTag.values()) {
			contentTypesStr.add(value.toString());
		}
		
		if (contentTag == null && categoryTag == null && formatTag == null) {
			
			filteredQuestions = questionService.getAllByApproved(true);
			filteredUserQuestions = questionService.getAllQuestionsWithUserId((int) session.getAttribute("id"));
			
		} else if (categoryTag == null && formatTag == null) {
			
			filteredQuestions = questionService.getAllByContentTypeTagApproved(contentTag);
			filteredUserQuestions = questionService.getAllByUserAndContentTypeTag(contentTag, (int) session.getAttribute("id"));
			
		} else if (contentTag == null && formatTag == null) {
			
			filteredQuestions = questionService.getAllByCategoryTagApproved(categoryTag);
			filteredUserQuestions = questionService.getAllByUserAndCategoryTag(categoryTag, (int) session.getAttribute("id"));
			
		} else if (contentTag == null && categoryTag == null) {
			
			filteredQuestions = questionService.getAllByFormatTagApproved(formatTag);
			filteredUserQuestions = questionService.getAllByUserAndFormatTag(formatTag, (int) session.getAttribute("id"));
			
		} else if (formatTag == null) {
			
			filteredQuestions = questionService.getAllQuestionsWithMatchingContentCatgoryTagApproved(contentTag, categoryTag);
			filteredUserQuestions = questionService.getAllUserQuestionsWithMatchingCategoryContentTag(categoryTag, contentTag, (int) session.getAttribute("id"));
			
		} else if (contentTag == null) {
			
			filteredQuestions = questionService.getAllQuestionsWithMatchingFormatCatgoryTagApproved(formatTag, categoryTag);
			filteredUserQuestions = questionService.getAllUserQuestionsWithMatchingCategoryFormatTag(categoryTag, formatTag, (int) session.getAttribute("id"));
			
		} else if (categoryTag == null) {
			
			filteredQuestions = questionService.getAllQuestionsWithMatchingFormatContentTagApproved(formatTag, contentTag);
			filteredUserQuestions = questionService.getAllUserQuestionsWithMatchingFormatContentTag(formatTag, contentTag, (int) session.getAttribute("id"));
			
		} else {
			//Stores the questions that are common based on the input tags
			filteredQuestions = questionService.getAllQuestionsWithMatchingFormatContentCatgoryTagApproved(formatTag, contentTag, categoryTag);
	 		
			//Stores the questions that are common based on the input tags that belong to the user
			filteredUserQuestions = questionService.getAllUserQuestionsWithMatchingFormatContentCatgoryTag(formatTag, contentTag, categoryTag, (int) session.getAttribute("id"));
		}
		
		List<Question> questions;
		List<Question> userQuestions;
		Optional<List<Question>> questionsOpt = Optional.of(filteredQuestions);
		Optional<List<Question>> userQuestionsOpt = Optional.of(filteredUserQuestions);
		
		if (questionsOpt.isPresent()) {			
			questions = questionsOpt.get();
			model.addAttribute("questions", questions);			
		} 
		
		if (userQuestionsOpt.isPresent()) {			
			userQuestions = userQuestionsOpt.get();
			model.addAttribute("myQuestions", userQuestions);			
		}
		
		model.addAttribute("lockedQuestions", userService.getUserById((int) session.getAttribute("id")).getLockedQuestions());
		model.addAttribute("contentTags", contentTypesStr);
		model.addAttribute("page", "questionListSorted");
		
		return("/questionList");
	} 
	
	/**
	 * redirects a user to index when they try to access a post method without submitting a form
	 * 
	 * @return returns to index page
	 */
	@GetMapping("/questionListSorted")
	public String getAllQuestionsForTagIndex() {
		return("redirect:/index");
	}
	
	/**
	 * Provide autocomplete with list of content type tags
	 * 
	 * @param term - dunno
	 * @return list of strings for autocompletion
	 */
	@GetMapping("/autocompleteContentTags")
	@ResponseBody
	public List<String> getAllContentTagsAutocomplete(@RequestParam(value="term", required = false, defaultValue="")String term) {
		
		List<String> contentTypes = new ArrayList<>();
		
		for (ContentTypeTag value : ContentTypeTag.values()) {
			contentTypes.add(value.toString());
		}
		
		logger.info("Autocomplete Processing");
		
		return contentTypes;
	}

	/**
	 * Create an empty TempQuestion Object and pass to the question creation form
	 * 
	 * @param model - TempQuestion object stored as an attribute to gather all information regarding questions and answers in the form
	 * @param session - Retrieve user id from session
	 * @return A HTML page for creating question and answers
	 */
	@GetMapping("/createQuestion")
	public String createQuestionForm(Model model, HttpSession session) {
		String returnString;
		if (session.getAttribute("id") != null) {
			User user = userService.getUserById((int) session.getAttribute("id"));
			model.addAttribute("user",user);
			
			TempQuestion question = new TempQuestion();
			model.addAttribute("questionObject",question);

			logger.info("Display question creation page for userId: " + user.getUserId() + "(" + user.getName() + ")");

			returnString = "createQuestionForm";
		} else {
			logger.error("user not logged in redirecting to login");
			returnString = "redirect:/index";
		}
		return returnString;
	}
	
	/**
	 * Process creating question and answers and storing them into the database
	 * 
	 * @param questionObject - TempQuestion object posted from the question creation form page with information regarding a question its answers
	 * @param session - Retrieve user id from session
	 * @return Redirects user back to the questionList page
	 */
	@PostMapping("/createQuestionObject")
	public String createQuestionObject(@ModelAttribute("questionObject") TempQuestion questionObject, HttpSession session) {

		// Get current user to set user id as a foreign key for the question
		User user = userService.getUserById((int) session.getAttribute("id"));
		
		// Setting up the Question Object to be stored in the database
		Question question = new Question();	
		
		question.setQuestionContent(questionObject.getQuestionContent());
		question.setCategoryTag(questionObject.getCategoryTag());
		question.setFormatTag(questionObject.getFormatTag());
		question.setContentTypeTag(questionObject.getContentTypeTag());
		question.setUser(user);
		
		// Questions created by Trainers and Sales are already pre-approved and reviewed
		if (user.getUserRole() == UserRole.TRAINER || user.getUserRole() == UserRole.SALES) {
			question.setApproved(true);
			question.setReviewed(true);
		}
		
		// Add question to the database
		questionService.saveOrUpdateQuestion(question);
		
		// Keeping track of how many answers we've traversed through
		int answer_counter = 0;
		// Keeping track of how many correct answers we've traversed through
		int correct_counter = 0;
		Answer answerDb;
		
		for (String answer : questionObject.getAnswers()) {
			answerDb = new Answer();
			// Check if question is a multi-answer or not
			if (questionObject.getFormatTag() == FormatTag.MULTI_ANSWER) {
				
				// At least one answer is correct in multi-answer
				if (questionObject.getCorrectAnswers() != null) {
					
					// Check if current answer is a correct answer in the multi-answer question
					if (answer_counter == Integer.parseInt(questionObject.getCorrectAnswers().get(correct_counter))) {
						answerDb.setCorrect(true);
						// Check that there is still correct answers to traverse to
						if (correct_counter != questionObject.getCorrectAnswers().size() - 1)
							// Increment the number of correct answers we've traversed through
							correct_counter++;
					}			
					else {
						answerDb.setCorrect(false);
					}
				}
				// No answer is correctg in the multi-answer
				else {
					answerDb.setCorrect(false);
				}
			}
			// First answer in short answer and multiple choice will always be correct
			else if (correct_counter == 0){
				answerDb.setCorrect(true);
				// Increment the number of correct answers we've traversed through
				correct_counter++;
			}
			// Other multiple choice answers are incorrect answers
			else {
				answerDb.setCorrect(false);
			}
			// Set question associated with the answer as a foreign key to the answer entity
			answerDb.setQuestion(questionService.getMostRecentQuestionCreatedWithUserId(user.getUserId()));
			answerDb.setAnswer(answer);
			
			// Add answer to the database
			answerService.saveOrUpdateAnswer(answerDb);
			
			// Increment the number of answers we've traversed through
			answer_counter++;
		}
		
		// Getting question id for logging
		question = questionService.getMostRecentQuestionCreatedWithUserId(user.getUserId());
//		logger.info("QuestionId: " + question.getQuestionId() + " created by userId: " + user.getUserId() + "(" + user.getName() + ")");
		
		return "redirect:/questionList";
	}
	
	/**
	 * redirects the user back to the index page when they try access a postmapping
	 * 
	 * @return returns the index page
	 */
	@GetMapping("/createQuestionObject")
	public String createQuestionObjectIndex() {
		return "redirect:/index";
	}
	
	
	// hello again - goto the quiz controller next
	
	/**
	 * Retrieve selected question from the database and display its information and answers
	 * 
	 * @param questionId - Id of the question selected
	 * @param model - Question object stored as an attribute so it can be displayed on the page
	 * @param session - Retrieve user id from session
	 * @return A HTML page displaying all details regarding the selected question
	 */
	@GetMapping("/questionDetails")
	public String getQuestionDetails(@RequestParam int questionId, Model model, HttpSession session) {
		String returnString;
		if (session.getAttribute("id") != null) {
			User user = userService.getUserById((int) session.getAttribute("id"));
			Question question = questionService.getQuestionById(questionId);
			if(user.getUserId() == question.getUser().getUserId() || user.getUserRole() == UserRole.TRAINER
					|| user.getUserRole() == UserRole.SALES || question.isApproved() == true) { // this if statement is working out who is allowed to
																								// view the question details based on its current variables state
				model.addAttribute("user", user);
				
				
				model.addAttribute("questionObject", question);
				model.addAttribute("userCreated", question.getUser().getName());
				
				List<Answer> answers = answerService.getAllAnswerByQuestionId(questionId);
				model.addAttribute("answers", answers);
	
				logger.info("Display details of questionId: " + question.getQuestionId());
	
				returnString = "questionDetails";
			}else {
				logger.error("user not logged in redirecting to login");
				returnString = "redirect:/index";
			}
		}else {
			logger.error("user not logged in redirecting to login");
			returnString = "redirect:/index";
		}
		return returnString;
	}
	
	/**
	 * Edit questionContent and answers of the selected question
	 * 
	 * @param questionId - Id of the question selected
	 * @param model - Question object stored as an attribute so its information can be edited on the page
	 * @param session - used to check if the user is logged in for security
	 * @return A HTML page for editing question answers
	 */
	@GetMapping("/editQuestion")
	public String editQuestionForm(@RequestParam int questionId, Model model, HttpSession session) {
		String returnString;
		if (session.getAttribute("id") != null) {
			User user = userService.getUserById((int) session.getAttribute("id"));
			Question question = questionService.getQuestionById(questionId);
			if(user.getUserId() == question.getUser().getUserId() && question.isApproved() == false 
					|| user.getUserRole() == UserRole.TRAINER
					|| user.getUserRole() == UserRole.SALES && question.getCategoryTag() == CategoryTag.INTERVIEW_PREP) {
				List<Answer> answers = answerService.getAllAnswerByQuestionId(questionId);
		
				TempQuestion tempQuestion = new TempQuestion(question.getQuestionId(), question.getQuestionContent(), question.isApproved(), 
						question.isReviewed(), question.getCategoryTag(), question.getFormatTag(), question.getContentTypeTag(), question.getUser());
				tempQuestion.setNewAnswers(answers);
				
				model.addAttribute("questionObject", tempQuestion);
	
				logger.info("Display edit question page for questionId: " + question.getQuestionId());
	
				returnString = "editQuestionForm";
			}else {
				logger.error("user not logged in redirecting to login");
				returnString = "redirect:/index";
			}
		}else {
			logger.error("user not logged in redirecting to login");
			returnString = "redirect:/index";
		}
		return returnString;
	}
	
	/**
	 * Process updated question answers and storing updated information into the database
	 * 
	 * @param questionObject - TempQuestion object posted from the question edit form page with information regarding a question its answers
	 * @param session - Retrieve user id from session
	 * @return Redirects user back to the selected question details page
	 */
	@PostMapping("/editQuestionObject")
	public String editQuestionObject(@ModelAttribute("questionObject") TempQuestion questionObject, HttpSession session) {

		User user = userService.getUserById((int) session.getAttribute("id"));
		
		// Setting up the Question Object to be updated in the database
		Question question = new Question(questionObject.getQuestionContent(), questionObject.isApproved(), 
				questionObject.isReviewed(), questionObject.getCategoryTag(), questionObject.getFormatTag(), questionObject.getContentTypeTag(), questionObject.getUser());
		question.setQuestionId(questionObject.getQuestionId());
		
		// Put the question up for a re-review after a student has edited a rejected question
		if (user.getUserRole() == UserRole.BEACHED || user.getUserRole() == UserRole.POND || user.getUserRole() == UserRole.TRAINING) {
			question.setReviewed(false);
		}
		else {
			question.setReviewed(true);
		}
		
		// Notify the original user that their question has been edited by another user
		if (user.getUserId() != question.getUser().getUserId()) {
			createEditQuestionNotification(question, question.getUser(), user.getUserId());
		}
		
		// Update Question in the database
		questionService.saveOrUpdateQuestion(question);
		
		// Update Answers in the database
		List<Answer> answers = questionObject.getNewAnswers();
		for (Answer newAnswer : answers) {
			answerService.saveOrUpdateAnswer(newAnswer);
		}
		
		logger.info("QuestionId: " + question.getQuestionId() + " edited by userId: " + user.getUserId() + "(" + user.getName() + ")");
		
		return "redirect:/questionDetails?questionId=" + questionObject.getQuestionId();
	}
	
	/**
	 * redirects the user to the index page if they try access a post mapping
	 * 
	 * @return returns the user to the index page
	 */
	@GetMapping("/editQuestionObject")
	public String editQuestionObjectIndex() {
		return("redirect:/index");
	}
	
	/**
	 * Process deleting question and corresponding answers in the database
	 * 
	 * @param questionId - TempQuestion object posted from the question edit form page with information regarding a question its answers
	 * @param session - holds the session information of the logged in user
	 * @return Redirects user back to the selected question details page
	 */
	@GetMapping("/deleteQuestionObject")
	public String deleteQuestionObject(@RequestParam int questionId, HttpSession session) {
		String returnString;
		if (session.getAttribute("id") != null) {
			Question question = questionService.getQuestionById(questionId);
			User user = userService.getUserById((int) session.getAttribute("id"));
			if(user.getUserId() == question.getUser().getUserId() && question.isApproved() == false 
					|| user.getUserRole() == UserRole.TRAINER
					|| user.getUserRole() == UserRole.SALES && question.getCategoryTag() == CategoryTag.INTERVIEW_PREP) {
			
				questionService.deleteById(questionId);
		
				// Notify the original user that their question has been deleted by another user
				if (user.getUserId() != question.getUser().getUserId()) {
					createDeleteQuestionNotification(question.getQuestionContent(), question.getUser(), user.getUserId());
				}
	
				logger.info("QuestionId: " + question.getQuestionId() + " deleted by userId: " + user.getUserId() + "(" + user.getName() + ")");
	
				returnString = "redirect:/questionList";
			}else {
				logger.error("user not logged in redirecting to login");
				returnString = "redirect:/index";
			}
		} else {
			logger.error("user not logged in redirecting to login");
			returnString = "redirect:/index";
		}
		return returnString;
	}
	
	/**
	 * Gets all unreviewed questions
	 * 
	 * @param model - attribute to pass to HTML
	 * @param session - holds the session information of the logged in user
	 * @return page to see all unreviewed questions
	 */
	@GetMapping("/viewUnreviewed")
	public String viewUnreviewed(Model model, HttpSession session) {
		String returnString;
		if (session.getAttribute("id") != null) {
			User user = userService.getUserById((int) session.getAttribute("id"));
			if( user.getUserRole() == UserRole.TRAINER || user.getUserRole() == UserRole.SALES) {
			
				model.addAttribute("user", user);
				
				List<Question> questions = questionService.getUnreviewedQuestions();
				model.addAttribute("questions", questions); 
	
				logger.info("Displaying unreviewed questions page by " + user.getUserRole() + " userId: " + user.getUserId() + "(" + user.getName() + ")");
				
				returnString = "/questionsToReivew";
			}else {
				logger.error("user not logged in redirecting to login");
				returnString = "redirect:/index";
			}
		} else {
			logger.error("user not logged in redirecting to login");
			returnString = "redirect:/index";
		}
		return returnString;
	}
	
	 /**
	 * Will set the question related to the id to approved or rejected depending on value
	 * 
	 * @param id - question id
	 * @param value - boolean value of approval
	 * @param model - attribute to pass to HTML
	 * @param session - holds the session information of the logged in user
	 * @return returns to page to view all unreviewed questions
	 */
	@GetMapping("/finishReview")
	public String finishReview(@RequestParam Integer id, @RequestParam boolean value,Model model, HttpSession session) {
		String returnString;
		if (session.getAttribute("id") != null) {
			User user1 = userService.getUserById((int) session.getAttribute("id"));
			if(user1.getUserRole() == UserRole.TRAINER || user1.getUserRole() == UserRole.SALES) {
				Question question = questionService.getQuestionById(id);
				question.setApproved(value);
				question.setReviewed(true);
				
				User student = userService.getUserById(question.getUser().getUserId());
	
				int aID = (int) session.getAttribute("id");
				createApprovedQuestionNotification(question,value,student,aID);
				
				questionService.saveOrUpdateQuestion(question);
				
				List<Question> questions = questionService.getUnreviewedQuestions();
				
				model.addAttribute("questions", questions);
				
				User user = userService.getUserById(aID);
				
				model.addAttribute("user", user);
	
				if(value) {
					logger.info(user.getUserRole() + " userId: " + user.getUserId() + "(" + user.getName() + ")"
							+ "has reviewed and approved questionId: " + question.getQuestionId());
				}
				else {
					logger.info(user.getUserRole() + " userId: " + user.getUserId() + "(" + user.getName() + ")"
							+ "has reviewed and rejected questionId: " + question.getQuestionId());
				}
	
				returnString = "redirect:/viewUnreviewed";
			}else {
				logger.error("user not logged in redirecting to login");
				returnString = "redirect:/index";
			}
		} else {
			logger.error("user not logged in redirecting to login");
			returnString = "redirect:/index";
		}
		return returnString;
	}

	/**
	 * Method to create notification for when a question gets either approved or rejected	 * 
	 * 
	 * @param question - question that got approved/rejected
	 * @param value - boolean value for either approve/rejection
	 * @param student - student that created the question
	 * @param aID - admin account that is either approving or rejecting
	 */
	private void createApprovedQuestionNotification(Question question, boolean value, User student, int aID) {
		Notification notification = new Notification();
		User user = userService.getUserById(aID);
		
		String message ="";
		if(value) {
			message = user.getName() + " has approved your question : " + question.getQuestionContent();
			logger.info("User userId: " + student.getUserId() + "(" + student.getName() + ")" + "has been notified that "
					+ user.getUserRole() + " userId: " + user.getUserId() + "(" + user.getName() + ")" 
					+ "has reviewed and approved their questionId: " + question.getQuestionId());
		} else {
			message = user.getName() + " has rejected your question : " + question.getQuestionContent();
			logger.info("User userId: " + student.getUserId() + "(" + student.getName() + ")" + "has been notified that "
					+ user.getUserRole() + " userId: " + user.getUserId() + "(" + user.getName() + ")" 
					+ "has reviewed and rejected their questionId: " + question.getQuestionId());
		}
		
		notification.setMessage(message);
		notification.setQuestionID(question);
		notification.setUserID(student);
		notification.setEditor(user.getName());
		
		notificationService.saveNotification(notification);
	}
	
	/**
	 * Method to create notification for when a question gets edited by another user 
	 * 
	 * @param question - Question that has been edited
	 * @param student - User that created the question
	 * @param editorId - Id of the user that edited the question
	 */
	private void createEditQuestionNotification(Question question, User creator, int editorId) {
		Notification notification = new Notification();
		User user = userService.getUserById(editorId);
		
		String message = user.getName() + " has edited your question : " + question.getQuestionContent();

		notification.setMessage(message);
		notification.setQuestionID(question);
		notification.setUserID(creator);
		notification.setEditor(user.getName());
		
		logger.info("User userId: " + creator.getUserId() + "(" + creator.getName() + ")" + "has been notified that "
				+ user.getUserRole() + " userId: " + user.getUserId() + "(" + user.getName() + ")" 
				+ " has edited their questionId: " + question.getQuestionId());
		
		notificationService.saveNotification(notification);
	}
	
	/**
	 * Method to create notification for when a question gets edited by another user 
	 * 
	 * @param question - Question that has been edited
	 * @param student - User that created the question
	 * @param editorId - Id of the user that edited the question
	 */
	private void createDeleteQuestionNotification(String questionContent, User creator, int editorId) {
		Notification notification = new Notification();
		User user = userService.getUserById(editorId);
		
		String message = user.getName() + " has deleted your question : " + questionContent;

		notification.setMessage(message);
		notification.setUserID(creator);
		notification.setEditor(user.getName());
		
		logger.info("User userId: " + creator.getUserId() + "(" + creator.getName() + ")" + "has been notified that "
				+ user.getUserRole() + " userId: " + user.getUserId() + "(" + user.getName() + ")" 
				+ " has deleted their question: " + questionContent);
		
		notificationService.saveNotification(notification);
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
    
    /**
     * Retrieves all locked questions and display lockedQuestionsDetails.html to user
     * @param model - stores info that can be accessed from the front-end, in this case a list of locked questions
     * @param session
     * @return String Object - user is directed to lockedQuestionsDetails.html
     */
    @GetMapping("/lockedQuestionsDetails")
    private String displayLockQuestions(Model model, HttpSession session) {
    	
    	String returnString;
    	User user = userService.getUserById((int) session.getAttribute("id"));
    	
		if (session.getAttribute("id") != null) {
	    	model.addAttribute("lockedQuestions", user.getLockedQuestions());
	    	
	    	returnString =  "/lockedQuestionsDetails";
		} else {
			logger.error("user not logged in redirecting to login");
			returnString = "redirect:/index";
		}
		return returnString;
    }
    
    /**
     * Allows users to lock and unlock questions. Locked questions will appear in auto generated quizzes by default
     * @param questionId - the id of the questions object, identifies question to be locked/unlocked
     * @param page - method detects which page the user is on to redirect them appropriately
     * @param session - holds the session information of the logged in user 
     * @return redirects user to page that they locked/unlocked question on
     */
    @GetMapping("/lockQuestion")
    private String lockQuestion(@RequestParam Integer questionId, @RequestParam String page, HttpSession session) {
    	
    	String returnString;
    	
    	User user = userService.getUserById((int) session.getAttribute("id"));
    	
    	if (session.getAttribute("id") != null) {
	    	List<Question> lockedQuestions = user.getLockedQuestions();
			if(!lockedQuestions.contains(questionService.getQuestionById(questionId))) {
				logger.info("Locking question: " + questionService.getQuestionById(questionId).getQuestionContent());
				
				lockedQuestions.add(questionService.getQuestionById(questionId));
	    		user.setLockedQuestions(lockedQuestions);
	    		userService.saveOrUpdate(user);
			} else {
				logger.info("Unlocking question: " + questionService.getQuestionById(questionId).getQuestionContent());
				
				lockedQuestions.remove(questionService.getQuestionById(questionId));
				user.setLockedQuestions(lockedQuestions);
				userService.saveOrUpdate(user);
			}
	    	
			if (page.equals("questionList")) {
				returnString = "redirect:/questionList";
			} else if (page.equals("lockedQuestionsDetails")) {
				returnString = "redirect:/lockedQuestionsDetails";
			} else {
				returnString = "redirect:/questionList";
			} 
    	} else {
    		logger.error("user not logged in redirecting to login");
			returnString = "redirect:/index";
		}
		return returnString;		   	
    }
    
}
