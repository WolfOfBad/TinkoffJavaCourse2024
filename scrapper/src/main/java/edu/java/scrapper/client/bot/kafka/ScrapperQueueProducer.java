package edu.java.scrapper.client.bot.kafka;

import edu.java.scrapper.client.bot.BotClient;
import edu.java.scrapper.client.bot.dto.request.LinkUpdateRequest;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

@AllArgsConstructor
public class ScrapperQueueProducer implements BotClient {
    private KafkaTemplate<String, LinkUpdateRequest> kafkaTemplate;
    private String topicName;

    @Override
    public void send(LinkUpdateRequest update) {
        kafkaTemplate.send(topicName, update);
    }
}
