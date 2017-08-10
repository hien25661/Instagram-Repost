package com.sns.repost.api;


import com.sns.repost.BuildConfig;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by hien.nv on 5/4/17.
 */

public class InstagramService {

    //Instagram API
    public static final String INSTAGRAM_API_BASE_URL = "https://api.instagram.com/v1/";
    public static final String DEFAULT_TOKEN = BuildConfig.DEFAULT_TOKEN;
    public static final String CONSUMER_KEY = "e1998f69f0e94024ac1d3f41caf3290a";
    public static final String CONSUMER_SECRET = "492e75d00319458fbc900e932eee5f59";
    public static final String AUTHORIZE_URL = "https://api.instagram.com/oauth/authorize";
    public static final String ACCESS_TOKEN_URL = "https://api.instagram.com/oauth/access_token";
    public static final String CALLBACK_URL = "http://www.korecow.jp/api/chao_redirect.php";
    public static final String SCOPE = "public_content+follower_list+comments+likes+relationships";

    public static InstagramApi createService() {
        return getRetrofit().create(InstagramApi.class);
    }

    public static InstagramApi instagramApi,instagramApi1;

    public static InstagramApi createServiceGetInstagramToken() {
        if (instagramApi == null) {
            instagramApi = getRetrofitInstagramToken().create(InstagramApi.class);
        }
        return instagramApi;
    }

    private static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(INSTAGRAM_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private static Retrofit getRetrofitInstagramToken() {
        return new Retrofit.Builder()
                .baseUrl("https://api.instagram.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    private static Retrofit getRetrofitInstagramTokenTest() {
        return new Retrofit.Builder()
                .baseUrl("https://api.instagram.com/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }
    public static final String API_BASE_URL = "https://i.instagram.com/api/v1/";
    public static InstagramApi creatTestServiceApi() {
        instagramApi1 = getRetrofitInstagramTokenTest().create(InstagramApi.class);
        return instagramApi1;
    }

}
