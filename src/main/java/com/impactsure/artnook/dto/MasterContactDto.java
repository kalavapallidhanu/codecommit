package com.impactsure.artnook.dto;

import java.io.Serializable;

public class MasterContactDto implements Serializable {

	private static final long serialVersionUID = 7179669663425992713L;
	private String contactId;
	private String contactFirstName;
	private String contactMiddleName;
	private String contactLastName;
	private String imageCode;
	private String email;
	public String getContactId() {
		return contactId;
	}
	public void setContactId(String contactId) {
		this.contactId = contactId;
	}
	public String getContactFirstName() {
		return contactFirstName;
	}
	public void setContactFirstName(String contactFirstName) {
		this.contactFirstName = contactFirstName;
	}
	public String getContactMiddleName() {
		return contactMiddleName;
	}
	public void setContactMiddleName(String contactMiddleName) {
		this.contactMiddleName = contactMiddleName;
	}
	public String getContactLastName() {
		return contactLastName;
	}
	public void setContactLastName(String contactLastName) {
		this.contactLastName = contactLastName;
	}
	public String getImageCode() {
		return imageCode;
	}
	public void setImageCode(String imageCode) {
		this.imageCode = imageCode;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	

}
