package com.sns.repost.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.sns.repost.BaseActivity;
import com.sns.repost.R;
import com.sns.repost.RepostApplication;
import com.sns.repost.helpers.customview.LayoutTutorialNew;
import com.sns.repost.helpers.events.FinishTutEvent;
import com.sns.repost.utils.AppSettings;
import com.sns.repost.utils.Utils;


import net.londatiga.android.instagram.Instagram;
import net.londatiga.android.instagram.InstagramSession;
import net.londatiga.android.instagram.InstagramUser;

import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nguyenvanhien on 8/12/17.
 */

public class RepostLoginActivity extends BaseActivity {
    private InstagramSession mInstagramSession;
    private AppSettings appSettings;
    @Bind(R.id.viewTutorial)
    FrameLayout viewTutorial;
    LayoutTutorialNew layoutTutorial;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relogin);
        ButterKnife.bind(this);
        appSettings = RepostApplication.getInstance().getAppSettings();
        this.mInstagramSession = Utils.getInstagramSession(this);
        if(!appSettings.getShowTut()){
            showTutorialView(true);
            appSettings.setShowTut(true);
        }
    }

    private Instagram.InstagramAuthListener mAuthListener = new loginListener();

    class loginListener implements Instagram.InstagramAuthListener {
        loginListener() {
        }

        public void onSuccess(InstagramUser user) {
            //MainActivity.this.startActivity(new Intent(MainActivity.this, MainActivity.class));
            RepostApplication.getInstance().getAppSettings().setInstagramAccessToken(
                    mInstagramSession.getAccessToken());
            Intent t = new Intent(RepostLoginActivity.this,MainActivity.class);
            t.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(t);
            finish();
        }

        public void onError(String error) {
        }

        public void onCancel() {
            Utils.showToast(RepostLoginActivity.this, "May be later");
        }

        @Override
        public void onCookie(String cookie) {
            Log.e("loadPopular","cookie: "+cookie);
            RepostApplication.getInstance().getAppSettings().setInstagramCookie(cookie);
        }
    }

    @OnClick(R.id.btnLogin)
    public void login() {
        Utils.getInstagram(RepostLoginActivity.this, true).authorize(RepostLoginActivity.this.mAuthListener);
    }

    private void showTutorialView(boolean isShow) {
        if (isShow) {
            viewTutorial.removeAllViews();
            layoutTutorial = new LayoutTutorialNew(this);
            viewTutorial.addView(layoutTutorial, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            viewTutorial.setVisibility(View.VISIBLE);
            viewTutorial.setAlpha(1);
            layoutTutorial.setMainTut();

        } else {
            viewTutorial.removeAllViews();
            viewTutorial.setVisibility(View.GONE);
        }

    }

    @Subscribe
    public void finishTut(FinishTutEvent event) {
        if (!event.isFromUser()) {
            viewTutorial.setVisibility(View.GONE);
            viewTutorial.removeAllViews();
            appSettings.setShowTut(true);
        }
    }
}
