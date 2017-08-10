package com.sns.repost.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;

import com.optimus.edittextfield.EditTextField;
import com.sns.repost.BaseActivity;
import com.sns.repost.R;
import com.sns.repost.adapters.SearchPagerAdapter;
import com.sns.repost.fragments.PeopleSearchFragment;
import com.sns.repost.fragments.TagSearchFragment;
import com.sns.repost.helpers.EventBusUtils;
import com.sns.repost.helpers.events.SearchEvent;
import com.sns.repost.utils.StringUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nguyenvanhien on 8/10/17.
 */

public class SearchActivity extends BaseActivity {
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.viewpager)
    ViewPager viewpager;
    @Bind(R.id.edtSearch)
    EditTextField edtSearch;

    private SearchPagerAdapter searchPagerAdapter;
    ArrayList<Fragment> listFragment = new ArrayList<>();
    ArrayList<String> listFragmentTitle = new ArrayList<>();
    private String keyWord = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initView();
        loadDataFragment();
    }

    private void initView() {
        searchEvent();
    }

    private void loadDataFragment() {
        if(searchPagerAdapter == null) {
            listFragment.add(PeopleSearchFragment.newInstance());
            listFragment.add(TagSearchFragment.newInstance());
            listFragmentTitle.add("USERS");
            listFragmentTitle.add("HASHTAGS");

            searchPagerAdapter = new SearchPagerAdapter(getSupportFragmentManager(), listFragment, listFragmentTitle);
            viewpager.setAdapter(searchPagerAdapter);
            viewpager.setOffscreenPageLimit(1);
            tabLayout.setupWithViewPager(viewpager);
        }else {
            viewpager.setAdapter(searchPagerAdapter);
            viewpager.setOffscreenPageLimit(1);
            tabLayout.setupWithViewPager(viewpager);
        }
    }

    private void searchEvent(){
        if(StringUtils.isNotEmpty(edtSearch.getText().toString())){
            EventBusUtils.getDefault().post(new SearchEvent(edtSearch.getText().toString()));
        }
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(StringUtils.isNotEmpty(s.toString())){
                    EventBusUtils.getDefault().post(new SearchEvent(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    @OnClick(R.id.imvBack)
    public void onBack(){
        onBackPressed();
    }
}
