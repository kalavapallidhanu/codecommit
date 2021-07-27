package com.impactsure.artnook.controller;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.ChallengeNameType;
import com.amazonaws.services.cognitoidp.model.ChangePasswordRequest;
import com.amazonaws.services.cognitoidp.model.CodeMismatchException;
import com.amazonaws.services.cognitoidp.model.ConfirmForgotPasswordRequest;
import com.amazonaws.services.cognitoidp.model.ForgotPasswordRequest;
import com.amazonaws.services.cognitoidp.model.ForgotPasswordResult;
import com.amazonaws.services.cognitoidp.model.NotAuthorizedException;
import com.amazonaws.services.cognitoidp.model.RespondToAuthChallengeRequest;
import com.amazonaws.services.cognitoidp.model.RespondToAuthChallengeResult;
import com.amazonaws.services.cognitoidp.model.UserNotFoundException;
import com.impactsure.artnook.cognito.AuthenticationHelper;
import com.impactsure.artnook.cognito.CognitoData;
import com.impactsure.artnook.cognito.UserAttributes;
import com.impactsure.artnook.dbconfig.tenants.Tenant;
import com.impactsure.artnook.dbconfig.tenants.TenantRepository;
import com.impactsure.artnook.dto.CognitoUser;
import com.impactsure.artnook.dto.MasterContactDto;
import com.impactsure.artnook.entity.User_Login;
import com.impactsure.artnook.repository.LoginRepository;
import com.impactsure.artnook.service.UserService;
import com.impactsure.artnook.utils.ArtnookUtils;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;


@Controller
@RequestMapping(value = "/artnook")
public class CognitoController {
	protected static Logger logger = LoggerFactory.getLogger(CognitoController.class);
	@Autowired
	UserService userService;
	
	@Autowired
	private TenantRepository tenantRepository;
	
	@Autowired
	private LoginRepository myLoginRepository;
	
	
	@Autowired
	protected RestTemplate restTemplate;
	
	@Autowired
	private AuthenticationHelper authenticationHelper;
	
	@RequestMapping(value = "/login")
	public String login() {
		return "login";
	}
	
	
	
	
	@RequestMapping(value = "/sms-mfa")
	public ModelAndView smsMfa(HttpServletRequest request, HttpSession session) {
		ModelAndView model = new ModelAndView();
		model.addObject("cognitoData", session.getAttribute("cognitoData"));
		model.setViewName("sms-mfa");
		return model;
	} 

	@RequestMapping(value = "/new-pwd")
	public ModelAndView newPwd(HttpServletRequest request, HttpSession session) {
		ModelAndView model = new ModelAndView();
		model.addObject("cognitoData", session.getAttribute("cognitoData"));
		model.setViewName("new-pwd");
		return model;
	}

	@RequestMapping(value = "/cognito-change-pwd")
	public ModelAndView changePwd(CognitoData cognitoData, HttpServletRequest request, HttpServletRequest response) {

		ModelAndView mv = new ModelAndView();
		AnonymousAWSCredentials awsCreds = new AnonymousAWSCredentials();
		AWSCognitoIdentityProvider cognitoIdentityProvider = AWSCognitoIdentityProviderClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(awsCreds))
				.withRegion(Regions.fromName(authenticationHelper.getRegion())).build();
		
		UserAttributes ua = cognitoData.getUserAttributes();
		if(cognitoData.getPassword().equals(cognitoData.getConfirmPassword() )) {
			try {
			Map<String, String> srpAuthResponses = new HashMap<>();
			srpAuthResponses.put("USERNAME", cognitoData.getUserName());
			srpAuthResponses.put("NEW_PASSWORD", cognitoData.getConfirmPassword());
			srpAuthResponses.put("userAttributes.name", ua.getName());

			RespondToAuthChallengeRequest authChallengeRequest = new RespondToAuthChallengeRequest();
			authChallengeRequest.setChallengeName(ChallengeNameType.NEW_PASSWORD_REQUIRED);
			authChallengeRequest.setClientId(authenticationHelper.getClientId());
			authChallengeRequest.setSession(cognitoData.getCognitoSession());
			authChallengeRequest.setChallengeResponses(srpAuthResponses);
	
			RespondToAuthChallengeResult result = cognitoIdentityProvider.respondToAuthChallenge(authChallengeRequest);
			if (result.getChallengeName() == null) {
				try {
					authenticationHelper.processToken(request, result);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (BadJOSEException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JOSEException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mv.setViewName("redirect:sync-user");
			} else if (result.getChallengeName().equals(ChallengeNameType.SMS_MFA.toString())) {
				CognitoData cd = new CognitoData();
				cd.setUserName(cognitoData.getUserName());
				cd.setCognitoSession(result.getSession());
				mv.addObject("cognitoData", cd);
				mv.setViewName("sms-mfa");
			}
			}
			
			catch (Exception e) {
				mv.addObject("cognitoData", cognitoData);
				mv.addObject("error",e.getMessage());
				mv.setViewName("new-pwd");
				// TODO: handle exception
			}
			
			
		}
		else {
			mv.addObject("cognitoData", cognitoData);
			mv.addObject("error","New Password and Confirm Password doesn't Match.");
			mv.setViewName("new-pwd");
		}
		return mv;
	}

	@RequestMapping(value = "/cognito-sms-mfa")
	public ModelAndView smsMfa(CognitoData cognitoData, HttpServletRequest request, HttpServletRequest response) {
		ModelAndView mv = new ModelAndView();
		try {
		AnonymousAWSCredentials awsCreds = new AnonymousAWSCredentials();
		AWSCognitoIdentityProvider cognitoIdentityProvider = AWSCognitoIdentityProviderClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(awsCreds))
				.withRegion(Regions.fromName(authenticationHelper.getRegion())).build();

		Map<String, String> srpAuthResponses = new HashMap<>();
		srpAuthResponses.put("USERNAME", cognitoData.getUserName());
		srpAuthResponses.put("SMS_MFA_CODE", cognitoData.getSmsMfaCode());
		RespondToAuthChallengeRequest authChallengeRequest = new RespondToAuthChallengeRequest();
		authChallengeRequest.setChallengeName(ChallengeNameType.SMS_MFA);
		authChallengeRequest.setClientId(authenticationHelper.getClientId());
		authChallengeRequest.setSession(cognitoData.getCognitoSession());
		authChallengeRequest.setChallengeResponses(srpAuthResponses);
		RespondToAuthChallengeResult result = cognitoIdentityProvider.respondToAuthChallenge(authChallengeRequest);

		if (result.getChallengeName() == null) {
			try {
				authenticationHelper.processToken(request, result);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BadJOSEException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JOSEException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		mv.setViewName("redirect:sync-user");
		}
		catch (Exception e) {
			mv.addObject("cognitoData", cognitoData);
			mv.addObject("error",e.getMessage());
			mv.setViewName("sms-mfa");
		}
		return mv;
	}
	@RequestMapping(value = "/sync-user")
	public String syncUser(HttpSession session) {
		CognitoUser cognitoUser = (CognitoUser) session.getAttribute("user");
		try {
			MasterContactDto mcd=userService.findByEmail(cognitoUser.getEmail());
			if(mcd==null) {
				return "redirect:login?error=Please Login Again.";
			}
			else {
			cognitoUser.setMasterContactDto(mcd);
			}
		}
		catch (Exception e) {
			return "redirect:login?error=Please Contact Your Administrator.";
		}
		if(cognitoUser.getTenant()==null || cognitoUser.getTenant().trim().length()==0) {
			return "redirect:login?error=Please Contact Your Administrator.";
		} else {
			String lastLogin=userService.findLastLogin(cognitoUser.getEmail());
			 if(lastLogin != null && !lastLogin.trim().isEmpty())
			 {
			cognitoUser.setLastLogin("Last Login: "+ArtnookUtils.convertDateFormat(lastLogin));
			 }
			 cognitoUser.setSessionId(session.getId());
			
			

			Tenant t=  tenantRepository.findById(cognitoUser.getTenant()).get();
		

		 cognitoUser.setLogo(t.getLogo());
		
//		 cognitoUser.setShowAnnouncement(t.getShowAnnouncements());
		 cognitoUser.setShowBanner(t.getShowBanner());
		
		 User_Login history=new User_Login(); 
			//	history.setLoginTime(cognitoUser.getLoginTime());
				history.setUserName(cognitoUser.getEmail());
				history.setSessionId(cognitoUser.getSessionId());
				myLoginRepository.save(history);
        session.setAttribute("user", cognitoUser);
		
		return "redirect:dashboard";
		}
	}
	@RequestMapping("/error")
	public String handleError(HttpServletRequest request) {
	    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
	     
	    if (status != null) {
	        Integer statusCode = Integer.valueOf(status.toString());
	     
	        if(statusCode == HttpStatus.NOT_FOUND.value()) {
	            return "error-404";
	        }
	        else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
	            return "error-500";
	        }
	    }
	    return "login";
	}
	@RequestMapping(value = "/cpwd")
	@ResponseBody
	public String cpwd(@RequestParam String currentPwd,@RequestParam String pwd,@RequestParam String confPwd,HttpSession session ) {
			try {
			
				if(pwd.equals(confPwd)) {
				CognitoUser user=(CognitoUser) session.getAttribute("user");
				 AnonymousAWSCredentials awsCreds = new AnonymousAWSCredentials();
			        @SuppressWarnings("unused")
					AWSCognitoIdentityProvider cognitoIdentityProvider = AWSCognitoIdentityProviderClientBuilder
			                .standard()
			                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
			                .withRegion(Regions.fromName(authenticationHelper.getRegion()))
			                .build();
		
			        	ChangePasswordRequest cpr=new ChangePasswordRequest();
			        	cpr.setAccessToken(user.getAccessToken());
			        	cpr.setPreviousPassword(currentPwd);
			        	cpr.setProposedPassword(pwd);
			       // ChangePasswordResult result=	cognitoIdentityProvider.changePassword(cpr);
			        	 return "Password Changed Successfully.";
				}
				else {
					return "New Password and Confirm Password doesn't Match.";
				}
			}
			catch (NotAuthorizedException e) {
				return "Current Password is Wrong.";
			}
			catch (Exception e) {
				// TODO: handle exception
				return "Sorry! Unable to Change the Password.";
			}
			
		
	}
	@RequestMapping(value = "/fp")
	public String forgotPassword() {
		return "forgot-password";
	}
	
	@RequestMapping(value = "/fph")
	public ModelAndView ResetPassword(String username) {
       ModelAndView model=new ModelAndView();
		AnonymousAWSCredentials awsCreds = new AnonymousAWSCredentials();
        AWSCognitoIdentityProvider cognitoIdentityProvider = AWSCognitoIdentityProviderClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withRegion(Regions.fromName(authenticationHelper.getRegion()))
                .build();

        ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest();
        forgotPasswordRequest.setUsername(username);
        forgotPasswordRequest.setClientId(authenticationHelper.getClientId());
        ForgotPasswordResult forgotPasswordResult = new ForgotPasswordResult();
        
        try {
            forgotPasswordResult = cognitoIdentityProvider.forgotPassword(forgotPasswordRequest);
            model.addObject("username",username);
            model.addObject("destination",forgotPasswordResult.getCodeDeliveryDetails().getDestination());
            model.addObject("delivery_type",forgotPasswordResult.getCodeDeliveryDetails().getDeliveryMedium());
            model.setViewName("reset-password");
        }
        catch (UserNotFoundException e) {
        	model.addObject("error",username+"  Doesn't Exist.");
        	model.setViewName("forgot-password");
		}
        catch (Exception e) {
            // handle exception here
        	model.addObject("error","Please Try After Some Time.");
        	model.setViewName("forgot-password");
        }
        return model;
    }
	
	@RequestMapping(value = "/cfp")
	public ModelAndView fgtPassword(String username,String newPwd,String confPwd,String code,String destination,String delivery_type) {
		ModelAndView model=new ModelAndView();  
		if(newPwd.equals(confPwd)) {
		AnonymousAWSCredentials awsCreds = new AnonymousAWSCredentials();
	        AWSCognitoIdentityProvider cognitoIdentityProvider = AWSCognitoIdentityProviderClientBuilder
	                .standard()
	                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
	                .withRegion(Regions.fromName(authenticationHelper.getRegion()))
	                .build();

	        ConfirmForgotPasswordRequest confirmPasswordRequest = new ConfirmForgotPasswordRequest();
	        confirmPasswordRequest.setUsername(username);
	        confirmPasswordRequest.setPassword(newPwd);
	        confirmPasswordRequest.setConfirmationCode(code);
	        confirmPasswordRequest.setClientId(authenticationHelper.getClientId());

	        try {
	            cognitoIdentityProvider.confirmForgotPassword(confirmPasswordRequest);
	            model.addObject("error","Password Changed Successfully.");
	            model.setViewName("login");
	        }catch(CodeMismatchException e) {
	        	model.addObject("error","Invalid Verification Code.");
	        	model.setViewName("reset-password");
	        	model.addObject("destination",destination);
	        	model.addObject("delivery_type",delivery_type);
	        	model.addObject("username",username);
	        }
	        catch (Exception e) {
	            // handle exception here
	        	model.addObject("destination",destination);
	        	model.addObject("delivery_type",delivery_type);
	            model.addObject("username",username);
	        	model.addObject("error","Please Try After Some Time.");
	        	
	        	model.setViewName("reset-password");
	        }
		}
		else {
			model.addObject("error","New Password and Confirm Password doesn't Match.");
        	model.setViewName("reset-password");
        	model.addObject("username",username);
        	model.addObject("destination",destination);
        	model.addObject("delivery_type",delivery_type);
		}
	        return model;
    }
	
	
}
