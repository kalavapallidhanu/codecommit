package com.impactsure.artnook.service;

/*
 * Created By Gufran
 */
import org.springframework.web.multipart.MultipartFile;

import com.impactsure.artnook.dto.CompetitionDto;

public interface AmazonS3ClientService {
	String uploadFileToS3Bucket(MultipartFile multipartFile, boolean enablePublicReadAccess);

	void deleteFileFromS3Bucket(String fileName);
	
	
	 String  uploadCompetitionImageToS3Bucket(CompetitionDto competitiondto, boolean enablePublicReadAccess);

}
