package com.impactsure.artnook.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

@Entity(name = "User_Login")
public class User_Login {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "LOGIN_HISTORY_ID")
	private Long id;
	
	
	@CreationTimestamp 
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LOGIN_TIME")
	private Date loginTime;

	@Column(name = "LOGOUT_TIME")
	private Date logoutTime;
	
	@Column(name = "USER_NAME")
	private String userName;
	
	@Column(name = "SESSION_ID")
	private String sessionId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public Date getLogoutTime() {
		return logoutTime;
	}

	public void setLogoutTime(Date logoutTime) {
		this.logoutTime = logoutTime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	
}
