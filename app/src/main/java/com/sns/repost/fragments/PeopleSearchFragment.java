package com.sns.repost.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.sns.repost.R;
import com.sns.repost.adapters.SearchPeopleAdapter;
import com.sns.repost.helpers.callback.SimpleCallback;
import com.sns.repost.helpers.events.SearchEvent;
import com.sns.repost.loader.UserLoader;
import com.sns.repost.models.User;
import com.sns.repost.utils.StringUtils;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by nguyenvanhien on 7/10/17.
 */

public class PeopleSearchFragment extends BaseFragment {
    private Context mContext;
    private Activity mAct;
    @Bind(R.id.rcvResult)
    RecyclerView rcvResult;

    private String searchWord = "";
    private LinearLayoutManager layoutManager;
    private SearchPeopleAdapter searchPeopleAdapter;
    private ArrayList<User> users = new ArrayList<>();
    private UserLoader userLoader;
    public PeopleSearchFragment(){}

    public static PeopleSearchFragment newInstance() {
        PeopleSearchFragment fragment = new PeopleSearchFragment();
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_child, container, false);
        ButterKnife.bind(this, view);
        mContext = view.getContext();
        mAct = getActivity();
        userLoader = UserLoader.getInstance();
        initView();
        if(searchPeopleAdapter == null) {
            searchData(searchWord);
        }else {
            rcvResult.setAdapter(searchPeopleAdapter);
        }
        return view;
    }
    private void initView() {
        layoutManager = new LinearLayoutManager(mContext);
        rcvResult.setHasFixedSize(false);
        rcvResult.setLayoutManager(layoutManager);

    }

    public void searchData(String searchWord) {
        if(StringUtils.isNotEmpty(searchWord)) {
            userLoader.searchUser(searchWord, new SimpleCallback() {
                @Override
                public void success(Object... params) {
                    if (params[0] != null) {
                        users = (ArrayList<User>) params[0];
                        setAdapterResult(users);
                    }
                }

                @Override
                public void failed() {

                }
            });
        }
    }

    private void setAdapterResult(ArrayList<User> users) {
        if(users!=null && users.size()>0){
            searchPeopleAdapter = new SearchPeopleAdapter(users);
            rcvResult.setAdapter(searchPeopleAdapter);
        }
    }

    @Subscribe
    public void updateResult(SearchEvent event){
        if(event!=null && StringUtils.isNotEmpty(event.getKeyWord())){
            searchData(event.getKeyWord());
        }
    }
}
