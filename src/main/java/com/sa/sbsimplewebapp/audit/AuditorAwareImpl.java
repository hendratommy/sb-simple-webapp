package com.sa.sbsimplewebapp.audit;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

public class AuditorAwareImpl implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			return Optional.empty();
		}
		return authentication.getPrincipal() instanceof User
                ? Optional.of(((User) authentication.getPrincipal()).getUsername()) : Optional.of(authentication.getPrincipal().toString());
	}

}
