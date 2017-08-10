package com.sns.repost.loader;

import android.util.Log;


import com.sns.repost.RepostApplication;
import com.sns.repost.api.InstagramService;
import com.sns.repost.helpers.callback.SimpleCallback;
import com.sns.repost.helpers.callback.SuccessfullCallback;
import com.sns.repost.models.Media;
import com.sns.repost.services.response.LoadLikedResponse;
import com.sns.repost.services.response.TagsResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nguyenvanhien on 6/23/17.
 */

public class TagsLoader {
    static TagsLoader instance;
    private String nextUrl = "";

    public String getNextUrl() {
        return nextUrl;
    }

    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }

    public static TagsLoader getInstance() {
        if (instance == null) {
            instance = new TagsLoader();
        }
        return instance;
    }

    public void loadMediaFromTag(String tag, final SimpleCallback callback) {
        InstagramService.createService().loadMediaFromTag(tag,
                RepostApplication.getInstance().getAppSettings().getInstagramAccessToken())
                .enqueue(new Callback<LoadLikedResponse>() {
                    @Override
                    public void onResponse(Call<LoadLikedResponse> call, Response<LoadLikedResponse> response) {
                        if (response != null && response.body() != null
                                && response.body() instanceof LoadLikedResponse) {
                            List<LoadLikedResponse.MediaData> mListData = response.body().getData();
                            ArrayList<Media> mediaList = new ArrayList<Media>();
                            if (mListData != null && mListData.size() > 0) {
                                for (LoadLikedResponse.MediaData data : mListData) {
                                    mediaList.add(data.getMedia());
                                }
                            }
                            Log.e("loadLiked", "Next URL : " + response.body().getPagination().getNextUrl());
                            setNextUrl(response.body().getPagination().getNextUrl());

                            callback.success(mediaList);
                        }
                    }

                    @Override
                    public void onFailure(Call<LoadLikedResponse> call, Throwable t) {
                        callback.failed();
                    }
                });
    }

    public void loadMoreTagMedia(String nextUrl, final SuccessfullCallback callback) {
        InstagramService.createService().loadMoreLike(nextUrl).enqueue(new Callback<LoadLikedResponse>() {
            @Override
            public void onResponse(Call<LoadLikedResponse> call, Response<LoadLikedResponse> response) {
                List<LoadLikedResponse.MediaData> mListData = response.body().getData();
                ArrayList<Media> mediaList = new ArrayList<Media>();
                if (mListData != null && mListData.size() > 0) {
                    for (LoadLikedResponse.MediaData data : mListData) {
                        mediaList.add(data.getMedia());
                    }
                }
                //Log.e("MediaData","Next URL : "+response.body().getPagination().getNextUrl());
                setNextUrl(response.body().getPagination().getNextUrl());
                callback.success(mediaList);
            }

            @Override
            public void onFailure(Call<LoadLikedResponse> call, Throwable t) {

            }
        });
    }

    public void searchTag(String searchWord, final SimpleCallback callback) {
        InstagramService.createService().searchTag(searchWord,
                RepostApplication.getInstance().getAppSettings().getInstagramAccessToken())
                .enqueue(new Callback<TagsResponse>() {
                    @Override
                    public void onResponse(Call<TagsResponse> call, Response<TagsResponse> response) {
                        if (response != null && response.body() != null
                                && response.body() instanceof TagsResponse) {
                            if (response.body().getTags() != null) {
                                callback.success(response.body().getTags());
                            } else {
                                callback.failed();
                            }
                        } else {
                            callback.failed();
                        }
                    }

                    @Override
                    public void onFailure(Call<TagsResponse> call, Throwable t) {
                        Log.e("VAVA", "" + t.getMessage());
                        callback.failed();
                    }
                });
    }






}
