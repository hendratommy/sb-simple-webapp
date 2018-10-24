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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sa.sbsimplewebapp.model.form.AccountDataForm;
import com.sa.sbsimplewebapp.model.form.CifDataForm;
import com.sa.sbsimplewebapp.model.form.TypedRequestModel;

@RestController
public class EchoApiController {
	private static final Logger logger = LoggerFactory.getLogger(EchoApiController.class);

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping("/api/echo")
	public JsonNode echoGet(HttpServletRequest request) {
		logger.debug(request.getMethod() + " " + request.getRequestURI()
				+ (request.getQueryString() != null ? "?" + request.getQueryString() : ""));
		
		ObjectNode responseNode = JsonNodeFactory.instance.objectNode();
		responseNode.put("message", "ECHO OK");
		
		return responseNode;
	}
	
	@PostMapping("/api/echo")
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
	
	@PostMapping("/api/echo/test")
	public JsonNode echoTestPost(@RequestBody TypedRequestModel<AccountDataForm> requestModel, HttpServletRequest request, HttpSession session) {
		logger.debug(request.getMethod() + " " + request.getRequestURI()
				+ (request.getQueryString() != null ? "?" + request.getQueryString() : ""));
		
		ObjectMapper objectMapper = new ObjectMapper();
		String requestBody = null;
		try {
			requestBody = objectMapper.writeValueAsString(requestModel);

			logger.info("requestBody: " + requestBody);
			logger.info("accountNo: " + requestModel.getData().getAccountNo());
		} catch (JsonProcessingException e) {
			logger.error("Cannot write object as String", e);
		}
		
		
		ObjectNode responseNode = JsonNodeFactory.instance.objectNode();
		responseNode.put("message", "ECHO OK");
		responseNode.put("request", requestBody);
		
		return responseNode;
	}
	
	@PostMapping("/api/echo/test2")
	public JsonNode echoTest2Post(@RequestBody TypedRequestModel<CifDataForm> requestModel, HttpServletRequest request, HttpSession session) {
		logger.debug(request.getMethod() + " " + request.getRequestURI()
				+ (request.getQueryString() != null ? "?" + request.getQueryString() : ""));
		
		ObjectMapper objectMapper = new ObjectMapper();
		String requestBody = null;
		try {
			requestBody = objectMapper.writeValueAsString(requestModel);

			logger.info("requestBody: " + requestBody);
			logger.info("cifNo: " + requestModel.getData().getCifNo());
		} catch (JsonProcessingException e) {
			logger.error("Cannot write object as String", e);
		}
		
		
		ObjectNode responseNode = JsonNodeFactory.instance.objectNode();
		responseNode.put("message", "ECHO OK");
		responseNode.put("request", requestBody);
		
		return responseNode;
	}
}
