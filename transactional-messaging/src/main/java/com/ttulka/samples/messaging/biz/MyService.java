package com.ttulka.samples.messaging.biz;

import com.ttulka.samples.messaging.jpa.MyEntity;
import com.ttulka.samples.messaging.jpa.MyRepository;
import com.ttulka.samples.messaging.amqp.Sender;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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

        if (true) throw new RuntimeException("Oops! 1");

        myRepository.save(new MyEntity(null, "Test 2"));
    }

    @Transactional
    @SneakyThrows
    public void saveSomething() {
        myRepository.save(new MyEntity(null, "Test A"));
        System.out.println("Sending 'saved' message.");
        sender.sendMessage("Saved!");
        Thread.sleep(1000);
        System.out.println("'saved' message sent.");
        myRepository.save(new MyEntity(null, "Test B"));
    }

    @Transactional
    public void updateSomething(Long id) {
        for (var myEntity : myRepository.findAll()) {
            myEntity.setValue(myEntity.getValue() + " --- updated");

            sender.sendMessage("Updated! " + myEntity.getId());

            if (true) throw new RuntimeException("Oops! 2");

            //myRepository.save(myEntity); // not necessary in @Transactional
        }
    }

    public void printAll() {
        myRepository.findAll()
                .forEach(System.out::println);
    }
}
