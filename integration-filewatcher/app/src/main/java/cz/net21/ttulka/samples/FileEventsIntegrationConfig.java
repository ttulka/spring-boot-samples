package cz.net21.ttulka.samples;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.core.MessageSelector;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.messaging.MessageChannel;

import cz.net21.ttulka.samples.filewatcher.FileEventsProducer;

@Configuration
public class FileEventsIntegrationConfig {

    @Bean
    public MessageProducer fileEventsProducer(@Value("${watcher.root}") Path rootPath) throws IOException {
        MessageProducer producer = new FileEventsProducer(rootPath);

        producer.setOutputChannel(fileEventsChannel());
        //producer.setErrorChannel(indexErrorChannel);

        return producer;
    }

    @Bean
    public MessageChannel fileEventsChannel() {
        Integer concurrentIndexing = 3;
        if (concurrentIndexing > 0) {
            return MessageChannels.executor(Executors.newCachedThreadPool()).get();
        } else {
            return MessageChannels.direct().get();
        }
    }

    @Bean
    public IntegrationFlow fileEventsFlow() {
        return IntegrationFlows
                .from(fileEventsChannel())
                .filter((MessageSelector) message -> message.getHeaders().containsKey("FILE_MODE"))
                .handle(message -> {
                    System.out.println("new event >>> ");
                    System.out.println("    FILE: " + message.getPayload());
                    System.out.println("    MODE: " + message.getHeaders().get("FILE_MODE"));
                })
                .get();
    }
}
