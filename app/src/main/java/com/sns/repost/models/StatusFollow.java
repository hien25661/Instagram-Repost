package com.sns.repost.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nguyenvanhien on 6/9/17.
 */

public class StatusFollow extends BaseModel {
    @SerializedName("outgoing_status")
    @Expose
    private String outgoingStatus;
    @SerializedName("incoming_status")
    @Expose
    private String incomingStatus;

    public String getOutgoingStatus() {
        return outgoingStatus;
    }

    public void setOutgoingStatus(String outgoingStatus) {
        this.outgoingStatus = outgoingStatus;
    }

    public String getIncomingStatus() {
        return incomingStatus;
    }

    public void setIncomingStatus(String incomingStatus) {
        this.incomingStatus = incomingStatus;
    }
}
