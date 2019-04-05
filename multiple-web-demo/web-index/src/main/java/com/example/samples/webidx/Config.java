package com.example.samples.webidx;

import java.util.Collection;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration
public class Config {

    private final String pathRoot;

    public Config(GlobalWebProperties properties) {
        String pathRoot = properties.getPathRoot();
        this.pathRoot = pathRoot.endsWith("/")
                        ? pathRoot.substring(0, pathRoot.length() - 1)
                        : pathRoot;
    }

    @ConditionalOnMissingBean
    @Bean
    public MessageSource messageSource(MessageSource webidxMessageSource) {
        return webidxMessageSource;
    }

    @Bean
    public MessageSource webidxMessageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:/messages/webidx/messages");
        return messageSource;
    }

    @Bean
    public IndexController webidxIndexController(Collection<RequestMappingHandlerMapping> requestMappings, Collection<SimpleUrlHandlerMapping> simpleMappings) {
        return new IndexController(this.pathRoot, Stream.concat(
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
                .collect(Collectors.toCollection(TreeSet::new)));
    }
}
