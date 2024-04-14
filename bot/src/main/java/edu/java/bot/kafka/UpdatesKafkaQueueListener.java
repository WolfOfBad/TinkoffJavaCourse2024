package edu.java.bot.kafka;

import edu.java.bot.controller.dto.request.LinkUpdateRequest;
import edu.java.bot.service.UpdatesSendService;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

@AllArgsConstructor
public class UpdatesKafkaQueueListener {
    private UpdatesSendService updatesSendService;
    private KafkaTemplate<String, String> kafkaTemplate;
    private String dlqTopicName;

    @KafkaListener(topics = "${kafka.scrapper-topic.name}", containerFactory = "updateKafkaListenerContainerFactory")
    public void listen(LinkUpdateRequest request) {
        try {
            updatesSendService.send(request);
        } catch (Exception exception) {
            sendToDlq(request, exception);
        }
    }

    private void sendToDlq(LinkUpdateRequest request, Exception exception) {
        String data = String.format("""
            {
                request: {
                    %s
                }
                exceptionMessage: {
                    %s
                }
                stackTrace: {
                    %s
                }
            }
            """, request, exception.getMessage(), Arrays.toString(exception.getStackTrace()));

        kafkaTemplate.send(dlqTopicName, data);
    }
}
