package com.sa.sbsimplewebapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import com.sa.sbsimplewebapp.service.ApiClientService;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class WebApiSecurityConfig extends WebSecurityConfigurerAdapter {
	@Value("${sb.simplewebapp.api.security.default_realm:DEFAULT_REALM}")
	private String defaultRealm;
	
	@Autowired
	private ApiClientService apiClientService;

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.requestMatchers().antMatchers("/api/**", "/actuator/**").and().csrf().disable().cors().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and().authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
				.antMatchers("/api/echo").permitAll()
				.antMatchers("/actuator/**").hasAuthority("ACTUATOR_CLIENT")
				.anyRequest().authenticated()
			.and().httpBasic().realmName(defaultRealm).authenticationEntryPoint(basicAuthenticationEntryPoint())
			.and().formLogin().disable().logout().disable();
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(apiClientService);
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Bean
	public BasicAuthenticationEntryPoint basicAuthenticationEntryPoint() {
		CustomBasicAuthenticationEntryPoint basicEntryPoint = new CustomBasicAuthenticationEntryPoint();
		basicEntryPoint.setRealmName(defaultRealm);
		
		return basicEntryPoint;
	}
	
}
