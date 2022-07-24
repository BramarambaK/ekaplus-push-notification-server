package com.eka.notification.push.service;

import static com.eka.notification.push.CommonConstants.APP_ID;
import static com.eka.notification.push.CommonConstants.BODY;
import static com.eka.notification.push.CommonConstants.CLIENTID;
import static com.eka.notification.push.CommonConstants.DATA;
import static com.eka.notification.push.CommonConstants.ERROR;
import static com.eka.notification.push.CommonConstants.FAILURE;
import static com.eka.notification.push.CommonConstants.FCM_RESPONSE_SUCCESS;
import static com.eka.notification.push.CommonConstants.INVALID_REGISTRATION;
import static com.eka.notification.push.CommonConstants.IOS_DEVICE;
import static com.eka.notification.push.CommonConstants.NOTIFICATION;
import static com.eka.notification.push.CommonConstants.NOT_REGISTERED;
import static com.eka.notification.push.CommonConstants.RESULTS;
import static com.eka.notification.push.CommonConstants.SUCCESS;
import static com.eka.notification.push.CommonConstants.TITLE;
import static com.eka.notification.push.CommonConstants.USERNAME;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.eka.notification.push.CommonConstants;
import com.eka.notification.push.model.PushNotificationLog;
import com.eka.notification.push.model.UserDeviceMapping;
import com.eka.notification.push.repository.PushNotificationLogRepository;
import com.eka.notification.push.repository.UserDeviceMappingRepository;

@Service
@Transactional
public class NotificationConsumerService {
	
	private static final Logger logger = LogManager.getLogger(NotificationConsumerService.class);
	
	@Value("${fcm.server.key}")
	private String serverKey;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private UserDeviceMappingRepository userDeviceMappingRepository;
	
	@Autowired
	private PushNotificationLogRepository pushNotificationLogRepository;
	
	@KafkaListener(topics="#{'${kafka.topic}'}", groupId = "#{'${kafka.consumer.groupid}'}")
    public void consume(String message){
		
		logger.debug("New Message Received: " + message);
		
		JSONObject notificationPayload = new JSONObject(message);
		String username = notificationPayload.getString(USERNAME);
		Integer clientId = notificationPayload.getInt(CLIENTID);
		Integer appId = notificationPayload.getInt(APP_ID);
		List<UserDeviceMapping> deviceMappings = commonService.getDeviceTokens(username, clientId);
		if(CollectionUtils.isEmpty(deviceMappings) && logger.isDebugEnabled()) {
			logger.debug("No registered device for user: " + username);
			return;
		}
		invokeFCMAPI(username, clientId, appId, translateToFCMPayload(notificationPayload), deviceMappings);
    }

	@SuppressWarnings("unchecked")
	private void invokeFCMAPI(String username, Integer clientId, Integer appId, JSONObject commonPayload, List<UserDeviceMapping> deviceMappings) {
		
		String status;
		Object results;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add(AUTHORIZATION, serverKey);
		for (UserDeviceMapping deviceMapping : deviceMappings) {
			JSONObject updatedPayload = customizePayloadForDifferntPlatforms(commonPayload, deviceMapping);
			final String fcmPayload = updatedPayload.put(CommonConstants.TO, deviceMapping.getDeviceToken()).toString();
			try {
				Map<String, Object> response = (Map<String, Object>)
												restTemplate.exchange(CommonConstants.FCM_ENDPOINT, 
																	  HttpMethod.POST, 
																	  new HttpEntity<>(fcmPayload, headers), 
																	  Object.class).getBody();
				
				NotificationDeliveryInfo deliveryInfo = getNotificationDeliveryInfo(response, deviceMapping); 
				results = deliveryInfo.getResults();
				if(deliveryInfo.isDelivered()) {
					status = SUCCESS;
					logger.debug("Notification Sent Successfully: " + response);
				}else {
					logger.error("Push Notification Error: " + response);
					status = FAILURE;
				}
			}catch(Exception ex) {
				status = FAILURE;
				results = ex.getMessage();
				logger.error("Push Notification Error: " + ex.getMessage() + " Stack Trace: " + ex.getStackTrace());
			}
			pushNotificationLogRepository.save(new PushNotificationLog(clientId, username, appId, deviceMapping.getDeviceToken(), updatedPayload.toString(), status, new Timestamp(System.currentTimeMillis()), results.toString()));
		}
	}

	private JSONObject customizePayloadForDifferntPlatforms(JSONObject payload, UserDeviceMapping deviceMapping) {

		switch (deviceMapping.getDeviceType()) {
		case IOS_DEVICE:
			return new JSONObject(payload.toMap()).put(CommonConstants.APS, CommonConstants.CONTENT_AVAILABLE);
		default:
			return payload;
		}
	}

	private NotificationDeliveryInfo getNotificationDeliveryInfo(Map<String, Object> fcmResponse, UserDeviceMapping deviceMapping) {
		
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> results = (List<Map<String, Object>>)fcmResponse.get(RESULTS);
		
		if(Integer.parseInt(String.valueOf(fcmResponse.get(FCM_RESPONSE_SUCCESS))) == 1) {
			return new NotificationDeliveryInfo(true, results);
		}
			
		if(!CollectionUtils.isEmpty(results)) {
			if(results.stream().anyMatch(result -> result.containsKey(ERROR) && 
										 StringUtils.equalsAny(String.valueOf(result.get(ERROR)), 
																NOT_REGISTERED, 
																INVALID_REGISTRATION))) {
				userDeviceMappingRepository.deleteByDeviceToken(deviceMapping.getDeviceToken());
			}
		}
		return new NotificationDeliveryInfo(false, results);
	}
	private JSONObject translateToFCMPayload(JSONObject notificationPayload) throws JSONException {
		
		return new JSONObject()
					.put(NOTIFICATION, new JSONObject()
											.put(TITLE, notificationPayload.getString(TITLE))
											.put(BODY, notificationPayload.getString(BODY)))
					.put(DATA, notificationPayload.getJSONObject(DATA));
	}
	
	private static class NotificationDeliveryInfo {
		
		private final boolean isDelivered;
		private final Object results;
		
		private NotificationDeliveryInfo(boolean isDelivered, Object results) {
			super();
			this.isDelivered = isDelivered;
			this.results = results;
		}
		public boolean isDelivered() {
			return isDelivered;
		}
		public Object getResults() {
			return results;
		}
	}
}