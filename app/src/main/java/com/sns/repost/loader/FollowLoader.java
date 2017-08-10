package com.sns.repost.loader;

import android.util.Log;

import com.sns.repost.RepostApplication;
import com.sns.repost.api.InstagramService;
import com.sns.repost.helpers.callback.SimpleCallback;
import com.sns.repost.helpers.callback.SuccessfullCallback;
import com.sns.repost.models.Media;
import com.sns.repost.models.User;
import com.sns.repost.services.response.GetFollowStatusResponse;
import com.sns.repost.services.response.LoadFollowsResponse;
import com.sns.repost.services.response.LoadLikedResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Hien on 5/11/2017.
 */

public class FollowLoader {
    private String nextUrl;
    static FollowLoader instance;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getNextUrl() {
        return nextUrl;
    }

    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }

    public static FollowLoader getInstance() {
        if (instance == null) {
            instance = new FollowLoader();
        }
        return instance;
    }

    public void getFollowStatus(String userId, final SimpleCallback callback) {
        InstagramService.createService().getFollowStatus(userId,
                RepostApplication.getInstance().getAppSettings().getInstagramAccessToken()).
                enqueue(new Callback<GetFollowStatusResponse>() {
                    @Override
                    public void onResponse(Call<GetFollowStatusResponse> call, Response<GetFollowStatusResponse> response) {
                        if (response != null && response.body() instanceof GetFollowStatusResponse) {
                            callback.success(response.body().getData().getTargetUserIsPrivate());
                        }else {
                            callback.failed();
                        }
                    }

                    @Override
                    public void onFailure(Call<GetFollowStatusResponse> call, Throwable t) {
                        callback.failed();
                    }
                });
    }

    public void getMediaFollowUser(String userId, final SimpleCallback callback) {
        InstagramService.createService().getMediaFollowUser(userId,
                RepostApplication.getInstance().getAppSettings().getInstagramAccessToken()).
                enqueue(new Callback<LoadLikedResponse>() {
                    @Override
                    public void onResponse(Call<LoadLikedResponse> call, Response<LoadLikedResponse> response) {
                        if (response != null && response.body() instanceof LoadLikedResponse) {
                            List<LoadLikedResponse.MediaData> mListData = response.body().getData();
                            ArrayList<Media> mediaList = new ArrayList<Media>();
                            if (mListData != null && mListData.size() >= 0) {
                                for (LoadLikedResponse.MediaData data : mListData) {
                                    mediaList.add(data.getMedia());
                                }
                            }
                            Log.e("Follow Data", "Next URL : " + response.body().getPagination().getNextUrl());
                            setNextUrl(response.body().getPagination().getNextUrl());
                            callback.success(mediaList);

                        }else {
                            callback.failed();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoadLikedResponse> call, Throwable t) {
                        callback.failed();
                    }
                });
    }

    public void loadMoreUserFeed(String nextUrl, final SuccessfullCallback callback){
        InstagramService.createService().loadMoreLike(nextUrl).enqueue(new Callback<LoadLikedResponse>() {
            @Override
            public void onResponse(Call<LoadLikedResponse> call, Response<LoadLikedResponse> response) {
                List<LoadLikedResponse.MediaData> mListData = response.body().getData();
                ArrayList<Media> mediaList = new ArrayList<Media>();
                if(mListData!=null && mListData.size() >0){
                    for (LoadLikedResponse.MediaData data : mListData){
                        mediaList.add(data.getMedia());
                    }
                }
                Log.e("MediaData","Next URL : "+response.body().getPagination().getNextUrl());
                setNextUrl(response.body().getPagination().getNextUrl());
                callback.success(mediaList);
            }

            @Override
            public void onFailure(Call<LoadLikedResponse> call, Throwable t) {

            }
        });
    }
    public void loadFollows(final SimpleCallback callback) {

        InstagramService.createService().loadFollows(RepostApplication.getInstance().getAppSettings().getInstagramAccessToken())
                .enqueue(new Callback<LoadFollowsResponse>() {
                    @Override
                    public void onResponse(Call<LoadFollowsResponse> call, Response<LoadFollowsResponse> response) {
                        Log.e("Follow: ", "" + response.body().toJson());
                        if (response != null && response.body() != null) {
                            List<User> mList = (List<User>) response.body().getData();
                            setNextUrl(response.body().getPagination().getNextUrl());
                            if (mList != null && mList.size() >= 0) {
                                callback.success(mList);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<LoadFollowsResponse> call, Throwable t) {
                        callback.failed();
                    }
                });
    }

    public void loadMoreFollows(String nextUrl,final SuccessfullCallback callback) {

        InstagramService.createService().loadMoreFollow(nextUrl)
                .enqueue(new Callback<LoadFollowsResponse>() {
                    @Override
                    public void onResponse(Call<LoadFollowsResponse> call, Response<LoadFollowsResponse> response) {
                        Log.e("Follow: ", "" + response.body().toJson());
                        if (response != null && response.body() != null) {
                            List<User> mList = (List<User>) response.body().getData();
                            setNextUrl(response.body().getPagination().getNextUrl());
                            if (mList != null && mList.size() > 0) {
                                callback.success(mList);
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<LoadFollowsResponse> call, Throwable t) {

                    }
                });
    }

}
