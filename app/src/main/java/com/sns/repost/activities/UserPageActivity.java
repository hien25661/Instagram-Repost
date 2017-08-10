package com.sns.repost.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.sns.repost.BaseActivity;
import com.sns.repost.R;
import com.sns.repost.adapters.MediaAdapter;
import com.sns.repost.helpers.callback.SimpleCallback;
import com.sns.repost.helpers.callback.SuccessfullCallback;
import com.sns.repost.loader.FollowLoader;
import com.sns.repost.loader.UserLoader;
import com.sns.repost.models.Media;
import com.sns.repost.models.User;
import com.sns.repost.utils.Consts;
import com.sns.repost.utils.StringUtils;
import com.sns.repost.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nguyenvanhien on 8/10/17.
 */

public class UserPageActivity extends BaseActivity {
    private User user;
    private UserLoader userLoader;
    private String user_id;
    @Bind(R.id.imvAvatar)
    ImageView imvAvatar;
    @Bind(R.id.tvUserName)
    TextView tvUserName;
    @Bind(R.id.tvNumbMedia)
    TextView tvNumbMedia;
    @Bind(R.id.tvNumbFollower)
    TextView tvNumbFollower;
    @Bind(R.id.tvNumbFollow)
    TextView tvNumbFollow;
    @Bind(R.id.rcvMedia)
    UltimateRecyclerView rcvMedia;
    @Bind(R.id.tvTitle)
    TextView tvTitle;

    private MediaAdapter mediaAdapter;
    private FollowLoader followLoader;
    private ArrayList<Media> mediaList = new java.util.ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userpage);
        ButterKnife.bind(this);
        userLoader = UserLoader.getInstance();
        user_id = getIntent().getStringExtra(Consts.USER_ID);
        followLoader = FollowLoader.getInstance();
        initView();
        loadData();
    }

    private void initView() {
        rcvMedia.setHasFixedSize(false);
        rcvMedia.setLayoutManager(new GridLayoutManager(this,3));
        setupLoadMoreMediaList();
    }

    private void loadData() {
        if(StringUtils.isEmpty(user_id)) return;
        loadUserMedia();
        userLoader.loadUser(user_id, new SimpleCallback() {
            @Override
            public void success(Object... params) {
                user = (User) params[0];
                bindDataUser();
            }

            @Override
            public void failed() {

            }
        });
    }

    private void bindDataUser(){
        tvNumbMedia.setText(user.getCounts().getMedia() + " MEDIA");
        tvNumbFollower.setText(user.getCounts().getFollowedBy() + " FOLLOWERS");
        tvNumbFollow.setText(user.getCounts().getFollows() + " FOLLOWS");
        Utils.showImage(UserPageActivity.this,user.getProfilePicture(),imvAvatar);
        tvUserName.setText(user.getUsername());
        tvTitle.setText(user.getUsername());
    }

    private void loadUserMedia(){
        rcvMedia.setRefreshing(true);
        followLoader.getMediaFollowUser(user_id, new SimpleCallback() {
            @Override
            public void success(Object... params) {
                mediaList = (ArrayList<Media>) params[0];
                mediaAdapter = new MediaAdapter(mediaList);
                mediaAdapter.setmAct(UserPageActivity.this);
                mediaAdapter.setUserFeed(true);
                mediaAdapter.setNumColumn(3);
                rcvMedia.setAdapter(mediaAdapter);
                rcvMedia.setRefreshing(false);
            }

            @Override
            public void failed() {

            }
        });
    }

    private void setupLoadMoreMediaList() {
        rcvMedia.reenableLoadmore();
        rcvMedia.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                if (StringUtils.isNotEmpty(followLoader.getNextUrl())) {
                    followLoader.loadMoreUserFeed(followLoader.getNextUrl(), new SuccessfullCallback() {
                        @Override
                        public void success(Object... params) {
                            List<Media> mListMore = (List<Media>) params[0];
                            if (mListMore != null && mListMore.size() > 0) {
                                if (mListMore != null && mListMore.size() > 0) {
                                    for (Media media : mListMore) {
                                        if (mediaAdapter.getMediaList() != null && !Utils.checkMediaContain(mediaAdapter.getMediaList(), media)) {
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

    @OnClick(R.id.imvBack)
    public void onBack(){
        onBackPressed();
    }
}
