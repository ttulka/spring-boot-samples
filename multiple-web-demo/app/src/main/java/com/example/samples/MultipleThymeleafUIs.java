package com.example.samples;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.context.MessageSourceProperties;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootConfiguration
@EnableAutoConfiguration
@EnableWebMvc
public class MultipleThymeleafUIs {

    public static void main(String[] args) {
        new SpringApplicationBuilder(MultipleThymeleafUIs.class)
                .properties(
                        "spring.jmx.enabled=false",
                        "logging.level.org.springframework=DEBUG"
                )
                .run(args);
    }

    @Configuration
    static class AllResourceBundlesConfig {
        @Bean
        public MessageSource messageSource(MessageSourceProperties properties, Collection<MessageSource> messageSources) {
            ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
            if (StringUtils.hasText(properties.getBasename())) {
                messageSource.setBasenames(StringUtils.commaDelimitedListToStringArray(
                        StringUtils.trimAllWhitespace(properties.getBasename())));
            }
            if (properties.getEncoding() != null) {
                messageSource.setDefaultEncoding(properties.getEncoding().name());
            }
            messageSource.setFallbackToSystemLocale(properties.isFallbackToSystemLocale());
            Duration cacheDuration = properties.getCacheDuration();
            if (cacheDuration != null) {
                messageSource.setCacheMillis(cacheDuration.toMillis());
            }
            messageSource.setAlwaysUseMessageFormat(properties.isAlwaysUseMessageFormat());
            messageSource.setUseCodeAsDefaultMessage(properties.isUseCodeAsDefaultMessage());
            if (messageSources != null) {
                messageSource.setParentMessageSource(new CompositeMessageSource(messageSources));
            }
            return messageSource;
        }

        @Bean
        @ConfigurationProperties(prefix = "forum.messages")
        public MessageSourceProperties messageSourceProperties() {
            return new MessageSourceProperties();
        }
    }

    @EnableWebSecurity
    static class SecurityConfig {
        @Bean
        public UserDetailsService userDetailsService() {
            return new InMemoryUserDetailsManager(new ArrayList<>(Set.of(
                    User.withUsername("demoadmin")
                            .password("{bcrypt}$2a$10$v.iz86Wsg80nFZKclezXQek3arWlHvd4Bao.zK6ZT9SwgSEskLKO2")
                            .roles("ARCHIVEADMIN", "SECURITYADMIN")
                            .build(),
                    User.withUsername("demo")
                            .roles("ARCHIVEREADER")
                            .password("{bcrypt}$2a$10$v.iz86Wsg80nFZKclezXQek3arWlHvd4Bao.zK6ZT9SwgSEskLKO2")
                            .build())));
        }

        @Configuration
        public static class AuthorizationConfig extends WebSecurityConfigurerAdapter {
            @Override
            protected void configure(HttpSecurity http) throws Exception {
                http
                        .authorizeRequests()
                        .antMatchers("/ui/web2/**").hasRole("ARCHIVEADMIN")
                        .and()
                        .authorizeRequests()
                        .anyRequest().authenticated()
                        .and()
                        .formLogin();
            }
        }
    }
}
