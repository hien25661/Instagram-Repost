package com.sns.repost.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.sns.repost.BaseActivity;
import com.sns.repost.R;
import com.sns.repost.adapters.MediaAdapter;
import com.sns.repost.helpers.callback.SimpleCallback;
import com.sns.repost.helpers.callback.SuccessfullCallback;
import com.sns.repost.loader.MediaLoader;
import com.sns.repost.loader.TagsLoader;
import com.sns.repost.models.Media;
import com.sns.repost.utils.Consts;
import com.sns.repost.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nguyenvanhien on 8/10/17.
 */

public class TagFeedActivity extends BaseActivity {
    @Bind(R.id.rcv_feed)
    UltimateRecyclerView rcvMedia;
    @Bind(R.id.tvTitle)
    TextView tvTitle;
    private MediaAdapter mediaAdapter;
    private MediaLoader mediaLoader;
    private TagsLoader tagsLoader;
    private String tagName;
    private ArrayList<Media> mediaList = new ArrayList<>();

    @Bind(R.id.adView)
    AdView adView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mylike);
        ButterKnife.bind(this);
        mediaLoader = MediaLoader.getInstance();
        tagsLoader = TagsLoader.getInstance();
        tagName = getIntent().getStringExtra(Consts.PARAM_TAG_NAME);
        tvTitle.setText("#"+tagName);
        loadAd();
        initView();
    }
    private void loadAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }
    private void initView() {
        rcvMedia.setHasFixedSize(false);
        rcvMedia.setLayoutManager(new GridLayoutManager(this, 2));
        refreshMediaList();
        pullToRefreshMediaList();
        setupLoadMoreMediaList();
    }

    private void pullToRefreshMediaList() {
        rcvMedia.mSwipeRefreshLayout.setEnabled(true);
        rcvMedia.setDefaultOnRefreshListener(listener);
    }

    SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            refreshMediaList();
        }
    };

    private void refreshMediaList() {
        rcvMedia.setRefreshing(true);
        tagsLoader.loadMediaFromTag(tagName, new SimpleCallback() {
            @Override
            public void success(Object... params) {
                mediaList = (ArrayList<Media>) params[0];
                mediaAdapter = new MediaAdapter(mediaList);
                mediaAdapter.setmAct(TagFeedActivity.this);
                mediaAdapter.setUserFeed(true);
                mediaAdapter.setNumColumn(2);

                rcvMedia.setAdapter(mediaAdapter);
                rcvMedia.setRefreshing(false);
            }

            @Override
            public void failed() {
                rcvMedia.setRefreshing(false);
            }
        });
    }

    private void setupLoadMoreMediaList() {
        rcvMedia.reenableLoadmore();
        rcvMedia.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                if (StringUtils.isNotEmpty(tagsLoader.getNextUrl())) {
                    tagsLoader.loadMoreTagMedia(tagsLoader.getNextUrl(), new SuccessfullCallback() {
                        @Override
                        public void success(Object... params) {
                            List<Media> mListMore = (List<Media>) params[0];
                            if (mListMore != null && mListMore.size() > 0) {
                                if (mListMore != null && mListMore.size() > 0) {
                                    for (Media media : mListMore) {
                                        if (mediaAdapter.getMediaList() != null && !checkMediaContain(mediaAdapter.getMediaList(), media)) {
                                            mediaAdapter.insert(media, mediaAdapter.getAdapterItemCount());
                                        }
                                    }
                                } else {
                                }
                            } else {
                            }
                        }
                    });
                } else {
                    rcvMedia.disableLoadmore();
                }
            }
        });
    }

    private boolean checkMediaContain(ArrayList<Media> mList, Media item) {
        if (mList == null || mList.size() == 0) return false;
        for (Media media : mList) {
            if (media.getId().equals(item.getId())) {
                return true;
            }
        }
        return false;
    }
    @OnClick(R.id.imvBack)
    public void onBack(){
        onBackPressed();
    }
}
