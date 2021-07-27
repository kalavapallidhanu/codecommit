package com.impactsure.artnook.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.impactsure.artnook.dto.CognitoUser;
import com.impactsure.artnook.service.AmazonS3ClientService;
import com.impactsure.artnook.service.ExcelService;

@RestController
@RequestMapping("/artnook")
public class FileHandlerController {

	@Autowired
	private AmazonS3ClientService amazonS3ClientService;
	@Autowired
	private ExcelService fileServices;
	@Autowired
	private HttpSession httpSession;
	private CognitoUser user;

    
    /*
	 * Added By Gufran
	 * Upload File in Amazon S3
	 */
	@PostMapping(value = "/uploadDetails")
	public String uploadFile(@RequestPart(value = "file") MultipartFile file, HttpServletResponse httpResponse) {
		//ModelAndView mv = new ModelAndView();
		System.out.println(file);
		String messageLink = this.amazonS3ClientService.uploadFileToS3Bucket(file, true);
		// String fileDetails=file.getOriginalFilename();
		if (messageLink.equals("Exist")) {
			messageLink = "Exist";
		}
		return messageLink;
	}

	 /*
		 * Added By Gufran
		 * Delete File in Amazon S3
		 */
	
	    @DeleteMapping
	    public Map<String, String> deleteFile(@RequestParam("file_name") String fileName)
	    {
	        this.amazonS3ClientService.deleteFileFromS3Bucket(fileName);

	        Map<String, String> response = new HashMap<>();
	        response.put("message", "file [" + fileName + "] removing request submitted successfully.");

	        return response;
	    }

	    @GetMapping("/file")
	    public ResponseEntity<InputStreamResource> downloadFile() {
	    	user = (CognitoUser) httpSession.getAttribute("user");
	    	String tenant=user.getTenant();
	    	Calendar cal = Calendar.getInstance(); // creates calendar
	        cal.setTime(new Date()); // sets calendar time/date
	        cal.add(Calendar.HOUR_OF_DAY, 5); // adds one hour
	        cal.add(Calendar.MINUTE, 30);
	    	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	    	
	    	String fileName = tenant.substring(0, 1).toUpperCase() + tenant.substring(1)+"_Customer_Details_"+formatter.format(cal.getTime());
	      HttpHeaders headers = new HttpHeaders();
	      
	          headers.add("Content-Disposition", "attachment; filename="+fileName+".xlsx");
	      
	      return ResponseEntity
	                  .ok()
	                  .headers(headers)
	                  .body(new InputStreamResource(fileServices.loadFile()));  
	    }

}