package com.sns.repost.services.request;


import com.sns.repost.models.BaseModel;

/**
 * Created by hien.nv on 6/7/17.
 */

public class LikeRequest extends BaseModel {
    private String access_token = "";

    public LikeRequest(String access_token) {
        this.access_token = access_token;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
}
