package com.example.digitalreceipts.data.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {
    //TODO fill in relevant data fields required of User
    private String userId;
    private String phoneNumber;
    private String displayName;

//    public LoggedInUser(String userId, String displayName) {
//        this.userId = userId;
//        this.displayName = displayName;
//        this.phoneNumber = phoneNumber;
//    }
    public LoggedInUser(String userId) {
        this.userId = userId;
    }

    public void setUserDetails(String displayName, String phoneNumber) {
        this.displayName = displayName;
        this.phoneNumber = phoneNumber;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {return displayName; }

    public String getPhoneNumber() { return phoneNumber; }

    public Map<String,Object> getAllInfo() {
        Map<String, Object> userData = new HashMap<>();
        userData.put("userId",getUserId());
        userData.put("phoneNumber",getPhoneNumber());
        userData.put("displayName",getDisplayName());
        return userData;
    }
}
