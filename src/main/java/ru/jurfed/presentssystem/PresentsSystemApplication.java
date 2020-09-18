package ru.jurfed.presentssystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
public class PresentsSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(PresentsSystemApplication.class, args);
	}

}
