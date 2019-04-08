package com.example.samples.web1;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
public class Config {

    @ConditionalOnMissingBean
    @Bean
    public MessageSource messageSource(MessageSource web1MessageSource) {
        return web1MessageSource;
    }

    @Bean
    public MessageSource web1MessageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:/messages/web1/messages");
        return messageSource;
    }

    @Slf4j
    @RequestMapping("${path.web1:}")
    @RequiredArgsConstructor
    @Controller("web1IndexController")
    static class IndexController {
        
        @GetMapping
        @PreAuthorize("hasAnyRole('ARCHIVEADMIN','SECURITYADMIN')")
        public String index() {
            log.debug("INDEX WEB1");
            return "web1/index";
        }
    }

    @Slf4j
    @RequestMapping("${path.web1:}echo")
    @RequiredArgsConstructor
    @Controller("web1EchoController")
    static class EchoController {
    
        @PostMapping
        @PreAuthorize("hasAnyRole('ARCHIVEADMIN','SECURITYADMIN')")
        public String echo(String input, Model model) {
            log.debug("ECHO WEB1: {}", input);
            model.addAttribute("output", String.join("1", input.split("")));
            return "web1/echo";
        }
    }
}
