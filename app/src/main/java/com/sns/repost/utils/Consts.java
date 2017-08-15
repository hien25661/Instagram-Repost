package com.sns.repost.utils;

import okhttp3.Request;

/**
 * Created by nguyenvanhien on 8/10/17.
 */

public class Consts {
    public static final String USER_ID = "USER_ID";
    public static final String PARAM_TAG_NAME = "PARAM_TAG_NAME";
    public static final String PARAM_MEDIA = "MEDIA";
    public static String cookie = "is_starred_enabled=yes; rur=ASH; ds_user=hien211194; ds_user_id=1915924482; sessionid=IGSC230a974dcc415b287e886d8c5ad62fc74f137d6865ff48276a6fb77c0cecd507%3AhCLqebUhc4ILjHNvNlYVleYMwWW6fOGu%3A%7B%22_auth_user_id%22%3A1915924482%2C%22_auth_user_backend%22%3A%22accounts.backends.CaseInsensitiveModelBackend%22%2C%22_auth_user_hash%22%3A%22%22%2C%22_token_ver%22%3A2%2C%22_token%22%3A%221915924482%3AcqfelQiVgUhliwPk1OEE0HjYOkjwKiGE%3Ac3bfc94efc9ce09299f86efe6afd3be426eda7904d90a6b6dcae812f90d9494a%22%2C%22_platform%22%3A1%2C%22asns%22%3A%7B%22time%22%3A1502381839%7D%2C%22last_refreshed%22%3A1502381839.7640721798%7D; csrftoken=kMTPZz1htVF2UYwHAJqb1Gte4P3KLZJX; igfl=hien211194; mid=WYsynAABAAGQeYvoAxwOolFiEMvW";
    public static Request requestreformer(Request paramRequest, String cookie) {
        return paramRequest.newBuilder().
                header("User-Agent", "Instagram 8.0.0 Android (18/4.3; 320dpi; 720x1280; Xiaomi; HM 1SW; armani; qcom; en_US)")
                .header("Connection", "close")
                .header("Accept-Language", "en-US")
                .header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                .header("Accept", "*/*")
                .header("Cookie2", "$Version=1")
                .header("cookie",cookie)
                .method(paramRequest.method(), paramRequest.body()).removeHeader("Accept-Encoding").build();
    }

    public static String morecookie = "mid=WZFrGwABAAE-Oaa6QDy7TCErAoC6; sessionid=IGSC151220e312d192ca17d4f7a783b744c849261e71bba1ff4d68efb2fb8b483888%3Av4JxgKmuhn3SMvVPmE39706MvIHpvszs%3A%7B%22_auth_user_id%22%3A1915924482%2C%22_auth_user_backend%22%3A%22accounts.backends.CaseInsensitiveModelBackend%22%2C%22_auth_user_hash%22%3A%22%22%2C%22_token_ver%22%3A2%2C%22_token%22%3A%221915924482%3AUsmZj98upJChSlDOXWNCEDqxgZtjltHx%3Aca792b21afd0a7658f4a97165cea3f1f960fa3ff9ccbdf07753e0bce85b5a6e0%22%2C%22_platform%22%3A4%2C%22last_refreshed%22%3A1502702375.4693627357%7D; csrftoken=F3s5nxs0Ss5iqzKyflHtKFjOB4fhFja0; rur=FTW";

}
