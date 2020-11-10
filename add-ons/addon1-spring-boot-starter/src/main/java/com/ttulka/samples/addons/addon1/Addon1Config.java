package com.ttulka.samples.addons.addon1;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

// see https://www.baeldung.com/spring-yaml-propertysource for YAML support
@Configuration
@PropertySource("classpath:addon1.properties")
class Addon1Config {

    // some configuration goes here...
}
