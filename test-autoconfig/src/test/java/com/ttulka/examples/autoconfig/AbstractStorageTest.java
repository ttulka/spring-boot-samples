package com.ttulka.examples.autoconfig;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = StorageAutoconfiguration.class)
abstract class AbstractStorageTest {

	@Autowired
	protected StorageService storageService;

	@Test
	void downloadSuccess() {
		var result = storageService.download("abc");
		assertThat(result).isNotEmpty();
		System.out.println("DOWNLOADED: " + result);
	}
}
