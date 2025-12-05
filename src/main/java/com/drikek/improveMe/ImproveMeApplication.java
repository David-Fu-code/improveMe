package com.drikek.improveMe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // for automatic run methods annotated with schedule
public class ImproveMeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImproveMeApplication.class, args);
	}

}
