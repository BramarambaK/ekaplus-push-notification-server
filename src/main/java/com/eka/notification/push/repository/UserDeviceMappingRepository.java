package com.eka.notification.push.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.eka.notification.push.model.UserDeviceMapping;

public interface UserDeviceMappingRepository extends CrudRepository<UserDeviceMapping, Integer>{
	
	List<UserDeviceMapping> findByUsernameAndClientId(String username, Integer clientId);
	
	boolean existsByDeviceToken(String deviceToken);
	
	Integer deleteByDeviceToken(String deviceToken);
}
