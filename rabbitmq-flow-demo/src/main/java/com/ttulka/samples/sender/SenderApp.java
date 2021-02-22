package com.ttulka.samples.sender;

import com.ttulka.samples.MyCommand;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class SenderApp implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SenderApp.class, args);
    }

    @Autowired
    private RabbitMessageSender<MyCommand> myCommandSender;

    @Override
    public void run(String... args) {
        for (int i = 1; i <= 10; i++) {
            var msg = MyCommand.builder()
                .content(i + ": Hello, world! at " + LocalDateTime.now())
                .build();

            log.info("Sending command " + msg);
            myCommandSender.send(msg);
        }
    }
}
