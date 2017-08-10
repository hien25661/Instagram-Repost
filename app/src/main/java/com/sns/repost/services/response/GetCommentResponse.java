package com.sns.repost.services.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sns.repost.models.BaseModel;
import com.sns.repost.models.Comment;

import java.util.List;

/**
 * Created by hien.nv on 6/5/17.
 */

public class GetCommentResponse extends BaseModel {
    @SerializedName("data")
    @Expose
    private List<Comment> data = null;

    public List<Comment> getData() {
        return data;
    }

    public void setData(List<Comment> data) {
        this.data = data;
    }

}
