package com.spring.web;


import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.spring.web.controller.RegisterController;
import com.spring.web.model.User;
import com.spring.web.model.UserTemp;
import com.spring.web.model.EntityEnum.UserRole;
import com.spring.web.service.NotificationService;
import com.spring.web.service.UserService;



@WebMvcTest(RegisterController.class)
@AutoConfigureMockMvc(addFilters = false)
class RegisterControllerTest {
	
	@MockBean
	private UserService userService;
	
	@MockBean
	private NotificationService notificationService;
	
	
	@Autowired
	private MockMvc mockMvc;
	
	@BeforeEach
	void setup() {
		
	}
	
	@Test
	void test_register_with_existing_email() throws Exception {
		
		User user = new User("test name", "test email", "test password", UserRole.TRAINING);
		UserTemp userTemp = new UserTemp("test name", "test email", "test password");
		userTemp.setConfirmPassword("test password");
		
		when(userService.getUserByEmail(userTemp.getEmail())).thenReturn(user);
		
		this.mockMvc.perform(MockMvcRequestBuilders.post("/registerUserObject").sessionAttr("id", user.getUserId()).flashAttr("tempuser", userTemp))
        .andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.content().string(containsString("email already exists")));
	}
	
	@Test
	void test_register_with_wrong_confirm_password() throws Exception {
		
		User user = new User("test name", "test email", "test password", UserRole.TRAINING);
		UserTemp userTemp = new UserTemp("test name", "test email", "test password");
		userTemp.setConfirmPassword("test password1");
		
		when(userService.getUserByEmail(userTemp.getEmail())).thenReturn(null);
		
		this.mockMvc.perform(MockMvcRequestBuilders.post("/registerUserObject").sessionAttr("id", user.getUserId()).flashAttr("tempuser", userTemp))
        .andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.content().string(containsString("Passwords dont match")));
	}
	
	@Test
	void test_register_with_correct_confirm_password() throws Exception {
		
		User user = new User("test name", "test email", "test password", UserRole.TRAINING);
		UserTemp userTemp = new UserTemp("test name", "test email", "test password");
		userTemp.setConfirmPassword("test password");
		
		when(userService.getUserByEmail(userTemp.getEmail())).thenReturn(null);
		
		this.mockMvc.perform(MockMvcRequestBuilders.post("/registerUserObject").sessionAttr("id", user.getUserId()).flashAttr("tempuser", userTemp))
        .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}
	
	@Test
	void test_user_attempt_registerUserObject_by_GET_REQUEST() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/registerUserObject"))
        .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}
	
}
