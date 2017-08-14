package com.sns.repost.utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginUtil {
    @SerializedName("logged_in_user")
    @Expose
    private LoggedInUser loggedInUser;
    @SerializedName("status")
    @Expose
    private String status;

    public LoggedInUser getLoggedInUser() {
        return this.loggedInUser;
    }

    public String getStatus() {
        return this.status;
    }

    public void setLoggedInUser(LoggedInUser paramLoggedInUser) {
        this.loggedInUser = paramLoggedInUser;
    }

    public void setStatus(String paramString) {
        this.status = paramString;
    }
}
