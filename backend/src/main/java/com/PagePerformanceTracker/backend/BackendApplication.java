package com.PagePerformanceTracker.backend;

import org.apache.catalina.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		System.out.println(Runtime.getRuntime().availableProcessors());
		SpringApplication.run(BackendApplication.class, args);
	}

}
