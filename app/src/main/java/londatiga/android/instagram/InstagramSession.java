package londatiga.android.instagram;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.sns.repost.utils.Util;

public class InstagramSession {
    private static final String ACCESS_TOKEN = "access_token";
    private static final String FULLNAME = "fullname";
    private static final String PROFILPIC = "profilpic";
    private static final String SHARED = "Instagram_Preferences";
    private static final String USERID = "userid";
    private static final String USERNAME = "username";
    private Context mContext;
    private SharedPreferences mSharedPref;

    public InstagramSession(Context context) {
        this.mContext = context;
        this.mSharedPref = context.getSharedPreferences(SHARED, 0);
    }

    public void store(InstagramUser user) {
        Editor editor = this.mSharedPref.edit();
        editor.putString(ACCESS_TOKEN, user.accessToken);
        editor.putString(USERID, user.id);
        editor.putString(USERNAME, user.username);
        editor.putString(FULLNAME, user.fullName);
        editor.putString(PROFILPIC, user.profilPicture);
        editor.commit();
    }

    public void reset() {
        Editor editor = this.mSharedPref.edit();
        editor.putString(ACCESS_TOKEN, "");
        editor.putString(USERID, "");
        editor.putString(USERNAME, "");
        editor.putString(FULLNAME, "");
        editor.putString(PROFILPIC, "");
        editor.commit();
        CookieSyncManager.createInstance(this.mContext);
        CookieManager.getInstance().removeAllCookie();
    }

    public InstagramUser getUser() {
        if (this.mSharedPref.getString(ACCESS_TOKEN, "").equals("")) {
            return null;
        }
        InstagramUser user = new InstagramUser();
        user.id = this.mSharedPref.getString(USERID, "");
        user.username = this.mSharedPref.getString(USERNAME, "");
        user.fullName = this.mSharedPref.getString(FULLNAME, "");
        user.profilPicture = this.mSharedPref.getString(PROFILPIC, "");
        user.accessToken = this.mSharedPref.getString(ACCESS_TOKEN, "");
        user.mediaCount = Util.mediaCount;
        user.followCount = Util.followCount;
        user.followerCount = Util.followerCount;
        return user;
    }

    public String getAccessToken() {
        return this.mSharedPref.getString(ACCESS_TOKEN, "");
    }

    public boolean isActive() {
        return !this.mSharedPref.getString(ACCESS_TOKEN, "").equals("");
    }
}
