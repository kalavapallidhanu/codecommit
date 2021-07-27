package com.impactsure.artnook.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
/*
 * Created By Gufran
 * 
 */
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.impactsure.artnook.dto.CognitoUser;
import com.impactsure.artnook.dto.CompetitionDto;
import com.impactsure.artnook.service.AmazonS3ClientService;

@Component
public class AmazonS3ClientServiceImpl implements AmazonS3ClientService {
	private String awsS3AudioBucket;
	private AmazonS3 amazonS3;
	private static final Logger logger = LoggerFactory.getLogger(AmazonS3ClientServiceImpl.class);
	@Autowired
	private HttpSession httpSession;
	private CognitoUser user;

	@Autowired
	public AmazonS3ClientServiceImpl(Region awsRegion, AWSCredentialsProvider awsCredentialsProvider,
			String awsS3AudioBucket) {
		this.amazonS3 = AmazonS3ClientBuilder.standard().withCredentials(awsCredentialsProvider)
				.withRegion(awsRegion.getName()).build();
		this.awsS3AudioBucket = awsS3AudioBucket;
	}

	/*
	 * Added By Gufran Upload File to S3
	 */
	public String uploadFileToS3Bucket(MultipartFile multipartFile, boolean enablePublicReadAccess) {
		user = (CognitoUser) httpSession.getAttribute("user");
		String tenant = user.getTenant();
		String folderName = tenant + "_media";
		String path = "";
		String pathLink = "";
		String fileName = multipartFile.getOriginalFilename();
		String fileType = "";
		if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
			fileType = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

		if (fileType.contains("png") || fileType.contains("jpg") || fileType.contains("jpeg")
				|| fileType.contains("gif")) {
			path = this.awsS3AudioBucket + "/" + folderName + "/images";

		}
		if (fileType.contains("mp4")) {
			path = this.awsS3AudioBucket + "/" + folderName + "/videos";
		}

		System.out.println("Message " + path);

		try {
			// creating the file in the server (temporarily)
			File file = new File(fileName);
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(multipartFile.getBytes());
			fos.close();

			System.out.println("MediaAudio" + this.awsS3AudioBucket);

			// Check if File name is Already Exist or Not
			boolean exists = this.amazonS3.doesObjectExist(path, fileName);
			if (exists) {
				System.out.println("Object \"" + path + "/" + fileName + "\" exists!");
				path = "Exist";
			} else {
				System.out.println("Object \"" + path + "/" + fileName + "\" does not exist!");
				PutObjectRequest putObjectRequest = new PutObjectRequest(path, fileName, file);

				if (enablePublicReadAccess) {
					putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead);
				}

				this.amazonS3.putObject(putObjectRequest);

				pathLink = path.replace("artnookschools/", "");
				pathLink = pathLink + "/" + fileName;
				// removing the file created in the server
				file.delete();
				return pathLink;
			}
		} catch (IOException | AmazonServiceException ex) {

			logger.error("error [" + ex.getMessage() + "] occurred while uploading [" + fileName + "] ");
			path = "Failed";
		}
		return path;
	}

	public String uploadDocumentToS3Bucket(MultipartFile multipartFile, boolean enablePublicReadAccess) {
		user = (CognitoUser) httpSession.getAttribute("user");
		String tenant = user.getTenant();
		;
		String folderName = this.awsS3AudioBucket + "/" + tenant + "_media/Document/EmpId";
		String path = "";
		String pathLink = "";
		String fileName = multipartFile.getOriginalFilename();
//		String fileType="";
//		  if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
//			  fileType=fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase();
//		  
//		  
//		  if(fileType.contains("png")||fileType.contains("jpg")||fileType.contains("jpeg")||fileType.contains("gif"))
//			{
//			  path=this.awsS3AudioBucket+"/"+folderName+"/images";
//				
//			}
//			if(fileType.contains("mp4"))
//			{
//				path=this.awsS3AudioBucket+"/"+folderName+"/videos";
//			}
		path = folderName;
		System.out.println("Message " + path);

		try {
			// creating the file in the server (temporarily)
			File file = new File(fileName);
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(multipartFile.getBytes());
			fos.close();

			System.out.println("MediaAudio" + this.awsS3AudioBucket);

			// Check if File name is Already Exist or Not
			boolean exists = this.amazonS3.doesObjectExist(path, fileName);
			if (exists) {
				System.out.println("Object \"" + path + "/" + fileName + "\" exists!");
				path = "Exist";
			} else {
				System.out.println("Object \"" + path + "/" + fileName + "\" does not exist!");
				PutObjectRequest putObjectRequest = new PutObjectRequest(path, fileName, file);

				if (enablePublicReadAccess) {
					putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead);
				}

				this.amazonS3.putObject(putObjectRequest);

				pathLink = path.replace("artnookschools/", "");
				pathLink = pathLink + "/" + fileName;
				// removing the file created in the server
				file.delete();
				return pathLink;
			}
		} catch (IOException | AmazonServiceException ex) {

			logger.error("error [" + ex.getMessage() + "] occurred while uploading [" + fileName + "] ");
			path = "Failed";
		}
		return path;
	}

	public void deleteFileFromS3Bucket(String fileName) {
		try {
			amazonS3.deleteObject(new DeleteObjectRequest(awsS3AudioBucket, fileName));
		} catch (AmazonServiceException ex) {
			logger.error("error [" + ex.getMessage() + "] occurred while removing [" + fileName + "] ");
		}
	}
	
	public String uploadCompetitionImageToS3Bucket(CompetitionDto competitiondto, boolean enablePublicReadAccess) {
		byte[] bI = org.apache.commons.codec.binary.Base64.decodeBase64((competitiondto.getFilebase64().substring(competitiondto.getFilebase64().indexOf(",")+1)).getBytes());

		InputStream fis = new ByteArrayInputStream(bI);
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(bI.length);
		metadata.setContentType("image/png");
		metadata.setCacheControl("public, max-age=31536000");
		user = (CognitoUser) httpSession.getAttribute("user");
		String tenant=user.getTenant();
		String folderName=tenant+"_media";
		String path = "";
		String pathLink = "";
		String fileName = competitiondto.getFileName();
		String fileType="";
		  if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
			  fileType=fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase();
		  
		  
		  if(fileType.contains("png")||fileType.contains("jpg")||fileType.contains("jpeg")||fileType.contains("gif"))
			{
			  path=this.awsS3AudioBucket+"/"+folderName+"/Competition";
				
			}

			path=path;
		  
		 
		 
		System.out.println("Message "+path);
		
		try {
			// creating the file in the server (temporarily)
			File file = new File(fileName);
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(bI);
			fos.close();
   SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	    	
   Calendar cal = Calendar.getInstance(); // creates calendar
   cal.setTime(new Date()); // sets calendar time/date
   cal.add(Calendar.HOUR_OF_DAY, 5); // adds one hour
   cal.add(Calendar.MINUTE, 30);
   
			System.out.println("MediaAudio"+this.awsS3AudioBucket);
			
			fileName=fileName.replace("."+fileType, "");
			fileName=fileName+"_"+formatter.format(cal.getTime());
			fileName=fileName+"."+fileType;
			//Check if File name is Already Exist or Not
			boolean exists = this.amazonS3.doesObjectExist(path, fileName);
			if (exists) {
				System.out.println("Object \"" + path + "/" + fileName + "\" exists!");
				path = "Exist";
			} else {
				System.out.println("Object \"" + path + "/" + fileName + "\" does not exist!");
				PutObjectRequest putObjectRequest1 = new PutObjectRequest(path, fileName,fis, metadata);

				if (enablePublicReadAccess) {
					putObjectRequest1.withCannedAcl(CannedAccessControlList.PublicRead);
				}

				this.amazonS3.putObject(putObjectRequest1);
			
			pathLink = path.replace("artnookschools/","");
			pathLink=pathLink+"/"+fileName;
				// removing the file created in the server
				file.delete();
				return pathLink;
			}
		} catch (IOException | AmazonServiceException ex) {

			logger.error("error [" + ex.getMessage() + "] occurred while uploading [" + fileName + "] ");
			path = "Failed";
		}
		return path;
	}
	
	

}