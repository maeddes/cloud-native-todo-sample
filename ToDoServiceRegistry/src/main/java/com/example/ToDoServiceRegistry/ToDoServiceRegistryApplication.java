package com.example.ToDoServiceRegistry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ToDoServiceRegistryApplication {

	public static void main(String[] args) {
		SpringApplication.run(ToDoServiceRegistryApplication.class, args);
	}
}
