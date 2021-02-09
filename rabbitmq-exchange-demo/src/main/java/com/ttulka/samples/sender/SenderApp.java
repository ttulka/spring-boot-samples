package com.ttulka.samples.sender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SenderApp implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SenderApp.class, args);
	}

	@Autowired
	private RabbitMessageSender<MyMessage> myMessageSender;

	@Override
	public void run(String... args) {
		for (int i = 1; i <= 10; i++) {
			myMessageSender.send(MyMessage.builder()
					.content("Hello, world! " + i)
					.build());
		}
	}
}
