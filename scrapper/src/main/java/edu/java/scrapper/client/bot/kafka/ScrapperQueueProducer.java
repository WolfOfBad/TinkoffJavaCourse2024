package edu.java.scrapper.client.bot.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.scrapper.client.bot.BotClient;
import edu.java.scrapper.client.bot.dto.request.LinkUpdateRequest;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

@AllArgsConstructor
public class ScrapperQueueProducer implements BotClient {
    private KafkaTemplate<String, String> kafkaTemplate;
    private String topicName;
    private ObjectMapper mapper;

    @Override
    public void send(LinkUpdateRequest update) {
        try {
            String data = mapper.writeValueAsString(update);
            kafkaTemplate.send(topicName, data);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
