package com.example.jaxbmoxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	JakartaXmlRootElementHttpMessageConverter jakartaXmlRootElementHttpMessageConverter() {
		return new JakartaXmlRootElementHttpMessageConverter();
	}

	@RestController
	@RequestMapping("/api")
	static class MyController {

		@GetMapping(path = "/my", produces = MediaType.APPLICATION_XML_VALUE)
		public MyXml getMyXml() {
			var myXml = new MyXml();
			myXml.setStructureVersion(12345);
			myXml.setMyObjects(List.of(
					new MyXmlObject(0, 100),
					new MyXmlObject(1, 101),
					new MyXmlObject(2, 102)));
			return myXml;
		}
	}
}
