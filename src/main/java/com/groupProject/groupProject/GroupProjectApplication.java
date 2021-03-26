package com.groupProject.groupProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@Configuration
@EnableJpaRepositories("com.groupProject.groupProject.repo")
public class GroupProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(GroupProjectApplication.class, args);
	}

}
