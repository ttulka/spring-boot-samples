package com.ttulka.samples.addons.addon2;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

// see https://www.baeldung.com/spring-yaml-propertysource for YAML support
@Configuration
@PropertySource("classpath:addon2.properties")
class Addon2Config {

    // some configuration goes here...
}
