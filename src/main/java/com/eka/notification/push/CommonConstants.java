package com.eka.notification.push;

import org.json.JSONObject;

public interface CommonConstants {

	String CLIENTID = "clientId";
	String USERNAME = "username";
	String USERNAME_CAMELCASE = "userName";
	String APP_ID = "appId";
	String STATUS = "status";
	String REF_ID = "refId";
	String TITLE = "title";
	String BODY = "body";
	String NOTIFICATION = "notification";
	String DATA = "data";
	String TARGET = "target";
	
	String FARMER_CONNECT_TARGET = "22/bidDetails/";
	
	String FCM_ENDPOINT = "https://fcm.googleapis.com/fcm/send";
	String TO = "to";
	String TENANT_FULL_DOMAIN_HEADER = "Tenant-Domain";
	String USER_INFO_URI = "/cac-security/api/userinfo";
	String USER_INFO_FILTER = "filter=all";
	String DEVICE_TOKEN = "deviceToken";
	String DEVICE_TYPE = "deviceType";
	String TENANT_INFO = "tenantInfo";
	String SUCCESS = "Success";
	String FAILURE = "Failure";
	String FCM_RESPONSE_SUCCESS = "success";
	String ERROR = "error";
	String RESULTS = "results";
	String DEVICE_ID = "Device-Id";
	String NOT_REGISTERED = "NotRegistered";
	String INVALID_REGISTRATION = "InvalidRegistration";
	
	String IOS_DEVICE = "iOS";
	String ANDROID_DEVICE = "android";
	String APS = "aps";
	
	JSONObject CONTENT_AVAILABLE = new JSONObject().put("content-available", 1);
}
