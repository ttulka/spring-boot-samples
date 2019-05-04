package com.ttulka.sample.web.globmenu;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ConfigurationProperties(prefix = "ttulka.ui.web.path")
@Getter
@Setter
@ToString
public class GlobalWebProperties {

    private String root;

    public String getPathRoot() {
        return root != null ? root : "";
    }
}
