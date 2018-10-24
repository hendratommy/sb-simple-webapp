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

import com.sa.sbsimplewebapp.model.domain.ApiClient;
import com.sa.sbsimplewebapp.model.domain.ApiPermission;
import com.sa.sbsimplewebapp.repository.ApiClientRepository;

@Service("clientDetailsService")
public class ApiClientService implements UserDetailsService {
	private static final Logger logger = LoggerFactory.getLogger(ApiClientService.class);
	
	@Autowired
    private ApiClientRepository clientRepository;
	
	@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("Client " + username + " logged in");
        logger.debug("Loading client info for " + username);
        ApiClient client = clientRepository.findByUsername(username);

        if (client == null) {
        	logger.warn("Client not found: " + username);
        	throw new BadCredentialsException("Bad credentials");
        }
        logger.debug("Found client: " + username + " with permissions " + Arrays.toString(client.getApiPermissionsValue().toArray(new String[0])));
        return setupUserDetails(client);
    }
	
	protected org.springframework.security.core.userdetails.User setupUserDetails(ApiClient client) {
		return new org.springframework.security.core.userdetails.User(client.getUsername(), client.getPassword(), client.isEnabled(),
                true, true, !client.isLocked(), getAuthorities(client.getApiPermissions()));
	}

    public List<? extends GrantedAuthority> getAuthorities(Collection<ApiPermission> permissions) {
        if (permissions != null && !permissions.isEmpty()) {
            List<GrantedAuthority> authorities = new ArrayList<>();
            for (ApiPermission permission : permissions) {
                authorities.add(new SimpleGrantedAuthority(permission.getName()));
            }
            return authorities;
        }
        logger.warn("Permissions is empty!");
        return null;
    }
}
