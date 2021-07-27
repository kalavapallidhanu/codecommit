package com.impactsure.artnook.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "Competition")
public class Competition implements Serializable {

	private static final long serialVersionUID = 1799492865317075979L;

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "ART_URL")
	private String artUrl;

	@Column(name = "ART_DESCRIPTION")
	private String artDescription;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "CUSTOMER_ID", nullable = false)
    private Customer customer;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "EVENT_ID", nullable = false)
    private Eventdetails eventdetails;
	
	
	
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

	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getArtUrl() {
		return artUrl;
	}

	public void setArtUrl(String artUrl) {
		this.artUrl = artUrl;
	}

	public String getArtDescription() {
		return artDescription;
	}

	public void setArtDescription(String artDescription) {
		this.artDescription = artDescription;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
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

	public Eventdetails getEventdetails() {
		return eventdetails;
	}

	public void setEventdetails(Eventdetails eventdetails) {
		this.eventdetails = eventdetails;
	}

	
}
