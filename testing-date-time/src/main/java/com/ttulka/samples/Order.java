package com.ttulka.samples;

import lombok.Builder;
import lombok.Value;

import java.time.ZonedDateTime;
import java.util.UUID;

@Value
@Builder
public class Order {

    private UUID id;
    private ZonedDateTime createdAt;
    private String content;
}
