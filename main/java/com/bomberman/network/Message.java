package com.bomberman.network;

import com.bomberman.models.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum MessageType {
        CONNECT, DISCONNECT, GAME_UPDATE, PLAYER_MOVE, BOMB_PLACED,
        GAME_START, GAME_OVER, LEVEL_UP, PLAYER_DIED, CHAT
    }

    private MessageType type;
    private String playerId;
    private long timestamp;
    private Map<String, Object> data;

    public Message(MessageType type) {
        this.type = type;
        this.timestamp = System.currentTimeMillis();
        this.data = new HashMap<>();
    }

    public Message(MessageType type, String playerId) {
        this(type);
        this.playerId = playerId;
    }

    public void putData(String key, Object value) {
        data.put(key, value);
    }

    public Object getData(String key) {
        return data.get(key);
    }

    public MessageType getType() { return type; }
    public String getPlayerId() { return playerId; }
    public long getTimestamp() { return timestamp; }
    public Map<String, Object> getAllData() { return data; }
}