package edu.java.scrapper.controller;

import edu.java.scrapper.api.LinksApi;
import edu.java.scrapper.controller.dto.request.AddLinkRequest;
import edu.java.scrapper.controller.dto.request.RemoveLinkRequest;
import edu.java.scrapper.controller.dto.response.LinkResponse;
import edu.java.scrapper.controller.dto.response.ListLinksResponse;
import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.service.LinkService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class LinksController implements LinksApi {
    private LinkService linkService;

    @Override
    public ResponseEntity<ListLinksResponse> linksGet(Long tgChatId) {
        List<Link> links = linkService.allLinks(tgChatId);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new ListLinksResponse(
                links.stream()
                    .map(link -> new LinkResponse(link.id(), link.uri()))
                    .toList(),
                links.size()
            ));
    }

    @Override
    public ResponseEntity<LinkResponse> linksPost(Long tgChatId, AddLinkRequest addLinkRequest) {
        Link link = linkService.add(tgChatId, addLinkRequest.uri());
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new LinkResponse(link.id(), link.uri()));
    }

    @Override
    public ResponseEntity<LinkResponse> linksDelete(Long tgChatId, RemoveLinkRequest removeLinkRequest) {
        Link link = linkService.remove(tgChatId, removeLinkRequest.uri());
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new LinkResponse(link.id(), link.uri()));
    }
}
