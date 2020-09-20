package ru.jurfed.presentssystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.jurfed.presentssystem.repository.StorageRepository;

import javax.annotation.PostConstruct;


@SpringBootApplication
public class PresentsSystemApplication {


	public static void main(String[] args) {
		SpringApplication.run(PresentsSystemApplication.class, args);
	}



}
