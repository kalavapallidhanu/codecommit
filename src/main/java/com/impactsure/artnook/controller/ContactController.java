	package com.impactsure.artnook.controller;

	import java.text.DateFormat;
	import java.text.ParseException;
	import java.text.SimpleDateFormat;
	import java.util.Date;
	import java.util.List;

	import com.amazonaws.services.cognitoidp.model.*;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.stereotype.Controller;
	import org.springframework.ui.Model;
	import org.springframework.web.bind.annotation.ModelAttribute;
	import org.springframework.web.bind.annotation.PathVariable;
	import org.springframework.web.bind.annotation.RequestMapping;
	import org.springframework.web.bind.annotation.RequestMethod;
	import org.springframework.web.bind.annotation.RequestParam;
	import org.springframework.web.bind.annotation.ResponseBody;
	import org.springframework.web.servlet.ModelAndView;

	import com.amazonaws.auth.AWSCredentials;
	import com.amazonaws.auth.AWSStaticCredentialsProvider;
	import com.amazonaws.auth.BasicAWSCredentials;
	import com.amazonaws.regions.Regions;
	import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
	import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
	import com.impactsure.artnook.cognito.AuthenticationHelper;
	import com.impactsure.artnook.configs.BeanUtil;
	import com.impactsure.artnook.dto.CustomerDto;
	import com.impactsure.artnook.dto.MasterDataDto;
	import com.impactsure.artnook.dto.MasterTableDto;
	import com.impactsure.artnook.dto.ResetPasswordDto;
	import com.impactsure.artnook.entity.Customer;
	import com.impactsure.artnook.repository.ContactRepository;
	import com.impactsure.artnook.repository.MasterDataRepository;
	import com.impactsure.artnook.utils.Utils;
	import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

	@Controller
	@EnableEncryptableProperties
	@RequestMapping(value = "/artnook")
	public class ContactController {

		private String secretAccessKey;

		@Autowired
		ContactRepository contactRepository;

		@Autowired
		AuthenticationHelper authenticationHelper;

		@Autowired
		private MasterDataRepository masterDataRepository;
		@RequestMapping("/newContact")
		public String newContact(Model model) {
			CustomerDto contact = new CustomerDto();
			List<MasterDataDto> userType=masterDataRepository.getUserType();
			model.addAttribute("userType",userType);
			model.addAttribute("contact", contact);
			return "customerform";
		}
		
		@RequestMapping("/viewAllContact")
		public String viewAllContact(Model model) {
			MasterTableDto master = new MasterTableDto();

			model.addAttribute("master", master);
			ResetPasswordDto resetPwd=new ResetPasswordDto();
			model.addAttribute("resetPwd",resetPwd);
			return "viewAllContactDetails";
		}
		
		@RequestMapping("/getContactData")
		@ResponseBody
		public List<Customer> getContactData() {
			return (List<Customer>) contactRepository.findAll();
		}



		@RequestMapping(value="/resetPassword",method = RequestMethod.POST)
		public String resetPassword(@ModelAttribute("master") MasterTableDto master) {

			ResetPasswordDto reset = new ResetPasswordDto();
			reset= masterDataRepository.fetchResetDetails(master.getEmpId());


			authenticationHelper=BeanUtil.getBean(AuthenticationHelper.class);
			  BasicAWSCredentials bac=new BasicAWSCredentials(authenticationHelper.getCognitoAccessKey(),
					  authenticationHelper.getCognitoSecretKey());

					  AWSCognitoIdentityProvider cognitoIdentityProvider =
					  AWSCognitoIdentityProviderClientBuilder .standard() .withCredentials(new
					  AWSStaticCredentialsProvider(bac))
					  .withRegion(Regions.fromName(authenticationHelper.getRegion())) .build();


					  AdminDisableUserRequest adminDisableUserRequest=new AdminDisableUserRequest();

								  adminDisableUserRequest.setUsername(reset.getContactMailId());
								  adminDisableUserRequest.setUserPoolId(authenticationHelper.getUserPoolID());
								  cognitoIdentityProvider.adminDisableUser(adminDisableUserRequest);
								  AdminDeleteUserRequest adminDeleteUserRequest=new AdminDeleteUserRequest();
								  adminDeleteUserRequest.setUserPoolId(authenticationHelper.getUserPoolID());
								  adminDeleteUserRequest.setUsername(reset.getContactMailId());
								  cognitoIdentityProvider.adminDeleteUser(adminDeleteUserRequest);




								  AdminCreateUserRequest cognitoRequest = new AdminCreateUserRequest()
								 .withUserPoolId(authenticationHelper.getUserPoolID()).withUsername(reset.getContactMailId()) .withUserAttributes(new
								  AttributeType().withName("name").withValue(reset.getFirstName()), new
								  AttributeType().withName("email").withValue(reset.getContactMailId()), new
								  AttributeType().withName("email_verified").withValue("true"))
								  .withTemporaryPassword("Test@123");

								  AdminCreateUserResult createUserResult =
								  cognitoIdentityProvider.adminCreateUser(cognitoRequest); UserType cognitoUser
								  = createUserResult.getUser();

								  AdminAddUserToGroupRequest addUserToGroupRequest = new
								  AdminAddUserToGroupRequest()
								 .withGroupName(reset.getGroupName()).withUserPoolId(authenticationHelper.
								  getUserPoolID()) .withUsername(reset.getContactMailId());
								  cognitoIdentityProvider.adminAddUserToGroup(addUserToGroupRequest);



			return "viewAllContactDetails";
		}


		
		@ResponseBody
		@RequestMapping("/fetchGroupName/{id}")
		public MasterDataDto fetchGroupName(@PathVariable(name = "id") Long id) {
			MasterDataDto mav = new MasterDataDto();
			 mav=masterDataRepository.fetchGroupName(id);

			return mav;
		}


		@RequestMapping("/editContact/{id}")
		public ModelAndView editContact(@PathVariable(name = "id") String id) {
			ModelAndView mav = new ModelAndView();
			Customer contact =masterDataRepository.fetchContactDetailsByID(id) ;
			CustomerDto customerDto=new CustomerDto();

			MasterDataDto groupDto = new MasterDataDto();
			groupDto=masterDataRepository.fetchUserGrpValue(contact.getUserGroup());

			/*
			 * List<MasterDataDto> userType=masterDataRepository.getUserType();
			 * mav.addObject("userType",userType);
			 */

			
			List<MasterDataDto> userType=masterDataRepository.getUserType();
			mav.addObject("userType",userType);
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String dob = dateFormat.format(contact.getDob());
			customerDto.setId(contact.getId());
			customerDto.setFirstName(contact.getFirstName());
			customerDto.setLastName(contact.getLastName());
			customerDto.setMiddleName(contact.getMiddleName());
			customerDto.setGender(contact.getGender());

			customerDto.setClassName(contact.getClassName());
			customerDto.setDivisionName(contact.getDivisionName());
			customerDto.setBranch(contact.getBranch());
			customerDto.setBloodGroup(contact.getBloodGroup());
			customerDto.setCountryCode(contact.getCountryCode());
			customerDto.setMailId(contact.getMailId());

			customerDto.setMobileNo(contact.getMobileNo());

		
			customerDto.setStatus(contact.getStatus());

			
			customerDto.setUserGroup(contact.getUserGroup());
			customerDto.setIsActive(contact.getIsActive());
			customerDto.setUserGroupName(groupDto.getName());
			customerDto.setDob(dob);
			mav.addObject("contact", customerDto);
			mav.setViewName("customerform");
			return mav;
		}


		@RequestMapping(value = "/saveContact", method = RequestMethod.POST)
		public String saveArticle(@ModelAttribute("contact") CustomerDto contactdto) {
			 authenticationHelper=BeanUtil.getBean(AuthenticationHelper.class);
			  BasicAWSCredentials bac=new BasicAWSCredentials(authenticationHelper.getCognitoAccessKey(),
					  authenticationHelper.getCognitoSecretKey());

					  AWSCognitoIdentityProvider cognitoIdentityProvider =
					  AWSCognitoIdentityProviderClientBuilder .standard() .withCredentials(new
					  AWSStaticCredentialsProvider(bac))
					  .withRegion(Regions.fromName(authenticationHelper.getRegion())) .build();






			Customer contact = new Customer();


			try {
	
				Date dob=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(contactdto.getDob()+" 05:30:00");
				//Customer_Master contactDetails = masterContactService.findOne(contactdto.getId());
				Customer mav=masterDataRepository.fetchContactDetails(contactdto.getId());
				//if(contactdto.getUpdateStatus()==null)
				if(Utils.isEmpty(mav)){
					contact.setId(contactdto.getId()); 
					contact.setFirstName(contactdto.getFirstName());
					  contact.setMiddleName(contactdto.getMiddleName());
					  contact.setLastName(contactdto.getLastName());
					  contact.setGender(contactdto.getGender());
					  contact.setBloodGroup(contactdto.getBloodGroup());
					  contact.setCountryCode(contactdto.getCountryCode());
					  contact.setImageCode("https://sure-people.s3.ap-south-1.amazonaws.com/profile.png");
					  contact.setDob(dob);

					  contact.setClassName(contactdto.getClassName());
					  contact.setDivisionName(contactdto.getDivisionName());

					  contact.setBranch(contactdto.getBranch());

					
					  contact.setMailId(contactdto.getMailId());
					  contact.setCountryCode(contactdto.getCountryCode());
					  contact.setMobileNo(contactdto.getMobileNo());
					 


					  contact.setIsActive(contactdto.getIsActive());
					  contact.setUserGroup(contactdto.getUserGroup());
					  contactRepository.save(contact);

					String mobileNumber=contactdto.getCountryCode()+contactdto.getMobileNo();
							  AdminGetUserRequest getRequest=new AdminGetUserRequest();
							  getRequest.getUsername();
							  AdminCreateUserRequest cognitoRequest = new AdminCreateUserRequest()
							  .withUserPoolId(authenticationHelper.getUserPoolID()).withUsername(contactdto
							  .getMailId()) .withUserAttributes(new
							  AttributeType().withName("name").withValue(contactdto.getFirstName()),
											  new AttributeType().withName("email").withValue(contactdto.getMailId()),
											  new AttributeType().withName("phone_number").withValue(mobileNumber),
											  new AttributeType().withName("email_verified").withValue("true"))
							  .withTemporaryPassword("Test@123")
									  .withDesiredDeliveryMediums(DeliveryMediumType.SMS)
									  .withDesiredDeliveryMediums(DeliveryMediumType.EMAIL);

							  AdminCreateUserResult createUserResult =
							  cognitoIdentityProvider.adminCreateUser(cognitoRequest); UserType cognitoUser
							  = createUserResult.getUser();

							  AdminAddUserToGroupRequest addUserToGroupRequest = new
							  AdminAddUserToGroupRequest()
							  .withGroupName(contactdto.getUserGroupName()).withUserPoolId(authenticationHelper.
							  getUserPoolID()) .withUsername(contactdto.getMailId());
							  cognitoIdentityProvider.adminAddUserToGroup(addUserToGroupRequest);



				}
				else {
					contact.setId(contactdto.getId());
					contact.setFirstName(contactdto.getFirstName());
					  contact.setMiddleName(contactdto.getMiddleName());
					  contact.setLastName(contactdto.getLastName());
					  contact.setGender(contactdto.getGender());
					  contact.setImageCode("https://artnookschools.s3.ap-south-1.amazonaws.com/profile.png");
					  contact.setDob(dob);
					  contact.setBloodGroup(contactdto.getBloodGroup());
					  contact.setCountryCode(contactdto.getCountryCode());
					  contact.setClassName(contactdto.getClassName());
					  contact.setDivisionName(contactdto.getDivisionName());

					  contact.setBranch(contactdto.getBranch());

					
					  contact.setMailId(contactdto.getMailId());
					  contact.setCountryCode(contactdto.getCountryCode());
					  contact.setMobileNo(contactdto.getMobileNo());
					 


					  contact.setIsActive(contactdto.getIsActive());
					  contact.setUserGroup(contactdto.getUserGroup());
					  contactRepository.save(contact);

				}



			 } catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			System.out.println();

			/* return "redirect:viewAllContact"; */
			return "redirect:newContact";
		}


		@ResponseBody

		  @RequestMapping("/fetchMailDetails/{mail}") public String
		  fetchResetDetails(@PathVariable(name = "mail") String mail) { String
		  result=null; ResetPasswordDto mav = new ResetPasswordDto();
		  mav=masterDataRepository.fetchMailDetails(mail); if(Utils.isEmpty(mav)) {
		 result="Not Exist"; } else { result="Exist"; }

		  return result; }





		@ResponseBody
		@RequestMapping("/fetchContactDetails/{id}")
		public String fetchContactDetails(@PathVariable(name = "id") String id) {
			String result=null;
			Customer mav = new Customer();
			 mav=masterDataRepository.fetchContactDetails(id);
			 if(Utils.isEmpty(mav))
			 {
				 result="Not Exist";
			 }
			 else {
				 result="Exist";
			 }

			return result;
		}
	}