package com.rgt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class RgtCrmApplication {

	public static void main(String[] args) {
		SpringApplication.run(RgtCrmApplication.class, args);
	}

}
