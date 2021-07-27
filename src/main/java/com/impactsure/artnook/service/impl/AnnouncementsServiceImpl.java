package com.impactsure.artnook.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.impactsure.artnook.entity.Announcements;
import com.impactsure.artnook.repository.AnnouncementsRepository;
import com.impactsure.artnook.service.AnnouncementsService;
@Service
public class AnnouncementsServiceImpl implements AnnouncementsService{
	
	@Autowired
	private AnnouncementsRepository announcementsRepository;

	@Override
	public Iterable<Announcements> findAll() {
		return announcementsRepository.findAll();
	}

	@Override
	public Announcements save(Announcements t) {
		return announcementsRepository.save(t);
	}

	@Override
	public void delete(Announcements t) {
		announcementsRepository.delete(t);
	}

	@Override
	public Announcements findOne(Long t) {
		return announcementsRepository.findById(t).get();
	}

	@Override
	public List<Announcements> findByCategory(Integer category,Integer pageNo,Integer pageSize) {
		/*
		 * Added By Gufran
		 * Sort based on orderSequence
		 */
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by("displayOrder"));
		Slice<Announcements> slicedResult = announcementsRepository.findByCategory(category, paging);
		List<Announcements> announcements = slicedResult.getContent();
		return announcements;
	}


	
	

}
