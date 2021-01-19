package com.ttulka.samples.messaging.biz;

import com.ttulka.samples.messaging.jpa.MyEntity;
import com.ttulka.samples.messaging.jpa.MyRepository;
import com.ttulka.samples.messaging.amqp.Sender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MyService {

    private final MyRepository myRepository;
    private final Sender sender;

    @Transactional
    public void doSomething() {
        myRepository.save(new MyEntity(null, "Test 1"));

        sender.sendMessage("Hello!");

        if (true) throw new RuntimeException("Oops!");

        myRepository.save(new MyEntity(null, "Test 2"));
    }

    public void printAll() {
        myRepository.findAll()
                .forEach(System.out::println);
    }
}
