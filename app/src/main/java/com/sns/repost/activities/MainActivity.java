package com.sns.repost.activities;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.sns.repost.BaseActivity;
import com.sns.repost.BuildConfig;
import com.sns.repost.R;
import com.sns.repost.RepostApplication;
import com.sns.repost.adapters.MediaAdapter;
import com.sns.repost.dialog.LoginInstagramDialog;
import com.sns.repost.helpers.callback.SimpleCallback;
import com.sns.repost.helpers.callback.SuccessfullCallback;
import com.sns.repost.loader.MediaLoader;
import com.sns.repost.loader.UserLoader;
import com.sns.repost.models.Media;
import com.sns.repost.models.User;
import com.sns.repost.utils.AppSettings;
import com.sns.repost.utils.Consts;
import com.sns.repost.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    @Bind(R.id.rcv_feed)
    UltimateRecyclerView rcvMedia;
    private MediaAdapter mediaAdapter;
    private UserLoader userLoader;
    private MediaLoader mediaLoader;
    private AppSettings appSettings;
    private ArrayList<Media> mediaList = new ArrayList<>();
    private LoginInstagramDialog loginInstagramDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        userLoader = UserLoader.getInstance();
        mediaLoader = MediaLoader.getInstance();
        appSettings = RepostApplication.getInstance().getAppSettings();
        initView();
        loadData();
    }

    private void loadData() {
        if (appSettings.getInstagramAccessToken().equals("") || appSettings.getInstagramAccessToken()
                .equals(BuildConfig.DEFAULT_TOKEN)) {
            loginInstagramDialog.show();
            loginInstagramDialog.setLoginCallBack(new SuccessfullCallback() {
                @Override
                public void success(Object... params) {
                    if (userLoader.getUser() == null) {
                        userLoader.loadSelf(new SimpleCallback() {
                            @Override
                            public void success(Object... params) {
                                User user = (User) params[0];
                                if (user != null) {
                                    userLoader.setUser((User) params[0]);
                                }
                            }

                            @Override
                            public void failed() {

                            }
                        });
                    }
                    refreshMediaList();
                }
            });
        }else {
            if (userLoader.getUser() == null) {
                userLoader.loadSelf(new SimpleCallback() {
                    @Override
                    public void success(Object... params) {
                        User user = (User) params[0];
                        if (user != null) {
                            userLoader.setUser((User) params[0]);
                        }
                    }

                    @Override
                    public void failed() {

                    }
                });
            }
            refreshMediaList();
        }
    }

    private void initView() {
        rcvMedia.setHasFixedSize(false);
        rcvMedia.setLayoutManager(new LinearLayoutManager(this));
        pullToRefreshMediaList();
        setupLoadMoreMediaList();
        loginInstagramDialog = new LoginInstagramDialog(this);
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
        mediaLoader.loadLiked(new SuccessfullCallback() {
            @Override
            public void success(Object... params) {
                mediaList = (ArrayList<Media>) params[0];
                mediaAdapter = new MediaAdapter(mediaList);
                mediaAdapter.setmAct(MainActivity.this);
                rcvMedia.setAdapter(mediaAdapter);
                rcvMedia.setRefreshing(false);
            }
        });
    }

    private void setupLoadMoreMediaList() {
        rcvMedia.reenableLoadmore();
        rcvMedia.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                if (StringUtils.isNotEmpty(mediaLoader.getNextUrl())) {
                    mediaLoader.loadMoreLike(mediaLoader.getNextUrl(), new SuccessfullCallback() {
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

    @OnClick(R.id.imvUserPage)
    public void showUserSelfPage(){
        Intent t = new Intent(this,UserPageActivity.class);
        if(userLoader.getUser()!=null) {
            t.putExtra(Consts.USER_ID, userLoader.getUser().getId());
            startActivity(t);
        }
    }

    @OnClick(R.id.imvSearch)
    public void showSearchPage(){
        Intent t = new Intent(this,SearchActivity.class);
        startActivity(t);
    }

    @OnClick(R.id.imvMyLike)
    public void showMyLike(){
        Intent t = new Intent(this,MyLikeActivity.class);
        startActivity(t);
    }

}
