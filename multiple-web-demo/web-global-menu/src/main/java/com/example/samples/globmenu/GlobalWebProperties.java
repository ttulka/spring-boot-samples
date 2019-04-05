package com.example.samples.globmenu;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties
@Data
public class GlobalWebProperties {

    private Map<String, String> path;

    private String mySuperTest;

    public String getPathRoot() {
        return path.getOrDefault("root", "");
    }
}
