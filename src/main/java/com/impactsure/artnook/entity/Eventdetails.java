package com.impactsure.artnook.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "Event_Details")
public class Eventdetails implements Serializable {

	private static final long serialVersionUID = 1799492865317075979L;

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "EVENT_NAME")
	private String eventName;

	@Column(name = "EVENT_DESCRIPTION")
	private String eventDescription;

	@Column(name = "DATE_AVAILABLE_FROM")
	private Date availableFrom;
	
	@Column(name = "IS_ACTIVE")
	private Long isActive;
	
	@Column(name = "DATE_AVAILABLE_TO")
	private Date availableTo;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@UpdateTimestamp
	@Column(name = "MODIFIED_ON")
	private LocalDateTime modifiedOn;

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getEventDescription() {
		return eventDescription;
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}

	public Date getAvailableFrom() {
		return availableFrom;
	}

	public void setAvailableFrom(Date availableFrom) {
		this.availableFrom = availableFrom;
	}

	public Date getAvailableTo() {
		return availableTo;
	}

	public void setAvailableTo(Date availableTo) {
		this.availableTo = availableTo;
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

	public Long getIsActive() {
		return isActive;
	}

	public void setIsActive(Long isActive) {
		this.isActive = isActive;
	}

	

	
}
