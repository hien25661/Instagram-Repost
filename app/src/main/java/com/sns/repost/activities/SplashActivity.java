package com.sns.repost.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.sns.repost.BaseActivity;
import com.sns.repost.R;

/**
 * Created by nguyenvanhien on 8/12/17.
 */

public class SplashActivity extends BaseActivity {
    private static int SPLASH_TIME_OUT = 3500;
    InterstitialAd interstitial;
    AdRequest adRequest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        adRequest = new AdRequest.Builder().build();

        interstitial = new InterstitialAd(this);
        // Insert the Ad Unit ID
        interstitial.setAdUnitId(getResources().getString(R.string.ads_id_interstis));
        interstitial.loadAd(adRequest);
        interstitial.setAdListener(new AdListener() {
            // Listen for when user closes ad
            public void onAdClosed() {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                // close this activity
                finish();
            }
        });

        new Handler().postDelayed(new Runnable() {

			/*
             * Showing splash screen with a timer. This will be useful when you
			 * want to show case your app logo / company
			 */

            @Override
            public void run() {
                // Load ads into Interstitial Ads
                if (interstitial.isLoaded()){
                    interstitial.show();
                }else {
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(i);
                    // close this activity
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }
}
