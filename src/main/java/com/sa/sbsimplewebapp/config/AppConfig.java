package com.sa.sbsimplewebapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.EnableLoadTimeWeaving;
import org.springframework.context.annotation.EnableLoadTimeWeaving.AspectJWeaving;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;

@Configuration
@EnableAspectJAutoProxy
@EnableLoadTimeWeaving(aspectjWeaving=AspectJWeaving.ENABLED)
public class AppConfig {
	@Bean
	public InstrumentationLoadTimeWeaver loadTimeWeaver() throws Throwable {
	    InstrumentationLoadTimeWeaver loadTimeWeaver = new InstrumentationLoadTimeWeaver();
	    return loadTimeWeaver;
	}
}