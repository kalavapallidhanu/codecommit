package com.impactsure.artnook.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.impactsure.artnook.dto.CognitoUser;
import com.impactsure.artnook.dto.CompetitionDto;
import com.impactsure.artnook.dto.MasterCompetitionTableDto;
import com.impactsure.artnook.entity.Competition;
import com.impactsure.artnook.entity.Customer;
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
public class CompetitionController {
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

	@RequestMapping("/newCompetition")
	public String newContact(Model model) {
		Long isActive = eventDetailsRepository.eventCount((long) 1);
		CompetitionDto competition = new CompetitionDto();
		model.addAttribute("competition", competition);
		if (isActive != 0) {
			return "competitionform";
		} else {
			return "viewAllCompetitionDetails";
		}

	}

	@RequestMapping("/viewAllCompetition")
	public String viewAllContact(Model model) {
		MasterCompetitionTableDto master = new MasterCompetitionTableDto();

		model.addAttribute("master", master);
		return "viewAllCompetitionDetails";
	}

	@RequestMapping("/getCompetitionData")
	@ResponseBody
	public List<Competition> getCompetitionData() {
		user = (CognitoUser) httpSession.getAttribute("user");
		Pageable pageable = PageRequest.of(0, 10, Sort.by(Order.desc("id")));

		Slice<Competition> slicedResult = competitionRepository
				.findByCustomerId(user.getMasterContactDto().getContactId(), pageable);
		List<Competition> competition = slicedResult.getContent();
		return competition;
	}

	@RequestMapping("/getCompetitionAllUserData")
	@ResponseBody
	public List<Competition> getCompetitionAllUserData() {
		return (List<Competition>) competitionRepository.findAll();
	}

	@RequestMapping("/viewAllCompetitionAdmin")
	public String viewAllCompetitionAdmin(Model model) {
		MasterCompetitionTableDto master = new MasterCompetitionTableDto();

		model.addAttribute("master", master);
		return "viewAllCompetitionDetailsAdmin";
	}

	@RequestMapping("/checkCompetitionStatus")
	@ResponseBody
	public Long getCompetitionStatus() {
		return eventDetailsRepository.eventCount((long) 1);
	}

	@RequestMapping(value = "/saveCompetition", method = RequestMethod.POST)
	public String saveCompetition(@ModelAttribute("competition") CompetitionDto competitiondto) {

		user = (CognitoUser) httpSession.getAttribute("user");

		Competition competition = new Competition();
		Customer contact = new Customer();

		String imageUrl = this.amazonS3ClientService.uploadCompetitionImageToS3Bucket(competitiondto, true);

		contact = masterDataRepository.findByMailId(user.getMasterContactDto().getEmail());

		Eventdetails eventdetails = new Eventdetails();

		Pageable pageable = PageRequest.of(0, 10, Sort.by(Order.desc("id")));

		Slice<Eventdetails> slicedResult = eventDetailsRepository.findByIsActive((long) 1, pageable);
		List<Eventdetails> eventdetailsList = slicedResult.getContent();
		eventdetails = eventdetailsList.get(0);
		try {

			competition
					.setArtUrl("<img src='https://artnookschools.s3.ap-south-1.amazonaws.com/" + imageUrl + "'</img>");
			competition.setArtDescription(competitiondto.getArtDescription());
			competition.setCustomer(contact);
			competition.setEventdetails(eventdetails);

			competitionRepository.save(competition);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "redirect:newCompetition";
//			return "redirect:newCompetition";
	}

}