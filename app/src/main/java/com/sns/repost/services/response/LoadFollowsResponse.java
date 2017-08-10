package com.sns.repost.services.response;

/**
 * Created by Hien on 5/9/2017.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sns.repost.models.BaseModel;
import com.sns.repost.models.User;

import java.util.List;

public class LoadFollowsResponse extends BaseModel {

    @SerializedName("pagination")
    @Expose
    private Pagination pagination;
    @SerializedName("data")
    @Expose
    private List<User> data = null;
    @SerializedName("meta")
    @Expose
    private Meta meta;

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public List<User> getData() {
        return data;
    }

    public void setData(List<User> data) {
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

    public class Pagination {
        @SerializedName("next_url")
        @Expose
        private String nextUrl = "";

        public String getNextUrl() {
            return nextUrl;
        }

        public void setNextUrl(String nextUrl) {
            this.nextUrl = nextUrl;
        }

    }
}
