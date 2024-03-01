package edu.java.bot.controller;

import edu.java.bot.api.UpdatesApi;
import edu.java.bot.controller.dto.request.LinkUpdateRequest;
import edu.java.bot.service.UpdatesSendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UpdatesController implements UpdatesApi {
    private final UpdatesSendService updatesSendService;

    @Override
    public ResponseEntity<Void> updatesPost(LinkUpdateRequest linkUpdate) {
        updatesSendService.send(linkUpdate);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
