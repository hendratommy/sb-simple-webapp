package com.sa.sbsimplewebapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;

public interface UserRepositoryCustom {
	Page<User> searchAny(Pageable pageable, String keyword);
}
