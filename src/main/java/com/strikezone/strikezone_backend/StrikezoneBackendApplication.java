package com.strikezone.strikezone_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
public class StrikezoneBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(StrikezoneBackendApplication.class, args);
	}

}
