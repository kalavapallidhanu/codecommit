package com.impactsure.artnook.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.impactsure.artnook.dto.CognitoUser;
import com.impactsure.artnook.dto.MasterDataDto;
import com.impactsure.artnook.entity.Announcements;
import com.impactsure.artnook.repository.UserRepository;
import com.impactsure.artnook.service.AnnouncementsService;
import com.impactsure.artnook.utils.GlobalConstants;

@Controller
@RequestMapping(value = "/artnook") 
public class DashboardController {
	
	
	
	@Autowired
	private HttpSession httpSession;
	
	private CognitoUser user;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AnnouncementsService announcementsService;
	
	@RequestMapping(value = { "","/","/dashboard"})
	public ModelAndView dashboard() {
		ModelAndView mv=new ModelAndView();
				
		user = (CognitoUser) httpSession.getAttribute("user");
		if(user==null || user.getTenant()==null) {
			mv.addObject("error", "Session exipred.");
			mv.setViewName("login");
			return mv;
		}
		String contactId;
		if(user.getMasterContactDto()!=null)
			contactId= user.getMasterContactDto().getContactId();
		
		MasterDataDto mav = new MasterDataDto();
		 mav=userRepository.fetchStaus(user.getEmail());
		 if(mav.getValue()==1)
		 {
			 mv.setViewName("dashboard");
		 }
		 else {
		 mv.setViewName("login");
		 }
		return mv;
	}
	
	
	@RequestMapping(value = "/bannerDetails")
	public ModelAndView getBannerDetails() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("banners", announcementsService.findByCategory(GlobalConstants.BANNER, 0, 3));
		mv.setViewName("bannerDetails");
		return mv;
	}
	
	
	@RequestMapping(value = "/section1Details")
	public ModelAndView getSection1() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("sec1", announcementsService.findByCategory(GlobalConstants.SECTIONONE, 0, 3));
		mv.setViewName("section1");
		return mv;
	}
	
	
	@RequestMapping(value = "/sec1-read/{id}")
	public ModelAndView section1ViewAll(@PathVariable ("id") Long id) {
		ModelAndView mv=new ModelAndView();
		user = (CognitoUser) httpSession.getAttribute("user");
		List<Announcements> ann=announcementsService.findByCategory(2, 0, 15);
		mv.addObject("sec1",ann);
		
		mv.addObject("sec1latest",announcementsService.findByCategory(2, 0, 1));
		MasterDataDto mav = new MasterDataDto();
		 mav=userRepository.fetchStaus(user.getEmail());
		if(id==0) {
			if(ann!=null)
			mv.addObject("sec1ViewOne",ann.get(0));
		}
		else {
			mv.addObject("sec1ViewOne",announcementsService.findOne(id));
		}	
		mv.setViewName("section1-read");
		return mv;
	}
	
	
	
	
	@RequestMapping(value = "/section2Details")
	public ModelAndView getSection2() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("sec2", announcementsService.findByCategory(GlobalConstants.SECTIONTWO, 0, 3));
		mv.setViewName("section2");
		return mv;
	}
	
	
	@RequestMapping(value = "/sec2-read/{id}")
	public ModelAndView section2ViewAll(@PathVariable ("id") Long id) {
		ModelAndView mv=new ModelAndView();
		user = (CognitoUser) httpSession.getAttribute("user");
		List<Announcements> ann=announcementsService.findByCategory(3, 0, 15);
		mv.addObject("sec2",ann);
		
		mv.addObject("sec2latest",announcementsService.findByCategory(3, 0, 1));
		if(id==0) {
			if(ann!=null)
			mv.addObject("sec2ViewOne",ann.get(0));
		}
		else {
			mv.addObject("sec2ViewOne",announcementsService.findOne(id));
		}	
		mv.setViewName("section2-read");
		return mv;
	}
	
	
	@RequestMapping(value = "/section3Details")
	public ModelAndView getSection3() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("sec3", announcementsService.findByCategory(GlobalConstants.SECTIONTHREE, 0, 3));
		mv.setViewName("section3");
		return mv;
	}
	
	
	@RequestMapping(value = "/sec3-read/{id}")
	public ModelAndView section3ViewAll(@PathVariable ("id") Long id) {
		ModelAndView mv=new ModelAndView();
		user = (CognitoUser) httpSession.getAttribute("user");
		List<Announcements> ann=announcementsService.findByCategory(4, 0, 15);
		mv.addObject("sec3",ann);
		
		mv.addObject("sec3latest",announcementsService.findByCategory(4, 0, 1));
		if(id==0) {
			if(ann!=null)
			mv.addObject("sec3ViewOne",ann.get(0));
		}
		else {
			mv.addObject("sec3ViewOne",announcementsService.findOne(id));
		}	
		mv.setViewName("section3-read");
		return mv;
	}
	
	
	@RequestMapping(value = "/section4Details")
	public ModelAndView getSection4() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("sec4", announcementsService.findByCategory(GlobalConstants.SECTIONFOUR, 0, 3));
		mv.setViewName("section4");
		return mv;
	}
	
	
	@RequestMapping(value = "/sec4-read/{id}")
	public ModelAndView section4ViewAll(@PathVariable ("id") Long id) {
		ModelAndView mv=new ModelAndView();
		user = (CognitoUser) httpSession.getAttribute("user");
		List<Announcements> ann=announcementsService.findByCategory(5, 0, 15);
		mv.addObject("sec4",ann);
		
		mv.addObject("sec4latest",announcementsService.findByCategory(5, 0, 1));
		if(id==0) {
			if(ann!=null)
			mv.addObject("sec4ViewOne",ann.get(0));
		}
		else {
			mv.addObject("sec4ViewOne",announcementsService.findOne(id));
		}	
		mv.setViewName("section4-read");
		return mv;
	}
//	@RequestMapping(value = "/birthdatEventsDetails")
//	public ModelAndView getBirthdatEventsDetails() {
//		ModelAndView mv = new ModelAndView();
//		mv.addObject("bes", announcementsService.findByCategory(GlobalConstants.SECTIONTWO, 0, 3));
//		mv.setViewName("birthdayEventsDetails");
//		return mv;
//	}
	
	
	
//	@RequestMapping(value = "/bes-read/{id}")
//	public ModelAndView knowledgeViewAll(@PathVariable ("id") Long id) {
//		ModelAndView mv=new ModelAndView();
//		user = (CognitoUser) httpSession.getAttribute("user");
//		List<Announcements> bes=announcementsService.findByCategory(3, 0, 15);
//		mv.addObject("bes",bes);
//		mv.addObject("beslatest",bes.get(0));
//		
//		if(id!=0) {
//			mv.addObject("besViewOne",announcementsService.findOne(id));
//		}
//		else {
//			mv.addObject("besViewOne",bes.get(0));
//		}
//		mv.setViewName("birthdayevent-read");
//		return mv;
//	}
}	
