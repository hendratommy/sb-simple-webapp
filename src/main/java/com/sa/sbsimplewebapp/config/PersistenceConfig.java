package com.sa.sbsimplewebapp.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.sa.sbsimplewebapp.audit.AuditorAwareImpl;


@Configuration
@EnableTransactionManagement(mode=AdviceMode.ASPECTJ)
@EnableJpaRepositories(basePackages = {"com.sa.sbsimplewebapp.repository"})
@EnableJpaAuditing(auditorAwareRef="auditorProvider", modifyOnCreate=false)
@EntityScan(basePackages = {"com.sa.sbsimplewebapp.model.domain"})
public class PersistenceConfig {
	@Bean
	AuditorAware<String> auditorProvider() {
		return new AuditorAwareImpl();
	}
}
