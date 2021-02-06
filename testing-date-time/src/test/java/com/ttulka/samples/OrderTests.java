package com.ttulka.samples;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

class OrderTests {

    @TestConfiguration
    static class FixedClockConfig {
        @Primary
        @Bean
        Clock fixedClock() {
            return Clock.fixed(
                    Instant.parse("2020-12-01T10:05:23.653Z"),
                    ZoneId.of("Europe/Prague"));
        }
    }

    /**
     * Fixing the clock
     */
    @Nested
    @SpringBootTest(classes = OrderTests.FixedClockConfig.class)
    class WithFixedClock {

        @Autowired
        private OrderService orderService;

        @Autowired
        private Clock clock;

        @Test
        void order_is_placed() {
            var order = orderService.place("Test");
            assertAll(
                    () -> assertThat(order).isNotNull(),
                    () -> assertThat(order.getId()).isNotNull(),
                    () -> assertThat(order.getCreatedAt()).isEqualTo(ZonedDateTime.now(clock)),
                    () -> assertThat(order.getContent()).isEqualTo("Test")
            );
        }
    }

    /**
     * Mocking the clock
     */
    @Nested
    @SpringBootTest
    class WithMockedClock {

        @Autowired
        private OrderService orderService;

        @MockBean
        private Clock clock;

        @BeforeEach
        void setupClock() {
            when(clock.getZone()).thenReturn(ZoneId.of("Europe/Prague"));
        }

        @Test
        void order_is_placed() {
            when(clock.instant()).thenReturn(Instant.parse("2020-12-01T10:05:23.653Z"));

            var order = orderService.place("Test");
            assertAll(
                    () -> assertThat(order).isNotNull(),
                    () -> assertThat(order.getId()).isNotNull(),
                    () -> assertThat(order.getCreatedAt()).isEqualTo(ZonedDateTime.now(clock)),
                    () -> assertThat(order.getContent()).isEqualTo("Test")
            );
        }
    }
}
