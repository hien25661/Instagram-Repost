package com.sns.repost.models;

import com.google.gson.Gson;

/**
 * Created by Hien on 5/7/2017.
 */

public class BaseModel {
    public String toJson(){
        if(this!=null){
            return new Gson().toJson(this);
        }
        return null;
    }
}
