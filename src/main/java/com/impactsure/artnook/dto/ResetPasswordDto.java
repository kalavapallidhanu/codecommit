package com.impactsure.artnook.dto;

public class ResetPasswordDto {
	private String contactMailId;
	private String firstName;
	private String groupName;

	public String getContactMailId() {
		return contactMailId;
	}

	public void setContactMailId(String contactMailId) {
		this.contactMailId = contactMailId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

}
