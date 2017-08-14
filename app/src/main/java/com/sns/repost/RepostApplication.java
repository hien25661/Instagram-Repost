package com.sns.repost;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


import com.crashlytics.android.Crashlytics;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.sns.repost.utils.AppSettings;

import io.fabric.sdk.android.Fabric;
import java.util.concurrent.atomic.AtomicBoolean;

import okhttp3.OkHttpClient;


public class RepostApplication extends Application {
    private AppSettings appSettings;
    private static RepostApplication instance;
    public static AtomicBoolean networkConnected;
    public static RepostApplication getInstance(){
        return instance;
    }

    ClearableCookieJar cookieJar;
    OkHttpClient httpClient;
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        appSettings = new AppSettings(this);
        instance = this;
        this.cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(this));
        this.httpClient = new OkHttpClient.Builder().cookieJar(this.cookieJar).build();
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
    public OkHttpClient getHttpClient() {
        return this.httpClient;
    }

    public void logout() {
        this.cookieJar.clear();
    }
}
