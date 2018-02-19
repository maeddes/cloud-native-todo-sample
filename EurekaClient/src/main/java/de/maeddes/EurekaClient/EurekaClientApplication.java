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
public class EurekaClientApplication {

	@Value("${spring.profiles:none}")
	String profile;

	@GetMapping("/")
	public String hello(){
		return "Hello World running on "+profile;
	}

	public static void main(String[] args) {
		SpringApplication.run(EurekaClientApplication.class, args);
	}
}
