package com.spring.web;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.spring.web.controller.LoginController;
import com.spring.web.model.User;
import com.spring.web.model.UserTemp;
import com.spring.web.model.EntityEnum.UserRole;
import com.spring.web.service.UserService;

import jakarta.servlet.http.HttpSession;

//@WebMvcTest(value = LoginController.class)
//@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class LoginControllerTests { 

	@Value(value="${local.server.port}")
	private int port;
	
	@Autowired
	private LoginController loginController;
	
	@Autowired
	private UserService userService;
	
//	@Autowired
//	private MockMvc mockMvc;
	
	@Mock
    Model model;
    
    HttpSession session;
    
    @Mock
    HttpSession mockSession;
	
	@BeforeEach
	void setup() {
		
		userService.deleteAllUsers();
		session = new MockHttpSession();
		
	}
	
	/**
	 * Tests that if there is a valid id attached to the HttpSession
	 * and asserts that the user is redirected to the index
	 */
	@Test
	void test_UserWithValidSessionId_RedirectedToIndex() {
		
		String redirectString;

		when(mockSession.getAttribute("id")).thenReturn(101);
		
		redirectString = loginController.getLogin(model, mockSession);
		
		assertEquals("redirect:/index", redirectString);
		
	}
	
	/**
	 * Test case to redirect user to login screen if they attempt to
	 * access index without a valid ID in the session
	 */
	@Test
	void test_UserWithNoSessionId_RedirectedToIndex() {
		
		String redirectString;

		when(mockSession.getAttribute("id")).thenReturn(null);
		
		redirectString = loginController.getLogin(model, mockSession);
		
		assertEquals("login", redirectString);
		
	}
	
	/**
	 * Test case to check user is redirected to index after
	 * successful login
	 */
	@Test
	void test_UserWithValidDetails_SuccessfulLogin() {
		
		String redirectString;
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String password = passwordEncoder.encode("something");
		
		User user = new User("Test", "test@gmail.com", password, UserRole.TRAINING);
		userService.saveOrUpdate(user);
		
		redirectString = loginController.postLogin(new UserTemp("Test", "test@gmail.com", "something"), model, session);
		
		assertEquals("redirect:/index", redirectString);
		
	}
	
	/**
	 * Test case to check user is redirected to login screen
	 * if login attempt is unsuccessful with invalid password
	 */
	@Test
	void test_UserWithInvalidPassword_RedirectedToLogin() {
		
		String redirectString;
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String password = passwordEncoder.encode("1something1");
		
		User user = new User("Test", "test1@gmail.com", password, UserRole.TRAINING);
		userService.saveOrUpdate(user);
		
		redirectString = loginController.postLogin(new UserTemp("Test", "test1@gmail.com", "something"), model, session);
		
		assertEquals("login", redirectString);
		
	}
	
	/**
	 * Test case to check user is redirected to login screen
	 * if login attempt is unsuccessful with invalid email
	 */
	@Test
	void test_UserWithInvalidEmail_RedirectedToLogin() {
		
		String redirectString;
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String password = passwordEncoder.encode("something");
		
		User user = new User("Test", "test2@gmail.com", password, UserRole.TRAINING);
		userService.saveOrUpdate(user);
		
		redirectString = loginController.postLogin(new UserTemp("Test", "test3@gmail.com", "something"), model, session);
		
		assertEquals("login", redirectString);
		
	}
	
	/**
	 * Test case to check that the session is correctly
	 * invalidated after using the exit method
	 */
	@Test
	void test_LoginController_ExitMapping() {
		
		String redirectString;
		
		redirectString = loginController.ExitSpring(session);
		
		assertThrows(IllegalStateException.class, () -> session.invalidate());
		assertEquals("redirect:/login", redirectString);
		
	}
	
	/**
	 * Test case to check that user is redirected to
	 * changepass form after successful login
	 */
	@Test
	void test_ChangePasswordOnLoginSuccessfulRedirect() {
		
		String redirectString;
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String password = passwordEncoder.encode("test4@gmail.com");
		
		User user = new User("Test4", "test4@gmail.com", password, UserRole.TRAINING);
		userService.saveOrUpdate(user);
		
		redirectString = loginController.postLogin(new UserTemp("Test4", "test4@gmail.com", "test4@gmail.com"), model, session);
		
		redirectString = loginController.postLogin(new UserTemp("Test4", "test4@gmail.com", "test4@gmail.com"), model, session);
		
		assertEquals("redirect:/changePass", redirectString);
		
	}
	
	/**
	 * Test case to check that changepass get mapping
	 * directs user to changepass form correctly
	 */
	@Test
	void test_LoginController_ChangePass() {
		
		String redirectString;
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		User user = new User("Test4", "test4@gmail.com", passwordEncoder.encode("test4@gmail.com"), UserRole.TRAINING);
		userService.saveOrUpdate(user);
		
		when(mockSession.getAttribute("id")).thenReturn(userService.getUserByEmail("test4@gmail.com").getUserId());
		
		redirectString = loginController.createUser(model, mockSession);
		
		assertEquals("changepass", redirectString);
		
	}
	
	
	@Test
	void test_LoginController_ChangePass_notmatching() {
		
		String redirectString;
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		User user = new User("Test4", "test4@gmail.com", passwordEncoder.encode("password1"), UserRole.TRAINING);
		userService.saveOrUpdate(user);
		
		when(mockSession.getAttribute("id")).thenReturn(userService.getUserByEmail("test4@gmail.com").getUserId());
		
		redirectString = loginController.createUser(model, mockSession);
		
		assertEquals("redirect:/index", redirectString);
		
	}
	
	
	@Test
	void test_LoginController_ChangePass_notloggedin() {
		
		String redirectString;
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		User user = new User("Test4", "test4@gmail.com", passwordEncoder.encode("password1"), UserRole.TRAINING);
		userService.saveOrUpdate(user);
		
		redirectString = loginController.createUser(model, mockSession);
		
		assertEquals("redirect:/index", redirectString);
		
	}
	
	
	/**
	 * Test case to check user password is successfully
	 * updated after changepass and redirected to index
	 */
	@Test
	void test_LoginController_UpdatePasswordSuccess() {
		
		String redirectString;
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		
		User user = new User("Test5", "test6@gmail.com", "something1", UserRole.TRAINING);
		userService.saveOrUpdate(user);
		
		when(mockSession.getAttribute("id")).thenReturn(userService.getUserByEmail("test6@gmail.com").getUserId());
		UserTemp user1 = new UserTemp("Test", "test6@gmail.com", "something1");
		
		user1.setConfirmPassword("something1");
		
		redirectString = loginController.updatePassword(user1, model, mockSession);
		
		assertTrue(passwordEncoder.matches("something1", userService.getUserByEmail("test6@gmail.com").getPassword()));
		assertEquals("redirect:/index", redirectString);
		 
	}
	
	/**
	 * Test case to check user password is successfully
	 * updated after changepass and redirected to index
	 */
	@Test
	void test_LoginController_UpdatePasswordFail() {
		
		String redirectString;
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		
		User user = new User("Test5", "test6@gmail.com", "something1", UserRole.TRAINING);
		userService.saveOrUpdate(user);
		
		when(mockSession.getAttribute("id")).thenReturn(userService.getUserByEmail("test6@gmail.com").getUserId());
		UserTemp user1 = new UserTemp("Test", "test6@gmail.com", "something1");
		
		user1.setConfirmPassword("something2");
		
		redirectString = loginController.updatePassword(user1, model, mockSession);
		
		assertEquals("changepass", redirectString);
		 
	}
	
	@Test
	void updatepassword_redirect() {
		String redirectString = loginController.updatePasswordIndex();
		assertEquals("redirect:/index", redirectString);
	}	
	
//	@Test
//	void handleMissingParams_test() {
//		String redirectString = loginController.handleMissingParams("error");
//		assertEquals("redirect:/index", redirectString);
//	}
	
}
