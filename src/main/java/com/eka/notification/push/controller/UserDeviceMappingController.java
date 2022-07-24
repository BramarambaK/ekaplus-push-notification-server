package com.eka.notification.push.controller;

import static com.eka.notification.push.CommonConstants.CLIENTID;
import static com.eka.notification.push.CommonConstants.DEVICE_TOKEN;
import static com.eka.notification.push.CommonConstants.DEVICE_TYPE;
import static com.eka.notification.push.CommonConstants.USERNAME_CAMELCASE;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eka.notification.push.service.CommonService;
import com.eka.notification.push.service.UserDeviceMappingService;

@RestController
@RequestMapping("/device-mappings")
public class UserDeviceMappingController {

	@Autowired
	private UserDeviceMappingService deviceMappingService;
	
	@Autowired
	private CommonService commonService;
	
	@PutMapping
	public ResponseEntity<Object> updateUserDeviceMapping(@RequestBody Map<String, Object> payload,
														  HttpServletRequest request){
		
		Map<String, Object> userInfo = commonService.authenticateRequestAndGetUserInfo(request);
		String username = String.valueOf(userInfo.get(USERNAME_CAMELCASE));
		Integer clientId = Integer.parseInt(String.valueOf(userInfo.get(CLIENTID)));
		String deviceToken = String.valueOf(payload.get(DEVICE_TOKEN));
		String deviceType = String.valueOf(payload.get(DEVICE_TYPE));
		deviceMappingService.updateUserDeviceMapping(username, clientId, deviceType, deviceToken);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@DeleteMapping
	public ResponseEntity<Object> unmapUserDeviceMapping(@RequestBody Map<String, Object> payload,
														 HttpServletRequest request){
		
		commonService.authenticateRequestAndGetUserInfo(request);
		String deviceToken = String.valueOf(payload.get(DEVICE_TOKEN));
		deviceMappingService.deleteUserDeviceMapping(deviceToken);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
