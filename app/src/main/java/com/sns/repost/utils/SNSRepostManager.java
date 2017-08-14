package com.sns.repost.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SNSRepostManager {
    private static final String API_ACCESS_TOKEN = "new_access_token";
    private static final String API_ID = "id";
    private static final String API_IMAGE_URL = "imgurl";
    private static final String API_NAME = "name";
    private static final String API_USERNAME = "username";
    private static final String SHARED = "Instagram_Preferences";
    private Editor editor = this.sharedPref.edit();
    private SharedPreferences sharedPref;

    public SNSRepostManager(Context paramContext) {
        this.sharedPref = paramContext.getSharedPreferences(SHARED, 0);
    }

    public String getAccessToken() {
        return this.sharedPref.getString(API_ACCESS_TOKEN, null);
    }

    public String getId() {
        return this.sharedPref.getString(API_ID, null);
    }

    public String getName() {
        return this.sharedPref.getString(API_NAME, null);
    }

    public String getProfileImage() {
        return this.sharedPref.getString(API_IMAGE_URL, null);
    }

    public String getUsername() {
        return this.sharedPref.getString(API_USERNAME, null);
    }

    public void resetAccessToken() {
        this.editor.putString(API_ID, null);
        this.editor.putString(API_NAME, null);
        this.editor.putString(API_ACCESS_TOKEN, null);
        this.editor.putString(API_USERNAME, null);
        this.editor.putString(API_IMAGE_URL, null);
        this.editor.commit();
        this.editor.apply();
    }

    public void storeAccessToken(String paramString) {
        this.editor.putString(API_ACCESS_TOKEN, paramString);
        this.editor.commit();
    }

    public void storeAccessToken(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5) {
        this.editor.putString(API_ID, paramString2);
        this.editor.putString(API_NAME, paramString4);
        this.editor.putString(API_ACCESS_TOKEN, paramString1);
        this.editor.putString(API_USERNAME, paramString3);
        this.editor.putString(API_IMAGE_URL, paramString5);
        this.editor.commit();
    }
}
