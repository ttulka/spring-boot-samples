package com.example.jaxbmoxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class DemoJaxbMoxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoJaxbMoxyApplication.class, args);
	}

	@RestController
	@RequestMapping("/")
	static class MyController {

		@GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
		public MyXml getMyXml() {
			return new MyXml();
		}
	}
}
