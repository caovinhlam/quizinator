package com.spring.web.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.spring.web.dal.NotificationRepo;
import com.spring.web.model.Notification;

/**
 * service for Notification class
 * 
 * @author Samantha Jermyn
 */
@Service
public class NotificationService {
	
	@Autowired
	NotificationRepo notificationRepo;
	
	
	
	/**
	 * Returns the notification with the corresponding ID, if none exit then return null
	 * 
	 * @param id - notification of id to find
	 * @return Notification - if none found then null
	 */
	public Notification getNotificationById(int id) {
		
		Optional<Notification> notifications = notificationRepo.findById(id);
		
		if(notifications.isPresent())			// process optional
			return notifications.get();
		else 
			return null;
	}
	
	
	
	/**
	 * Gets all notifications and returns the list of them all
	 * 
	 * @return Notifications list 
	 */
	public List<Notification> getAllNotifications(){
		return notificationRepo.findAll();
	}

	
	/**
	 * adds notification parameter to database
	 * 
	 * @param notification - notification to save
	 */
	public void saveNotification(Notification notification) {
		notificationRepo.save(notification);
	}
	
	/**
	 * updates notification parameter in database
	 * 
	 * @param notification - notification to update
	 */
	public  void updateNotification(Notification notification){
		saveNotification(notification);
	}
	
	
	/**
	 * deletes corresponding notification with parameter ID
	 * 
	 * @param id - id of notification to delete
	 */
	public void deleteById(int id){
		notificationRepo.deleteById(id);
	}
	
	
	/**
	 * gets all notifications with the userID parameter
	 * @param userID - id of user
	 * @return notifications list with same userID
	 */
	public List<Notification> getAllNotificaitonsWithUserId(int userID){
		return notificationRepo.getAllNotificaitonsWithUserId(userID);
	}

	

	/**
	 * deletes all notification records in database
	 * 
	 * @return integer representing total amount deleted
	 */
	public int deleteAll() {
		int count = 0;
		
		List<Notification> notifications = notificationRepo.findAll();
			
		for (Notification notification : notifications) {
			deleteById(notification.getNotificationID());
			count++;
		}	
		return count;
	}
	
	
	
}
