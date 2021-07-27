package com.impactsure.artnook.controller;

import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.impactsure.artnook.dto.CognitoUser;
import com.impactsure.artnook.dto.MasterProfileDto;
import com.impactsure.artnook.service.ProfileService;
import com.impactsure.artnook.utils.ArtnookUtils;


@Controller
@RequestMapping(value = "/artnook") 
public class ProfileController {
	@Autowired
	private HttpSession httpSession;
	
	private CognitoUser user;
	
	@Autowired
	private ProfileService profileService;

	@RequestMapping(value = "/profileView")
	public ModelAndView profileView() {
		ModelAndView mv=new ModelAndView();
		String contactId;	
		user = (CognitoUser) httpSession.getAttribute("user");
		if(user.getMasterContactDto()!=null) {
			contactId=user.getMasterContactDto().getContactId();
			 mv.addObject("profile",profileService.findByID(contactId));
			 
		}
		
		mv.setViewName("profile");
		return mv;
	}
	

	
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String saveProfile(@ModelAttribute("profile") MasterProfileDto profile,HttpSession session){
		
		ModelAndView mv=new ModelAndView();
			Long displayedId=0l;	
			user = (CognitoUser) session.getAttribute("user");
			
			try {
				if(profile.getFile()!=null && profile.getFile().getBytes()!=null && profile.getFile().getBytes().length>0) {
					profile.setImage(ArtnookUtils.convertToBase64String(profile.getFile()));
				}
				else {
					profile.setImage(user.getMasterContactDto().getImageCode());
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			 user.getMasterContactDto().setImageCode(profile.getImage());
			 user.setProfilePhoto(profile.getImage());
			 session.setAttribute("user", user);
			 profileService.update(profile);
			 user.getMasterContactDto().setContactFirstName(profile.getFirstName());
			 
			return "profile";
	}
	
}
