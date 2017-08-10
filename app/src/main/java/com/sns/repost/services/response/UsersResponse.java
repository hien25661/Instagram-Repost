package com.sns.repost.services.response;


import com.sns.repost.models.BaseModel;
import com.sns.repost.models.User;

import java.util.ArrayList;

/**
 * Created by nguyenvanhien on 7/10/17.
 */

public class UsersResponse extends BaseModel {
    ArrayList<User> data = new ArrayList<>();

    public ArrayList<User> getUsers() {
        return data;
    }

    public void setUsers(ArrayList<User> users) {
        this.data = users;
    }
}
