package com.impactsure.artnook.dto;

import org.springframework.web.multipart.MultipartFile;

public class EventDetailsDto {

	private Long id;
	private String eventName;
	private String eventDescription;
	private String availableFrom;
	private String availableTo;
	private Long isActive;
	
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
	public String getAvailableFrom() {
		return availableFrom;
	}
	public void setAvailableFrom(String availableFrom) {
		this.availableFrom = availableFrom;
	}
	public String getAvailableTo() {
		return availableTo;
	}
	public void setAvailableTo(String availableTo) {
		this.availableTo = availableTo;
	}
	public Long getIsActive() {
		return isActive;
	}
	public void setIsActive(Long isActive) {
		this.isActive = isActive;
	}
	
	
}
