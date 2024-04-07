package edu.java.scrapper.client.bot;

import edu.java.scrapper.client.bot.dto.request.LinkUpdateRequest;

public interface BotClient {
    void send(LinkUpdateRequest update);
}
