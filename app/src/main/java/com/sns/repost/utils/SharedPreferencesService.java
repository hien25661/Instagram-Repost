package com.sns.repost.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.lang.ref.WeakReference;

/**
 * Created by hien.nv on 5/4/17.
 */

public class SharedPreferencesService {
    private SharedPreferences settings;

    private WeakReference<Context> context;

    public SharedPreferencesService(Context context, String prefsName) {
        this.context = new WeakReference<>(context);
        this.settings = context.getSharedPreferences(prefsName, Activity.MODE_PRIVATE);
    }

    protected SharedPreferences.Editor getEditor() {
        return this.settings.edit();
    }

    protected SharedPreferences getSettings() {
        return this.settings;
    }

    protected Context getContext() {
        return this.context.get();
    }
}
