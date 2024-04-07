package edu.java.scrapper.configuration;

import edu.java.scrapper.client.bot.BotClient;
import edu.java.scrapper.client.bot.dto.request.LinkUpdateRequest;
import edu.java.scrapper.client.bot.kafka.ScrapperQueueProducer;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.Name;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@ConfigurationProperties(prefix = "kafka", ignoreUnknownFields = false)
@ConditionalOnProperty(prefix = "app", name = "bot-api", havingValue = "kafka")
@EnableKafka
public record BotKafkaConfiguration(
    @Name("scrapper-topic")
    TopicConfig scrapperTopicConfig
) {
    @Bean
    public NewTopic scrapperTopic() {
        return TopicBuilder.name(scrapperTopicConfig.topicName)
            .partitions(scrapperTopicConfig.partitions)
            .replicas(scrapperTopicConfig.replicas)
            .build();
    }

    @Bean
    public ProducerFactory<String, LinkUpdateRequest> updatesProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, scrapperTopicConfig.bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, LinkUpdateRequest> updatesKafkaTemplate() {
        return new KafkaTemplate<>(updatesProducerFactory());
    }

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "bot-api", havingValue = "kafka")
    public BotClient scrapperQueueProducer() {
        return new ScrapperQueueProducer(updatesKafkaTemplate(), scrapperTopicConfig.topicName);
    }

    public record TopicConfig(
        @NotNull
        @NotEmpty
        @Name("name")
        String topicName,
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
