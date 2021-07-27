package com.impactsure.artnook.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.impactsure.artnook.dto.CognitoUser;
import com.impactsure.artnook.repository.UserRepository;

@Controller
@RequestMapping(value = "/artnook")
public class IndexController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private HttpSession httpSession;

	@RequestMapping(value = "/logout")
	public ModelAndView logout(HttpServletRequest request) {

		ModelAndView mv = new ModelAndView();
		HttpSession session = request.getSession();
		CognitoUser cognitoUser = (CognitoUser) httpSession.getAttribute("user");
		// User_Login history=new User_Login();
		cognitoUser.setLogoutTime(new Date());
		userRepository.update(cognitoUser);
		session.removeAttribute("user");
		session.invalidate();
		mv.setViewName("index");
		return mv;
	}

}
