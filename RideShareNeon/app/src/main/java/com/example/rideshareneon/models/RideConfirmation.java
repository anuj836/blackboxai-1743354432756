package com.example.rideshareneon.models;

public class RideConfirmation {
    private String userId;
    private String rideId;
    private long timestamp;
    private String status; // pending, confirmed, cancelled, completed
    private double price;
    private String paymentMethod;

    public RideConfirmation() {
        // Default constructor required for Firebase
    }

    public RideConfirmation(String userId, String rideId, long timestamp, String status) {
        this.userId = userId;
        this.rideId = rideId;
        this.timestamp = timestamp;
        this.status = status;
        this.price = 0.0;
        this.paymentMethod = "";
    }

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public String getRideId() {
        return rideId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getStatus() {
        return status;
    }

    public double getPrice() {
        return price;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}