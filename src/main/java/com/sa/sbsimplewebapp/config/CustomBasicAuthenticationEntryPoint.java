package com.sa.sbsimplewebapp.config;


import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

public class CustomBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {
	private static final Logger logger = LoggerFactory.getLogger(CustomBasicAuthenticationEntryPoint.class);
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		// Authentication failed, send error response.
		if (authException instanceof BadCredentialsException || authException instanceof UsernameNotFoundException) {
			logger.warn("Authentication failed", authException);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
			response.getWriter().println("{\"status\": \"" + HttpStatus.UNAUTHORIZED + "\", \"message\": \"" + authException.getMessage() + "\"}");
		}
		else {
			logger.error("[FATAL] Cannot authenticate", authException);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
			response.getWriter().println("{\"status\": \"" + HttpStatus.UNAUTHORIZED + "\", \"message\": \"" + authException.getMessage() + "\"}");
		}
	}
}