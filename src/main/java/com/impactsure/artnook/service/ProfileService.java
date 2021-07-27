package com.impactsure.artnook.service;

import org.springframework.stereotype.Service;
import com.impactsure.artnook.dto.MasterProfileDto;

@Service
public interface ProfileService {
	public MasterProfileDto findByID(String id);
	public Integer update(MasterProfileDto profileDto);
}
