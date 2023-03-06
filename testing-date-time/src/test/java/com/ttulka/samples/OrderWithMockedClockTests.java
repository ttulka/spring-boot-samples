package com.ttulka.samples;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@SpringBootTest
class OrderWithMockedClockTests {

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
