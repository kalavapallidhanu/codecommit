package com.impactsure.artnook.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.impactsure.artnook.entity.Announcements;

@Service
public interface AnnouncementsService extends GenericService<Announcements,Long>{
	List<Announcements> findByCategory(Integer category,Integer pageNo,Integer pageSize);
}
