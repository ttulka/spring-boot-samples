package com.example.samples.webres;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@Configuration
public class Config {

    private static final String DEFAULT_HEADER_TEMPLATE = "global/default-header";

    @ConditionalOnMissingBean
    @Bean
    @Order(Ordered.LOWEST_PRECEDENCE - 10)
    public String webresHeaderTemplate() {
        return DEFAULT_HEADER_TEMPLATE;
    }

    @ControllerAdvice
    static class WebresHeaderTemplateAdvice {

        private final String template;

        public WebresHeaderTemplateAdvice(String webresHeaderTemplate) {
            this.template = webresHeaderTemplate;
        }

        @ModelAttribute
        public void addAttributes(Model model) {
            model.addAttribute("webresHeaderTemplate", this.template);
        }
    }
}
