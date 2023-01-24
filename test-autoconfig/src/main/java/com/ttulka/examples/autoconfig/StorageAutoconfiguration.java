package com.ttulka.examples.autoconfig;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@AutoConfiguration
public class StorageAutoconfiguration {

	@Profile("aws")
	@Configuration
	static class S3Configuration {
		@Bean
		StorageService s3StorageService() {
			return new StorageService() {
				@Override
				public String download(String key) {
					return "s3";
				}
			};
		}
	}

	@Profile("azure")
	@Configuration
	static class AzureConfiguration {
		@Bean
		StorageService azureStorageService() {
			return new StorageService() {
				@Override
				public String download(String key) {
					return "azure";
				}
			};
		}
	}

	@Profile("!aws && !azure")
	@Configuration
	static class DefaultConfiguration {
		@Bean
		StorageService localStorageService() {
			return new StorageService() {
				@Override
				public String download(String key) {
					return "local";
				}
			};
		}
	}
}
