package edu.java.bot.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.bot.controller.dto.request.LinkUpdateRequest;
import edu.java.bot.service.UpdatesSendService;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

@AllArgsConstructor
public class UpdatesKafkaQueueListener {
    private UpdatesSendService updatesSendService;
    private ObjectMapper mapper;
    private KafkaTemplate<String, String> kafkaTemplate;
    private String dlqTopicName;

    @KafkaListener(topics = "${kafka.scrapper-topic.name}", containerFactory = "updateKafkaListenerContainerFactory")
    public void listen(ConsumerRecord<String, String> consumerRecord) {
        String id = consumerRecord.key();
        String json = consumerRecord.value();

        try {
            LinkUpdateRequest request = mapper.readValue(json, LinkUpdateRequest.class);

            updatesSendService.send(request);
        } catch (Exception exception) {
            sendToDlq(id, json, exception);
        }
    }

    private void sendToDlq(String id, String json, Exception exception) {
        String data = String.format("""
            {
                json: {
                    %s
                }
                exceptionMessage: {
                    %s
                }
                stackTrace: {
                    %s
                }
            }
            """, json, exception.getMessage(), Arrays.toString(exception.getStackTrace()));

        kafkaTemplate.send(dlqTopicName, id, data);
    }
}
