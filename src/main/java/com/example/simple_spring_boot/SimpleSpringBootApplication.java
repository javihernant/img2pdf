package com.example.simple_spring_boot;

import com.example.simple_spring_boot.storage.PdfStorageService;
import com.example.simple_spring_boot.storage.StorageProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
//@EnableConfigurationProperties(StorageProperties.class)
public class SimpleSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleSpringBootApplication.class, args);
	}

	@Bean
	CommandLineRunner init(PdfStorageService storageService) {
		return (args) -> {
			storageService.deleteAll();
			storageService.init();
		};
	}

}
