package de.maeddes.EurekaClient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class EurekaHybridClientApplication {

	@Value("${spring.application.name:unnamed}")
	String name;

	@Value("${spring.profiles:unset}")
	String profile;

	@GetMapping("/")
	public String hello(){
		return name+" running on profile"+profile;
	}

	public static void main(String[] args) {
		SpringApplication.run(EurekaHybridClientApplication.class, args);
	}
}
