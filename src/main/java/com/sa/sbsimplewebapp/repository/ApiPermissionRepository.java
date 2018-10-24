package com.sa.sbsimplewebapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sa.sbsimplewebapp.model.domain.ApiPermission;

/**
 * Created by hendr on 02/07/2017.
 */
public interface ApiPermissionRepository extends JpaRepository<ApiPermission, Long> {
	@Query("SELECT p FROM ApiPermission p WHERE p.name = :name")
	ApiPermission findByName(@Param("name") String name);
}
