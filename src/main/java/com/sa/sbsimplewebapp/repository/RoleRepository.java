package com.sa.sbsimplewebapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sa.sbsimplewebapp.model.domain.Role;

/**
 * Created by hendr on 02/07/2017.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
	@Query("SELECT r FROM Role r WHERE r.name = :name")
	Role findByName(@Param("name") String name);
}
