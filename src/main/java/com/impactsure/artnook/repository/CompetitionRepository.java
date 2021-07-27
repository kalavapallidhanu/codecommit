package com.impactsure.artnook.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.impactsure.artnook.entity.Competition;

@Repository
public interface CompetitionRepository extends JpaRepository<Competition, Long>{
		
	Page<Competition> findByCustomerId(String id, Pageable pageable);
}
