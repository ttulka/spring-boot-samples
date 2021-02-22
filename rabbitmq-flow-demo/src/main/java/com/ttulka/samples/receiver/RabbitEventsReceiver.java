package com.ttulka.samples.receiver;

import com.ttulka.samples.Messaging;
import com.ttulka.samples.MyEvent1;
import com.ttulka.samples.MyEvent2;
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
public class RabbitEventsReceiver {

    @RabbitListener(
        bindings = {
            @QueueBinding(
                value = @Queue("my-event1-queue-for-receiver-service"),
                exchange = @Exchange(value = Messaging.MY_EXCHANGE_NAME, type = ExchangeTypes.TOPIC),
                key = "MyEvent1.*")
        }
    )
    public void handle(MyEvent1 message) {
        log.info("Received event1: {}", message.toString());
    }

    @RabbitListener(
        bindings = {
            @QueueBinding(
                value = @Queue("my-event2-queue-for-receiver-service"),
                exchange = @Exchange(value = Messaging.MY_EXCHANGE_NAME, type = ExchangeTypes.TOPIC),
                key = "MyEvent2.*")
        }
    )
    public void handle(MyEvent2 message) {
        log.info("Received event2: {}", message.toString());
    }
}
