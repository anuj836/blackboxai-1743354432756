package com.example.rideshareneon.models;

public class RideOffer {
    private String driverId;
    private String startLocation;
    private String endLocation;
    private long timestamp;
    private boolean isRegular;
    private String schedule; // For regular commutes

    public RideOffer() {
        // Default constructor required for Firebase
    }

    public RideOffer(String driverId, String startLocation, String endLocation, long timestamp) {
        this.driverId = driverId;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.timestamp = timestamp;
        this.isRegular = false;
    }

    // Getters and setters
    public String getDriverId() {
        return driverId;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public boolean isRegular() {
        return isRegular;
    }

    public void setRegular(boolean regular) {
        isRegular = regular;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
}