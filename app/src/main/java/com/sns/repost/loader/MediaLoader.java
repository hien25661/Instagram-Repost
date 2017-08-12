package com.sns.repost.loader;

import android.util.Log;


import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.sns.repost.BuildConfig;
import com.sns.repost.RepostApplication;
import com.sns.repost.api.InstagramService;
import com.sns.repost.helpers.callback.SimpleCallback;
import com.sns.repost.helpers.callback.SuccessfullCallback;
import com.sns.repost.models.Media;
import com.sns.repost.services.response.GetCommentResponse;
import com.sns.repost.services.response.LikeResponse;
import com.sns.repost.services.response.LoadLikedResponse;
import com.sns.repost.utils.Consts;
import com.sns.repost.utils.Utils;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hien.nv on 5/4/17.
 */

public class MediaLoader {
    static MediaLoader instance;
    private ArrayList<Media> medias = new ArrayList<>();
    private String nextUrl = "";


    public static MediaLoader getInstance() {
        if (instance == null) {
            instance = new MediaLoader();
        }
        return instance;
    }

    public void loadLiked(final SuccessfullCallback callback) {
        InstagramService.createService().loadLiked(RepostApplication.getInstance().getAppSettings().getInstagramAccessToken()).enqueue(new Callback<LoadLikedResponse>() {
            @Override
            public void onResponse(Call<LoadLikedResponse> call, Response<LoadLikedResponse> response) {
                if (response != null && response.body() != null && response.body() instanceof LoadLikedResponse) {
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
                Log.e("loadLiked", t.getMessage());
            }
        });
    }

    public void loadPopular(final SuccessfullCallback callback) {
        String url = "https://i.instagram.com/api/v1/feed/popular/?people_teaser_supported=1";
//        InstagramService.creatTestServiceApi().loadPopular(Utils.userAgent,url).enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                Log.e("loadPopular", ""+response.body());
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                Log.e("loadPopular", t.getMessage());
//            }
//        });
//        ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(),
//                new SharedPrefsCookiePersistor(RepostApplication.getInstance()));
//        OkHttpClient client = new OkHttpClient.Builder()
//                .cookieJar(cookieJar)
//                .build();
//
//        Request request = new Request.Builder()
//                .url(url)
//                .get().addHeader("User-Agent",Utils.userAgent)
//                .build();
//        client.newCall(request).enqueue(new okhttp3.Callback() {
//            @Override
//            public void onFailure(okhttp3.Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(okhttp3.Call call, okhttp3.Response responses) throws IOException {
//                String response = responses.body().string();
//                Log.e("loadPopular", ""+response);
//            }
//        });
        RepostApplication.getInstance().getHttpClient()
                .newCall(Consts.requestreformer(Consts.requestreformer(
                        new Request.Builder().url(url).build()))).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response responses) throws IOException {
                String response = responses.body().string();
                Log.e("loadPopular", ""+response);
            }
        });

    }

    public void loadMoreLike(String nextUrl, final SuccessfullCallback callback) {
        InstagramService.createService().loadMoreLike(nextUrl).enqueue(new Callback<LoadLikedResponse>() {
            @Override
            public void onResponse(Call<LoadLikedResponse> call, Response<LoadLikedResponse> response) {
                if (response != null && response.body() != null && response.body() instanceof LoadLikedResponse) {
                    List<LoadLikedResponse.MediaData> mListData = response.body().getData();
                    ArrayList<Media> mediaList = new ArrayList<Media>();
                    if (mListData != null && mListData.size() >= 0) {
                        for (LoadLikedResponse.MediaData data : mListData) {
                            mediaList.add(data.getMedia());
                        }
                    }
                    Log.e("loadMoreLike", "" + response.body().toJson());
                    setNextUrl(response.body().getPagination().getNextUrl());
                    callback.success(mediaList);
                }
            }

            @Override
            public void onFailure(Call<LoadLikedResponse> call, Throwable t) {
                Log.e("loadMoreLike", "" + t.getMessage());
            }
        });
    }


    public void loadComments(String media_id, final SimpleCallback callback) {
        InstagramService.createService().loadComments(media_id, RepostApplication.getInstance().getAppSettings().
                getInstagramAccessToken()).enqueue(new Callback<GetCommentResponse>() {
            @Override
            public void onResponse(Call<GetCommentResponse> call, Response<GetCommentResponse> response) {
                if (response != null && response.body() != null && response.body() instanceof GetCommentResponse) {
                    if (response.body().getData() != null && response.body().getData().size() >= 0) {
                        Log.e("loadComments",""+response.body().toJson());
                        callback.success(response.body().getData());
                    } else callback.failed();
                } else callback.failed();
            }

            @Override
            public void onFailure(Call<GetCommentResponse> call, Throwable t) {
                callback.failed();
            }
        });
    }

    public ArrayList<Media> getMedias() {
        return medias;
    }

    public void setMedias(ArrayList<Media> medias) {
        this.medias = medias;
    }

    public String getNextUrl() {
        return nextUrl;
    }

    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }

    public void likeMedia(String media_id, final SimpleCallback callback){
        InstagramService.createService().
                likeInstagram(media_id,RepostApplication.getInstance().getAppSettings().getInstagramAccessToken())
                .enqueue(new Callback<LikeResponse>() {
            @Override
            public void onResponse(Call<LikeResponse> call, Response<LikeResponse> response) {
                callback.success();
            }

            @Override
            public void onFailure(Call<LikeResponse> call, Throwable t) {
                callback.failed();
            }
        });
    }

    public void removeLikeMedia(String media_id, final SimpleCallback callback){
        InstagramService.createService().
                removeLikeInstagram(media_id,RepostApplication.getInstance().getAppSettings().getInstagramAccessToken())
                .enqueue(new Callback<LikeResponse>() {
                    @Override
                    public void onResponse(Call<LikeResponse> call, Response<LikeResponse> response) {
                        callback.success();
                    }

                    @Override
                    public void onFailure(Call<LikeResponse> call, Throwable t) {
                        callback.failed();
                    }
                });
    }
}
