package com.strikezone.strikezone_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class StrikezoneBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(StrikezoneBackendApplication.class, args);
	}

}
