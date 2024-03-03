package edu.java.controller;

import edu.java.api.LinksApi;
import edu.java.controller.dto.request.AddLinkRequest;
import edu.java.controller.dto.request.RemoveLinkRequest;
import edu.java.controller.dto.response.LinkResponse;
import edu.java.controller.dto.response.ListLinksResponse;
import java.util.ArrayList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class LinksController implements LinksApi {
    @Override
    public ResponseEntity<ListLinksResponse> linksGet(Long tgChatId) {
        log.info("Getting links...");
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new ListLinksResponse(new ArrayList<>(), 0));
    }

    @Override
    public ResponseEntity<LinkResponse> linksPost(Long tgChatId, AddLinkRequest addLinkRequest) {
        log.info("Adding link...");
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new LinkResponse(0, addLinkRequest.uri()));
    }

    @Override
    public ResponseEntity<LinkResponse> linksDelete(Long tgChatId, RemoveLinkRequest removeLinkRequest) {
        log.info("Deleting link...");
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new LinkResponse(0, removeLinkRequest.uri()));
    }
}
