package com.ttulka.samples.messaging;

import com.ttulka.samples.messaging.biz.MyService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TransactionalMessagingApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionalMessagingApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(MyService myService) {
		return args -> {
			try {
				myService.doSomething();

			} catch (Exception e) {
				e.printStackTrace();
			}

			myService.printAll();
		};
	}
}
