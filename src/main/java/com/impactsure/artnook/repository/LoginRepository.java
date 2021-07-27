package com.impactsure.artnook.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.impactsure.artnook.entity.User_Login;
@Repository
public interface LoginRepository extends PagingAndSortingRepository<User_Login, Long> {

}
