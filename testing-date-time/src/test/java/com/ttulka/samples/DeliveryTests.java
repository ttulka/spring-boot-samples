package com.ttulka.samples;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * Mocking the clock
 */
@Disabled
@SpringBootTest
class DeliveryTests {

    @Autowired
    private DeliveryService deliveryService;

    private static Clock clock;
    private static ZonedDateTime now;

    @BeforeAll
    static void setupClock() {
        clock = Clock.fixed(
            Instant.parse("2020-12-01T10:05:23.653Z"),
            ZoneId.of("Europe/Prague"));
        now = ZonedDateTime.now(clock);

        var clockMock = Mockito.mockStatic(Clock.class);
        clockMock.when(Clock::systemUTC).thenReturn(clock);
    }

    @Test
    void delivery_is_planned() {
        var orderId = UUID.randomUUID();
        var delivery = deliveryService.plan(orderId);

        var tomorrowAt8am = now.plusDays(1).withHour(8).truncatedTo(ChronoUnit.HOURS);

        assertAll(
                () -> assertThat(delivery).isNotNull(),
                () -> assertThat(delivery.getId()).isNotNull(),
                () -> assertThat(delivery.getOrderId()).isEqualTo(orderId),
                () -> assertThat(delivery.getCreatedAt()).isEqualTo(now),
                () -> assertThat(delivery.getPlannedAt()).isEqualTo(tomorrowAt8am),
                () -> assertThat(delivery.getShippedAt()).isNull()
        );
    }

    @Test
    void delivery_is_shipped() {
        var delivery = deliveryService.plan(UUID.randomUUID());
        var shipped = deliveryService.ship(delivery);
        assertAll(
                () -> assertThat(shipped).isNotNull(),
                () -> assertThat(shipped.getId()).isEqualTo(delivery.getId()),
                () -> assertThat(shipped.getOrderId()).isEqualTo(delivery.getOrderId()),
                () -> assertThat(shipped.getCreatedAt()).isEqualTo(delivery.getCreatedAt()),
                () -> assertThat(shipped.getShippedAt()).isEqualTo(now)
        );
    }
}
