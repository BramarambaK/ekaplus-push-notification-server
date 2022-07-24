package com.eka.notification.push.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class PushNotificationLog{
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
	
	@Column(nullable = false)
	private String username;
	
	@Column(nullable = false)
	private Integer clientId;
	
	@Column(nullable = false)
	private Integer appId;
	
	@Column(nullable = false, length = 1000)
	private String deviceToken;
	
	@Column(columnDefinition = "text", nullable = false)
	private String payload;
	
	@Column(nullable = false, length = 50)
	private String status;
	
	@Column(columnDefinition = "timestamp not null default current_timestamp")
	private Timestamp createdDate;
	
	@Column(columnDefinition = "text", nullable = true)
	private String results;

	public PushNotificationLog() {
		super();
	}

	public PushNotificationLog(Integer clientId, String username, Integer appId, String deviceToken, String payload,
			String status, Timestamp createdDate, String results) {
		super();
		this.clientId = clientId;
		this.username = username;
		this.appId = appId;
		this.deviceToken = deviceToken;
		this.payload = payload;
		this.status = status;
		this.createdDate = createdDate;
		this.results = results;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getAppId() {
		return appId;
	}

	public void setAppId(Integer appId) {
		this.appId = appId;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getResults() {
		return results;
	}

	public void setResults(String results) {
		this.results = results;
	}
}
