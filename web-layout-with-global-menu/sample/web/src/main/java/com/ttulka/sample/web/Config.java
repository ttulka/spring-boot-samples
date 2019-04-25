package com.ttulka.sample.web;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
public class Config {

    @Bean
    public MessageSource sampleMessageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:/messages/sample/messages");
        return messageSource;
    }

    @Slf4j
    @RequestMapping("${ttulka.ui.web.path.sample:}")
    @RequiredArgsConstructor
    @Controller("sampleIndexController")
    static class IndexController {

        @GetMapping
        @PreAuthorize("hasAnyRole('ARCHIVEADMIN','ADMINGROUP')")
        public String index() {
            log.debug("INDEX SAMPLE WEB");
            return "sample/index";
        }
    }
}
