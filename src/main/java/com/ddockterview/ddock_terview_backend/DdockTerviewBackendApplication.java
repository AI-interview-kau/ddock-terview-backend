package com.ddockterview.ddock_terview_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DdockTerviewBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(DdockTerviewBackendApplication.class, args);
	}

}
