package com.ttulka.samples.springboot.propsfromdb.util;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "util.properties")
@Getter
@Setter
public class PropsUtilConfigurationProperties {

    private final String table = "properties";
}
