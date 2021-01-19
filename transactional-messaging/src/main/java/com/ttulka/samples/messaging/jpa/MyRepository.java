package com.ttulka.samples.messaging.jpa;

import org.springframework.data.repository.CrudRepository;

public interface MyRepository extends CrudRepository<MyEntity, Long> {
}
