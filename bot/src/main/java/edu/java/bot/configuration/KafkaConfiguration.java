package edu.java.bot.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.bot.controller.dto.request.LinkUpdateRequest;
import edu.java.bot.kafka.UpdatesKafkaQueueListener;
import edu.java.bot.service.UpdatesSendService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.Name;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@ConfigurationProperties(prefix = "kafka", ignoreUnknownFields = false)
public record KafkaConfiguration(
    @Name("scrapper-topic")
    TopicConfig scrapperTopicConfig,
    @Name("dlq-topic")
    TopicConfig dlqTopicConfig
) {
    @Bean
    public NewTopic scrapperNewTopic() {
        return TopicBuilder
            .name(scrapperTopicConfig.topicName)
            .partitions(scrapperTopicConfig.partitions)
            .replicas(scrapperTopicConfig.replicas)
            .build();
    }

    @Bean
    public NewTopic dlqScrapperNewTopic() {
        return TopicBuilder
            .name(dlqTopicConfig.topicName)
            .partitions(dlqTopicConfig.partitions)
            .replicas(dlqTopicConfig.replicas)
            .build();
    }

    @Bean
    public ConsumerFactory<String, LinkUpdateRequest> updateConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, scrapperTopicConfig.bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, scrapperTopicConfig.listenerId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, LinkUpdateRequest> updateKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, LinkUpdateRequest> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(updateConsumerFactory());
        factory.setRecordMessageConverter(new JsonMessageConverter(new ObjectMapper()));
        return factory;
    }

    @Bean
    public ProducerFactory<String, String> updateProducerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, scrapperTopicConfig.bootstrapAddress);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, String> updateKafkaTemplate() {
        return new KafkaTemplate<>(updateProducerFactory());
    }

    @Bean
    public UpdatesKafkaQueueListener updatesKafkaQueueListener(
        UpdatesSendService updatesSendService
    ) {
        return new UpdatesKafkaQueueListener(
            updatesSendService,
            updateKafkaTemplate(),
            dlqTopicConfig.topicName
        );
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
        int partitions,
        @NotNull
        int replicas,
        @NotNull
        @NotEmpty
        String bootstrapAddress
    ) {
    }
}
