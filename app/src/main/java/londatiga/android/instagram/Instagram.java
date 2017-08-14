package londatiga.android.instagram;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.sns.repost.utils.LoggedInUser;

import java.net.URL;
import londatiga.android.instagram.InstagramDialog.InstagramDialogListener;

public class Instagram {
    Activity mActivity;
    private String mClientId;
    private String mClientSecret;
    private Context mContext;
    private InstagramDialog mDialog;
    private InstagramAuthListener mListener;
    private String mRedirectUri;
    private InstagramSession mSession;

    public class AccessTokenTask extends AsyncTask<URL, Integer, Long> {
        String code;
        LoggedInUser lUser;
        ProgressDialog progressDlg;
        InstagramUser user;

        public AccessTokenTask(String code) {
            this.code = code;
            this.progressDlg = new ProgressDialog(Instagram.this.mContext);
            this.progressDlg.setMessage("Getting access token...");
        }

        public AccessTokenTask(LoggedInUser user) {
            this.lUser = user;
            this.progressDlg = new ProgressDialog(Instagram.this.mContext);
            this.progressDlg.setMessage("Getting access token...");
        }

        protected void onCancelled() {
            this.progressDlg.cancel();
        }

        protected void onPreExecute() {
            this.progressDlg.show();
        }

        protected Long doInBackground(URL... urls) {
            this.user = new InstagramUser();
            this.user.accessToken = "" + this.lUser.getPk();
            this.user.id = "" + this.lUser.getPk();
            this.user.username = this.lUser.getUsername();
            this.user.fullName = this.lUser.getFullName();
            this.user.profilPicture = this.lUser.getProfilePicUrl();
            return Long.valueOf(0);
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Long result) {
            try {
                if (this.progressDlg != null && this.progressDlg.isShowing()) {
                    this.progressDlg.dismiss();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (this.user != null) {
                Instagram.this.mSession.store(this.user);
                Instagram.this.mListener.onSuccess(this.user);
                return;
            }
            Instagram.this.mListener.onError("Failed to get access token");
        }
    }

    public interface InstagramAuthListener {
        void onCancel();

        void onError(String str);

        void onSuccess(InstagramUser instagramUser);
    }

    class C10331 implements InstagramDialogListener {
        C10331() {
        }

        public void onSuccess(String code) {
            Instagram.this.retreiveAccessToken(code);
        }

        public void onSuccess(LoggedInUser user) {
            Instagram.this.retreiveAccessToken(user);
        }

        public void onError(String error) {
            Instagram.this.mListener.onError(error);
        }

        public void onCancel() {
            Instagram.this.mListener.onCancel();
        }
    }

    public Instagram(Context context, Activity act, String clientId, String clientSecret, String redirectUri) {
        this.mContext = context;
        this.mActivity = act;
        this.mClientId = clientId;
        this.mClientSecret = clientSecret;
        this.mRedirectUri = redirectUri;
        String authUrl = "https://api.instagram.com/oauth/authorize/?client_id=" + this.mClientId + "&redirect_uri=" + this.mRedirectUri + "&response_type=code";
        this.mSession = new InstagramSession(context);
        this.mDialog = new InstagramDialog(context, this.mActivity, authUrl, redirectUri, new C10331());
    }

    public void authorize(InstagramAuthListener listener) {
        this.mListener = listener;
        try {
            this.mDialog.show();
        } catch (Exception e) {
        }
    }

    public void resetSession() {
        this.mSession.reset();
        this.mDialog.clearCache();
    }

    public InstagramSession getSession() {
        return this.mSession;
    }

    private void retreiveAccessToken(String code) {
        new AccessTokenTask(code).execute(new URL[0]);
    }

    private void retreiveAccessToken(LoggedInUser user) {
        new AccessTokenTask(user).execute(new URL[0]);
    }
}
