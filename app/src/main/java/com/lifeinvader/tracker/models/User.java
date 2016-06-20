package com.lifeinvader.tracker.models;

public class User {
    public String firstName;
    public String lastName;

    public double latitude;
    public double longitude;

    public User() {
        // NOOP
    }

    public User(String firstName, String lastName, double latitude, double longitude) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
