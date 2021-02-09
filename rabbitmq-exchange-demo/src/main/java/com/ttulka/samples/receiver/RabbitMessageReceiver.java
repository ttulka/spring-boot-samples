package com.ttulka.samples.receiver;

import com.ttulka.samples.Messaging;
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
public class RabbitMessageReceiver {

  @RabbitListener(
      bindings = {
          @QueueBinding(
              value = @Queue(RabbitConfig.MY_QUEUE_NAME),
              exchange = @Exchange(value = Messaging.MY_EXCHANGE_NAME, type = ExchangeTypes.TOPIC),
              key = "MyMessage.*"
          )
      }
  )
  public void handle(MyMessage message) {
    log.info("Received message: {}", message.toString());

    // handle message...
  }
}
