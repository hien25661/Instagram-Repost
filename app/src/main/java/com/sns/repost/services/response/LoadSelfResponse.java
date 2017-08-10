
package com.sns.repost.services.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sns.repost.models.User;

public class LoadSelfResponse {

    @SerializedName("data")
    @Expose
    private User data;
    @SerializedName("meta")
    @Expose
    private Meta meta;

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
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
