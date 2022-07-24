package com.eka.notification.push.service;

import static com.eka.notification.push.CommonConstants.CLIENTID;
import static com.eka.notification.push.CommonConstants.DEVICE_ID;
import static com.eka.notification.push.CommonConstants.TENANT_FULL_DOMAIN_HEADER;
import static com.eka.notification.push.CommonConstants.TENANT_INFO;
import static com.eka.notification.push.CommonConstants.USER_INFO_FILTER;
import static com.eka.notification.push.CommonConstants.USER_INFO_URI;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.eka.notification.push.model.UserDeviceMapping;
import com.eka.notification.push.repository.UserDeviceMappingRepository;

@Service
@Transactional
public class CommonService {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private UserDeviceMappingRepository userDeviceMappingRepository;
	
	public List<UserDeviceMapping> getDeviceTokens(String username, Integer clientId) {
		
		return userDeviceMappingRepository.findByUsernameAndClientId(username, clientId);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> authenticateRequestAndGetUserInfo(HttpServletRequest request) {
		
		HttpHeaders headers = new HttpHeaders();
		headers.add(AUTHORIZATION, request.getHeader(AUTHORIZATION));
		headers.add(DEVICE_ID, request.getHeader(DEVICE_ID));
		UriComponentsBuilder uriBuilder = UriComponentsBuilder
													.fromHttpUrl(request.getHeader(TENANT_FULL_DOMAIN_HEADER))
													.path(USER_INFO_URI)
													.query(USER_INFO_FILTER);
		
		Map<String, Object> userInfo = (Map<String, Object>)restTemplate.exchange(uriBuilder.build().toUri(), 
				 										  HttpMethod.GET, 
				 										  new HttpEntity<>(headers), 
				 										  Object.class).getBody();
		
		userInfo.put(CLIENTID, ((Map<String, Object>)userInfo.get(TENANT_INFO)).get(CLIENTID));
		return userInfo;
	}
}
