package com.impactsure.artnook.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
	
	/**
	 * @author Dell
	 *
	 */
	@Entity(name = "Announcements")
	public class Announcements implements Serializable{

		private static final long serialVersionUID = 1799492865317075979L;

		@Id
		@GeneratedValue(strategy=GenerationType.IDENTITY)
		@Column(name = "ID")
		private Long id;
		
		@Column(name = "TITLE")
		private String title;
		
		@Column(name = "SUBTITLE")
		private String subTitle;
		
		@Column(name = "DASHBOARD_CONTENT")
		private String slipContent;
		
		@Column(name = "READMORE_CONTENT")
		private String readMoreContent;
		
		@Column(name = "DisplayOrder")
		private Integer displayOrder;
		
		
		@Column(name = "MOBILE_BANNER")
		private String mobileImage;
		


		@Column(name="WEB_BANNER")
		private String webImage;
		
		@Column(name = "CATEGORY")
		private Integer category;
		
		
		@CreationTimestamp 
		@Column(name="CREATION_DATE",nullable = false, updatable = false)
		private LocalDateTime  creationDate;
		
		@Column(name="CREATED_BY")
		private String createdBy;
		
		@UpdateTimestamp
		@Column(name="MODIFIED_ON")
		private LocalDateTime  modifiedOn;
		
		@Column(name="MODIFIED_BY")
		private String modifiedBy;
		
		@Column(name = "ISACTIVE")
		private Integer active;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getSubTitle() {
			return subTitle;
		}

		public void setSubTitle(String subTitle) {
			this.subTitle = subTitle;
		}

		public String getSlipContent() {
			return slipContent;
		}

		public void setSlipContent(String slipContent) {
			this.slipContent = slipContent;
		}

		public String getReadMoreContent() {
			return readMoreContent;
		}

		public void setReadMoreContent(String readMoreContent) {
			this.readMoreContent = readMoreContent;
		}

		public Integer getDisplayOrder() {
			return displayOrder;
		}

		public void setDisplayOrder(Integer displayOrder) {
			this.displayOrder = displayOrder;
		}

		public String getMobileImage() {
			return mobileImage;
		}

		public void setMobileImage(String mobileImage) {
			this.mobileImage = mobileImage;
		}

		public String getWebImage() {
			return webImage;
		}

		public void setWebImage(String webImage) {
			this.webImage = webImage;
		}

		public Integer getCategory() {
			return category;
		}

		public void setCategory(Integer category) {
			this.category = category;
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

		public Integer getActive() {
			return active;
		}

		public void setActive(Integer active) {
			this.active = active;
		}

		public static long getSerialversionuid() {
			return serialVersionUID;
		}

		
	}