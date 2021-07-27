	package com.impactsure.artnook.controller;

	import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.impactsure.artnook.dto.CognitoUser;
import com.impactsure.artnook.dto.EventDetailsDto;
import com.impactsure.artnook.dto.MasterCompetitionTableDto;
import com.impactsure.artnook.entity.Eventdetails;
import com.impactsure.artnook.repository.CompetitionRepository;
import com.impactsure.artnook.repository.ContactRepository;
import com.impactsure.artnook.repository.EventDetailsRepository;
import com.impactsure.artnook.repository.MasterDataRepository;
import com.impactsure.artnook.repository.UserRepository;
import com.impactsure.artnook.service.AmazonS3ClientService;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

	@Controller
	@EnableEncryptableProperties
	@RequestMapping(value = "/artnook")
	public class EventDetailsController {
		@Autowired
		private HttpSession httpSession;
		
		@Autowired
		private AmazonS3ClientService amazonS3ClientService;
		
		private CognitoUser user;
		
		@Autowired
		private MasterDataRepository masterDataRepository;
		
		@Autowired
		private UserRepository userRepository;
		
		@Autowired
		ContactRepository contactRepository;

		@Autowired
		CompetitionRepository competitionRepository;
		
		@Autowired
		EventDetailsRepository eventDetailsRepository;

		@RequestMapping("/newEventDetails")
		public String newContact(Model model) {
			EventDetailsDto eventDetailsDto = new EventDetailsDto();
			model.addAttribute("eventDetailsDto", eventDetailsDto);
			return "eventdetails";
		}
		
		@RequestMapping("/viewAllEventDetails")
		public String viewAllEventDetails(Model model) {
			MasterCompetitionTableDto master = new MasterCompetitionTableDto();

			model.addAttribute("master", master);
			return "viewAllEventDetails";
		}
		
		
		@RequestMapping("/getEventDetailsData")
		@ResponseBody
		public List<Eventdetails> getEventDetailsData() {
			return (List<Eventdetails>) eventDetailsRepository.findAll();
		}

		@RequestMapping("/editEventDetails/{id}")
		public ModelAndView editEventDetails(@PathVariable(name = "id") String id) {
			ModelAndView mav = new ModelAndView();
			Long eventid=Long. parseLong(id);
			Optional<Eventdetails> eventdetailsList = eventDetailsRepository.findById(eventid);
			Eventdetails eventdetails=new Eventdetails();
			eventdetails=eventdetailsList.get();
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String availableFrom = dateFormat.format(eventdetails.getAvailableFrom());
			String availableTo = dateFormat.format(eventdetails.getAvailableTo());
		/* String eventId = String.valueOf(eventdetails.getId()); */
			EventDetailsDto eventDetailsDto = new EventDetailsDto();
			
			eventDetailsDto.setId(eventdetails.getId());
			eventDetailsDto.setEventName(eventdetails.getEventName());
			eventDetailsDto.setEventDescription(eventdetails.getEventDescription());
			eventDetailsDto.setAvailableFrom(availableFrom);
			eventDetailsDto.setAvailableTo(availableTo);
			eventDetailsDto.setIsActive(eventdetails.getIsActive());
			mav.addObject("eventDetailsDto", eventDetailsDto);
			mav.setViewName("eventdetails");
			return mav;
		}

	
		@RequestMapping(value = "/saveEventDetails", method = RequestMethod.POST)
		public String saveEventDetails(@ModelAttribute("eventDetailsDto") EventDetailsDto eventDetailsDto) {
			
			
			
			Eventdetails eventdetails = new Eventdetails();
			
			try {
				
				Date availableFrom=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(eventDetailsDto.getAvailableFrom()+":00");
      			Date availableTo=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(eventDetailsDto.getAvailableTo()+":00");

				
				if (eventDetailsDto.getId() != null) {

					eventdetails.setId(eventDetailsDto.getId());
					eventdetails.setEventName(eventDetailsDto.getEventName());
					eventdetails.setEventDescription(eventDetailsDto.getEventDescription());
					eventdetails.setAvailableFrom(availableFrom);
					eventdetails.setAvailableTo(availableTo);
					eventdetails.setIsActive(eventDetailsDto.getIsActive());
					eventDetailsRepository.save(eventdetails);
					
				}
				else {
					eventdetails.setEventName(eventDetailsDto.getEventName());
					eventdetails.setEventDescription(eventDetailsDto.getEventDescription());
					eventdetails.setAvailableFrom(availableFrom);
					eventdetails.setAvailableTo(availableTo);
					
					eventdetails.setIsActive(eventDetailsDto.getIsActive());
					eventDetailsRepository.save(eventdetails);
				}
				
				
			
				


			 } catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			return "redirect:newEventDetails"; 
//			return "redirect:newCompetition";
		}



		
		




		
	}