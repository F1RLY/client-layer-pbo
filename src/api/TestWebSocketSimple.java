// File: api/TestWebSocketSimple.java
package api;

import javax.websocket.*;
import javax.websocket.server.*;;



public class TestWebSocketSimple {
    public static void main(String[] args) {
        System.out.println("=== WEB SOCKET SIMPLE TEST ===\n");
        
        // Test 1: Cek import basic
        System.out.println("1. Testing javax.websocket.* import...");
        System.out.println("   ✅ Import SUCCESS!");
        
        // Test 2: Cek annotation
        System.out.println("\n2. Testing WebSocket annotations...");
        try {
            Class<?> clientEndpoint = ClientEndpoint.class;
            Class<?> serverEndpoint = ServerEndpoint.class;
            System.out.println("   ✅ @ClientEndpoint: " + clientEndpoint.getName());
            System.out.println("   ✅ @ServerEndpoint: " + serverEndpoint.getName());
        } catch (Exception e) {
            System.err.println("   ❌ Error: " + e.getMessage());
        }
        
        // Test 3: Cek class dari library Tyrus
        System.out.println("\n3. Testing Tyrus implementation...");
        try {
            // Coba buat WebSocketContainer (dari Tyrus)
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            String containerClass = container.getClass().getName();
            
            if (containerClass.contains("tyrus")) {
                System.out.println("   ✅ Tyrus found: " + containerClass);
            } else {
                System.out.println("   ⚠️  Container: " + containerClass + " (not Tyrus?)");
            }
        } catch (Exception e) {
            System.err.println("   ❌ Error getting container: " + e.getMessage());
            System.err.println("   📌 Make sure tyrus JAR is in classpath!");
        }
        
        // Test 4: Cek MessageType kita
        System.out.println("\n4. Testing custom MessageType enum...");
        try {
            MessageType[] types = MessageType.values();
            System.out.println("   ✅ MessageType has " + types.length + " types:");
            for (MessageType type : types) {
                System.out.println("      - " + type.name() + " = '" + type.getValue() + "'");
            }
        } catch (Exception e) {
            System.err.println("   ❌ Error: " + e.getMessage());
        }
        
        System.out.println("\n=== TEST COMPLETE ===");
        System.out.println("\n📌 Next steps:");
        System.out.println("1. Run this test to check WebSocket setup");
        System.out.println("2. If success, compile WebSocketClient.java");
        System.out.println("3. Test real connection when server is ready");
    }
}