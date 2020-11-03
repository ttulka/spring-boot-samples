package com.ttulka.samples.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.Instant;

@SpringBootApplication
@EnableAsync
public class RedisMessagingApplication {

	public static void main(String[] args) throws Exception {
		var ac =	SpringApplication.run(RedisMessagingApplication.class, args);

		var publisher = ac.getBean(EventPublisher.class);

		publisher.raise(new MyEvent1(Instant.now(), "TEST1"));
		publisher.raise(new MyEvent2(Instant.now(), "TEST2", "test 2"));

		Thread.sleep(100);
		ac.close(); // otherwise the listeners will be running forever
	}

	@Bean // fallback to application events
	EventPublisher eventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		return applicationEventPublisher::publishEvent;
	}

	@Component
	static class Listeners {

		@TransactionalEventListener
		@Async
		public void on(MyEvent1 event) {
			System.out.println("Received App MyEvent1: " + event);
		}

		@TransactionalEventListener
		@Async
		public void on(MyEvent2 event) {
			System.out.println("Received App MyEvent2: " + event);
		}
	}

	@Configuration
	static class RedisConfig {

		@Primary
		@Bean
		EventPublisher redisEventPublisher(RedisTemplate<String, DomainEvent> redisTemplate) {
			return evt -> redisTemplate.convertAndSend(evt.getClass().getSimpleName(), evt);
		}

		@Bean
		RedisMessageListenerContainer redisContainer(RedisConnectionFactory factory, MessageListenerAdapter adapter) {
			var container = new RedisMessageListenerContainer();
			container.setConnectionFactory(factory);
			container.addMessageListener(adapter, new ChannelTopic(MyEvent1.class.getSimpleName()));
			container.addMessageListener(adapter, new ChannelTopic(MyEvent2.class.getSimpleName()));
			return container;
		}

		@Bean
		MessageListenerAdapter redisMessageAdapter(RedisSerializer redisSerializer, ApplicationEventPublisher applicationEventPublisher) {
			return new MessageListenerAdapter(new EventListenerApplicationAdapter(redisSerializer, applicationEventPublisher));
		}

		@Bean
		RedisTemplate<String, DomainEvent> redisTemplate(RedisConnectionFactory factory, RedisSerializer redisSerializer) {
			var template = new RedisTemplate<String, DomainEvent>();
			template.setConnectionFactory(factory);
			template.setDefaultSerializer(redisSerializer);
			return template;
		}

		// listens to Redis, re-publishes as Spring application events
		@RequiredArgsConstructor
		static class EventListenerApplicationAdapter implements MessageListener {

			private final RedisSerializer serializer;
			private final ApplicationEventPublisher publisher;

			@Override
			public void onMessage(Message message, byte[] pattern) {
				publisher.publishEvent(serializer.deserialize(message.getBody()));
			}
		}

		@Configuration
		static class JdkSerializationConfig {

			@Primary
			@Bean // this is the default for Redis
			JdkSerializationRedisSerializer jdkSerializationRedisSerializer() {
				return new JdkSerializationRedisSerializer();
			}
		}

		@Configuration
		static class JsonSerializationConfig {

			@Bean
			GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer(ObjectMapper objectMapper) {
				return new GenericJackson2JsonRedisSerializer(objectMapper);
			}

			@Bean // because of Java 8 time instances
			public ObjectMapper objectMapper() {
				return JsonMapper.builder()
						.findAndAddModules()
						.build();
			}
		}
	}
}
