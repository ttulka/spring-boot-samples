package org.springframework.boot.autoconfigure.orm.jpa;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.jpa.eclipselink")
public class EclipselinkProperties {
    private Map<String, String> properties = new HashMap<>();

    public Map<String, String> getProperties() {
        return this.properties;
    }
}
