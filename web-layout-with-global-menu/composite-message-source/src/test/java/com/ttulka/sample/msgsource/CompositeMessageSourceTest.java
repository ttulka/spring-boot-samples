package com.ttulka.sample.msgsource;

import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {
        Config.class, CompositeMessageSourceTest.TestConfig.class
})
class CompositeMessageSourceTest {

    @Configuration
    static class TestConfig {

        @Bean
        public MessageSource bundle1MessageSource() {
            ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
            messageSource.setBasenames("classpath:/bundle1/messages");
            return messageSource;
        }

        @Bean
        public MessageSource bundle2MessageSource() {
            ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
            messageSource.setBasenames("classpath:/bundle2/bundleA/messages", "classpath:/bundle2/bundleB/messages");
            return messageSource;
        }
    }

    @Autowired
    private MessageSource messageSource;

    @Test
    public void compositeMessagesTest() {
        assertNotNull(messageSource);

        assertEquals("11", messageSource.getMessage("bundle1-msg1", null, Locale.getDefault()));
        assertEquals("2A", messageSource.getMessage("bundle2-msgA", null, Locale.getDefault()));
        assertEquals("2B", messageSource.getMessage("bundle2-msgB", null, Locale.getDefault()));
    }
}



