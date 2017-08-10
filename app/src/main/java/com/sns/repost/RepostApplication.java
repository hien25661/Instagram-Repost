package com.sns.repost;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


import com.sns.repost.utils.AppSettings;

import java.util.concurrent.atomic.AtomicBoolean;



public class RepostApplication extends Application {
    private AppSettings appSettings;
    private static RepostApplication instance;
    public static AtomicBoolean networkConnected;
    public static RepostApplication getInstance(){
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        appSettings = new AppSettings(this);
        instance = this;
    }
    public boolean isNetworkConnected() {
        if (networkConnected != null) return networkConnected.get();
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        networkConnected = new AtomicBoolean(ni != null);
        return networkConnected.get();
    }

    public AppSettings getAppSettings() {
        return appSettings;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

}
