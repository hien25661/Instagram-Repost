package com.sns.repost.fragments;

import android.support.v4.app.Fragment;

import com.sns.repost.helpers.EventBusUtils;


/**
 * Created by hien.nv on 5/9/17.
 */

public class BaseFragment extends Fragment{
    protected EventBusUtils.Holder eventBus = EventBusUtils.getDefault();
    private boolean isStillAlive;
    @Override
    public void onResume() {
        super.onResume();
        // Register EventBus
        EventBusUtils.register(eventBus, this);
        isStillAlive = true;

    }

    public boolean isStillAlive() {
        return isStillAlive;
    }

    @Override
    public void onPause() {
        super.onPause();
        // Register EventBus
        //EventBusUtils.unregister(eventBus, this);
        isStillAlive = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusUtils.unregister(eventBus, this);
        isStillAlive = false;
    }
}
