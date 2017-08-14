package com.sns.repost.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nguyenvanhien on 8/12/17.
 */

public class StandardResolution {

    @SerializedName("width")
    @Expose
    private Integer width = 0;
    @SerializedName("height")
    @Expose
    private Integer height = 0;
    @SerializedName("url")
    @Expose
    private String url = "";

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
