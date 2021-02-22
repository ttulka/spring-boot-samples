package com.ttulka.samples.sender;

import static com.ttulka.samples.Messaging.MY_EXCHANGE_NAME;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.ttulka.samples.MyCommand;
import java.io.IOException;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class RabbitConfig {

    private final ConnectionFactory connectionFactory;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        Jackson2JsonMessageConverter messageConverter = new Jackson2JsonMessageConverter(objectMapper);
        template.setMessageConverter(messageConverter);
        return template;
    }

    @Bean
    public RabbitMessageSender<MyCommand> myCommandSender(RabbitTemplate rabbitTemplate) {
        return new RabbitMessageSender<>(MY_EXCHANGE_NAME, rabbitTemplate);
    }

    public static String getMessageType(Class<?> messageClass) {
        return messageClass.getSimpleName();
    }

    @PostConstruct
    public void initialize() throws IOException {
        try (Connection connection = connectionFactory.createConnection()) {
            Channel channel = connection.createChannel(true);
            setupExchange(channel, MY_EXCHANGE_NAME);
            log.info("initialized queues and exchanges");
        }
    }

    private void setupExchange(Channel channel, String exchangeName) throws IOException {
        log.info("create exchange {}", exchangeName);
        channel.exchangeDeclare(exchangeName, ExchangeTypes.TOPIC, true, false, false, null);
    }
}
