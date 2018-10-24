package com.sa.sbsimplewebapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sa.sbsimplewebapp.model.domain.Privilege;

/**
 * Created by hendr on 02/07/2017.
 */
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
}
