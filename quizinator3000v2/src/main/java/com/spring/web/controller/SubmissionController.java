package com.spring.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties.Lettuce;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.web.model.Answer;
import com.spring.web.model.Question;
import com.spring.web.model.QuestionAttempt;
import com.spring.web.model.Quiz;
import com.spring.web.model.Submission;
import com.spring.web.model.TempSubmission;
import com.spring.web.model.User;
import com.spring.web.model.EntityEnum.CategoryTag;
import com.spring.web.model.EntityEnum.FormatTag;
import com.spring.web.model.EntityEnum.UserRole;
import com.spring.web.service.QuestionAttemptService;
import com.spring.web.service.QuizService;
import com.spring.web.service.SubmissionService;
import com.spring.web.service.UserService;

import jakarta.servlet.http.HttpSession;

 /**
 * submission controller is used for all the specific methods related to submission
 * 
 * @author Matthew Veldhuizen, Stanley Chilton
 *
 */
@Controller
public class SubmissionController {
	Logger logger = LoggerFactory.getLogger(SubmissionController.class);
	
	@Autowired
	QuizService quizService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	SubmissionService submissionService;

	@Autowired
	QuestionAttemptService questionAttemptService;
	
	/**
	 * A method to return the view for attempting a quiz and its questions. 
	 * Also populates a default map for short answer and multiple choice selections. 
	 * 
	 * @param id - The int id of the quiz that is being attempted
	 * @param model - Model containing tempSubmission object and quiz object
	 * @param session - HttpSession containing user ID
	 * @return - A string containing the html template name for either the takequiz page or index if not logged in
	 */
	@GetMapping("/quizattempt")
	public String takeQuiz(@RequestParam int id, Model model, HttpSession session) {
		String returnString;
		if (session.getAttribute("id") != null) {
			
			Quiz quiz = quizService.getQuizById(id);
			logger.info("" + quiz);
			
			HashMap<Integer, Boolean> userSelectionMap = new HashMap<>();
			
			HashMap<Integer, String> userShortAnswerMap = new HashMap<>();
			
			for(Question question : quiz.getQuestions()) {
				for (Answer answer : question.getAnswers()) {
					if (answer.getQuestion().getFormatTag() != FormatTag.SHORT_ANSWER)
						userSelectionMap.put(answer.getAnswerId(), false);
					else
						userShortAnswerMap.put(answer.getAnswerId(), "");
				}
			}
			
			TempSubmission tempSubmission = new TempSubmission(userSelectionMap, userShortAnswerMap, quiz);
			
			model.addAttribute("tempSubmission", tempSubmission);
			
			model.addAttribute("quiz", quiz);
			
			model.addAttribute("questions", quiz.getQuestions());
			
			returnString = "takequiz";
			
		}else {
			logger.error("index page access attempted while not logged in");
			returnString = "redirect:/index";
		}
		
		
		return returnString;
	}   
	
	/**
	 * The createQuizAttemptObject method creates the final submission object based off of the users selections in the 
	 * takequiz page. It is responsible for creating all the questionAttempt objects as well as auto marking the submission. 
	 * 
	 * @param id - The int id of the quiz to be submitted
	 * @param tempSubmission - the tempSubmission object that contains the maps that hold the user selections 
	 * @param model - The Model passed from the front end. 
	 * @param session - The HttpSession storing the id of the user
	 * @return returns String to redirect to the mapping for the viewresults HTML template
	 */
	@PostMapping("/quizAttemptObject")
	public String createQuizAttemptObject(@RequestParam int id, @ModelAttribute TempSubmission tempSubmission, Model model, HttpSession session) {
		
		Submission submission = new Submission();
		List<QuestionAttempt> questionAttempts = new ArrayList<>();
		tempSubmission.setQuiz(quizService.getQuizById(id));
		
		for (Question question : tempSubmission.getQuiz().getQuestions()) {
			for (Answer answer : question.getAnswers()) {
				if (question.getFormatTag() != FormatTag.SHORT_ANSWER) { // Condition to handle if question is short answer.
					Boolean individualAnswer = tempSubmission.getUserSelectionMap().get(answer.getAnswerId());
					if (individualAnswer == null) { // Condition to handle a button that has not been selected. Without null catch, error thrown. 
						individualAnswer = false;
						//If user did not select the checkbox for that answer, store false
						questionAttempts.add(new QuestionAttempt(submission, question, answer, false, null, individualAnswer == answer.isCorrect() ? true : false));
					} else {
						//If user selected checkbox for that answer, store true
						questionAttempts.add(new QuestionAttempt(submission, question, answer, true, null, individualAnswer == answer.isCorrect() ? true : false));
					}
				} else {
					// Creates questionAttempt for short answer question. Sets correct to null to flag that it has not been reviewed yet. 
					questionAttempts.add(new QuestionAttempt(submission, question, answer, false, tempSubmission.getUserShortAnswerMap().get(answer.getAnswerId()), null));
					break;
				}
			}
		}
		
		// Set visability of submission for reviewing if there is a null correct value on a questionAttempt identifying it as containing a short answer question. 
		for (int i = 0; i < questionAttempts.size(); i++) {
			if (questionAttempts.get(i).isCorrect() == null) {
				submission.setVisability(1);
				break;
			} else {
				submission.setVisability(0);
			}
		}
		
		submission.setQuiz(tempSubmission.getQuiz());
		submission.setUserId(userService.getUserById((int) session.getAttribute("id")));
		submission.setQuestionAttempt(questionAttempts);
		
		submissionService.saveOrUpdate(submission);
		
		List<Question> questions = submission.getQuiz().getQuestions();
		boolean questionCorrect = true;
		int correctCount = 0;
		
		for (QuestionAttempt questionAttempt : submission.getQuestionAttempt()) {
			questionAttemptService.saveOrUpdateQuestionAttempt(questionAttempt);
		}
		
		for (Question question : questions) {
			questionCorrect = true;
			correctCount = 0; 
			
			List<QuestionAttempt> questionAttemptsByQuestionAndSubmission = questionAttemptService.getQuestionAttemptsByQuestionAndSubmission(question.getQuestionId(), submission.getSubmissionID());
			
			// Loop to count number of correct answers. 
			for (QuestionAttempt questionAttempt : questionAttemptsByQuestionAndSubmission) {
				if (questionAttempt.getQuestion().getQuestionId() == question.getQuestionId()) {
					if (questionAttempt.isCorrect() != null && questionAttempt.isCorrect()) {
						if (questionAttempt.isSelection()) {
							correctCount++;
						}
					} else {
						// Set flag value if user made at least one incorrect selection so cannot receive part marks. 
						if (questionAttempt.isSelection())
							questionCorrect = false;
					}
				}
			}
			if (questionCorrect) {
				submission.setMark(submission.getMark() + correctCount);
			}
		}
		
		submissionService.saveOrUpdate(submission);
		
		
		
		return "redirect:/viewresults?id=" + submission.getSubmissionID();
	
	}
	
	
	/**
	 * Redirects the user to the index page as they arent allowed to access the post mapping
	 *
	 * @return returns a String containing a redirect to the index page
	 */
	@GetMapping("/quizAttemptObject")
	public String createQuizAttemptObjectindex() {
		return "redirect:/index";
	}
	
	
	/**
	 * The markSubmissionList method retrieves all the submissions in need of marking a displays a different set
	 * depending on the UserRole. 
	 *
	 * @param model - The Model that contains the list of submissions that need to be marked.  
	 * @param session - HttpSession that stores the user login information
	 * @return returns a String containing a redirect to the markSubmissionList HTML template. 
	 */
	@GetMapping("/markSubmissionList")
	public String markSubmissionList(Model model, HttpSession session) {
		
		
		String returnString;
		if (session.getAttribute("id") != null) {
			User user = userService.getUserById((int) session.getAttribute("id"));
			List<Submission> allSubmissions = new ArrayList<>(); 
			List<Submission> submissionsToMark = new ArrayList<>(); 
			List<Submission> submissionsToMarkSales = new ArrayList<>(); 
			
			allSubmissions = submissionService.getAllSubmissions();
			
			for (int i = 0; i < allSubmissions.size(); i++) {			
				if (allSubmissions.get(i).getVisability() == 1) {
					submissionsToMark.add(allSubmissions.get(i));
				}			
			}
			
			if(user.getUserRole() == UserRole.SALES) {			
				for (int i = 0; i < submissionsToMark.size(); i++) {
					if(submissionsToMark.get(i).getQuiz().getQuizType() == CategoryTag.INTERVIEW_PREP) {
						
						submissionsToMarkSales.add(submissionsToMark.get(i));
					}
				}
				
				model.addAttribute("submissionsToMark", submissionsToMarkSales);
				
			} else if (user.getUserRole() == UserRole.TRAINER) {
							
				model.addAttribute("submissionsToMark", submissionsToMark);
			}
			logger.info("submission being marked");
			returnString = "/markSubmissionList";
		} else {
			logger.error("index page access attempted while not logged in");
			returnString = "redirect:/index";
		}
		return returnString;
	}
	
	/**
	 * The markSubmission method is used to retrieve the relevant details of one individual submission
	 * and display this to the user. 
	 *
	 * @param submissionID - The Integer id of the submission to mark
	 * @param model - The Model that contains a tempSubmission object to set the changes on.
	 * @param session - HttpSession that stores the user login information
	 * @return - returns a String to display the markSubmission HTML Template.
	 */
	@GetMapping("/markSubmission")
	public String markSubmission(@RequestParam Integer submissionID, Model model, HttpSession session) {
		
		String returnString;
		if (session.getAttribute("id") != null) {
			TempSubmission tempSubmission = new TempSubmission(); 
			
			Submission submissionObject = submissionService.getSubmissionById(submissionID);
			
			List<QuestionAttempt> shortAnswerAttempts = new ArrayList<>();
			List<QuestionAttempt> attempts = submissionObject.getQuestionAttempt();
			
			for (int i = 0; i < attempts.size(); i++) {
				if (attempts.get(i).getShortAnswerAttempt() != null) {
					shortAnswerAttempts.add(attempts.get(i));
				}
			}	
			
			tempSubmission.setShortAnswerAttempts(shortAnswerAttempts);
			tempSubmission.setSubmission(submissionObject);
			
			model.addAttribute("tempSubmissionObject", tempSubmission);
			
			returnString = "/markSubmission";
		} else {
			logger.error("index page access attempted while not logged in");
			returnString = "redirect:/index";
		}
		return returnString;
	}
	
	/**
	 * The markQuestionAttempt method is used to alter the mark of a submission for each individual questionattempt object.
	 * 
	 * @param questionAttemptID - The Integer id of the question attempt
	 * @param model - The Model that passes the information from the backend to the frontend
	 * @param session - HttpSession that stores the user login information
	 * @return - returns a String containing a redirect to the markSubmission get Mapping 
	 */
	@GetMapping("/markQuestionAttempt")
	public String markQuestionAttempt(@RequestParam Integer questionAttemptID, Model model, HttpSession session) {
		
		QuestionAttempt attempt = questionAttemptService.getQuestionAttemptById(questionAttemptID);
		int submissionID = attempt.getSubmission().getSubmissionID();
		Submission submission = submissionService.getSubmissionById(submissionID);
		
		if (Boolean.FALSE.equals(attempt.isCorrect())) {
			attempt.setCorrect(true);
			submission.setMark(submission.getMark()+1);
			submissionService.saveOrUpdate(submission);
			questionAttemptService.saveOrUpdateQuestionAttempt(attempt);
		} else if (Boolean.TRUE.equals(attempt.isCorrect())){
			attempt.setCorrect(false);
			submission.setMark(submission.getMark()-1);
			submissionService.saveOrUpdate(submission);
			questionAttemptService.saveOrUpdateQuestionAttempt(attempt);
		} else {
			submission.setMark(submission.getMark()+1);
			submissionService.saveOrUpdate(submission);
			attempt.setCorrect(true);
			questionAttemptService.saveOrUpdateQuestionAttempt(attempt);
		}
				
		return "redirect:/markSubmission?submissionID=" + submissionID;
	}
	
	/**
	 * The finishMarking method is called after a user has completed marking a submission
	 * and returns the user to the markSubmissionList
	 * 
	 * @param submissionID - The Integer id of the submission object
	 * @return - returns a String containing a redirect to the markSubmissionList HTML template. 
	 */
	@GetMapping("/finishMarking")
	public String finishMarking(@RequestParam Integer submissionID) {
		
		Submission submission = submissionService.getSubmissionById(submissionID);
		
		submission.setVisability(0);
		submissionService.saveOrUpdate(submission);
		return "redirect:/markSubmissionList";
	}
	
}
