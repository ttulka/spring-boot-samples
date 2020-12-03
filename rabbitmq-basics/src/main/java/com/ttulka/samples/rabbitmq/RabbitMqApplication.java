package com.ttulka.samples.rabbitmq;

import java.io.Serializable;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@SpringBootApplication
public class RabbitMqApplication {

    static final String QUEUE_NAME = "mytest";
    static final String TOPIC_EXCHANGE_NAME = "mytest-exchange";

    public static void main(String[] args) {
        SpringApplication.run(RabbitMqApplication.class, args)
                .getBean(Sender.class)
                .run();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    static class MyMessage implements Serializable {
        private String message;
    }

    @Component
    @RequiredArgsConstructor
    static class Sender implements Runnable {

        private final RabbitTemplate rabbit;

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                rabbit.convertAndSend(
                        TOPIC_EXCHANGE_NAME, "foo.bar.baz",
                        new MyMessage("Hello from RabbitMQ! " + (i + 1)));
            }
        }
    }

    @Bean
    Queue queue() {
        return new Queue(QUEUE_NAME, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(TOPIC_EXCHANGE_NAME);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("foo.bar.#");
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(QUEUE_NAME);
        container.setMessageListener(msg -> System.out.println("Received: " + new String(msg.getBody())));
        return container;
    }

    @Bean
    public MessageConverter jsonConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
