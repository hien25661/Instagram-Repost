package com.sns.repost.services.response;

/**
 * Created by hien.nv on 5/10/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class GetFollowStatusResponse {

    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("meta")
    @Expose
    private Meta meta;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public class Data {

        @SerializedName("outgoing_status")
        @Expose
        private String outgoingStatus;
        @SerializedName("incoming_status")
        @Expose
        private String incomingStatus;
        @SerializedName("target_user_is_private")
        @Expose
        private Boolean targetUserIsPrivate;

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

        public Boolean getTargetUserIsPrivate() {
            return targetUserIsPrivate;
        }

        public void setTargetUserIsPrivate(Boolean targetUserIsPrivate) {
            this.targetUserIsPrivate = targetUserIsPrivate;
        }

    }
    public class Meta {

        @SerializedName("code")
        @Expose
        private Integer code;

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

    }

}


