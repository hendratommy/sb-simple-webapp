package com.sa.sbsimplewebapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sa.sbsimplewebapp.model.domain.User;

/**
 * Created by hendr on 02/07/2017.
 */
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
	@Query("SELECT u FROM User u LEFT JOIN FETCH u.roles")
	List<User> findAllEager();

	@Query("SELECT u FROM User u LEFT JOIN FETCH u.roles r LEFT JOIN FETCH r.privileges rp WHERE u.id = :id")
	User findOneEager(@Param("id") Long id);

	@Query("SELECT u FROM User u WHERE u.username = :username")
	User findByUsername(@Param("username") String username);

	@Query("SELECT u FROM User u LEFT JOIN FETCH u.roles r LEFT JOIN FETCH r.privileges rp WHERE u.username = :username")
	User findByUsernameEager(@Param("username") String username);
}
