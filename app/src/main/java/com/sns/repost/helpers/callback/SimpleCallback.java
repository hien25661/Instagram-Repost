package com.sns.repost.helpers.callback;

/**
 * Created by Hien on 5/4/2017.
 */

public interface SimpleCallback {
    void success(Object... params);
    void failed();
}
