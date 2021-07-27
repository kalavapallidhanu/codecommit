package com.impactsure.artnook.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.impactsure.artnook.entity.Announcements;

@Repository
public interface AnnouncementsRepository extends PagingAndSortingRepository<Announcements, Long>{
	/*
	 * Added By Gufran
	 * Select details based on category and where active =1
	 */
	@Query("SELECT   a  FROM Announcements a where a.category =?1 AND a.active=1")
	Slice<Announcements> findByCategory(Integer category,Pageable pageable);
}
