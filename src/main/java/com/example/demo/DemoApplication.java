package com.example.demo;

import com.example.demo.exceptions.DataException;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableAutoConfiguration
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public  MongoClient mongoClient()
	{
		return new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
	}

}
