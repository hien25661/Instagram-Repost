package com.sns.repost.utils;

import android.content.Context;

/**
 * Created by hien.nv on 5/4/17.
 */

public class AppSettings extends SharedPreferencesService{
    public Context mContext;
    public AppSettings(Context context) {
        super(context,context.getPackageName());
        mContext = context;
    }

    public void setInstagramAccessToken(String token){
        this.getSettings().edit().putString("InstagramToken",token).apply();
    }
    public String getInstagramAccessToken(){
        return getSettings().getString("InstagramToken", "");
    }

    public void setLogin(boolean isLogin){
        this.getSettings().edit().putBoolean("isLogin",isLogin).apply();
    }
    public boolean isLogin(){
        return getSettings().getBoolean("isLogin", false);
    }

}
