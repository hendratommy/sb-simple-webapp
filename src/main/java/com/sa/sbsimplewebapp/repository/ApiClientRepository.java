package com.sa.sbsimplewebapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sa.sbsimplewebapp.model.domain.ApiClient;

public interface ApiClientRepository extends JpaRepository<ApiClient, Long> {
	@Query("SELECT c FROM ApiClient c WHERE c.username = :username")
	ApiClient findByUsername(@Param("username") String username);
}
