package com.ttulka.samples.receiver;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReceiverApp implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ReceiverApp.class, args);
	}

	@Override
	public void run(String... args) {

	}
}
