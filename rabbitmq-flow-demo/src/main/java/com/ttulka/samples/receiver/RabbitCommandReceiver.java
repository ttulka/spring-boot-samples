package com.ttulka.samples.receiver;

import com.ttulka.samples.Messaging;
import com.ttulka.samples.MyCommand;
import com.ttulka.samples.MyEvent1;
import com.ttulka.samples.MyEvent2;
import com.ttulka.samples.sender.RabbitMessageSender;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RabbitCommandReceiver {

    private final RabbitMessageSender<MyEvent1> myEvent1Sender;
    private final RabbitMessageSender<MyEvent2> myEvent2Sender;

    @RabbitListener(
        bindings = {
            @QueueBinding(
                value = @Queue("my-command-queue-for-receiver-service"),
                exchange = @Exchange(value = Messaging.MY_EXCHANGE_NAME, type = ExchangeTypes.TOPIC),
                key = "MyCommand.*")
        }
    )
    public void handle(MyCommand message) {
        log.info("Received command: {}", message.toString());

        // handle message...
        var event1 = MyEvent1.builder()
            .content("Received 1 " + message + " at " + LocalDateTime.now())
            .build();

        var event2 = MyEvent2.builder()
            .content("Received 2 " + message + " at " + LocalDateTime.now())
            .build();

        log.info("Sending event1 " + event1);
        myEvent1Sender.send(event1);

        log.info("Sending event2 " + event2);
        myEvent2Sender.send(event2);
    }
}
