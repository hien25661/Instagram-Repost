package com.sns.repost.helpers.customview;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;


import com.ramotion.paperonboarding.PaperOnboardingEngine;
import com.ramotion.paperonboarding.PaperOnboardingPage;
import com.ramotion.paperonboarding.listeners.PaperOnboardingOnChangeListener;
import com.ramotion.paperonboarding.listeners.PaperOnboardingOnRightOutListener;
import com.sns.repost.R;
import com.sns.repost.helpers.EventBusUtils;
import com.sns.repost.helpers.events.FinishTutEvent;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by hien.nv on 5/8/17.
 */

public class LayoutTutorialNew extends RelativeLayout {

    Context mContext;

    public LayoutTutorialNew(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public LayoutTutorialNew(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public LayoutTutorialNew(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        View v = inflate(getContext(), R.layout.onboarding_main_layout, this);
        ButterKnife.bind(this, v);
    }


    private String getString(int string_id) {
        return this.getResources().getString(string_id);
    }

    public void setMainTut(){
        PaperOnboardingEngine engine = new PaperOnboardingEngine(findViewById(R.id.onboardingRootView), getDataForOnboarding(), mContext);
        engine.isShowIconFirst = true;
        engine.setOnChangeListener(new PaperOnboardingOnChangeListener() {
            @Override
            public void onPageChanged(int oldElementIndex, int newElementIndex) {
                if(newElementIndex == 6){
                    EventBusUtils.getDefault().post(new FinishTutEvent(false));
                }
            }
        });

        engine.setOnRightOutListener(new PaperOnboardingOnRightOutListener() {
            @Override
            public void onRightOut() {
                // Probably here will be your exit action
                EventBusUtils.getDefault().post(new FinishTutEvent(false));
            }
        });
    }

    // Just example data for Onboarding
    private ArrayList<PaperOnboardingPage> getDataForOnboarding() {
        // prepare data
        PaperOnboardingPage scr1 = new PaperOnboardingPage("Login Your Account",
               "",
                Color.parseColor("#dcd20d10"),
                R.mipmap.h7,
                R.drawable.onboarding_pager_circle_icon);



        PaperOnboardingPage scr2 = new PaperOnboardingPage("Get Feed Timeline",
                "",
                Color.parseColor("#dcd20d10"),
                R.mipmap.h1,
                R.drawable.onboarding_pager_circle_icon);

        PaperOnboardingPage scr3 = new PaperOnboardingPage("Show Your Profile",
                "",
                Color.parseColor("#dcd20d10"),
                R.mipmap.h4,
                R.drawable.onboarding_pager_circle_icon);
        PaperOnboardingPage scr3_1 = new PaperOnboardingPage("Search Information\nUser and HashTag",
                "",
                Color.parseColor("#dcd20d10"),
                R.mipmap.h3,
                R.drawable.onboarding_pager_circle_icon);
        PaperOnboardingPage scr4 = new PaperOnboardingPage("Your Media Like List",
                "",
                Color.parseColor("#dcd20d10"),
                R.mipmap.h5,
                R.drawable.onboarding_pager_circle_icon);

        PaperOnboardingPage scr5 = new PaperOnboardingPage("Save, Share and\nRe-post Picture and Video",
                "",
                Color.parseColor("#dcd20d10"),
                R.mipmap.h6,
                R.drawable.onboarding_pager_circle_icon);

        PaperOnboardingPage scr6 = new PaperOnboardingPage("",
                "",
                Color.parseColor("#dcd20d10"),
                0,
                0);

        ArrayList<PaperOnboardingPage> elements = new ArrayList<>();
        elements.add(scr1);
        elements.add(scr2);
        elements.add(scr3);
        elements.add(scr3_1);
        elements.add(scr4);
        elements.add(scr5);
        elements.add(scr6);
        return elements;
    }

}
