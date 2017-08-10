package com.sns.repost.services.response;;import com.sns.repost.models.BaseModel;
import com.sns.repost.models.User;

/**
 * Created by hien.nv on 5/4/17.
 */

public class InstagramGetTokenResponse extends BaseModel {
    private User user;
    private String access_token;

    public User getUser ()
    {
        return user;
    }

    public void setUser (User user)
    {
        this.user = user;
    }

    public String getAccess_token ()
    {
        return access_token;
    }

    public void setAccess_token (String access_token)
    {
        this.access_token = access_token;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [user = "+user+", access_token = "+access_token+"]";
    }

}
