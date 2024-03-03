package edu.java.controller;

import edu.java.api.TgChatApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TgChatController implements TgChatApi {
    @Override
    public ResponseEntity<Void> tgChatIdPost(Long id) {
        log.info("Registering user...");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> tgChatIdDelete(Long id) {
        log.info("Deleting user...");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
