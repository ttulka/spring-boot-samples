package com.ttulka.samples.redis;

import lombok.*;

import java.time.Instant;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "eventId")
@ToString
public class MyEvent1 implements DomainEvent {

    public @NonNull Instant when;
    public @NonNull String eventId;
}
