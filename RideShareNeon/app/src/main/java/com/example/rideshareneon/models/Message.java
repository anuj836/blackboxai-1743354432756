package com.example.rideshareneon.models;

import java.util.Date;

public class Message {
    private String senderId;
    private String text;
    private String imageUrl;
    private long timestamp;
    private boolean isRead;

    public Message() {
        // Default constructor required for Firebase
    }

    public Message(String senderId, String text, long timestamp) {
        this.senderId = senderId;
        this.text = text;
        this.timestamp = timestamp;
    }

    // Getters
    public String getSenderId() {
        return senderId;
    }

    public String getText() {
        return text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    // Format timestamp for display
    public String getFormattedTime() {
        return new Date(timestamp).toString();
    }
}