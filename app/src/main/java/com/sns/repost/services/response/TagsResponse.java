package com.sns.repost.services.response;


import com.sns.repost.models.BaseModel;
import com.sns.repost.models.Tag;

import java.util.ArrayList;

/**
 * Created by nguyenvanhien on 7/10/17.
 */

public class TagsResponse extends BaseModel {
    ArrayList<Tag> data = new ArrayList<>();

    public ArrayList<Tag> getTags() {
        return data;
    }

    public void setTags(ArrayList<Tag> tags) {
        this.data = tags;
    }
}
