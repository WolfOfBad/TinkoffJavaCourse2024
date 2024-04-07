package edu.java.bot.controller;

import edu.java.bot.controller.dto.request.LinkUpdateRequest;
import edu.java.bot.service.UpdatesSendService;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UpdatesKafkaQueueListener {
    private UpdatesSendService updatesSendService;

    @KafkaListener(topics = "${kafka.scrapper-topic.name}", containerFactory = "updateKafkaListenerContainerFactory")
    public void listen(LinkUpdateRequest update) {
        updatesSendService.send(update);
    }
}
