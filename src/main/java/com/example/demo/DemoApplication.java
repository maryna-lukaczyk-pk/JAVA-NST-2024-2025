package com.example.demo;

import ch.qos.logback.core.encoder.JsonEscapeUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {




	public static void main(String[] args) {
		System.out.println("cos");
		SpringApplication.run(DemoApplication.class, args);
	}

}
