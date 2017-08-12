package com.sns.repost.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sns.repost.BaseActivity;
import com.sns.repost.R;
import com.sns.repost.RepostApplication;
import com.sns.repost.helpers.callback.SimpleCallback;
import com.sns.repost.models.User;
import com.sns.repost.utils.Utils;

import net.londatiga.android.instagram.Instagram;
import net.londatiga.android.instagram.InstagramSession;
import net.londatiga.android.instagram.InstagramUser;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nguyenvanhien on 8/12/17.
 */

public class RepostLoginActivity extends BaseActivity {
    private InstagramSession mInstagramSession;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relogin);
        ButterKnife.bind(this);
        this.mInstagramSession = Utils.getInstagramSession(this);
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
    }

    @OnClick(R.id.btnLogin)
    public void login() {
        Utils.getInstagram(RepostLoginActivity.this, true).authorize(RepostLoginActivity.this.mAuthListener);
    }
}
