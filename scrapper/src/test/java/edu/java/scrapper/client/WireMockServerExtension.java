package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.Getter;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class WireMockServerExtension implements BeforeAllCallback, ExtensionContext.Store.CloseableResource {
    private static boolean started;
    @Getter
    private static WireMockServer wireMockServer;
    @Getter
    private static final int port = 8080;

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        if (!started) {
            wireMockServer = new WireMockServer(port);
            wireMockServer.start();

            started = true;
            extensionContext.getRoot().getStore(ExtensionContext.Namespace.GLOBAL).put("wiremock server", this);
        }
    }

    @Override
    public void close() {
        wireMockServer.stop();
        started = false;
    }

    public static void resetAll() {
        wireMockServer.resetAll();
    }
}
