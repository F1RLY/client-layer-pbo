package api;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;
import java.util.function.Consumer;

public class WebSocketHandler extends WebSocketClient {
    private final Consumer<String> messageHandler;

    public WebSocketHandler(URI serverUri, Consumer<String> messageHandler) {
        super(serverUri);
        this.messageHandler = messageHandler;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("WebSocket Connected (Laragon Tier-2)");
    }

    @Override
    public void onMessage(String message) {
        // Meneruskan pesan ke UI via messageHandler
        messageHandler.accept(message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("WebSocket Closed: " + reason);
        // Logika auto-reconnect sesuai file tugas Anda
        new Thread(() -> {
            try {
                Thread.sleep(5000);
                reconnect();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("WebSocket Error: " + ex.getMessage());
    }
}