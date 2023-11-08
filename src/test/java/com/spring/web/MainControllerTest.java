package com.spring.web;


import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.spring.web.controller.MainController;
import com.spring.web.model.Notification;
import com.spring.web.model.User;
import com.spring.web.model.EntityEnum.UserRole;
import com.spring.web.service.AnswerService;
import com.spring.web.service.NotificationService;
import com.spring.web.service.QuestionService;
import com.spring.web.service.QuizService;
import com.spring.web.service.SubmissionService;
import com.spring.web.service.UserService;

import jakarta.servlet.http.HttpSession;



@WebMvcTest(MainController.class)
@AutoConfigureMockMvc(addFilters = false)
//@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT) 
class MainControllerTest {
	
	@MockBean
	private UserService userService;
	
	@MockBean
	private NotificationService notificationService;
	
	@MockBean
	private AnswerService answerService;
	
	@MockBean
	private QuizService quizService;
	
	@MockBean
	private QuestionService questionService;
	
	@MockBean
	private SubmissionService submissionService;
	
	@Autowired
	private MockMvc mockMvc;
	
    HttpSession session;
    
    @Mock
    HttpSession mockSession;
	
	@BeforeEach
	void setup() {
		session = new MockHttpSession();
	}
	
	
	
	@Test
	void toIndex_test() throws Exception {
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/"))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
		
	}
	
	
	@Test
	void IndexBasicSuccessful_test() throws Exception {
		
		User user = new User("test name", "test email", "test password", UserRole.TRAINING);
		when(userService.getUserById(user.getUserId())).thenReturn(user);

		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/index").sessionAttr("id", user.getUserId()))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attribute("user", user));
	}
	
	@Test
	void IndexBasicFail_test() throws Exception {
		
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/index"))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}
	
	
	@Test
	void getAllStudents0_test() throws Exception {
		
		List<User> students = new ArrayList<User>();
		
		when(userService.getAllStudents()).thenReturn(students);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/getAllStudents"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json("[]"));
	}
	
	
	@Test
	void getAllStudents3_test() throws Exception {
		
		User user1 = new User("test name1", "test email", "test password", UserRole.TRAINING);
		User user2 = new User("test name2", "test email", "test password", UserRole.POND);
		User user3 = new User("test name3", "test email", "test password", UserRole.BEACHED);
		
		List<User> students = new ArrayList<User>();
		students.add(user1);
		students.add(user2);
		students.add(user3);
		
		when(userService.getAllStudents()).thenReturn(students);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/getAllStudents"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json("[\"test name1 : test email\", \"test name2 : test email\", \"test name3 : test email\"]"));
	} 
	
	@Test
	void searchForStudent_test() throws Exception {
		
		User user1 = new User("test name1", "test email", "test password", UserRole.TRAINING);
		User user2 = new User("test name2", "test email", "test password", UserRole.POND);
		User user3 = new User("test name3", "test email", "test password", UserRole.BEACHED);
		
		List<User> students = new ArrayList<User>();
		students.add(user1);
		students.add(user2);
		students.add(user3);
		
		User user4 = new User("test", "test email", "test password", UserRole.BEACHED);

		when(userService.getAllStudentswithNameorEmail("test")).thenReturn(students);
		when(userService.getUserById(user4.getUserId())).thenReturn(user4);


		this.mockMvc.perform(MockMvcRequestBuilders.post("/searchForStudents").sessionAttr("id", user4.getUserId()).flashAttr("forSearchStudents", user4))
        .andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attribute("user", user4));
	}
	
	
	@Test
	void editStudentSuccessfulAsTrainer_test() throws Exception {
		
		List<UserRole> roles = new ArrayList<UserRole>();
		
		roles.add(UserRole.BEACHED);
		roles.add(UserRole.ABSENT);
		roles.add(UserRole.POND);
		roles.add(UserRole.TRAINING);
		
		User user4 = new User("test", "test email", "test password", UserRole.TRAINER);
		when(userService.getUserById(user4.getUserId())).thenReturn(user4);


		this.mockMvc.perform(MockMvcRequestBuilders.get("/editStudent?id="+user4.getUserId()).sessionAttr("id", user4.getUserId()))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.model().attribute("userStudent", user4))
        .andExpect(MockMvcResultMatchers.model().attribute("roles", roles));
	}
	
	@Test
	void editStudentSuccessfulAsSales_test() throws Exception {
		
		List<UserRole> roles = new ArrayList<UserRole>();
		
		roles.add(UserRole.BEACHED);
		roles.add(UserRole.ABSENT);
		roles.add(UserRole.POND);
		roles.add(UserRole.TRAINING);
		
		User user4 = new User("test", "test email", "test password", UserRole.SALES);
		when(userService.getUserById(user4.getUserId())).thenReturn(user4);


		this.mockMvc.perform(MockMvcRequestBuilders.get("/editStudent?id="+user4.getUserId()).sessionAttr("id", user4.getUserId()))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.model().attribute("userStudent", user4))
        .andExpect(MockMvcResultMatchers.model().attribute("roles", roles));
	}
	
	@Test
	void editStudentFailureAsStudent_test() throws Exception {
		
		List<UserRole> roles = new ArrayList<UserRole>();
		
		roles.add(UserRole.BEACHED);
		roles.add(UserRole.ABSENT);
		roles.add(UserRole.POND);
		roles.add(UserRole.TRAINING);
		
		User user4 = new User("test", "test email", "test password", UserRole.POND);
		when(userService.getUserById(user4.getUserId())).thenReturn(user4);


		this.mockMvc.perform(MockMvcRequestBuilders.get("/editStudent?id="+user4.getUserId()).sessionAttr("id", user4.getUserId()))
        .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}
	
	
	@Test
	void editStudentFailureNoSessionAttribute_test() throws Exception {
		
		List<UserRole> roles = new ArrayList<UserRole>();
		
		roles.add(UserRole.BEACHED);
		roles.add(UserRole.ABSENT);
		roles.add(UserRole.POND);
		roles.add(UserRole.TRAINING);
		
		User user4 = new User("test", "test email", "test password", UserRole.POND);
		when(userService.getUserById(user4.getUserId())).thenReturn(user4);


		this.mockMvc.perform(MockMvcRequestBuilders.get("/editStudent?id="+user4.getUserId()))
        .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}
	
	@Test
	void deleteNoteBothTrue_test() throws Exception {
		
		User user4 = new User("test", "test email", "test password", UserRole.BEACHED);
		Notification notification = new Notification();
		notification.setUserID(user4);
		
		when(userService.getUserById(user4.getUserId())).thenReturn(user4);
		when(notificationService.getNotificationById(notification.getNotificationID())).thenReturn(notification);


		this.mockMvc.perform(MockMvcRequestBuilders.get("/deleteNote?id="+notification.getNotificationID()).param("referer", "/index").sessionAttr("id", user4.getUserId()))
        .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
		
		//not checking session attribute added
	}
	
	
	@Test
	void deleteNoteBothFalse_test() throws Exception {
		
		User user4 = new User("test", "test email", "test password", UserRole.BEACHED);
		User user1 = new User("test", "test email", "test password", UserRole.BEACHED);

		Notification notification1 = new Notification();
		Notification notification2 = new Notification();

		notification1.setUserID(user4);
		notification2.setUserID(user1);
		


		this.mockMvc.perform(MockMvcRequestBuilders.get("/deleteNote?id="+notification1.getNotificationID()).param("referer", "/index"))
        .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
		
		//not checking session attribute added
	}
	
	@Test
	void deleteNoteSecondFalse_test() throws Exception {
		
		User user4 = new User("test4", "test email", "test password", UserRole.BEACHED);
		user4.setUserId(1);
		User user3 = new User("test3", "test email", "test password", UserRole.BEACHED);
		user3.setUserId(2);

		Notification notification1 = new Notification();
		notification1.setUserID(user3);
		
		when(userService.getUserById(user4.getUserId())).thenReturn(user4);
		when(notificationService.getNotificationById(notification1.getNotificationID())).thenReturn(notification1);


		this.mockMvc.perform(MockMvcRequestBuilders.get("/deleteNote?id="+notification1.getNotificationID()).param("referer", "/index").sessionAttr("id", user4.getUserId()))
        .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
		
		//not checking session attribute added
	}
	
	
	@Test
	void missingServlet_test() throws Exception {


		this.mockMvc.perform(MockMvcRequestBuilders.get("/deleteNote"))
        .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
		
		//not checking session attribute added
	}
	
	
	
	

}
