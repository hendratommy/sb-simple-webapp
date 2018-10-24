package com.sa.sbsimplewebapp;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.sa.sbsimplewebapp.service.SystemService;

@SpringBootApplication
@ComponentScan(basePackages={"com.sa.sbsimplewebapp"})
public class Application implements CommandLineRunner {
	private static final Logger logger = LoggerFactory.getLogger(Application.class);
	
	@Autowired
	private SystemService systemService;
	
	public static void main(String[] args) {
		logger.info("Starting application");
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if (args != null && args.length > 0) {
			for (String arg : args) {
				if (arg.equals("--install")) {
					logger.info("Starting install");
					
					List<GrantedAuthority> authorities = new ArrayList<>();
					authorities.add(new SimpleGrantedAuthority("ROLE_SYSTEM"));
					
					Authentication authentication =  new UsernamePasswordAuthenticationToken("SYSTEM", null, authorities);
					SecurityContextHolder.getContext().setAuthentication(authentication);
					
					systemService.install();
					logger.info("Install complete");
				}
	        }
		}
	}
}
