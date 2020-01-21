package com.example.digitalreceipts.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {
    //TODO fill in relevant data fields required of User
    private String userId;
    private String phoneNumber;
    private String displayName;
    public LoggedInUser(String userId, String displayName) {
        this.userId = userId;
        this.displayName = displayName;
        this.phoneNumber = phoneNumber;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {return displayName; }

    public String getPhoneNumber() { return phoneNumber; }
}
