package com.eka.notification.push;

import static com.eka.notification.push.CommonConstants.CLIENTID;
import static com.eka.notification.push.CommonConstants.USERNAME_CAMELCASE;

import java.sql.Timestamp;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eka.notification.push.service.CommonService;

@RestController
@RequestMapping("/push-notifications")
public class PushTestController {
	
	@Value("${kafka.topic}")
	private String kafkaTopic;
  
	@Autowired
	private CommonService commonService;
	
	@Autowired 
	private KafkaTemplate<String, String> kafkaTemplate;
	
	@PostMapping 
	private ResponseEntity<Object> publishMessage(HttpServletRequest request){
		
		Map<String, Object> userInfo = commonService.authenticateRequestAndGetUserInfo(request);
		String username = String.valueOf(userInfo.get(USERNAME_CAMELCASE));
		Integer clientId = Integer.parseInt(String.valueOf(userInfo.get(CLIENTID)));
		JSONObject fcmPayload = new JSONObject();
		fcmPayload.put(CommonConstants.USERNAME, username);
		fcmPayload.put(CommonConstants.CLIENTID, clientId);
		fcmPayload.put(CommonConstants.APP_ID, -1);
		fcmPayload.put(CommonConstants.BODY, "Test Notification Body: Current Time: "+new Timestamp(System.currentTimeMillis()));
		fcmPayload.put(CommonConstants.TITLE, "Test Notification");
		fcmPayload.put(CommonConstants.DATA, new JSONObject()
													.put("testNotification", true));
		kafkaTemplate.send(new ProducerRecord<String, String>(kafkaTopic, fcmPayload.toString()));
		return new ResponseEntity<Object>(HttpStatus.OK); 
	}
}