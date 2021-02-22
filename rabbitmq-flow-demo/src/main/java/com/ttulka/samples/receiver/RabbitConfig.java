package com.ttulka.samples.receiver;

import static com.ttulka.samples.Messaging.MY_EXCHANGE_NAME;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ttulka.samples.MyEvent1;
import com.ttulka.samples.MyEvent2;
import com.ttulka.samples.sender.RabbitMessageSender;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

@Configuration
@AllArgsConstructor
public class RabbitConfig implements RabbitListenerConfigurer {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(myHandlerMethodFactory());
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        Jackson2JsonMessageConverter messageConverter = new Jackson2JsonMessageConverter(objectMapper);
        template.setMessageConverter(messageConverter);
        return template;
    }

    @Bean
    public DefaultMessageHandlerMethodFactory myHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
        messageConverter.setObjectMapper(objectMapper);
        factory.setMessageConverter(messageConverter);
        return factory;
    }

    @Bean
    public RabbitMessageSender<MyEvent1> myEvent1Sender(RabbitTemplate rabbitTemplate) {
        return new RabbitMessageSender<>(MY_EXCHANGE_NAME, rabbitTemplate);
    }

    @Bean
    public RabbitMessageSender<MyEvent2> myEvent2Sender(RabbitTemplate rabbitTemplate) {
        return new RabbitMessageSender<>(MY_EXCHANGE_NAME, rabbitTemplate);
    }
}
