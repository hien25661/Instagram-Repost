package com.sns.repost.utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoggedInUser {
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("has_anonymous_profile_picture")
    @Expose
    private Boolean hasAnonymousProfilePicture;
    @SerializedName("is_private")
    @Expose
    private Boolean isPrivate;
    @SerializedName("pk")
    @Expose
    private Long pk;
    @SerializedName("profile_pic_url")
    @Expose
    private String profilePicUrl;
    @SerializedName("username")
    @Expose
    private String username;

    public String getFullName() {
        return this.fullName;
    }

    public Boolean getHasAnonymousProfilePicture() {
        return this.hasAnonymousProfilePicture;
    }

    public Boolean getIsPrivate() {
        return this.isPrivate;
    }

    public Long getPk() {
        return this.pk;
    }

    public String getProfilePicUrl() {
        return this.profilePicUrl;
    }

    public String getUsername() {
        return this.username;
    }

    public void setFullName(String paramString) {
        this.fullName = paramString;
    }

    public void setHasAnonymousProfilePicture(Boolean paramBoolean) {
        this.hasAnonymousProfilePicture = paramBoolean;
    }

    public void setIsPrivate(Boolean paramBoolean) {
        this.isPrivate = paramBoolean;
    }

    public void setPk(Long paramLong) {
        this.pk = paramLong;
    }

    public void setProfilePicUrl(String paramString) {
        this.profilePicUrl = paramString;
    }

    public void setUsername(String paramString) {
        this.username = paramString;
    }
}
