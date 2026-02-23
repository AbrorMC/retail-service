package uz.uzumtech.retail_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RetailServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RetailServiceApplication.class, args);
	}

}
