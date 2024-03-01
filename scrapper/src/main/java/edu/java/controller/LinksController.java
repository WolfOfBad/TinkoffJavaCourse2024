package edu.java.controller;

import edu.java.api.LinksApi;
import edu.java.controller.dto.request.AddLinkRequest;
import edu.java.controller.dto.request.RemoveLinkRequest;
import edu.java.controller.dto.response.LinkResponse;
import edu.java.controller.dto.response.ListLinksResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LinksController implements LinksApi {
    @Override
    public ResponseEntity<LinkResponse> linksDelete(Long tgChatId, RemoveLinkRequest removeLinkRequest) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<ListLinksResponse> linksGet(Long tgChatId) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<LinkResponse> linksPost(Long tgChatId, AddLinkRequest addLinkRequest) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}
