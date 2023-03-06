package com.ttulka.samples;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * Fixing the clock
 */
@SpringBootTest
@Import(FixedClockConfig.class)
class OrderWithFixedClockTests {

    @Autowired
    private OrderService orderService;

    @Autowired
    private Clock clock;

    @Test
    void order_is_placed() {
        System.out.println("CLOCK: " + clock);

        var order = orderService.place("Test");
        assertAll(
                () -> assertThat(order).isNotNull(),
                () -> assertThat(order.getId()).isNotNull(),
                () -> assertThat(order.getCreatedAt()).isEqualTo(ZonedDateTime.now(clock)),
                () -> assertThat(order.getContent()).isEqualTo("Test")
        );
    }
}
@TestConfiguration
class FixedClockConfig {
    @Primary
    @Bean
    Clock fixedClock() {
        return Clock.fixed(
                Instant.parse("2020-12-01T10:05:23.653Z"),
                ZoneId.of("Europe/Prague"));
    }
}
