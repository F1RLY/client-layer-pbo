// File: api/WebSocketServer.java
package api;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * WebSocket Server untuk real-time communication
 * Endpoint: ws://localhost:8080/antrean
 */
@ServerEndpoint("/antrean")
public class WebSocketServer {
    private static final Logger logger = Logger.getLogger(WebSocketServer.class.getName());
    
    // Simpan semua session yang terhubung
    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());
    
    // Simpan session berdasarkan room/ruangan
    private static final ConcurrentHashMap<String, Set<Session>> roomSessions = new ConcurrentHashMap<>();
    
    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        logger.info("WebSocket connected: " + session.getId() + " - Total: " + sessions.size());
        
        // Send welcome message
        sendMessageToSession(session, createMessage(MessageType.CONNECT, 
            "{\"status\":\"connected\",\"clientId\":\"" + session.getId() + "\"}"));
    }
    
    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("Received from " + session.getId() + ": " + message);
        
        try {
            // Parse message (format: type|data)
            String[] parts = message.split("\\|", 2);
            if (parts.length < 2) {
                logger.warning("Invalid message format: " + message);
                return;
            }
            
            String typeStr = parts[0];
            String data = parts[1];
            
            MessageType type = MessageType.fromString(typeStr);
            if (type == null) {
                logger.warning("Unknown message type: " + typeStr);
                return;
            }
            
            // Handle different message types
            switch (type) {
                case ANTREAN_PANGGIL:
                    handlePanggilAntrean(data, session);
                    break;
                    
                case ANTREAN_SELESAI:
                    handleSelesaiAntrean(data, session);
                    break;
                    
                case ANTREAN_BARU:
                    handleAntreanBaru(data, session);
                    break;
                    
                case CONNECT:
                    // Join room based on data
                    joinRoom(data, session);
                    break;
                    
                case PING:
                    sendMessageToSession(session, createMessage(MessageType.PONG, "{}"));
                    break;
                    
                default:
                    // Broadcast to all
                    broadcast(message);
            }
            
        } catch (Exception e) {
            logger.severe("Error processing message: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @OnClose
    public void onClose(Session session, CloseReason reason) {
        sessions.remove(session);
        
        // Remove from all rooms
        for (Set<Session> room : roomSessions.values()) {
            room.remove(session);
        }
        
        logger.info("WebSocket disconnected: " + session.getId() + " - Reason: " + reason);
    }
    
    @OnError
    public void onError(Session session, Throwable error) {
        logger.severe("WebSocket error for " + session.getId() + ": " + error.getMessage());
        error.printStackTrace();
    }
    
    // Handler methods
    private void handlePanggilAntrean(String data, Session sender) {
        // Format: nomorAntrean|pasien|dokter|ruangan
        String[] parts = data.split("\\|");
        if (parts.length >= 4) {
            String nomorAntrean = parts[0];
            String pasien = parts[1];
            String dokter = parts[2];
            String ruangan = parts[3];
            
            // Create response
            String response = createMessage(MessageType.ANTREAN_PANGGIL,
                String.format("{\"nomor\":\"%s\",\"pasien\":\"%s\",\"dokter\":\"%s\",\"ruangan\":\"%s\",\"timestamp\":%d}",
                    nomorAntrean, pasien, dokter, ruangan, System.currentTimeMillis()));
            
            // Broadcast to all clients
            broadcast(response);
            
            // Also broadcast to specific room if needed
            if (roomSessions.containsKey(ruangan)) {
                broadcastToRoom(ruangan, response);
            }
            
            logger.info("Antrean dipanggil: " + nomorAntrean + " ke ruangan " + ruangan);
        }
    }
    
    private void handleSelesaiAntrean(String data, Session sender) {
        // Format: nomorAntrean
        String response = createMessage(MessageType.ANTREAN_SELESAI,
            String.format("{\"nomor\":\"%s\",\"timestamp\":%d}", data, System.currentTimeMillis()));
        
        broadcast(response);
        logger.info("Antrean selesai: " + data);
    }
    
    private void handleAntreanBaru(String data, Session sender) {
        // Format: nomorAntrean|pasien|dokter
        String[] parts = data.split("\\|");
        if (parts.length >= 3) {
            String response = createMessage(MessageType.ANTREAN_BARU,
                String.format("{\"nomor\":\"%s\",\"pasien\":\"%s\",\"dokter\":\"%s\",\"timestamp\":%d}",
                    parts[0], parts[1], parts[2], System.currentTimeMillis()));
            
            broadcast(response);
            logger.info("Antrean baru: " + parts[0]);
        }
    }
    
    private void joinRoom(String roomName, Session session) {
        roomSessions.computeIfAbsent(roomName, k -> Collections.synchronizedSet(new HashSet<>()))
                    .add(session);
        
        logger.info("Client " + session.getId() + " joined room: " + roomName);
        
        // Send confirmation
        sendMessageToSession(session, createMessage(MessageType.SYSTEM_INFO,
            "{\"message\":\"Joined room " + roomName + "\"}"));
    }
    
    // Utility methods
    public static void broadcast(String message) {
        synchronized (sessions) {
            for (Session session : sessions) {
                if (session.isOpen()) {
                    sendMessageToSession(session, message);
                }
            }
        }
    }
    
    public static void broadcastToRoom(String roomName, String message) {
        Set<Session> room = roomSessions.get(roomName);
        if (room != null) {
            synchronized (room) {
                for (Session session : room) {
                    if (session.isOpen()) {
                        sendMessageToSession(session, message);
                    }
                }
            }
        }
    }
    
    private static void sendMessageToSession(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            logger.severe("Failed to send message to " + session.getId() + ": " + e.getMessage());
        }
    }
    
    private String createMessage(MessageType type, String data) {
        return type.getValue() + "|" + data;
    }
    
    // Server management
    public static void startServer(int port) {
        // Note: In production, use proper WebSocket server implementation
        // For Java EE or Spring Boot, this is handled by the container
        logger.info("WebSocket Server configured to run on port: " + port);
        logger.info("Endpoint: ws://localhost:" + port + "/antrean");
    }
    
    public static int getConnectedClients() {
        return sessions.size();
    }
    
    public static Set<String> getActiveRooms() {
        return roomSessions.keySet();
    }
}