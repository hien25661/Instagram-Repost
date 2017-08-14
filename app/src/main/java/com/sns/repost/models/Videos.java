package com.sns.repost.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sns.repost.services.response.LoadLikedResponse;

/**
 * Created by nguyenvanhien on 8/12/17.
 */

public class Videos {

    @SerializedName("standard_resolution")
    @Expose
    private StandardResolution standardResolution = new StandardResolution();
    @SerializedName("low_bandwidth")
    @Expose
    private transient LowBandwidth lowBandwidth = new LowBandwidth();
    @SerializedName("low_resolution")
    @Expose
    private transient LowResolution lowResolution = new LowResolution();

    public StandardResolution getStandardResolution() {
        return standardResolution;
    }

    public void setStandardResolution(StandardResolution standardResolution) {
        this.standardResolution = standardResolution;
    }

    public LowBandwidth getLowBandwidth() {
        return lowBandwidth;
    }

    public void setLowBandwidth(LowBandwidth lowBandwidth) {
        this.lowBandwidth = lowBandwidth;
    }

    public LowResolution getLowResolution() {
        return lowResolution;
    }

    public void setLowResolution(LowResolution lowResolution) {
        this.lowResolution = lowResolution;
    }

}
