package com.sa.sbsimplewebapp.repository.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sa.sbsimplewebapp.repository.UserRepositoryCustom;

@Repository
@Transactional(readOnly=true)
public class UserRepositoryImpl implements UserRepositoryCustom {
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(UserRepositoryImpl.class);

	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Override
	public Page<User> searchAny(Pageable pageable, String keyword) {
		String queryStr = "SELECT DISTINCT u FROM User u LEFT JOIN u.roles r "
				+ "WHERE ((u.username LIKE CONCAT('%', :keyword, '%')) OR (u.name LIKE CONCAT('%', :keyword, '%')) "
				+ "OR (r.name LIKE CONCAT('%', :keyword, '%')))";
		String orderBy = " ORDER BY %s %s";

		Sort sort = pageable.getSort();
		Order order = sort != null ? sort.iterator().next() : null;
		if (order != null) {
			if (order.getProperty().equals("roles")) {
				queryStr = queryStr + String.format(orderBy, "r.name", order.isAscending() ? "ASC" : "DESC");
			}
			else {
				queryStr = queryStr + String.format(orderBy, "u." + order.getProperty(), order.isAscending() ? "ASC" : "DESC");
			}
		}

		Query query = entityManager.createQuery(queryStr, User.class);
		query.setParameter("keyword", keyword);

		// get total rows for page
		int totalRows = query.getResultList().size();

		query.setFirstResult((int) pageable.getOffset());
		query.setMaxResults(pageable.getPageSize());

		return new PageImpl<User>(query.getResultList(), pageable, totalRows);
	}

}
