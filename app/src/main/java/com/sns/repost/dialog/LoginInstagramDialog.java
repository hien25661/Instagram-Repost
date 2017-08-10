package com.sns.repost.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import com.sns.repost.BuildConfig;
import com.sns.repost.R;
import com.sns.repost.RepostApplication;
import com.sns.repost.api.InstagramService;
import com.sns.repost.helpers.callback.SuccessfullCallback;
import com.sns.repost.loader.MediaLoader;
import com.sns.repost.loader.UserLoader;
import com.sns.repost.services.response.InstagramGetTokenResponse;
import com.sns.repost.utils.AppSettings;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Hien on 5/7/2017.
 */

public class LoginInstagramDialog extends Dialog {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.webView)
    WebView mWebView;
    private String mUrl = "https://api.instagram.com/oauth/authorize/?client_id=" + BuildConfig.CONSUMER_KEY + "&redirect_uri=" + BuildConfig.CALLBACK_URL + "&response_type=code&display=touch&scope=" + BuildConfig.SCOPE;
    private String mUrlLogout = "https://instagram.com/accounts/logout";
    private static final String TAG = LoginInstagramDialog.class.getSimpleName();
    private UserLoader userLoader;
    private MediaLoader mediaLoader;
    private AppSettings appSettings;
    private boolean requestLogout = false;
    private Context mContext;
    private SuccessfullCallback loginCallBack;

    public LoginInstagramDialog(@NonNull Context context) {
        super(context, R.style.full_screen_dialog);
        this.mContext = context;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_instagram);
        ButterKnife.bind(this);
        userLoader = UserLoader.getInstance();
        mediaLoader = MediaLoader.getInstance();
        appSettings = RepostApplication.getInstance().getAppSettings();
        setUpWebView();
        setCancelable(false);
        setCanceledOnTouchOutside(false);

    }

    private void setUpWebView() {
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setWebViewClient(new OAuthWebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        Log.e("URL: ", "" + mUrl);
        mWebView.loadUrl(mUrl);

        CookieSyncManager.createInstance(mContext);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
    }

    private class OAuthWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG, "Redirecting URL " + url);

            if (url.startsWith(BuildConfig.CALLBACK_URL)) {
                String urls[] = url.split("=");
                //mListener.onCodeReceived(urls[1]);
                if (urls != null && urls.length >= 2) {
                    InstagramService.createServiceGetInstagramToken().getInstagramAccessToken(BuildConfig.CONSUMER_KEY, BuildConfig.CONSUMER_SECRET,
                            BuildConfig.CALLBACK_URL, "authorization_code", urls[1]).enqueue(new Callback<InstagramGetTokenResponse>() {
                        @Override
                        public void onResponse(Call<InstagramGetTokenResponse> call, Response<InstagramGetTokenResponse> response) {
                            if (response.body() != null && response.body() instanceof InstagramGetTokenResponse) {
                                InstagramGetTokenResponse instagramGetTokenResponse = response.body();
                                appSettings.setInstagramAccessToken(instagramGetTokenResponse.getAccess_token());
                                userLoader.setUser(instagramGetTokenResponse.getUser());
                                if (loginCallBack != null) {
                                    loginCallBack.success();
                                    dismiss();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<InstagramGetTokenResponse> call, Throwable t) {
                            Log.e("VOA", "Failed");
                        }
                    });
                }
                return true;
            }


            return false;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            Log.d(TAG, "Page error: " + description);

            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.d(TAG, "Loading URL: " + url);

            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            String title = mWebView.getTitle();
            Log.d(TAG, "onPageFinished URL: " + url);
        }

    }

    public void setLoginCallBack(SuccessfullCallback loginCallBack) {
        this.loginCallBack = loginCallBack;
    }
}
