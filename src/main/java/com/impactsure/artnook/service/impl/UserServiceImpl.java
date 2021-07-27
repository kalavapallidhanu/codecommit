package com.impactsure.artnook.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.impactsure.artnook.dto.MasterContactDto;
import com.impactsure.artnook.repository.UserRepository;
import com.impactsure.artnook.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public MasterContactDto findByEmail(String email) {
		// TODO Auto-generated method stub
		return userRepository.findByEmail(email);
	}

	@Override
	public String findLastLogin(String email) {
		// TODO Auto-generated method stub
		return userRepository.findLastLogin(email);
	}

}
