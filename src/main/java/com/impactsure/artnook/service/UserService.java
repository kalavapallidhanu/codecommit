package com.impactsure.artnook.service;

import org.springframework.stereotype.Service;

import com.impactsure.artnook.dto.MasterContactDto;

@Service
public interface UserService {
	public MasterContactDto findByEmail(String email);

	public String findLastLogin(String email);
}
