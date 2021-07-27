package com.impactsure.artnook.dto;

import org.springframework.web.multipart.MultipartFile;

public class CompetitionDto {

	private String id;
	private String artUrl;
	private String artDescription;
	private MultipartFile file;
	private String filebase64;
	private String fileName;
	
	public String getFilebase64() {
		return filebase64;
	}
	public void setFilebase64(String filebase64) {
		this.filebase64 = filebase64;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
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
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	
}
