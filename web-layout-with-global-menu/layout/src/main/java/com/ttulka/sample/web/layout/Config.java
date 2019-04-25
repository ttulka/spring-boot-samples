package com.ttulka.sample.web.layout;

import java.util.Collections;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Configuration
public class Config {

    private static final String DEFAULT_HEADER_TEMPLATE = "global/default-header";

    @Bean
    public MessageSource layoutMessageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:/messages/ttulka/sample/web/layout/messages");
        return messageSource;
    }

    @ConditionalOnMissingBean
    @Bean
    @Order(Ordered.LOWEST_PRECEDENCE - 10)
    public LayoutTemplate layoutHeaderTemplate() {
        return new LayoutTemplate(DEFAULT_HEADER_TEMPLATE);
    }

    @ControllerAdvice
    static class LayoutHeaderTemplateAdvice {

        private final LayoutTemplate template;

        public LayoutHeaderTemplateAdvice(LayoutTemplate layoutHeaderTemplate) {
            this.template = layoutHeaderTemplate;
        }

        @ModelAttribute
        public void addAttributes(Model model) {
            model.addAttribute("layoutHeaderTemplate", this.template.getLocation());
        }
    }

    @ControllerAdvice
    @Slf4j
    static class GlobalExceptionHandler {

        @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        @ExceptionHandler(Exception.class)
        public ModelAndView handleException(Exception ex) {
            log.error("Application Exception", ex);
            return new ModelAndView("global/error", Collections.singletonMap("message", ex.getMessage()));
        }
    }

    @ControllerAdvice
    @Slf4j
    static class AccessDeniedExceptionHandler {

        @ResponseStatus(HttpStatus.FORBIDDEN)
        @ExceptionHandler(AccessDeniedException.class)
        public ModelAndView handleAccessDeniedException(AccessDeniedException ex) {
            log.error("Access Denied Exception", ex);
            return new ModelAndView("global/access-denied");
        }
    }

    @Controller
    @RequestMapping("/login")
    static class LoginController {

        @GetMapping
        public String login(Authentication authentication, Model model, String error, String logout) {
            if (authentication != null && authentication.isAuthenticated()) {
                return "redirect:/";
            }
            model.addAttribute("error", error);
            model.addAttribute("logout", logout);
            return "global/login";
        }
    }
}
