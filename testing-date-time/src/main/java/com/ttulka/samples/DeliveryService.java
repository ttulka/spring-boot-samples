package com.ttulka.samples;

import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class DeliveryService {

    private final Clock clock = Clock.systemUTC();

    /**
     * Creates a new delivery planned to be shipped at 8:00 AM the next day.
     * @param orderId the order ID
     * @return the delivery
     */
    public Delivery plan(UUID orderId) {
        return Delivery.builder()
                .id(UUID.randomUUID())
                .orderId(orderId)
                .createdAt(ZonedDateTime.now(clock))
                .plannedAt(ZonedDateTime.now(clock)
                        .plusDays(1)
                        .withHour(8)
                        .truncatedTo(ChronoUnit.HOURS))
                .build();
    }

    /**
     * Ships the delivery now.
     * @param delivery the delivery
     * @return the delivery
     */
    public Delivery ship(Delivery delivery) {
        return Delivery.builder()
                .id(delivery.getId())
                .orderId(delivery.getOrderId())
                .createdAt(delivery.getCreatedAt())
                .shippedAt(ZonedDateTime.now(clock))
                .build();
    }
}
