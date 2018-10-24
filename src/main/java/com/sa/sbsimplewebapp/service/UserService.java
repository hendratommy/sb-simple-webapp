package com.sa.sbsimplewebapp.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sa.sbsimplewebapp.model.domain.Privilege;
import com.sa.sbsimplewebapp.model.domain.Role;
import com.sa.sbsimplewebapp.model.domain.User;
import com.sa.sbsimplewebapp.repository.UserRepository;

@Service("customUserDetailsService")
public class UserService implements UserDetailsService {
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
    private UserRepository userRepository;
	
	@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("User " + username + " logged in");
        logger.debug("Loading user info for " + username);
        User user = userRepository.findByUsername(username);

        if (user == null) {
        	logger.warn("User not found: " + username);
            throw new BadCredentialsException("Bad credentials");
        }
        logger.debug("Found username: " + username + " with roles " + Arrays.toString(user.getRolesValue().toArray(new String[0])));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.isEnabled(),
                true, true, !user.isLocked(), getAuthorities(user.getRoles()));
    }

    public List<? extends GrantedAuthority> getAuthorities(Collection<Role> roles) {
        if (roles != null && !roles.isEmpty()) {
            List<GrantedAuthority> authorities = new ArrayList<>();
            for (Role role : roles) {
                authorities.add(new SimpleGrantedAuthority(role.getName()));
                if (role.getPrivileges() != null && !role.getPrivileges().isEmpty()) {
                    for (Privilege privilege : role.getPrivileges()) {
                        authorities.add(new SimpleGrantedAuthority(privilege.getName()));
                    }
                }
            }
            return authorities;
        }
        logger.warn("Role is empty!");
        return null;
    }
    
    public User findByUsername(String username) {
    	return userRepository.findByUsername(username);
    }
}
