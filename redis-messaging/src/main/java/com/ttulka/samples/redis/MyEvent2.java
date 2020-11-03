package com.ttulka.samples.redis;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.Instant;

@RequiredArgsConstructor
@EqualsAndHashCode(of = "eventId")
@ToString
public class MyEvent1 implements Event {

    public final @NonNull Instant when;
    public final @NonNull String eventId;
}
