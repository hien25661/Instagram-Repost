package com.sns.repost;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.sns.repost.helpers.EventBusUtils;

/**
 * Created by hien.nv on 5/9/17.
 */

public class BaseActivity extends AppCompatActivity{
    protected EventBusUtils.Holder eventBus = EventBusUtils.getDefault();
    @Override
    protected void onResume() {
        super.onResume();
        // Register EventBus
        EventBusUtils.register(eventBus, this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        // Register EventBus
        //EventBusUtils.unregister(eventBus, this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtils.unregister(eventBus, this);
    }
}
