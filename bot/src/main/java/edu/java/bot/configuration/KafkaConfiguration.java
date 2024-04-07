package edu.java.bot.configuration;

import edu.java.bot.controller.dto.request.LinkUpdateRequest;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.Name;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@ConfigurationProperties(prefix = "kafka", ignoreUnknownFields = false)
public record KafkaConfiguration(
    @Name("scrapper-topic")
    TopicConfig scrapperTopicConfig
) {
    @Bean
    public ConsumerFactory<String, LinkUpdateRequest> updateConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, scrapperTopicConfig.bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, scrapperTopicConfig.listenerId);

        JsonDeserializer<LinkUpdateRequest> deserializer = new JsonDeserializer<>(LinkUpdateRequest.class);
        deserializer.addTrustedPackages("*");
        deserializer.setRemoveTypeHeaders(false);
        deserializer.setUseTypeMapperForKey(true);
        return new DefaultKafkaConsumerFactory<>(
            props,
            new StringDeserializer(),
            deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, LinkUpdateRequest> updateKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, LinkUpdateRequest> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(updateConsumerFactory());
        return factory;
    }

    public record TopicConfig(
        @NotNull
        @NotEmpty
        @Name("name")
        String topicName,
        @NotNull
        @NotEmpty
        String listenerId,
        @NotNull
        @NotEmpty
        String bootstrapAddress
    ) {
    }
}
