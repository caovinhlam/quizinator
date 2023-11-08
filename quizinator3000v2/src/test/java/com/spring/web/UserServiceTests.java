package com.spring.web;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;

import com.spring.web.model.Notification;
import com.spring.web.model.Question;
import com.spring.web.model.User;
import com.spring.web.model.EntityEnum.UserRole;
import com.spring.web.service.NotificationService;
import com.spring.web.service.UserService;

@SpringBootTest
class UserServiceTests {
	
	@Autowired
	UserService userService;
	
	@Autowired
	NotificationService notificationService;
	
	User user1;
	User user2;
	User user3;
	User user4;

	@BeforeEach
	void test() {
		notificationService.deleteAll();
		userService.deleteAllUsers();
		
		user1= new User();
		user2= new User();
		user3= new User();
		user4= new User();

	}
	
	@Test
	void create_blank_user_test() {
		userService.saveOrUpdate(user1);
		
		User fromData = userService.getUserById(user1.getUserId());
		assertEquals(fromData.getUserId(), user1.getUserId());
	}
	
	@Test
	void create_filled_user_test() {
		user1.setEmail("email");
		user1.setName("name");
		user1.setPassword("password");
		user1.setUserRole(UserRole.ABSENT);
		
		userService.saveOrUpdate(user1);
		
		User user = new User("name1", "email1", "password1", UserRole.BEACHED);
		userService.saveOrUpdate(user);
		
		User fromData = userService.getUserById(user1.getUserId());
		User fromData1 = userService.getUserById(user.getUserId());
		
		assertEquals(user.getEmail(), fromData1.getEmail());
		assertEquals(user.getName(), fromData1.getName());
		assertEquals(user.getPassword(), fromData1.getPassword());
		assertEquals(user.getUserRole(), fromData1.getUserRole());
		
		assertEquals(user1.getEmail(), fromData.getEmail());
		assertEquals(user1.getName(), fromData.getName());
		assertEquals(user1.getPassword(), fromData.getPassword());
		assertEquals(user1.getUserRole(), fromData.getUserRole());
	}
	
	@Test
	void get_nonexisting_user() {
		userService.deleteAllUsers();
		
		User fromData = userService.getUserById(1);
		assertEquals(null, fromData);
	}
	
	@Test
	void set_ID_test() {
		User user = new User();
		userService.saveOrUpdate(user);

		
		int original = user.getUserId();
		List<Notification> notes = new ArrayList<>();
		notes.add(new Notification("message1",null,null,user));
		notes.add(new Notification("message2",null,null,user));
		
		for (Notification notification : notes) {
			notificationService.saveNotification(notification);
		}
		
		user.setNotifications(notes);
		userService.saveOrUpdate(user);
		
		User fromData = userService.getUserById(original);
		assertEquals(fromData.getNotifications().size(), user.getNotifications().size());

		
		user.setUserId(original+1);
		userService.saveOrUpdate(user);

		fromData = userService.getUserById(original+1);
		assertEquals(fromData.getUserId(), original+1);

	}
	
	
	@Test
	void get_all_trainers_test() {
		user1.setUserRole(UserRole.ABSENT);
		user2.setUserRole(UserRole.TRAINER);
		user3.setUserRole(UserRole.ABSENT);
		user4.setUserRole(UserRole.ABSENT);
		
		userService.saveOrUpdate(user1);
		userService.saveOrUpdate(user2);
		userService.saveOrUpdate(user3);
		userService.saveOrUpdate(user4);
		
		List<User> users = userService.getAllByUserRole(UserRole.TRAINER);
		assertEquals(1, users.size());
	}
	
	@Test
	void get_all_sales_test() {
		user1.setUserRole(UserRole.SALES);
		user2.setUserRole(UserRole.TRAINER);
		user3.setUserRole(UserRole.SALES);
		user4.setUserRole(UserRole.ABSENT);
		
		userService.saveOrUpdate(user1);
		userService.saveOrUpdate(user2);
		userService.saveOrUpdate(user3);
		userService.saveOrUpdate(user4);
		
		List<User> users = userService.getAllByUserRole(UserRole.SALES);
		assertEquals(2, users.size());
	}
	
	@Test
	void get_all_training_test() {
		user1.setUserRole(UserRole.TRAINING);
		user2.setUserRole(UserRole.TRAINER);
		user3.setUserRole(UserRole.TRAINING);
		user4.setUserRole(UserRole.TRAINING);
		
		userService.saveOrUpdate(user1);
		userService.saveOrUpdate(user2);
		userService.saveOrUpdate(user3);
		userService.saveOrUpdate(user4);
		
		List<User> users = userService.getAllByUserRole(UserRole.TRAINING);
		assertEquals(3, users.size());
	}
	
	@Test
	void get_all_pond_test() {
		user1.setUserRole(UserRole.ABSENT);
		user2.setUserRole(UserRole.TRAINER);
		user3.setUserRole(UserRole.ABSENT);
		user4.setUserRole(UserRole.ABSENT);
		
		userService.saveOrUpdate(user1);
		userService.saveOrUpdate(user2);
		userService.saveOrUpdate(user3);
		userService.saveOrUpdate(user4);
		
		List<User> users = userService.getAllByUserRole(UserRole.POND);
		assertEquals(0, users.size());
	}
	
	@Test
	void get_all_beached_test() {
		user1.setUserRole(UserRole.ABSENT);
		user2.setUserRole(UserRole.TRAINER);
		user3.setUserRole(UserRole.ABSENT);
		user4.setUserRole(UserRole.BEACHED);
		
		userService.saveOrUpdate(user1);
		userService.saveOrUpdate(user2);
		userService.saveOrUpdate(user3);
		userService.saveOrUpdate(user4);
		
		List<User> users = userService.getAllByUserRole(UserRole.BEACHED);
		assertEquals(1, users.size());
	}
	
	
	@Test
	void get_all_absent_test() {
		user1.setUserRole(UserRole.ABSENT);
		user2.setUserRole(UserRole.TRAINER);
		user3.setUserRole(UserRole.ABSENT);
		user4.setUserRole(UserRole.ABSENT);
		
		userService.saveOrUpdate(user1);
		userService.saveOrUpdate(user2);
		userService.saveOrUpdate(user3);
		userService.saveOrUpdate(user4);
		
		List<User> users = userService.getAllByUserRole(UserRole.ABSENT);
		assertEquals(3, users.size());
	}
	
	
	@Test
	void delete_by_id_test() {
		userService.saveOrUpdate(user1);
		userService.saveOrUpdate(user2);
		userService.saveOrUpdate(user3);
		userService.saveOrUpdate(user4);
		assertEquals(4, userService.getAllUsers().size());
		userService.deleteById(user2.getUserId());
		assertEquals(3, userService.getAllUsers().size());
		
		User fromData = userService.getUserById(user2.getUserId());
		assertEquals(null, fromData);
	}
	
	
	@Test
	void get_by_email_test() {
		user1.setEmail("email1");
		user2.setEmail("email2");
		user3.setEmail("email3");
		user4.setEmail("email4");

		userService.saveOrUpdate(user1);
		userService.saveOrUpdate(user2);
		userService.saveOrUpdate(user3);
		userService.saveOrUpdate(user4);
		
		User fromData = userService.getUserByEmail(user2.getEmail());
		assertEquals(user2.toString(), fromData.toString());
		
		fromData = userService.getUserByEmail("email");
		assertEquals(null, fromData);
	}
	
	
	@Test
	void get_all_students_test() {
		User user5 = new User();
		User user6= new User();
		user1.setUserRole(UserRole.ABSENT);
		user2.setUserRole(UserRole.TRAINER);
		user3.setUserRole(UserRole.POND);
		user4.setUserRole(UserRole.BEACHED);
		user5.setUserRole(UserRole.SALES);
		user6.setUserRole(UserRole.TRAINING);
		
		userService.saveOrUpdate(user1);
		userService.saveOrUpdate(user2);
		userService.saveOrUpdate(user3);
		userService.saveOrUpdate(user4);
		userService.saveOrUpdate(user5);
		userService.saveOrUpdate(user6);
		
		List<User> users = userService.getAllStudents();
		assertEquals(4, users.size());
	}
	
	
	@Test
	void get_all_students_with_name_or_email_test() {
		user1.setUserRole(UserRole.ABSENT);
		user2.setUserRole(UserRole.TRAINING);
		user3.setUserRole(UserRole.POND);
		user4.setUserRole(UserRole.BEACHED);
		
		user1.setEmail("email1");
		user2.setEmail("email2");
		user3.setEmail("email3");
		user4.setEmail("email4");
		
		user1.setName("name1");
		user2.setName("name2");
		user3.setName("name3");
		user4.setName("name4");
		
		userService.saveOrUpdate(user1);
		userService.saveOrUpdate(user2);
		userService.saveOrUpdate(user3);
		userService.saveOrUpdate(user4);
		
		List<User> users = userService.getAllStudentswithNameorEmail("1");
		assertEquals(1, users.size());
		
		users = userService.getAllStudentswithNameorEmail("e");
		assertEquals(4, users.size());
		
		users = userService.getAllStudentswithNameorEmail("name3");
		assertEquals(1, users.size());
	}
	
	
	
	
	

}
