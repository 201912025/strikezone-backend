package com.strikezone.strikezone_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })@EnableJpaAuditing
public class StrikezoneBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(StrikezoneBackendApplication.class, args);
	}

}
