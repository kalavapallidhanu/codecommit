package com.impactsure.artnook.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.impactsure.artnook.entity.Customer;
@Repository
public interface ContactRepository  extends PagingAndSortingRepository<Customer, Long>{


	
}
