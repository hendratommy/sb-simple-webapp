package com.sa.sbsimplewebapp.service;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sa.sbsimplewebapp.exception.SystemServiceInstallException;
import com.sa.sbsimplewebapp.model.domain.ApiClient;
import com.sa.sbsimplewebapp.model.domain.ApiPermission;
import com.sa.sbsimplewebapp.model.domain.Privilege;
import com.sa.sbsimplewebapp.model.domain.Role;
import com.sa.sbsimplewebapp.model.domain.User;
import com.sa.sbsimplewebapp.repository.ApiClientRepository;
import com.sa.sbsimplewebapp.repository.ApiPermissionRepository;
import com.sa.sbsimplewebapp.repository.PrivilegeRepository;
import com.sa.sbsimplewebapp.repository.RoleRepository;
import com.sa.sbsimplewebapp.repository.UserRepository;

@Service
@Transactional(propagation=Propagation.SUPPORTS)
public class SystemService {
	private static final Logger logger = LoggerFactory.getLogger(SystemService.class);
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PrivilegeRepository privilegeRepository;
	@Autowired
	private ApiClientRepository clientRepository;
	@Autowired
	private ApiPermissionRepository permissionRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@SuppressWarnings("serial")
	@Transactional(propagation=Propagation.REQUIRED)
	public void install() {
		long userCount = userRepository.count();
		if (userCount > 0) throw new SystemServiceInstallException("There are existing user or any other records. If you want to begin from scratch try to TRUNCATE or DROP tables first.");
		
		logger.info("Installing Privileges");
		List<Privilege> privileges = new ArrayList<Privilege>() {{
			add(new Privilege("CREATE_USERS"));
			add(new Privilege("UPDATE_USERS"));
			add(new Privilege("DELETE_USERS"));
			add(new Privilege("CREATE_ROLES"));
			add(new Privilege("UPDATE_ROLES"));
			add(new Privilege("DELETE_ROLES"));
			add(new Privilege("CREATE_CLIENTS"));
			add(new Privilege("UPDATE_CLIENTS"));
			add(new Privilege("DELETE_CLIENTS"));
		}};
		privileges = privilegeRepository.saveAll(privileges);
		
		List<Privilege> readPrivileges = new ArrayList<Privilege>() {{
			add(new Privilege("READ_USERS"));
			add(new Privilege("READ_ROLES"));
			add(new Privilege("READ_CLIENTS"));
		}};
		readPrivileges = privilegeRepository.saveAll(readPrivileges);
		
		privileges.addAll(readPrivileges);
		
		logger.info("Installing Roles");
		Role administratorsRole = new Role("ROLE_ADMINISTRATORS");
		administratorsRole.setPrivileges(new HashSet<>(privileges));
		administratorsRole = roleRepository.save(administratorsRole);
		
		Role usersRole = new Role("ROLE_USERS");
		usersRole.setPrivileges(new HashSet<>(readPrivileges));
		usersRole = roleRepository.save(usersRole);
		
		logger.info("Installing Users");
		User admin = new User();
		admin.setUsername("admin");
		admin.setEmail("admin@usermail.com");
		admin.setPassword(passwordEncoder.encode("admin"));
		admin.setName("admin");
		admin.setEnabled(true);
		admin.setLocked(false);
		admin.addRole(administratorsRole);
		userRepository.save(admin);
		
		User user = new User();
		user.setUsername("user");
		user.setEmail("user@usermail.com");
		user.setPassword(passwordEncoder.encode("user"));
		user.setName("user");
		user.setEnabled(true);
		user.setLocked(false);
		user.addRole(usersRole);
		userRepository.save(user);
		
		logger.info("Installing API Permissions");
		List<ApiPermission> permissions = new ArrayList<ApiPermission>() {{
			add(new ApiPermission("CREATE_USERS"));
			add(new ApiPermission("UPDATE_USERS"));
			add(new ApiPermission("DELETE_USERS"));
		}};
		permissions = permissionRepository.saveAll(permissions);
		
		ApiPermission readPermission = new ApiPermission("READ_USERS");
		readPermission = permissionRepository.save(readPermission);
		
		permissions.add(readPermission);
		
		logger.info("Installing Clients");
		ApiClient apiClient = new ApiClient();
		apiClient.setUsername("api_client");
		apiClient.setPassword(passwordEncoder.encode("api_client"));
		apiClient.setEnabled(true);
		apiClient.setLocked(false);
		apiClient.setApiPermissions(new HashSet<>(permissions));
		clientRepository.save(apiClient);
		
		apiClient = new ApiClient();
		apiClient.setUsername("read_client");
		apiClient.setPassword(passwordEncoder.encode("read_client"));
		apiClient.setEnabled(true);
		apiClient.setLocked(false);
		apiClient.addApiPermission(readPermission);
		clientRepository.save(apiClient);
		
		ApiPermission actuatorPermission = new ApiPermission("ACTUATOR_CLIENT");
		actuatorPermission = permissionRepository.save(actuatorPermission);
		
		apiClient = new ApiClient();
		apiClient.setUsername("actuator_client");
		apiClient.setPassword(passwordEncoder.encode("actuator_client"));
		apiClient.setEnabled(true);
		apiClient.setLocked(false);
		apiClient.addApiPermission(actuatorPermission);
		clientRepository.save(apiClient);
	}
	
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=SystemServiceInstallException.class)
	public void stepOne() throws Exception {
		ApiClient apiClient = new ApiClient();
		apiClient.setUsername("stepOne");
		apiClient.setPassword(passwordEncoder.encode("stepOne"));
		apiClient.setEnabled(true);
		apiClient.setLocked(false);
		clientRepository.save(apiClient);
		
		stepTwo();
		
		// throw new NoRollbackException("Won't rollback");
		throw new SystemServiceInstallException("Will rollback");
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void stepTwo() {
		ApiClient apiClient = new ApiClient();
		apiClient.setUsername("stepTwo");
		apiClient.setPassword(passwordEncoder.encode("stepTwo"));
		apiClient.setEnabled(true);
		apiClient.setLocked(false);
		clientRepository.save(apiClient);
	}
}
