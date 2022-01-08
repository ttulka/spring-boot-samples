package com.ttulka.samples.jib.java17;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
public class JibJava17Application {

	public static void main(String[] args) {
		SpringApplication.run(JibJava17Application.class, args);
	}

	@RestController
	static class HelloController {
		@GetMapping("/")
		String hello() throws Exception {
			return "Hello, World!"; }
	}
}
