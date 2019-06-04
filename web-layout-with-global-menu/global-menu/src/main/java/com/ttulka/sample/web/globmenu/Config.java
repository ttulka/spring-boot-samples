package com.ttulka.sample.web.globmenu;

import java.util.Collection;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.ttulka.sample.web.layout.LayoutTemplate;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration
public class Config {

    @Bean
    public MessageSource globmenuMessageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:/messages/ttulka/sample/web/globmenu/messages");
        return messageSource;
    }

    @ConditionalOnMissingBean
    @Bean
    @Order(Ordered.LOWEST_PRECEDENCE - 20)
    public LayoutTemplate layoutHeaderTemplate() {
        return new LayoutTemplate("globmenu/header");
    }

    @ControllerAdvice(basePackages = {"com.ttulka.sample.web"})
    static class PathsAdvice {

        private final String pathRoot;
        private final Collection<String> paths;

        public PathsAdvice(GlobalWebProperties properties,
                           Collection<RequestMappingHandlerMapping> requestMappings,
                           Collection<SimpleUrlHandlerMapping> simpleMappings) {
            String pathRoot = properties.getPathRoot();
            this.pathRoot = pathRoot.endsWith("/")
                            ? pathRoot.substring(0, pathRoot.length() - 1)
                            : pathRoot;

            this.paths = Stream.concat(
                    simpleMappings.stream()
                            .map(SimpleUrlHandlerMapping::getHandlerMap)
                            .map(Map::entrySet)
                            .flatMap(Collection::stream)
                            .filter(m -> m.getValue() instanceof Controller)
                            .map(Map.Entry::getKey),
                    requestMappings.stream()
                            .map(RequestMappingHandlerMapping::getHandlerMethods)
                            .map(Map::entrySet)
                            .flatMap(Collection::stream)
                            .map(Map.Entry::getKey)
                            .filter(mapping -> mapping.getMethodsCondition().getMethods().contains(RequestMethod.GET))
                            .flatMap(mapping -> mapping.getPatternsCondition().getPatterns().stream())
            )
                    .filter(path -> path.matches("[/]?(" + this.pathRoot + ")[/]([a-zA-Z0-9-_]+)[/]"))
                    .collect(Collectors.toCollection(TreeSet::new));
        }

        @ModelAttribute
        public void addAttributes(Model model) {
            model.addAttribute("pathRoot", this.pathRoot);
            model.addAttribute("paths", this.paths);
        }
    }
}
