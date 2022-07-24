package com.eka.notification.push.repository;

import org.springframework.data.repository.CrudRepository;

import com.eka.notification.push.model.PushNotificationLog;

public interface PushNotificationLogRepository extends CrudRepository<PushNotificationLog, Integer>{
	
}
