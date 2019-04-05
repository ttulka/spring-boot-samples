package com.example.samples.web2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
public class Config {

    @ConditionalOnMissingBean
    @Bean
    public MessageSource messageSource(MessageSource web2MessageSource) {
        return web2MessageSource;
    }

    @Bean
    public MessageSource web2MessageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:/messages/web2/messages");
        return messageSource;
    }

    @Slf4j
    @RequestMapping("${path.web2:}echo")
    @RequiredArgsConstructor
    @Controller("web2EchoController")
    static class EchoController {
        @PostMapping
        public String echo(String input, Model model) {
            log.debug("ECHO WEB2: {}", input);
            model.addAttribute("output", String.join("2", input.split("")));
            return "web2/echo";
        }
    }

    @Configuration
    static class MoreViewControllers implements WebMvcConfigurer {
        private final String path;

        // @Value here only becase no properties class was defined.
        public MoreViewControllers(@Value("${path.web2:}") String path) {
            this.path = path;
        }

        // Example, that no @Controller is necessary
        @Override
        public void addViewControllers(ViewControllerRegistry viewControllerRegistry) {
            viewControllerRegistry.addViewController(path).setStatusCode(HttpStatus.NOT_FOUND).setViewName("web2/index");

        }
    }
}
