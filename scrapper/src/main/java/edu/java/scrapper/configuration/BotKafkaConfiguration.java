package edu.java.scrapper.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.scrapper.client.bot.BotClient;
import edu.java.scrapper.client.bot.kafka.ScrapperQueueProducer;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.Name;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@ConfigurationProperties(prefix = "kafka", ignoreUnknownFields = false)
@ConditionalOnProperty(prefix = "app", name = "bot-api", havingValue = "kafka")
@EnableKafka
public record BotKafkaConfiguration(
    @Name("scrapper-topic")
    TopicConfig scrapperTopicConfig
) {
    @Bean
    public ProducerFactory<String, String> updatesProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, scrapperTopicConfig.bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, String> updatesKafkaTemplate() {
        return new KafkaTemplate<>(updatesProducerFactory());
    }

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "bot-api", havingValue = "kafka")
    public BotClient scrapperQueueProducer(ObjectMapper mapper) {
        return new ScrapperQueueProducer(updatesKafkaTemplate(), scrapperTopicConfig.topicName, mapper);
    }

    public record TopicConfig(
        @NotNull
        @NotEmpty
        @Name("name")
        String topicName,
        @NotNull
        @NotEmpty
        String bootstrapAddress
    ) {
    }
}
