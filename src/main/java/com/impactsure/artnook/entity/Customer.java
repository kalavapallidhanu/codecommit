package com.impactsure.artnook.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity(name = "Master_Contact")
public class Customer implements Serializable {

	private static final long serialVersionUID = 1799492865317075979L;

	@Id
	// @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "ID")
	private String id;

	@Column(name = "FIRST_NAME")
	private String firstName;

	@Column(name = "MIDDLE_NAME")
	private String middleName;

	@Column(name = "LAST_NAME")
	private String lastName;
//	
//	@OneToMany(mappedBy = "customer", fetch = FetchType.LAZY,
//            cascade = CascadeType.ALL)
//    private Set<Competition> competition;

	@Column(name = "GENDER")
	private String gender;

	@Column(name = "DOB")
	private Date dob;

	@Column(name = "PROFILE_IMAGE")
	private String imageCode;

	@Column(name = "CLASS")
	private String className;

	@Column(name = "BRANCH")
	private String branch;

	@Column(name = "DIVISION")
	private String divisionName;

	@Column(name = "MAIL_ID")
	private String mailId;

	@Column(name = "COUNTRY_CODE")
	private String countryCode;

	@Column(name = "MOBILE_NO")
	private Long mobileNo;

	@Column(name = "BLOOD_GROUP")
	private String bloodGroup;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "USER_GROUP")
	private Long userGroup;

	@Column(name = "ISACTIVE")
	private Long isActive;

	@CreationTimestamp
	@Column(name = "CREATION_DATE", nullable = false, updatable = false)
	private LocalDateTime creationDate;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@UpdateTimestamp
	@Column(name = "MODIFIED_ON")
	private LocalDateTime modifiedOn;

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getImageCode() {
		return imageCode;
	}

	public void setImageCode(String imageCode) {
		this.imageCode = imageCode;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	
	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getDivisionName() {
		return divisionName;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	public String getMailId() {
		return mailId;
	}

	public void setMailId(String mailId) {
		this.mailId = mailId;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public Long getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(Long mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getBloodGroup() {
		return bloodGroup;
	}

	public void setBloodGroup(String bloodGroup) {
		this.bloodGroup = bloodGroup;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(Long userGroup) {
		this.userGroup = userGroup;
	}

	

	public Long getIsActive() {
		return isActive;
	}

	public void setIsActive(Long isActive) {
		this.isActive = isActive;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(LocalDateTime modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

//	public Set<Competition> getCompetition() {
//		return competition;
//	}
//
//	public void setCompetition(Set<Competition> competition) {
//		this.competition = competition;
//	}
	
	

}
