package com.sns.repost.services.response;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sns.repost.models.BaseModel;

/**
 * Created by hien.nv on 6/7/17.
 */

public class LikeResponse extends BaseModel {
    @SerializedName("meta")
    @Expose
    private Meta meta;
    @SerializedName("data")
    @Expose
    private Object data;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
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
