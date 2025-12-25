// File: api/MessageType.java
package api;

/**
 * Enum untuk tipe-tipe pesan WebSocket
 */
public enum MessageType {
    // Antrean messages
    ANTREAN_PANGGIL("panggil"),
    ANTREAN_SELESAI("selesai"),
    ANTREAN_BATAL("batal"),
    ANTREAN_BARU("baru"),
    
    // System messages
    SYSTEM_INFO("info"),
    SYSTEM_WARNING("warning"),
    SYSTEM_ERROR("error"),
    
    // Connection
    CONNECT("connect"),
    DISCONNECT("disconnect"),
    PING("ping"),
    PONG("pong");
    
    private final String value;
    
    MessageType(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
    public static MessageType fromString(String text) {
        for (MessageType type : MessageType.values()) {
            if (type.value.equalsIgnoreCase(text)) {
                return type;
            }
        }
        return null;
    }
}