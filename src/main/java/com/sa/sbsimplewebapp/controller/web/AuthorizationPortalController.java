package com.sa.sbsimplewebapp.controller.web;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.sa.sbsimplewebapp.model.form.LoginForm;
import com.sa.sbsimplewebapp.support.serializer.PrincipalSerializer;

@Controller
public class AuthorizationPortalController {
	private static final Logger logger = LoggerFactory.getLogger(AuthorizationPortalController.class);
	
	@GetMapping("/")
	public String index(HttpServletRequest request, Authentication authentication, Model uiModel) {
		logger.debug(request.getMethod() + " " + request.getRequestURI()
				+ (request.getQueryString() != null ? "?" + request.getQueryString() : ""));

		/*
		if (authentication == null) {
			logger.debug("Auth null");
			uiModel.addAttribute("loginForm", new LoginForm());
			return "redirect:/login";
		}
		uiModel.addAttribute("authentication", authentication);
		return "redirect:/dashboard";
		*/
		return "redirect:/public";
	}
	
	@GetMapping("/login")
	public String loginPage(HttpServletRequest request, Authentication authentication, Model uiModel) {
		logger.debug(request.getMethod() + " " + request.getRequestURI()
				+ (request.getQueryString() != null ? "?" + request.getQueryString() : ""));

		if (authentication != null) return "redirect:/dashboard";
		uiModel.addAttribute("loginForm", new LoginForm());
		return "authorization/login";
	}
	
	@PostMapping("/forward_login")
	public String forwardToLoginPage(HttpServletRequest request, Model uiModel, LoginForm loginForm, BindingResult bindingResult) {
		logger.debug("Forwarding request to /forward_login from " + request.getMethod() + " " + request.getRequestURI()
				+ (request.getQueryString() != null ? "?" + request.getQueryString() : ""));
		
		Exception authenticationException = (Exception)request.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		if (authenticationException != null) {
			if (authenticationException.getMessage().equals("Bad credentials")) {
				uiModel.addAttribute("exception", "authentication.exception.badCredentials");
			}
			else {
				logger.warn("Authentication failed with message: " + authenticationException.getMessage(), authenticationException);
				uiModel.addAttribute("exception", "authentication.exception");
			}
		}
		uiModel.addAttribute("loginForm", loginForm);
		return "authorization/login";
	}
	
	@GetMapping("/user")
	public @ResponseBody JsonNode getPrincipal(HttpServletRequest request, Model uiModel, Authentication authentication) {
		logger.debug(request.getMethod() + " " + request.getRequestURI()
				+ (request.getQueryString() != null ? "?" + request.getQueryString() : ""));

		ObjectMapper objectMapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addSerializer(org.springframework.security.core.userdetails.User.class, new PrincipalSerializer());
		objectMapper.registerModule(module);
		org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User)authentication.getPrincipal();
		return objectMapper.valueToTree(user);
	}
}
