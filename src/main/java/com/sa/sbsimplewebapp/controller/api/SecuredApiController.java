package com.sa.sbsimplewebapp.controller.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
public class SecuredApiController {
	private static final Logger logger = LoggerFactory.getLogger(SecuredApiController.class);

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping("/api/secure/echo")
	public JsonNode echoGet(HttpServletRequest request) {
		logger.debug(request.getMethod() + " " + request.getRequestURI()
				+ (request.getQueryString() != null ? "?" + request.getQueryString() : ""));
		
		ObjectNode responseNode = JsonNodeFactory.instance.objectNode();
		responseNode.put("message", "ECHO OK");
		
		return responseNode;
	}
	
	@PostMapping("/api/secure/echo")
	public JsonNode echoPost(@RequestBody JsonNode json, HttpServletRequest request, HttpSession session) {
		logger.debug(request.getMethod() + " " + request.getRequestURI()
				+ (request.getQueryString() != null ? "?" + request.getQueryString() : ""));
		
		ObjectNode responseNode = JsonNodeFactory.instance.objectNode();
		responseNode.put("message", "ECHO OK");
		responseNode.set("request", json);
		
		if (json.get("password") != null) {
			responseNode.put("hashed_password", passwordEncoder.encode(json.get("password").asText()));
		}
		
		return responseNode;
	}
}
