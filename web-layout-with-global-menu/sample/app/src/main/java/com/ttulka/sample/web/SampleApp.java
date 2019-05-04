package com.ttulka.sample.web;

import java.util.ArrayList;
import java.util.Set;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@SpringBootConfiguration
@EnableAutoConfiguration
public class SampleApp {

    public static void main(String[] args) {
        new SpringApplicationBuilder(SampleApp.class)
                .properties(
                        "spring.jmx.enabled=false",
                        "logging.level.org.springframework=INFO"
                )
                .run(args);
    }

    @EnableGlobalMethodSecurity(prePostEnabled = true)
    @Configuration
    static class SecurityConfig {
        @Bean
        public UserDetailsService userDetailsService() {
            return new InMemoryUserDetailsManager(new ArrayList<>(Set.of(
                    User.withUsername("demoadmin")
                            .password("{bcrypt}$2a$10$v.iz86Wsg80nFZKclezXQek3arWlHvd4Bao.zK6ZT9SwgSEskLKO2")
                            .roles("ARCHIVEADMIN", "ADMINGROUP")
                            .build(),
                    User.withUsername("demo")
                            .roles("ARCHIVEREADER")
                            .password("{bcrypt}$2a$10$v.iz86Wsg80nFZKclezXQek3arWlHvd4Bao.zK6ZT9SwgSEskLKO2")
                            .build())));
        }

        @Configuration
        public static class AuthorizationConfig extends WebSecurityConfigurerAdapter {

            @Override
            protected void configure(HttpSecurity httpSecurity) throws Exception {
                httpSecurity
                        .cors().disable()
                        .csrf().disable();

                httpSecurity.authorizeRequests()
                        .antMatchers("/webjars/**", "/images/**", "/css/**", "/js/**").permitAll()
                        .anyRequest().authenticated()
                        .and()
                        .formLogin().loginPage("/login").permitAll()
                        .and()
                        .logout();
            }
        }
    }
}
