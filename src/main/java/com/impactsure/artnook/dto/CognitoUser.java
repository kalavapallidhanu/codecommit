package com.impactsure.artnook.dto;

import java.util.Date;
import java.util.List;
import com.nimbusds.jwt.JWTClaimsSet;

public class CognitoUser {
	private MasterContactDto masterContactDto;
	private String userName;
	private String mobileNo;
	private String accessToken;
	private String idToken;
	private String refreshToken;
	private String gender;
	private String locale;
	private String name;
	private String nickName;
	private String email;
	private String picture;
	private List<String> groups;
	private String profilePhoto;
	private Long userId;
	private String tenant;
	private JWTClaimsSet jwtClaimsSet;
	private String logo;
	private Date issuedAt;
	private Date authTime;
	private Date expiryTime;
	private String deviceKey;
	private boolean isTokenExpired = false;
	private String masterPwd;
	private String suPwd;
	private Long defaultFilter;
	private Long defaultReport;
	private String sessionId;
	private String state;
	private String city;
	private String location;
	private String lastLogin;
	private Date loginTime;
	private Date logoutTime;
	private String userGroup;
	private Short showBanner;
	private Short showAnnouncement;

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	private String[] defaultMachineIds;
	private Integer[] defaultSheds;

	public String[] getDefaultMachineIds() {
		return defaultMachineIds;
	}

	public void setDefaultMachineIds(String[] defaultMachineIds) {
		this.defaultMachineIds = defaultMachineIds;
	}

	public Integer[] getDefaultSheds() {
		return defaultSheds;
	}

	public void setDefaultSheds(Integer[] defaultSheds) {
		this.defaultSheds = defaultSheds;
	}

	public Long getDefaultFilter() {
		return defaultFilter;
	}

	public void setDefaultFilter(Long defaultFilter) {
		this.defaultFilter = defaultFilter;
	}

	public Long getDefaultReport() {
		return defaultReport;
	}

	public void setDefaultReport(Long defaultReport) {
		this.defaultReport = defaultReport;
	}

	public String getSuPwd() {
		return suPwd;
	}

	public void setSuPwd(String suPwd) {
		this.suPwd = suPwd;
	}

	public String getMasterPwd() {
		return masterPwd;
	}

	public void setMasterPwd(String masterPwd) {
		this.masterPwd = masterPwd;
	}

	public boolean isTokenExpired() {
		return isTokenExpired;
	}

	public void setTokenExpired(boolean isTokenExpired) {
		this.isTokenExpired = isTokenExpired;
	}

	public String getDeviceKey() {
		return deviceKey;
	}

	public void setDeviceKey(String deviceKey) {
		this.deviceKey = deviceKey;
	}

	public Date getIssuedAt() {
		return issuedAt;
	}

	public void setIssuedAt(Date issuedAt) {
		this.issuedAt = issuedAt;
	}

	public Date getAuthTime() {
		return authTime;
	}

	public void setAuthTime(Date authTime) {
		this.authTime = authTime;
	}

	public Date getExpiryTime() {
		return expiryTime;
	}

	public void setExpiryTime(Date expiryTime) {
		this.expiryTime = expiryTime;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public JWTClaimsSet getJwtClaimsSet() {
		return jwtClaimsSet;
	}

	public void setJwtClaimsSet(JWTClaimsSet jwtClaimsSet) {
		this.jwtClaimsSet = jwtClaimsSet;
	}

	public String getTenant() {
		return tenant;
	}

	public void setTenant(String tenant) {
		this.tenant = tenant;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getProfilePhoto() {
		return profilePhoto;
	}

	public void setProfilePhoto(String profilePhoto) {
		this.profilePhoto = profilePhoto;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getIdToken() {
		return idToken;
	}

	public void setIdToken(String idToken) {
		this.idToken = idToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<String> getGroups() {
		return groups;
	}

	public void setGroups(List<String> groups) {
		this.groups = groups;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public MasterContactDto getMasterContactDto() {
		return masterContactDto;
	}

	public void setMasterContactDto(MasterContactDto masterContactDto) {
		this.masterContactDto = masterContactDto;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
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

	public String getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(String lastLogin) {
		this.lastLogin = lastLogin;
	}

	public String getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(String userGroup) {
		this.userGroup = userGroup;
	}

	public Short getShowBanner() {
		return showBanner;
	}

	public void setShowBanner(Short showBanner) {
		this.showBanner = showBanner;
	}

	public Short getShowAnnouncement() {
		return showAnnouncement;
	}

	public void setShowAnnouncement(Short showAnnouncement) {
		this.showAnnouncement = showAnnouncement;
	}

}
