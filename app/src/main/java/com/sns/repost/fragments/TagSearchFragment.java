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
import com.sns.repost.adapters.SearchTagAdapter;
import com.sns.repost.helpers.callback.SimpleCallback;
import com.sns.repost.helpers.events.SearchEvent;
import com.sns.repost.loader.TagsLoader;
import com.sns.repost.models.Tag;
import com.sns.repost.utils.StringUtils;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by nguyenvanhien on 7/10/17.
 */

public class TagSearchFragment extends BaseFragment {
    @Bind(R.id.rcvResult)
    RecyclerView rcvResult;

    private Context mContext;
    private Activity mAct;
    private TagsLoader tagsLoader;
    private String searchWord = "";
    private LinearLayoutManager layoutManager;
    private SearchTagAdapter searchTagAdapter;
    private ArrayList<Tag> tags = new ArrayList<>();
    public TagSearchFragment(){}

    public static TagSearchFragment newInstance() {
        TagSearchFragment fragment = new TagSearchFragment();
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_child, container, false);
        ButterKnife.bind(this, view);
        mContext = view.getContext();
        mAct = getActivity();
        tagsLoader = TagsLoader.getInstance();
        initView();
        if(searchTagAdapter == null) {
            searchData(searchWord);
        }else {
            rcvResult.setAdapter(searchTagAdapter);
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
            tagsLoader.searchTag(searchWord, new SimpleCallback() {
                @Override
                public void success(Object... params) {
                    if (params[0] != null) {
                        tags = (ArrayList<Tag>) params[0];
                        setAdapterResult(tags);
                    }
                }

                @Override
                public void failed() {

                }
            });
        }
    }

    private void setAdapterResult(ArrayList<Tag> tags) {
        if(tags!=null && tags.size()>0){
            searchTagAdapter = new SearchTagAdapter(tags);
            rcvResult.setAdapter(searchTagAdapter);
        }
    }

    @Subscribe
    public void updateResult(SearchEvent event){
        if(event!=null && StringUtils.isNotEmpty(event.getKeyWord())){
            searchData(event.getKeyWord());
        }
    }
}
