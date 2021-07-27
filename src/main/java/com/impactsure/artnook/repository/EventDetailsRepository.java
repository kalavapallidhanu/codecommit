package com.impactsure.artnook.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.impactsure.artnook.entity.Eventdetails;

@Repository
public interface EventDetailsRepository extends JpaRepository<Eventdetails, Long>{
	@Query(value = "SELECT eventdetails FROM Eventdetails eventdetails  where eventdetails.availableFrom <= now() and eventdetails.availableTo >= now() and eventdetails.isActive = ?1 ")
	Page<Eventdetails> findByIsActive(Long isActive, Pageable pageable);
	
	@Query(value = "SELECT count(id) FROM Eventdetails  where availableFrom <= now() and availableTo >= now() and isActive = ?1 ")
    public Long eventCount(Long isActive);
}
