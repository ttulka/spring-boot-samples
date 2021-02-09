package com.ttulka.samples.sender;

import static com.ttulka.samples.Messaging.MY_EXCHANGE_NAME;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import javax.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Data;
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

  static final String MY_QUEUE_NAME = "my-queue-for-sender-service";

  private static final String ALL = "*";

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
  public RabbitMessageSender<MyMessage> myMessageSender(RabbitTemplate rabbitTemplate) {
    return new RabbitMessageSender<>(MY_EXCHANGE_NAME, rabbitTemplate);
  }

  public static String getMessageType(Class<?> messageClass) {
    return messageClass.getSimpleName();
  }

  @PostConstruct
  public void initialize() throws IOException {
    try (Connection connection = connectionFactory.createConnection()) {
      Channel channel = connection.createChannel(true);
      setupExchanges(channel);
      setupQueues(channel);
      log.info("initialized queues and exchanges");
    }
  }

  private void setupExchanges(Channel channel) throws IOException {
    setupExchange(channel, MY_EXCHANGE_NAME);
  }

  private void setupQueues(Channel channel) throws IOException {
    setupQueue(channel, MY_QUEUE_NAME, MY_EXCHANGE_NAME,
        binding(MyMessage.class, ALL));
  }

  private void setupQueue(Channel channel, String name, String exchangeName, BindingConfig... bindings) throws IOException {
    log.info("create queue {}", name);
    channel.queueDeclare(name, true, false, false, null);
    for (BindingConfig binding : bindings) {
      for (String apiVersion : binding.getApiVersions()) {
        String routingKey = binding.getMessageType() + "." + apiVersion;
        log.debug("bind queue {} to exchange {} with routing key {}", name, exchangeName, routingKey);
        channel.queueBind(name, exchangeName, routingKey);
      }
    }
  }

  private void setupExchange(Channel channel, String exchangeName) throws IOException {
    log.info("create exchange {}", exchangeName);
    channel.exchangeDeclare(exchangeName, ExchangeTypes.TOPIC, true, false, false, null);
  }

  private BindingConfig binding(Class<?> messageClass, String... apiVersions) {
    return new BindingConfig(getMessageType(messageClass), apiVersions);
  }

  @Data
  @AllArgsConstructor
  private static class BindingConfig {
    private String messageType;
    private String[] apiVersions;
  }
}
