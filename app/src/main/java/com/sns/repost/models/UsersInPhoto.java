package com.sns.repost.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sns.repost.services.response.LoadLikedResponse;

/**
 * Created by nguyenvanhien on 8/12/17.
 */

public class UsersInPhoto {

    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("position")
    @Expose
    private LoadLikedResponse.Position position;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LoadLikedResponse.Position getPosition() {
        return position;
    }

    public void setPosition(LoadLikedResponse.Position position) {
        this.position = position;
    }

}
