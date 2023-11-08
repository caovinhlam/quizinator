package com.spring.web;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Objects;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.spring.web.model.TempQuestion;
import com.spring.web.model.TempQuiz;
import com.spring.web.model.TempSubmission;
import com.spring.web.model.Submission;
import com.spring.web.model.Notification;
import com.spring.web.model.Answer;
import com.spring.web.model.Quiz;
import com.spring.web.model.Question;
import com.spring.web.model.QuestionAttempt;
import com.spring.web.model.User;
import com.spring.web.model.UserTemp;
import com.spring.web.model.EntityEnum.CategoryTag;
import com.spring.web.model.EntityEnum.ContentTypeTag;
import com.spring.web.model.EntityEnum.FormatTag;
import com.spring.web.model.EntityEnum.UserRole;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class EntityTests {
	
	@Mock
	Notification notificationMock;
	
	@Mock
	Question questionMock;
	
	@Mock
	Quiz quizMock;
	
	@Mock
	Answer answerMock;
	
	@Mock
	User userMock;
	
	@Mock
	QuestionAttempt questionAttemptMock;
	
	@Mock
	Submission submissionMock;
	
	User user;
	TempQuiz tempQuiz;
	TempSubmission tempSubmission;
	QuestionAttempt questionAttempt;
	
	@BeforeEach
	void setup() {
		user = new User();
	}
	
	//////////////////////TEST User Entity///////////////////////
	
	@Test
	void test_user_setter_and_getter_userId() {

		user.setUserId(1);
		
		assertEquals(1, user.getUserId());
	}
	
	@Test
	void test_user_setter_and_getter_user_name() {
		
		user.setName("test name");
		
		assertEquals("test name", user.getName());
	}
	
	@Test
	void test_user_setter_and_getter_user_email() {
		
		user.setEmail("test email");
		
		assertEquals("test email", user.getEmail());
	}
	
	@Test
	void test_user_setter_and_getter_user_password() {
		
		user.setPassword("test password");
		
		assertEquals("test password", user.getPassword());
	}
	
	@Test
	void test_user_setter_and_getter_user_userRole() {
		
		user.setUserRole(UserRole.TRAINING);
		
		assertEquals(UserRole.TRAINING, user.getUserRole());
	}
	
	@Test
	void test_user_setter_and_getter_user_notifications() {
				
		List<Notification> notifications = new ArrayList<>();
		notifications.add(notificationMock);
		notifications.add(notificationMock);
		notifications.add(notificationMock);
		
		user.setNotifications(notifications);
		
		assertEquals(notifications, user.getNotifications());
	}
	
	@Test
	void test_user_setter_and_getter_user_locked_questions() {
				
		List<Question> lockedQuestions = new ArrayList<>();
		lockedQuestions.add(questionMock);
		lockedQuestions.add(questionMock);
		lockedQuestions.add(questionMock);
		
		user.setLockedQuestions(lockedQuestions);
		
		assertEquals(lockedQuestions, user.getLockedQuestions());
	}
	
	@Test
	void test_user_setter_and_getter_user_favourite_quizzes() {
				
		List<Quiz> favouriteQuizzes = new ArrayList<>();
		favouriteQuizzes.add(quizMock);
		favouriteQuizzes.add(quizMock);
		favouriteQuizzes.add(quizMock);
		
		user.setFavouriteQuizzes(favouriteQuizzes);
		
		assertEquals(favouriteQuizzes, user.getFavouriteQuizzes());
	}
	
	@Test
	void test_user_toString() {
		
		User user = new User();
		
		assertEquals("User [userId=0, name=null, email=null, userRole=null]", user.toString());
	}
	
	@Test
	void test_user_constructor() {
		
		User user = new User("test name", "test email", "test password", UserRole.TRAINING);

		assertEquals("User [userId=0, name=test name, email=test email, userRole=Student in Training]", user.toString());
		
	}
	
	//////////////////////TEST Question Entity///////////////////////
	
	@Test
	void test_question_setter_and_getter_questionId() {
		
		Question question = new Question();
		question.setQuestionId(1);
		
		assertEquals(1, question.getQuestionId());
	}
	
	@Test
	void test_question_setter_and_getter_content() {
		
		Question question = new Question();
		question.setQuestionContent("test content");
		
		assertEquals("test content", question.getQuestionContent());
	}
	
	@Test
	void test_question_setter_and_getter_approved_status() {
		
		Question question = new Question();
		question.setApproved(true);
		
		assertEquals(true, question.isApproved());
	}
	
	@Test
	void test_question_setter_and_getter_reviewed_status() {
		
		Question question = new Question();
		question.setReviewed(false);
		
		assertEquals(false, question.isReviewed());
	}
	
	@Test
	void test_question_setter_and_getter_categoryTag() {
		
		Question question = new Question();
		question.setCategoryTag(CategoryTag.COURSE_CONTENT);
		
		assertEquals(CategoryTag.COURSE_CONTENT, question.getCategoryTag());
	}
	
	@Test
	void test_question_setter_and_getter_contentTag() {
		
		Question question = new Question();
		question.setContentTypeTag(ContentTypeTag.APM);
		
		assertEquals(ContentTypeTag.APM, question.getContentTypeTag());
	}
	
	@Test
	void test_question_setter_and_getter_formatTag() {
		
		Question question = new Question();
		question.setFormatTag(FormatTag.MULTI_ANSWER);
		
		assertEquals(FormatTag.MULTI_ANSWER, question.getFormatTag());
	}
	
	@Test
	void test_question_setter_and_getter_User() {
		
		Question question = new Question();
		question.setUser(user);
		
		assertEquals(user, question.getUser());
	}
	
	@Test
	void test_question_setter_and_getter_answers() {
		
		Question question = new Question();
		List<Answer> answers = new ArrayList<>();
		answers.add(answerMock);
		answers.add(answerMock);
		answers.add(answerMock);
		
		question.setAnswers(answers);
		
		assertEquals(answers, question.getAnswers());
	}
	
	@Test
	void test_question_setter_and_getter_notifications() {
		
		Question question = new Question();
		List<Notification> notifications = new ArrayList<>();
		notifications.add(notificationMock);
		notifications.add(notificationMock);
		notifications.add(notificationMock);
		
		question.setNotifications(notifications);
		
		assertEquals(notifications, question.getNotifications());
	}
	
	@Test
	void test_question_toString() {
		
		Question question = new Question();
		
		assertEquals("Question [questionId=null, questionContent=null, approved=false, reviewed=false, categoryTag=null, formatTag=null, contentTypeTag=null, user=null]", question.toString());
	}
	
	@Test
	void test_question_constructor() {
		
		User user = new User();
		Question question = new Question("test question", false, true, CategoryTag.COURSE_CONTENT, FormatTag.SHORT_ANSWER, ContentTypeTag.PRO_SKILLS, user);
		
		question.toString();
		
		assertEquals("Question [questionId=null, questionContent=test question, approved=false, reviewed=true, categoryTag=Course Content, formatTag=Short Answer, contentTypeTag=Pro Skills, user=User [userId=0, name=null, email=null, userRole=null]]", question.toString());
		
	}
	
	//////////////////////TEST Quiz Entity///////////////////////
	
	@Test
	void test_quiz_setter_and_getter_quizId() {
		
		Quiz quiz = new Quiz();
		quiz.setQuizID(1);
		
		assertEquals(1, quiz.getQuizID());
	}
	
	@Test
	void test_quiz_setter_and_getter_quiz_name() {
		
		Quiz quiz = new Quiz();
		
		quiz.setQuizName("QuizTest");
		
		assertEquals("QuizTest", quiz.getQuizName());
	}
	
	@Test
	void test_quiz_setter_and_getter_quiz_formatTag() {
		
		Quiz quiz = new Quiz();
		quiz.setQuizType(CategoryTag.COURSE_CONTENT);
		
		assertEquals(CategoryTag.COURSE_CONTENT, quiz.getQuizType());
	}
	
	@Test
	void test_quiz_setter_and_getter_user() {
		
		Quiz quiz = new Quiz();
		quiz.setUser(userMock);
		
		assertEquals(userMock.getUserId(), quiz.getUser().getUserId());
	}
	
	@Test
	void test_quiz_setter_and_getter_quiz_questions() {
		
		Question question = new Question();
		
		List<Question> questions = new ArrayList<>();
		questions.add(question);
		
		Quiz quiz = new Quiz();
		
		quiz.setQuestions(questions);
		
		assertEquals(questions, quiz.getQuestions());
	}
	
	@Test
	void test_quiz_setter_and_getter_allowed_time() {
		
		Quiz quiz = new Quiz();
		quiz.setAllowedTime(1);
		
		assertEquals(1, quiz.getAllowedTime());
	}
	
	@Test
	void test_quiz_setter_and_getter_reviewed() {
		
		Quiz quiz = new Quiz();
		quiz.setReviewed(true);
		
		assertEquals(true, quiz.isReviewed());
	}
	
	@Test
	void test_quiz_setter_and_getter_approved() {
		
		Quiz quiz = new Quiz();
		quiz.setApproved(true);
		
		assertEquals(true, quiz.isApproved());
	}
	
	@Test
	void test_quiz_setter_and_getter_number_of_questions() {
		
		Quiz quiz = new Quiz();
		
		List<Question> questions = new ArrayList<>(Arrays.asList(questionMock));
		quiz.setQuestions(questions);
		quiz.setNumberOfQuestions(quiz.getNumberOfQuestions());
		
		assertEquals(1, quiz.getNumberOfQuestions());
	}
	
	@Test
	void test_quiz_setter_and_getter_total_possible_marks() {
		
		Quiz quiz = new Quiz();
		Question question = new Question();		
		List<Answer> answers = new ArrayList<>(Arrays.asList(answerMock));
		
		question.setFormatTag(FormatTag.SHORT_ANSWER);
		question.setAnswers(answers);
		
		List<Question> questions = new ArrayList<>(Arrays.asList(question));
		quiz.setQuestions(questions);
		quiz.setTotalposmarks(quiz.getTotalposmarks());
		
		assertEquals(1, quiz.getTotalposmarks());
	}
	
	@Test
	void test_quiz_constructor_and_toString() {
		
		User user = new User();
		
		Question question = new Question();
		
		List<Question> questions = new ArrayList<>();
		questions.add(questionMock);
		
		Quiz quiz = new Quiz("QuizTest", CategoryTag.COURSE_CONTENT, userMock, questions, 0, true);

		assertEquals("Quiz [quizID=0, quizName=QuizTest, quizType=Course Content, user=userMock, questions=[questionMock], allowedTime=0, reviewed=true]", quiz.toString());
		
	}
	
	
	//////////////////////TEST Answer Entity///////////////////////
	
	@Test
	void test_answer_setter_and_getter_answerId() {
		
		Answer answer = new Answer();
		answer.setAnswerId(1);
		
		assertEquals(1, answer.getAnswerId());
	}
	
	@Test
	void test_answer_setter_and_getter_answerContent() {
		
		Answer answer = new Answer();
		answer.setAnswer("Answer Content");
		
		assertEquals("Answer Content", answer.getAnswer());
	}
	
	@Test
	void test_answer_setter_and_getter_question() {
		
		Question question = new Question();
		Answer answer = new Answer();
		answer.setQuestion(question);
		
		assertEquals(question, answer.getQuestion());
	}
	
	@Test
	void test_answer_setter_and_getter_boolCorrect() {
		
		Answer answer = new Answer();
		answer.setCorrect(true);
		
		assertEquals(true, answer.isCorrect());
	}
	
	@Test
	void test_answer_toString() {
		
		Answer answer = new Answer();
		
		assertEquals(answer.toString(), "Answer [answerId=null, answer=null, correct=false, question=null]");
	}
	
	@Test
	void test_answer_constructor1() {
		
		Question question = new Question();
		Answer answer = new Answer("test answer", question);
		
		assertEquals(answer.toString(), "Answer [answerId=null, answer=test answer, correct=false, question=Question [questionId=null, questionContent=null, approved=false, reviewed=false, categoryTag=null, formatTag=null, contentTypeTag=null, user=null]]");
	}
	
	@Test
	void test_answer_constructor2() {
		
		Question question = new Question();
		Answer answer = new Answer("test answer", true, question);
		
		assertEquals(answer.toString(), "Answer [answerId=null, answer=test answer, correct=true, question=Question [questionId=null, questionContent=null, approved=false, reviewed=false, categoryTag=null, formatTag=null, contentTypeTag=null, user=null]]");
	}
	
	//////////////////////TEST Notification Entity///////////////////////
	
	@Test
	void test_notification_setter_and_getter_notificationId() {
		
		Notification notification = new Notification();
		notification.setNotificationID(1);
		
		assertEquals(1, notification.getNotificationID());
	}
	
	@Test
	void test_notification_setter_and_getter_message() {
		
		Notification notification = new Notification();
		notification.setMessage("test message");
		
		assertEquals("test message", notification.getMessage());
	}
	
	@Test
	void test_notification_setter_and_getter_oldInfo() {
		
		Notification notification = new Notification();
		notification.setOldInfo("test old info");
		
		assertEquals("test old info", notification.getOldInfo());
	}
	
	@Test
	void test_notification_setter_and_getter_newInfo() {
		
		Notification notification = new Notification();
		notification.setNewInfo("test new info");
		
		assertEquals("test new info", notification.getNewInfo());
	}
	
	@Test
	void test_notification_setter_and_getter_notification_question() {
		
		Question question = new Question();
		Notification notification = new Notification();
		notification.setQuestionID(question);
		
		assertEquals(question, notification.getQuestionID());
	}
	
	@Test
	void test_notification_setter_and_getter_notification_user() {
		
		User user = new User();
		Notification notification = new Notification();
		notification.setUserID(user);
		
		assertEquals(user, notification.getUser());
	}
	
	@Test
	void test_notification_setter_and_getter_notification_editor() {
		
		Notification notification = new Notification();
		notification.setEditor("test editor");
		
		assertEquals("test editor", notification.getEditor());
	}
	
	@Test
	void test_notification_setter_and_getter_quiz() {
		
		Quiz quiz = new Quiz();
		Notification notification = new Notification();
		notification.setQuizID(quiz);
		
		assertEquals(quiz, notification.getQuizID());
	}
	
	@Test
	void test_notification_toString() {
		
		Question question = new Question();
		Quiz quiz = new Quiz();
		User user = new User();
		
		Notification notification = new Notification();
		notification.setQuestionID(question);
		notification.setQuizID(quiz);
		notification.setUserID(user);
		
		assertEquals("Notification [notificationID=0, message=null, questionID=null, quizID=0, userID=0]", notification.toString());
	}
	
	@Test
	void test_notification_constructor() {
		
		User user = new User();
		Question question = new Question();
		Quiz quiz = new Quiz();
		
		Notification notification = new Notification("test message", question, quiz, user);
		
		assertEquals(notification.getMessage(), "test message");
	}
	
	
	//////////////////////TEST Submission Entity///////////////////////
	
	@Test
	void test_submission_setter_and_getter_submissionId() {
		
		Submission submission = new Submission();
		submission.setSubmissionID(1);
		
		assertEquals(1, submission.getSubmissionID());
	}
	
	@Test
	void test_submission_setter_and_getter_quizId() {
		
		Quiz quiz = new Quiz();
		Submission submission = new Submission();
		submission.setQuiz(quiz);
		
		assertEquals(quiz, submission.getQuiz());
	}
	
	@Test
	void test_submission_setter_and_getter_userId() {
		
		User user = new User();
		Submission submission = new Submission();
		submission.setUserId(user);
		
		assertEquals(user, submission.getUserId());
	}
	
	@Test
	void test_submission_setter_and_getter_visability() {
		
		Submission submission = new Submission();
		submission.setVisability(1);
		
		assertEquals(1, submission.getVisability());
	}
	
	@Test
	void test_submission_setter_and_getter_question_attempts() {
		
		Submission submission = new Submission();
		List<QuestionAttempt> questionAttempts = Arrays.asList(questionAttemptMock);
		submission.setQuestionAttempt(questionAttempts);
		
		assertEquals(questionAttempts, submission.getQuestionAttempt());
	}
	
	@Test
	void test_submission_setter_and_getter_mark() {
		
		Submission submission = new Submission();
		submission.setMark(1);
		
		assertEquals(1, submission.getMark());
	}
	
	@Test
	void test_submission_constructor_and_toString() {

		List<QuestionAttempt> questionAttempts = Arrays.asList(questionAttemptMock);
		
		Submission submission = new Submission(quizMock, userMock, 1, questionAttempts, 2);
		
		assertEquals("Submission [submissionID=null, quiz=quizMock, userId=userMock, visability=1, questionAttempt=[questionAttemptMock], mark=2]", submission.toString());
	}
	
	@Test
	void test_submission_hashCode() {
		
		Submission submission = new Submission();
		
		assertEquals(Objects.hash(submission.getSubmissionID()), submission.hashCode());
	}
	
	@Test
	void test_submission_bool_equals() {
		
		Submission submission = new Submission();
		Submission submission1 = new Submission();
		
		assertTrue(submission.equals(submission));
		assertTrue(submission.equals(submission1));
		assertFalse(submission.equals(null));
	}
	
	//////////////////////TEST QuestionAttempt Entity///////////////////////
		
	@Test
	void test_question_attempt_setter_and_getter_questionAttemptId() {
		
		questionAttempt = new QuestionAttempt();
		questionAttempt.setQuestionAttemptID(1);
		
		assertEquals(1, questionAttempt.getQuestionAttemptID());
	}
	
	@Test
	void test_question_attempt_setter_and_getter_submission() {
		
		questionAttempt = new QuestionAttempt();
		questionAttempt.setSubmission(submissionMock);;
		
		assertEquals(submissionMock, questionAttempt.getSubmission());
	}
	
	@Test
	void test_question_attempt_setter_and_getter_question() {
	
		questionAttempt = new QuestionAttempt();
		questionAttempt.setQuestion(questionMock);;
		
		assertEquals(questionMock, questionAttempt.getQuestion());
	}
	
	@Test
	void test_question_attempt_setter_and_getter_answer() {
	
		questionAttempt = new QuestionAttempt();
		questionAttempt.setAnswer(answerMock);;
		
		assertEquals(answerMock, questionAttempt.getAnswer());
	}
	
	@Test
	void test_question_attempt_setter_and_getter_selection() {
	
		questionAttempt = new QuestionAttempt();
		questionAttempt.setSelection(true);;
		
		assertEquals(true, questionAttempt.isSelection());
	}
	
	@Test
	void test_question_attempt_setter_and_getter_short_answer_attempt() {
	
		questionAttempt = new QuestionAttempt();
		questionAttempt.setShortAnswerAttempt("Short Answer Attempt Test");
		
		assertEquals("Short Answer Attempt Test", questionAttempt.getShortAnswerAttempt());
	}
	
	@Test
	void test_question_attempt_setter_and_getter_correct_status() {
	
		questionAttempt = new QuestionAttempt();
		questionAttempt.setCorrect(false);
		
		assertEquals(false, questionAttempt.isCorrect());
	}
	
	@Test
	void test_question_attempt_constructor_and_toString() {
		
		questionAttempt = new QuestionAttempt(submissionMock, questionMock, answerMock, true, "Short Answer Attempt Test", false);
		
		String toStringTest =  "QuestionAttempt [questionAttemptID=null, submission=submissionMock, question=questionMock, answer=answerMock, selection=true, shortAnswerAttempt=Short Answer Attempt Test]";
		
		assertEquals(toStringTest, questionAttempt.toString());
	}
	
	//////////////////////TEST UserTemp Object///////////////////////
	
	@Test
	void test_userTemp_setter_and_getter_userId() {
		
		UserTemp userTemp = new UserTemp();
		
		userTemp.setUserId(0);
		
		int userId = userTemp.getUserId();
		assertEquals(0, userId);
	}
	
	@Test
	void test_userTemp_setter_and_getter_email() {
		
		UserTemp userTemp = new UserTemp();
		
		userTemp.setEmail("email@gmail.com");
		
		String userEmail = userTemp.getEmail();
		assertEquals("email@gmail.com", userEmail);
	}
	
	@Test
	void test_userTemp_setter_and_getter_name() {
		
		UserTemp userTemp = new UserTemp();
		
		userTemp.setName("Test Name");
		
		String userName = userTemp.getName();
		assertEquals("Test Name", userName);
	}
	
	@Test
	void test_userTemp_setter_and_getter_password() {
		
		UserTemp userTemp = new UserTemp();
		
		userTemp.setPassword("password");
		
		String userPassword = userTemp.getPassword();
		assertEquals("password", userPassword);
	}
	
	@Test
	void test_userTemp_setter_and_getter_confirm_password() {
		
		UserTemp userTemp = new UserTemp();
		
		userTemp.setConfirmPassword("confirm");
		
		String userConfirmPassword = userTemp.getConfirmPassword();
		assertEquals("confirm", userConfirmPassword);
	}
		
	@Test
	void test_userTemp_toString() {
		
		UserTemp userTemp = new UserTemp("test name", "email@gmail.com", "password");
		
		String toStringTest =  "User [userId=0, name=test name, email=email@gmail.com, password=password]";
		
		assertEquals(toStringTest, userTemp.toString());
	}

	//////////////////////TEST TempQuestion Object///////////////////////
	
	@Test
	void test_tempQuestion_setter_and_getter_questionId() {
		
		TempQuestion tempQuestion = new TempQuestion();
		
		tempQuestion.setQuestionId(0);
		
		int questionId = tempQuestion.getQuestionId();
		assertEquals(0, questionId);
	}
	
	@Test
	void test_tempQuestion_setter_and_getter_questionContent() {
		
		TempQuestion tempQuestion = new TempQuestion();
		
		tempQuestion.setQuestionContent("question");
		
		String questionContent = tempQuestion.getQuestionContent();
		assertEquals("question", questionContent);
	}
	
	@Test
	void test_tempQuestion_setter_and_getter_approved_status() {
		
		TempQuestion tempQuestion = new TempQuestion();
		
		tempQuestion.setApproved(true);
		
		Boolean questionApproved = tempQuestion.isApproved();
		assertEquals(true, questionApproved);
	}
	
	@Test
	void test_tempQuestion_setter_and_getter_reviewed_status() {
		
		TempQuestion tempQuestion = new TempQuestion();
		
		tempQuestion.setReviewed(false);
		
		Boolean questionReviewed = tempQuestion.isReviewed();
		assertEquals(false, questionReviewed);
	}
	
	@Test
	void test_tempQuestion_setter_and_getter_category_tag() {
		
		TempQuestion tempQuestion = new TempQuestion();
		
		tempQuestion.setCategoryTag(CategoryTag.COURSE_CONTENT);
		
		CategoryTag questionCategory = tempQuestion.getCategoryTag();
		assertEquals(CategoryTag.COURSE_CONTENT, questionCategory);
	}
		
	@Test
	void test_tempQuestion_setter_and_getter_format_tag() {
		
		TempQuestion tempQuestion = new TempQuestion();
		
		tempQuestion.setFormatTag(FormatTag.MULTI_CHOICE);
		
		FormatTag questionFormat = tempQuestion.getFormatTag();
		assertEquals(FormatTag.MULTI_CHOICE, questionFormat);
	}
	
	@Test
	void test_tempQuestion_setter_and_getter_content_type_tag() {
		
		TempQuestion tempQuestion = new TempQuestion();
		
		tempQuestion.setContentTypeTag(ContentTypeTag.SQL);
		
		ContentTypeTag questionContentType = tempQuestion.getContentTypeTag();
		assertEquals(ContentTypeTag.SQL, questionContentType);
	}
	
	@Test
	void test_tempQuestion_setter_and_getter_user() {
		
		TempQuestion tempQuestion = new TempQuestion();
		User user = new User();
		
		tempQuestion.setUser(user);
		
		User questionUser = tempQuestion.getUser();
		assertEquals(user, questionUser);
	}
	
	@Test
	void test_tempQuestion_setter_and_getter_answers() {
		
		TempQuestion tempQuestion = new TempQuestion();
		ArrayList<String> correctAnswers = new ArrayList<String>();
		String answer = "answer";
		
		correctAnswers.add(answer);
		correctAnswers.add(answer);
		correctAnswers.add(answer);
		
		tempQuestion.setAnswers(correctAnswers);
		
		assertEquals(3, tempQuestion.getAnswers().size());
		assertEquals(correctAnswers, tempQuestion.getAnswers());
	}
	
	@Test
	void test_tempQuestion_setter_and_getter_correct_answers() {
		
		TempQuestion tempQuestion = new TempQuestion();
		ArrayList<String> correctAnswers = new ArrayList<String>();

		correctAnswers.add("1");
		correctAnswers.add("2");
		correctAnswers.add("3");
		
		tempQuestion.setCorrectAnswers(correctAnswers);
		
		assertEquals(3, tempQuestion.getCorrectAnswers().size());
		assertEquals(correctAnswers, tempQuestion.getCorrectAnswers());
	}
	
	@Test
	void test_tempQuestion_setter_and_getter_new_answers() {
		
		TempQuestion tempQuestion = new TempQuestion();
		ArrayList<Answer> newAnswers = new ArrayList<Answer>();
		Answer answer = new Answer();
		
		newAnswers.add(answer);
		newAnswers.add(answer);
		newAnswers.add(answer);
		
		tempQuestion.setNewAnswers(newAnswers);
		
		assertEquals(3, tempQuestion.getNewAnswers().size());
		assertEquals(newAnswers, tempQuestion.getNewAnswers());
	}
	
	@Test
	void test_tempQuestion_toString() {
		User user = new User();
		TempQuestion tempQuestion = new TempQuestion(0, "question", true, false, CategoryTag.INTERVIEW_PREP, FormatTag.SHORT_ANSWER, ContentTypeTag.SQL, user);
		
		ArrayList<String> correctAnswers = new ArrayList<String>();

		correctAnswers.add("1");
		correctAnswers.add("2");
		correctAnswers.add("3");
		
		String toStringTest =  "TempQuestion [questionId=0, questionContent=question, approved=true, reviewed=false, categoryTag=Interview Preparation, formatTag=Short Answer, "
				+ "contentTypeTag=SQL, user="+ user + ", correctAnswers=null, answers=null, newAnswers=null]";
		
		assertEquals(toStringTest, tempQuestion.toString());
	}
	
	//////////////////////TEST TempQuiz Object///////////////////////
	
	@Test
	void test_temp_quiz_setter_and_getter_quizId() {
		
		tempQuiz = new TempQuiz();
		tempQuiz.setQuizID(1);
		
		assertEquals(1, tempQuiz.getQuizID());
	}
	
	@Test
	void test_temp_quiz_setter_and_getter_quiz_name() {
	
		tempQuiz = new TempQuiz();
		
		tempQuiz.setQuizName("QuizTest");
		
		assertEquals("QuizTest", tempQuiz.getQuizName());
	}
	
	@Test
	void test_temp_quiz_setter_and_getter_quiz_formatTag() {
	
		tempQuiz = new TempQuiz();
		tempQuiz.setQuizType(CategoryTag.COURSE_CONTENT);
		
		assertEquals(CategoryTag.COURSE_CONTENT, tempQuiz.getQuizType());
	}
	
	@Test
	void test_temp_quiz_setter_and_getter_user() {
	
		tempQuiz = new TempQuiz();
		tempQuiz.setUser(userMock);
		
		assertEquals(userMock.getUserId(), tempQuiz.getUser().getUserId());
	}
	
	@Test
	void test_temp_quiz_setter_and_getter_quiz_questions() {
	
		Question question = new Question();
		
		List<Question> questions = new ArrayList<>();
		questions.add(question);
		
		tempQuiz = new TempQuiz();
		
		tempQuiz.setQuestions(questions);
		
		assertEquals(questions, tempQuiz.getQuestions());
	}
	
	@Test
	void test_temp_quiz_setter_and_getter_allowed_time() {
		
		tempQuiz = new TempQuiz();
		tempQuiz.setAllowedTime(1);
		
		assertEquals(1, tempQuiz.getAllowedTime());
	}
	
	@Test
	void test_temp_quiz_setter_and_getter_reviewed() {
	
		tempQuiz = new TempQuiz();
		tempQuiz.setReviewed(true);
		
		assertEquals(true, tempQuiz.isReviewed());
	}
	
	@Test
	void test_temp_quiz_setter_and_getter_no_time_limit() {
	
		tempQuiz = new TempQuiz();
		tempQuiz.setNoTimeLimit(true);
		
		assertEquals(true, tempQuiz.isNoTimeLimit());
	}
	
	@Test
	void test_temp_quiz_setter_and_getter_content_type_tags() {
	
		tempQuiz = new TempQuiz();
		List<ContentTypeTag> contentTypeTags = new ArrayList<>(Arrays.asList(ContentTypeTag.SQL, ContentTypeTag.JAVA_EE_WEB));
		tempQuiz.setSelectContents(contentTypeTags);
		
		assertEquals(contentTypeTags, tempQuiz.getSelectContents());
	}
	
	@Test
	void test_temp_quiz_setter_and_getter_format_tags() {
	
		tempQuiz = new TempQuiz();
		List<FormatTag> formatTags = new ArrayList<>(Arrays.asList(FormatTag.MULTI_ANSWER, FormatTag.MULTI_CHOICE));
		tempQuiz.setSelectFormats(formatTags);
		
		assertEquals(formatTags, tempQuiz.getSelectFormats());
	}
	
	@Test
	void test_temp_quiz_setter_and_getter_category_tags() {
	
		tempQuiz = new TempQuiz();
		List<CategoryTag> categoryTags = new ArrayList<>(Arrays.asList(CategoryTag.COURSE_CONTENT));
		tempQuiz.setQuizCategories(categoryTags);
		
		assertEquals(categoryTags, tempQuiz.getQuizCategories());
	}
	
	@Test
	void test_temp_quiz_constructor_and_toString() {
	
		List<Question> questions = new ArrayList<>();
		questions.add(questionMock);
		List<ContentTypeTag> contentTypeTags = new ArrayList<>(Arrays.asList(ContentTypeTag.SQL, ContentTypeTag.JAVA_EE_WEB));
		List<FormatTag> formatTags = new ArrayList<>(Arrays.asList(FormatTag.MULTI_ANSWER, FormatTag.MULTI_CHOICE));
		List<CategoryTag> categoryTags = new ArrayList<>(Arrays.asList(CategoryTag.COURSE_CONTENT));
		
		tempQuiz = new TempQuiz();
		tempQuiz.setQuizName("QuizTest");
		tempQuiz.setQuizType(CategoryTag.COURSE_CONTENT);
		tempQuiz.setUser(userMock);
		tempQuiz.setQuestions(questions);
		tempQuiz.setAllowedTime(0);
		tempQuiz.setReviewed(false);
		tempQuiz.setSelectContents(contentTypeTags);
		tempQuiz.setSelectFormats(formatTags);
		tempQuiz.setQuizCategories(categoryTags);
		
		assertEquals("TempQuiz [quizName=QuizTest, quizType=Course Content, user=userMock, questions=[questionMock], allowedTime=0, reviewed=false, " +
		"selectContents=[SQL, Java EE Web], selectFormats=[Multiple Answer, Multiple Choice], quizCategories=[Course Content]]", tempQuiz.toString());
	
	}
	
	//////////////////////TEST TempSubmission Object///////////////////////
	
	@Test
	void test_temp_submission_setter_and_getter_submission() {
	
		tempSubmission = new TempSubmission();
		tempSubmission.setSubmission(submissionMock);
		
		assertEquals(submissionMock, tempSubmission.getSubmission());
	}
	
	@Test
	void test_temp_submission_setter_and_getter_quiz() {
	
		tempSubmission = new TempSubmission();
		tempSubmission.setQuiz(quizMock);
		
		assertEquals(quizMock, tempSubmission.getQuiz());
	}
	
	@Test
	void test_temp_submission_setter_and_getter_short_answer_question_attempts() {
	
		TempSubmission tempSubmission = new TempSubmission();
		List<QuestionAttempt> questionAttempts = Arrays.asList(questionAttemptMock);
		tempSubmission.setShortAnswerAttempts(questionAttempts);
		
		assertEquals(questionAttempts, tempSubmission.getShortAnswerAttempts());
	}
	
	@Test
	void test_temp_submission_setter_and_getter_user_selection_map() {
	
		TempSubmission tempSubmission = new TempSubmission();
		HashMap<Integer, Boolean> userSelections = new HashMap<>();
		userSelections.put(1, true);
		userSelections.put(2, false);
		
		tempSubmission.setUserSelectionMap(userSelections);
		
		assertEquals(userSelections, tempSubmission.getUserSelectionMap());
	}
	
	@Test
	void test_temp_submission_setter_and_getter_user_short_answer_map() {
	
		TempSubmission tempSubmission = new TempSubmission();
		HashMap<Integer, String> userShortAnswers = new HashMap<>();
		userShortAnswers.put(1, "Details about myself");
		userShortAnswers.put(2, "I love this job");
		
		tempSubmission.setUserShortAnswerMap(userShortAnswers);
		
		assertEquals(userShortAnswers, tempSubmission.getUserShortAnswerMap());
	}
	
	@Test
	void test_temp_submission_constructor_and_toString() {
	
		HashMap<Integer, Boolean> userSelections = new HashMap<>();
		userSelections.put(1, true);
		userSelections.put(2, false);
		
		HashMap<Integer, String> userShortAnswers = new HashMap<>();
		userShortAnswers.put(1, "Details about myself");
		userShortAnswers.put(2, "I love this job");
		
		TempSubmission tempSubmission = new TempSubmission(userSelections, userShortAnswers, quizMock);
		
		assertEquals("TempSubmission [userSelections={1=true, 2=false}, userShortAnswer={1=Details about myself, 2=I love this job}]", tempSubmission.toString());
	}
	

}
