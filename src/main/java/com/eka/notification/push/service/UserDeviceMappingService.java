package com.eka.notification.push.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eka.notification.push.model.UserDeviceMapping;
import com.eka.notification.push.repository.UserDeviceMappingRepository;

@Service
@Transactional
public class UserDeviceMappingService {
	
	@Autowired
	private UserDeviceMappingRepository userDeviceMappingRepository;

	public void updateUserDeviceMapping(String username, Integer clientId, String deviceType, String deviceToken) {
		
		if(userDeviceMappingRepository.existsByDeviceToken(deviceToken)) {
			userDeviceMappingRepository.deleteByDeviceToken(deviceToken);
		}
		userDeviceMappingRepository.save(new UserDeviceMapping(username, clientId, deviceType, deviceToken));
		
	}

	public void deleteUserDeviceMapping(String deviceToken) {
		
		userDeviceMappingRepository.deleteByDeviceToken(deviceToken);
		
	}

}
