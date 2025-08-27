package com.risha.laundryApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class LaundryManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(LaundryManagementApplication.class, args);
	}

	@Bean
	public PlatformTransactionManager abc(MongoDatabaseFactory dbFactory) {
		return new MongoTransactionManager(dbFactory);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
