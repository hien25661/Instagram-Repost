package com.sns.repost.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hien.nv on 6/5/17.
 */

public class Comment extends BaseModel {
    @SerializedName("created_time")
    @Expose
    private String created_time;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("from")
    @Expose
    private User from;
    @SerializedName("id")
    @Expose
    private String id;

    public String getCreatedTime() {
        return created_time;
    }

    public void setCreatedTime(String createdTime) {
        this.created_time = createdTime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
