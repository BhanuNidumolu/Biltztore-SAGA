package com.selfheal.blitzstoreorchestrator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableKafka
@EnableScheduling
public class BlitzstoreOrchestratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlitzstoreOrchestratorApplication.class, args);
	}

}
