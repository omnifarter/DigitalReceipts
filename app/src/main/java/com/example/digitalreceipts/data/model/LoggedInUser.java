package com.example.digitalreceipts.data.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {
    //TODO fill in relevant data fields required of User
    private String userId;
    private String userName;
    private String displayName;
    /** Note that UserID == User Name **/

    public LoggedInUser(String userName, String displayName) {
        this.userName = userName;
        this.displayName = displayName;
    }
    public LoggedInUser(String userId) {
        this.userId = userId;
    }

    public void setUserDetails(String userName, String displayName) {
        this.displayName = displayName;
        this.userName = userName;
    }


    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {return displayName; }

    public String getUserName() { return userName; }

    public Map<String,Object> getAllInfo() {
        Map<String, Object> userData = new HashMap<>();
        userData.put("userId",getUserId());
        userData.put("userName", getUserName());
        userData.put("displayName",getDisplayName());
        return userData;
    }

}
