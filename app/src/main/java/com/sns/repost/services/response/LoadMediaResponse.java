package com.sns.repost.services.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sns.repost.models.BaseModel;
import com.sns.repost.models.Media;

/**
 * Created by Hien on 6/1/2017.
 */

public class LoadMediaResponse extends BaseModel {
    @SerializedName("data")
    @Expose
    private Media media;

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }
}
