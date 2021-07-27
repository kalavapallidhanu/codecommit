package com.impactsure.artnook.dto;

public class ArticleDto {
	private Long id;
	private String title;
	private String subTitle;
	private String mobileImage;
	private String webImage;
	private String slipContent;
	private String readMoreContent;
	private Integer category;
	private String createdBy;
	private Integer active;
	private String activeDetails;
	private String modifiedBy;
	private Integer displayOrder;

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

	public Integer getCategory() {
		return category;
	}

	public void setCategory(Integer category) {
		this.category = category;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getActive() {
		return active;
	}

	public void setActive(Integer active) {
		this.active = active;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getActiveDetails() {
		return activeDetails;
	}

	public void setActiveDetails(String activeDetails) {
		this.activeDetails = activeDetails;
	}

}
