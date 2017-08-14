package com.sns.repost.helpers.customview;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.google.gson.Gson;
import com.sns.repost.R;
import com.sns.repost.RepostApplication;
import com.sns.repost.utils.Constants;
import com.sns.repost.utils.SNSRepostManager;
import com.sns.repost.utils.LoggedInUser;
import com.sns.repost.utils.LoginUtil;
import com.sns.repost.utils.Utils;

import londatiga.android.instagram.InstagramDialog;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;

public class InstagramWebView extends WebViewClient {
    Activity mActivity;
    InstagramDialog mDialog;
    private InstagramDialog.InstagramDialogListener mListener;
    ProgressDialog mSpinner;

    class C03252 implements Runnable {
        C03252() {
        }

        public void run() {
            InstagramWebView.this.mListener.onError("bilal");
            Toast.makeText(InstagramWebView.this.mActivity, "Login Failed", 1).show();
        }
    }

    class C08261 implements Callback {

        class C03242 implements Runnable {
            C03242() {
            }

            public void run() {
            }
        }

        C08261() {
        }

        public void onFailure(Call paramAnonymousCall, IOException paramAnonymousIOException) {
            paramAnonymousIOException.printStackTrace();
        }

        public void onResponse(Call paramAnonymousCall, Response paramAnonymousResponse) throws IOException {
            String str = paramAnonymousResponse.body().string();
            Log.d("leileore", str);
            if (paramAnonymousResponse.isSuccessful()) {
                final LoginUtil localLoginUtil = (LoginUtil) new Gson().fromJson(str, LoginUtil.class);
                if (InstagramWebView.this.mActivity != null) {
                    InstagramWebView.this.mActivity.runOnUiThread(new Runnable() {
                        public void run() {
                            InstagramWebView.this.processLoginResponse(localLoginUtil);
                        }
                    });
                    return;
                }
                return;
            }
            InstagramWebView.this.mActivity.runOnUiThread(new C03242());
        }
    }

    public InstagramWebView(InstagramDialog dialog, Activity act, InstagramDialog.InstagramDialogListener listener, ProgressDialog spinner) {
        this.mActivity = act;
        this.mListener = listener;
        this.mDialog = dialog;
        this.mSpinner = spinner;
    }

    private void login(Activity act, String paramString1, String paramString2) {
        this.mActivity = act;
        String str1 = PreferenceManager.getDefaultSharedPreferences(act).getString(Constants.CONSTANT_UID, "");
        String str2 = "{\"_uuid\":\"" + str1 + "\",\"password\":\"" + paramString2 + "\",\"username\":\"" + paramString1 + "\",\"device_id\":\"" + str1 + "\",\"_csrftoken\":\"missing\",\"login_attempt_count\":\"" + 1 + "\"}";
        Request localRequest = Constants.requestreformer(new Builder().url
                ("https://i.instagram.com/api/v1/accounts/login/")
                .post(new FormBody.Builder().add("ig_sig_key_version", "4")
                        .add("signed_body", Constants.requestEncryptor(str2) + "." + str2).build()).header("User-Agent", Utils.userAgent).build());
        Log.d("urlurlurl", localRequest.toString());
        RepostApplication.getInstance().getHttpClient().newCall(localRequest).enqueue(new C08261());
    }

    private void processLoginResponse(LoginUtil paramLoginUtil) {
        if (paramLoginUtil.getStatus().equals("ok")) {
            SNSRepostManager localRapidRepostManager = new SNSRepostManager(this.mActivity.getApplication());
            LoggedInUser localLoggedInUser = paramLoginUtil.getLoggedInUser();
            localRapidRepostManager.storeAccessToken("loggedin", localLoggedInUser.getPk() + "", localLoggedInUser.getUsername(), localLoggedInUser.getFullName(), localLoggedInUser.getProfilePicUrl());
            this.mDialog.dismiss();
            return;
        }
        if (this.mSpinner != null && this.mSpinner.isShowing()) {
            this.mSpinner.dismiss();
        }
        this.mActivity.runOnUiThread(new C03252());
    }

    public void onPageFinished(WebView paramWebView, String paramString) {
        super.onPageFinished(paramWebView, paramString);
        if (this.mSpinner != null && this.mSpinner.isShowing()) {
            this.mSpinner.dismiss();
        }
    }

    public void onPageStarted(WebView paramWebView, String paramString, Bitmap paramBitmap) {
        super.onPageStarted(paramWebView, paramString, paramBitmap);
        if (this.mSpinner != null) {
            this.mSpinner.show();
        }
    }

    public void onReceivedError(WebView paramWebView, WebResourceRequest paramWebResourceRequest, WebResourceError paramWebResourceError) {
        super.onReceivedError(paramWebView, paramWebResourceRequest, paramWebResourceError);
    }

    public boolean shouldOverrideUrlLoading(WebView paramWebView, String paramString) {
        if (!paramString.startsWith("ios://onLogin")) {
            return false;
        }
        HashMap localHashMap = Constants.getParms(paramString);
        String str1 = "";
        String localObject = "";
        if (localHashMap.size() == 2) {
            try {
            } catch (Exception localUnsupportedEncodingException) {
                while (true) {
                    localUnsupportedEncodingException.printStackTrace();
                }
            }
        }
        if (localHashMap.containsKey("username") && localHashMap.containsKey("password")) {
            try {
                str1 = URLDecoder.decode((String) localHashMap.get("username"), "UTF-8");
                localObject = URLDecoder.decode((String) localHashMap.get("password"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {
            Utils.showToast(this.mActivity, this.mActivity.getString(R.string.error));
        }
        if (!(TextUtils.isEmpty(str1) || TextUtils.isEmpty(localObject))) {
            if (this.mSpinner != null) {
                this.mSpinner.show();
            }
            login(this.mActivity, str1, localObject);
        }
        return true;
    }
}
