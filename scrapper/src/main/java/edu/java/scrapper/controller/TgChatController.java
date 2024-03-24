package edu.java.scrapper.controller;

import edu.java.scrapper.api.TgChatApi;
import edu.java.scrapper.service.TgChatService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class TgChatController implements TgChatApi {
    private TgChatService tgChatService;

    @Override
    public ResponseEntity<Void> tgChatIdPost(Long id) {
        tgChatService.register(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> tgChatIdDelete(Long id) {
        tgChatService.unregister(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
