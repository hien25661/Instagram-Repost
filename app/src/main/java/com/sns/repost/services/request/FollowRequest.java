package com.sns.repost.services.request;

/**
 * Created by hien.nv on 5/10/17.
 */

public class FollowRequest {
    private String access_token = "";
    private String action = "";

    public FollowRequest(String access_token, String action) {
        this.access_token = access_token;
        this.action = action;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
