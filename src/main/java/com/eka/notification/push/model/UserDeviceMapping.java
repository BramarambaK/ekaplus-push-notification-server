package com.eka.notification.push.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class UserDeviceMapping{
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
	
	@Column(nullable = false)
	private String username;
	
	@Column(nullable = false)
	private Integer clientId;
	
	@Column(nullable = false)
	private String deviceType;
	
	@Column(nullable = false, length = 1000)
	private String deviceToken;
	
	public UserDeviceMapping() {
		super();
	}
	
	public UserDeviceMapping(String username, Integer clientId, String deviceType, String deviceToken) {
		super();
		this.username = username;
		this.clientId = clientId;
		this.deviceType = deviceType;
		this.deviceToken = deviceToken;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getDeviceToken() {
		return deviceToken;
	}
	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public Integer getClientId() {
		return clientId;
	}
	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}
}
