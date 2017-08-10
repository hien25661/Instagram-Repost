package com.sns.repost.services.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sns.repost.models.BaseModel;
import com.sns.repost.models.StatusFollow;

/**
 * Created by nguyenvanhien on 6/9/17.
 */

public class GetRelationShipResponse extends BaseModel {

    @SerializedName("data")
    @Expose
    private StatusFollow data;

    public StatusFollow getData() {
        return data;
    }

    public void setData(StatusFollow data) {
        this.data = data;
    }
}
