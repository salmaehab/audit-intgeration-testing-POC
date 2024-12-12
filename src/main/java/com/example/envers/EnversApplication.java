package com.example.envers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class EnversApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnversApplication.class, args);
	}

}
