package com.sns.repost.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.sns.repost.BaseActivity;
import com.sns.repost.BuildConfig;
import com.sns.repost.R;
import com.sns.repost.RepostApplication;
import com.sns.repost.adapters.MediaAdapter;
import com.sns.repost.api.DownloadFeed;
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
import com.sns.repost.utils.Util;
import com.sns.repost.utils.Utils;

import net.londatiga.android.instagram.Instagram;
import net.londatiga.android.instagram.InstagramSession;
import net.londatiga.android.instagram.InstagramUser;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class MainActivity extends BaseActivity {
    @Bind(R.id.rcv_feed)
    UltimateRecyclerView rcvMedia;
    private MediaAdapter mediaAdapter;
    private UserLoader userLoader;
    private MediaLoader mediaLoader;
    private AppSettings appSettings;
    private ArrayList<Media> mediaList = new ArrayList<>();
    private LoginInstagramDialog loginInstagramDialog;
    private InstagramSession mInstagramSession;
    private static String[] PERMISSIONS_STORAGE = new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};

    @Bind(R.id.adView)
    AdView adView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        userLoader = UserLoader.getInstance();
        mediaLoader = MediaLoader.getInstance();
        appSettings = RepostApplication.getInstance().getAppSettings();
        loadAd();
        initView();
        loadData();
    }

    private void loadAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    public static void verifyStoragePermissions(Activity activity) {
        int writePermission = ContextCompat.checkSelfPermission(activity, "android.permission.WRITE_EXTERNAL_STORAGE");
        int readPermission = ContextCompat.checkSelfPermission(activity, "android.permission.READ_EXTERNAL_STORAGE");
        if (writePermission != 0 || readPermission != 0) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, 1);
        }
    }


    private Instagram.InstagramAuthListener mAuthListener = new loginListener();

    class loginListener implements Instagram.InstagramAuthListener {
        loginListener() {
        }

        public void onSuccess(InstagramUser user) {

            //MainActivity.this.startActivity(new Intent(MainActivity.this, MainActivity.class));
            RepostApplication.getInstance().getAppSettings().setInstagramAccessToken(mInstagramSession.getAccessToken());
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

        public void onError(String error) {
        }

        public void onCancel() {
            Utils.showToast(MainActivity.this, "May be later");
        }

        @Override
        public void onCookie(String cookie) {

        }
    }

    private void loadData() {
        this.mInstagramSession = Utils.getInstagramSession(this);

        if (appSettings.getInstagramAccessToken().equals("") || appSettings.getInstagramAccessToken()
                .equals(BuildConfig.DEFAULT_TOKEN)) {
            //Utils.getInstagram(MainActivity.this, true).authorize(MainActivity.this.mAuthListener);
            Intent t = new Intent(MainActivity.this, RepostLoginActivity.class);
            startActivity(t);
            finish();
            /*loginInstagramDialog.show();
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
            });*/
        } else {
            if (Build.VERSION.SDK_INT > 22) {
                verifyStoragePermissions(this);
            }
            //if (userLoader.getUser() == null) {
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
           // }
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
            refreshMediaListRefresh();
        }
    };

    private void refreshMediaList() {

        rcvMedia.setRefreshing(true);
        Utils.currentActivity = MainActivity.this;
        mediaLoader.loadPopularNew(new SuccessfullCallback() {
            @Override
            public void success(Object... params) {
                mediaList = (ArrayList<Media>) params[0];
                mediaAdapter = new MediaAdapter(mediaList);
                mediaAdapter.setmAct(MainActivity.this);
                rcvMedia.setAdapter(mediaAdapter);
                rcvMedia.setRefreshing(false);
               /* Handler mainHandler = new Handler(Looper.getMainLooper());
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // code to interact with UI

                    }
                });*/

            }
        });
    }
    private void refreshMediaListRefresh() {

        rcvMedia.setRefreshing(true);
        Utils.currentActivity = MainActivity.this;
        mediaLoader.loadPopularNewRefresh(new SuccessfullCallback() {
            @Override
            public void success(Object... params) {
                mediaList = (ArrayList<Media>) params[0];
                mediaAdapter = new MediaAdapter(mediaList);
                mediaAdapter.setmAct(MainActivity.this);
                rcvMedia.setAdapter(mediaAdapter);
                rcvMedia.setRefreshing(false);
               /* Handler mainHandler = new Handler(Looper.getMainLooper());
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // code to interact with UI

                    }
                });*/

            }
        });
    }

    private void setupLoadMoreMediaList() {
        rcvMedia.reenableLoadmore();
        rcvMedia.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
//                if (StringUtils.isNotEmpty(mediaLoader.getNextUrl())) {
//                    mediaLoader.loadMoreLike(mediaLoader.getNextUrl(), new SuccessfullCallback() {
//                        @Override
//                        public void success(Object... params) {
//                            List<Media> mListMore = (List<Media>) params[0];
//                            if (mListMore != null && mListMore.size() > 0) {
//                                if (mListMore != null && mListMore.size() > 0) {
//                                    for (Media media : mListMore) {
//                                        if (mediaAdapter.getMediaList() != null && !checkMediaContain(mediaAdapter.getMediaList(), media)) {
//                                            mediaAdapter.insert(media, mediaAdapter.getAdapterItemCount());
//                                        }
//                                    }
//                                } else {
//                                }
//                            } else {
//                            }
//                        }
//                    });
//                } else {
//                    rcvMedia.disableLoadmore();
//                }
                if (StringUtils.isNotEmpty(mediaLoader.getmMinMediaId())) {
                    mediaLoader.loadPopularNew(new SuccessfullCallback() {
                        @Override
                        public void success(Object... params) {
                            final List<Media> mListMore = (List<Media>) params[0];
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
                            /*Handler mainHandler = new Handler(Looper.getMainLooper());
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    // code to interact with UI

                                }
                            });*/

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
    public void showUserSelfPage() {
        Intent t = new Intent(this, UserPageActivity.class);
        if (userLoader.getUser() != null) {
            t.putExtra(Consts.USER_ID, userLoader.getUser().getId());
            startActivity(t);
        }
    }

    @OnClick(R.id.imvSearch)
    public void showSearchPage() {
        Intent t = new Intent(this, SearchActivity.class);
        startActivity(t);
    }

    @OnClick(R.id.imvMyLike)
    public void showMyLike() {
        Intent t = new Intent(this, MyLikeActivity.class);
        startActivity(t);
    }
}
