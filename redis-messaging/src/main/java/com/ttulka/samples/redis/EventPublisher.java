package com.ttulka.samples.redis;

public interface EventPublisher {

    void raise(DomainEvent event);
}
