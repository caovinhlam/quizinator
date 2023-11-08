package com.spring.web.dal;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spring.web.model.Notification;

/**
 * Repository for notification class
 * 
 * @author Samantha Jermyn
 */
@Repository
public interface NotificationRepo extends JpaRepository<Notification, Integer>{

	
	// if you goto quiz repo you might find the end
	
	/**
	 * Gets all notifications for userID parameter
	 * 
	 * @param userId - id of user
	 * @return List of notifications with userId matches parameter
	 */
	@Query("FROM Notification n  WHERE n.user.userId = :userId")
	List<Notification> getAllNotificaitonsWithUserId(@Param("userId") int userId);
}
