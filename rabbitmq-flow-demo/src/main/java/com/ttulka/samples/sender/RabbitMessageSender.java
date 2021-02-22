package com.ttulka.samples.sender;

import com.ttulka.samples.Messaging;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Slf4j
@RequiredArgsConstructor
public class RabbitMessageSender<T> {

    private static final String DEFAULT_API_VERSION_STRING = "v1";

    private final String exchangeName;
    private final RabbitTemplate rabbitTemplate;

    public void send(T msg) {
        String messageType = RabbitConfig.getMessageType(msg.getClass());
        String routingKey = messageType + "." + DEFAULT_API_VERSION_STRING;

        rabbitTemplate.convertAndSend(exchangeName, routingKey, msg, m -> {
            MessageProperties messageProperties = m.getMessageProperties();
            messageProperties.setHeader(Messaging.MESSAGE_HEADER_API_VERSION, DEFAULT_API_VERSION_STRING);
            messageProperties.setHeader(Messaging.MESSAGE_HEADER_CONTENT_TYPE, messageType);
            return m;
        });
        //log.info("Sent message: {}", msg);
    }
}
