// File: api/WebSocketClient.java
package api;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * WebSocket Client untuk Java Swing application
 */
@ClientEndpoint
public class WebSocketClient {
    private static final Logger logger = Logger.getLogger(WebSocketClient.class.getName());
    
    private Session session;
    private String serverUrl;
    private List<Consumer<String>> messageHandlers = new ArrayList<>();
    private CountDownLatch connectionLatch = new CountDownLatch(1);
    private boolean connected = false;
    
    // Constructor
    public WebSocketClient(String serverUrl) {
        this.serverUrl = serverUrl;
    }
    
    // Connect to WebSocket server
    public boolean connect() {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, new URI(serverUrl));
            
            // Wait for connection (max 5 seconds)
            boolean connected = connectionLatch.await(5, TimeUnit.SECONDS);
            this.connected = connected;
            
            if (connected) {
                logger.info("WebSocket connected to: " + serverUrl);
            } else {
                logger.warning("Connection timeout to: " + serverUrl);
            }
            
            return connected;
            
        } catch (Exception e) {
            logger.severe("Failed to connect to WebSocket: " + e.getMessage());
            return false;
        }
    }
    
    // Disconnect
    public void disconnect() {
        if (session != null && session.isOpen()) {
            try {
                session.close();
                logger.info("WebSocket disconnected");
            } catch (IOException e) {
                logger.severe("Error closing WebSocket: " + e.getMessage());
            }
        }
        connected = false;
    }
    
    // Send message
    public boolean sendMessage(String message) {
        if (session != null && session.isOpen()) {
            try {
                session.getBasicRemote().sendText(message);
                logger.fine("Message sent: " + message);
                return true;
            } catch (IOException e) {
                logger.severe("Failed to send message: " + e.getMessage());
                return false;
            }
        }
        return false;
    }
    
    // Send formatted message
    public boolean sendMessage(MessageType type, String data) {
        String message = type.getValue() + "|" + data;
        return sendMessage(message);
    }
    
    // Send antrean panggil
    public boolean sendPanggilAntrean(String nomorAntrean, String pasien, String dokter, String ruangan) {
        String data = nomorAntrean + "|" + pasien + "|" + dokter + "|" + ruangan;
        return sendMessage(MessageType.ANTREAN_PANGGIL, data);
    }
    
    // Send antrean selesai
    public boolean sendSelesaiAntrean(String nomorAntrean) {
        return sendMessage(MessageType.ANTREAN_SELESAI, nomorAntrean);
    }
    
    // Send antrean baru
    public boolean sendAntreanBaru(String nomorAntrean, String pasien, String dokter) {
        String data = nomorAntrean + "|" + pasien + "|" + dokter;
        return sendMessage(MessageType.ANTREAN_BARU, data);
    }
    
    // Join room
    public boolean joinRoom(String roomName) {
        return sendMessage(MessageType.CONNECT, roomName);
    }
    
    // Ping server
    public boolean ping() {
        return sendMessage(MessageType.PING, "{}");
    }
    
    // Event handlers
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        this.connected = true;
        connectionLatch.countDown();
        logger.info("WebSocket session opened: " + session.getId());
    }
    
    @OnMessage
    public void onMessage(String message) {
        logger.fine("Message received: " + message);
        
        // Notify all handlers
        for (Consumer<String> handler : messageHandlers) {
            try {
                handler.accept(message);
            } catch (Exception e) {
                logger.severe("Error in message handler: " + e.getMessage());
            }
        }
    }
    
    @OnClose
    public void onClose(Session session, CloseReason reason) {
        this.connected = false;
        logger.info("WebSocket closed: " + reason.getReasonPhrase());
    }
    
    @OnError
    public void onError(Session session, Throwable error) {
        logger.severe("WebSocket error: " + error.getMessage());
    }
    
    // Add message handler
    public void addMessageHandler(Consumer<String> handler) {
        messageHandlers.add(handler);
    }
    
    // Remove message handler
    public void removeMessageHandler(Consumer<String> handler) {
        messageHandlers.remove(handler);
    }
    
    // Getters
    public boolean isConnected() {
        return connected && session != null && session.isOpen();
    }
    
    public Session getSession() {
        return session;
    }
    
    public String getServerUrl() {
        return serverUrl;
    }
}