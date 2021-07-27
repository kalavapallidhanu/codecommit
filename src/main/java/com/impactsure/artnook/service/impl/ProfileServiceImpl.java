package com.impactsure.artnook.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.impactsure.artnook.dto.MasterProfileDto;
import com.impactsure.artnook.repository.ProfileRespository;
import com.impactsure.artnook.service.ProfileService;
@Service
public class ProfileServiceImpl  implements ProfileService{
	
	@Autowired
	private ProfileRespository profileRepository;

	@Override
	public MasterProfileDto findByID(String id) {
		return profileRepository.findByID(id);
	}

	@Override
	public Integer update(MasterProfileDto profileDto) {
		// TODO Auto-generated method stub
		return profileRepository.update(profileDto);
	}
	
	
}