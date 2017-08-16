package com.sns.repost.loader;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
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
import com.sns.repost.models.Caption;
import com.sns.repost.models.Images;
import com.sns.repost.models.Likes;
import com.sns.repost.models.Media;
import com.sns.repost.models.StandardResolution;
import com.sns.repost.models.User;
import com.sns.repost.models.UsersInPhoto;
import com.sns.repost.models.Videos;
import com.sns.repost.services.response.GetCommentResponse;
import com.sns.repost.services.response.LikeResponse;
import com.sns.repost.services.response.LoadLikedResponse;
import com.sns.repost.utils.Consts;
import com.sns.repost.utils.StoryInfo;
import com.sns.repost.utils.Util;
import com.sns.repost.utils.Utils;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import londatiga.android.instagram.util.Cons;
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
    private String mMinMediaId = "";
    public Activity act;


    public String getmMinMediaId() {
        return mMinMediaId;
    }

    public void setmMinMediaId(String mMinMediaId) {
        this.mMinMediaId = mMinMediaId;
    }

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
        String url = "https://i.instagram.com/api/v1/feed/timeline/";
        if (this.mMinMediaId != "") {
            url = url + "?max_id=" + this.mMinMediaId;
        }
        String cookie = RepostApplication.getInstance().getAppSettings().getInstagramCookie();
        RepostApplication.getInstance().getHttpClient()
                .newCall(Consts.requestreformer(
                        new Request.Builder().url(url).build(),cookie)).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response responses) throws IOException {
                final ArrayList<Media> listMedia = new ArrayList<Media>();
                try {
                    String response = responses.body().string();
                    Utils.writeToFile("", response);
                    if (!response.equals("")) {
                        JSONObject jsonObj = (JSONObject) new JSONTokener(response).nextValue();
                        JSONArray jsonData = jsonObj.getJSONArray("items");
                        if (jsonObj.getString("next_max_id") != null) {
                            mMinMediaId = jsonObj.getString("next_max_id");
                        } else {
                            mMinMediaId = "";
                        }
                        int length = jsonData.length();
                        String maxId = "";
                        if (length > 0) {
                            int max = length;
                            int i = 0;
                            while (i < max) {
                                StoryInfo si = new StoryInfo();
                                String type = jsonData.getJSONObject(i).getString("media_type");
                                String jsonLikes = jsonData.getJSONObject(i).getString("like_count");
                                boolean isLiked = jsonData.getJSONObject(i).getBoolean("has_liked");
                                JSONObject jsonUser = jsonData.getJSONObject(i).getJSONObject("user");
                                maxId = jsonData.getJSONObject(i).getString("id");
                                String link = "https://www.instagram.com/p/" + jsonData.getJSONObject(i).getString("code") + "/";
                                JSONObject jsonVideo = null;
                                JSONObject jsonVideoHq = null;
                                JSONObject jsonPhoto = jsonData.getJSONObject(i).getJSONObject("image_versions2").getJSONArray("candidates").getJSONObject(1);
                                JSONObject jsonPhotoHq = jsonData.getJSONObject(i).getJSONObject("image_versions2").getJSONArray("candidates").getJSONObject(0);


                                ArrayList<UsersInPhoto> usersInPhotos = new ArrayList<UsersInPhoto>();
                                if(jsonData.getJSONObject(i).has("usertags")) {
                                    JSONObject jsonUserInPhotoTag = jsonData.getJSONObject(i).getJSONObject("usertags");
                                    if (jsonUserInPhotoTag.has("in")) {
                                        JSONArray jsonUserInPhotos = jsonUserInPhotoTag.getJSONArray("in");
                                        int numbUser = jsonUserInPhotos.length();
                                        if (numbUser > 0) {
                                            int j = 0;
                                            while (j < numbUser) {
                                                JSONObject jsonUserIn = jsonUserInPhotos.getJSONObject(j)
                                                        .getJSONObject("user");
                                                User inUser = new User();
                                                inUser.setId(jsonUserIn.getString("pk"));
                                                inUser.setUsername(jsonUserIn.getString("username"));
                                                inUser.setProfilePicture(jsonUserIn.getString("profile_pic_url"));

                                                UsersInPhoto usersInPhoto = new UsersInPhoto();
                                                usersInPhoto.setUser(inUser);
                                                usersInPhotos.add(usersInPhoto);
                                                j++;
                                            }
                                        }

                                    }
                                }
                                si.isVideo = false;
                                if (type.equals("1")) {
                                    si.isVideo = false;
                                } else if (type.equals("2")) {
                                    si.isVideo = true;
                                    jsonVideo = jsonData.getJSONObject(i).getJSONArray("video_versions").getJSONObject(1);
                                    jsonVideoHq = jsonData.getJSONObject(i).getJSONArray("video_versions").getJSONObject(0);
                                }
                                String caption = "";
                                String userId = "";
                                if (!jsonData.getJSONObject(i).isNull("caption")) {
                                    caption = jsonData.getJSONObject(i).getJSONObject("caption").getString("text");
                                    userId = jsonData.getJSONObject(i).getJSONObject("caption").getString("user_id");
                                }
                                String createdTime = jsonData.getJSONObject(i).getString("taken_at");
                                si.photo = jsonPhoto.getString("url");
                                si.photoHQ = jsonPhotoHq.getString("url");
                                int withPhoto = 0, heightPhoto = 0;
                                int withVideo = 0, heighVideo = 0;
                                if (si.isVideo) {
                                    si.video = jsonVideo.getString("url");
                                    si.videoHQ = jsonVideoHq.getString("url");
                                    withVideo = jsonVideo.getInt("width");
                                    heighVideo = jsonVideo.getInt("height");
                                } else {
                                    withPhoto = jsonPhoto.getInt("width");
                                    heightPhoto = jsonPhoto.getInt("height");
                                }
                                si.likesCount = jsonLikes;
                                si.username = jsonUser.getString("username");
                                si.userId = userId;
                                si.caption = caption;
                                si.createdTime = createdTime;
                                si.id = maxId;
                                si.userPhoto = jsonUser.getString("profile_pic_url");
                                si.link = link;
                                si.isStory = true;

                                Media media = new Media();
                                media.setId(maxId);
                                media.setCreated_time(createdTime);
                                media.setLink(link);
                                media.setType(type.equals("1") ? "image" : "video");

                                Caption media_caption = new Caption();
                                media_caption.setText(caption);
                                media.setCaption(media_caption);

                                StandardResolution photo_standardResolution = new StandardResolution();
                                photo_standardResolution.setUrl(si.photo);
                                photo_standardResolution.setWidth(withPhoto);
                                photo_standardResolution.setHeight(heightPhoto);
                                Images images = new Images();
                                images.setStandardResolution(photo_standardResolution);
                                media.setImages(images);

                                StandardResolution video_standardResolution = new StandardResolution();
                                video_standardResolution.setUrl(si.video);
                                video_standardResolution.setWidth(withVideo);
                                video_standardResolution.setHeight(heighVideo);
                                Videos videos = new Videos();
                                videos.setStandardResolution(video_standardResolution);
                                media.setVideos(videos);

                                Likes likes = new Likes();
                                likes.setCount(Integer.parseInt(si.likesCount));
                                media.isLiked = isLiked;
                                media.setLikes(likes);

                                User user = new User();
                                user.setId(si.userId);
                                user.setUsername(si.username);
                                user.setProfilePicture(si.userPhoto);
                                media.setUser(user);

                                media.setUsersinphoto(usersInPhotos);


                                listMedia.add(media);
                                Log.e("loadPopular", "" + media.toJson());
                                i++;
                            }
                            Handler mainHandler = new Handler(Looper.getMainLooper());
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    // code to interact with UI
                                    callback.success(listMedia);
                                }
                            });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }



    public void loadPopularNew(final SuccessfullCallback callback) {
        String url = "https://i.instagram.com/api/v1/feed/timeline/";
        if (this.mMinMediaId != "") {
            url = url + "?max_id=" + this.mMinMediaId;
        }
        String cookie = RepostApplication.getInstance().getAppSettings().getInstagramCookie();
        Map<String, String> map = new HashMap<>();
        map.put("User-Agent", "Instagram 8.0.0 Android (18/4.3; 320dpi; 720x1280; Xiaomi; HM 1SW; armani; qcom; en_US)");
        map.put("Connection", "close");
        map.put("Accept-Language", "en-US");
        map.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        map.put("Accept", "*/*");
        map.put("Cookie2", "$Version=1");
        map.put("cookie",cookie);



        InstagramService.creatTestServiceApi().loadPopular(map,url).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> responses) {
                final ArrayList<Media> listMedia = new ArrayList<Media>();
                try {
                    String response = responses.body().toString();
                    Utils.writeToFile("", response);
                    if (!response.equals("")) {
                        JSONObject jsonObj = (JSONObject) new JSONTokener(response).nextValue();
                        JSONArray jsonData = jsonObj.getJSONArray("items");
                        if (jsonObj.getString("next_max_id") != null) {
                            mMinMediaId = jsonObj.getString("next_max_id");
                        } else {
                            mMinMediaId = "";
                        }
                        int length = jsonData.length();
                        String maxId = "";
                        if (length > 0) {
                            int max = length;
                            int i = 0;
                            while (i < max) {
                                StoryInfo si = new StoryInfo();
                                String type = jsonData.getJSONObject(i).getString("media_type");
                                String jsonLikes = jsonData.getJSONObject(i).getString("like_count");
                                boolean isLiked = jsonData.getJSONObject(i).getBoolean("has_liked");
                                JSONObject jsonUser = jsonData.getJSONObject(i).getJSONObject("user");
                                maxId = jsonData.getJSONObject(i).getString("id");
                                String link = "https://www.instagram.com/p/" + jsonData.getJSONObject(i).getString("code") + "/";
                                JSONObject jsonVideo = null;
                                JSONObject jsonVideoHq = null;
                                JSONObject jsonPhoto = jsonData.getJSONObject(i).getJSONObject("image_versions2").getJSONArray("candidates").getJSONObject(1);
                                JSONObject jsonPhotoHq = jsonData.getJSONObject(i).getJSONObject("image_versions2").getJSONArray("candidates").getJSONObject(0);


                                ArrayList<UsersInPhoto> usersInPhotos = new ArrayList<UsersInPhoto>();
                                if(jsonData.getJSONObject(i).has("usertags")) {
                                    JSONObject jsonUserInPhotoTag = jsonData.getJSONObject(i).getJSONObject("usertags");
                                    if (jsonUserInPhotoTag.has("in")) {
                                        JSONArray jsonUserInPhotos = jsonUserInPhotoTag.getJSONArray("in");
                                        int numbUser = jsonUserInPhotos.length();
                                        if (numbUser > 0) {
                                            int j = 0;
                                            while (j < numbUser) {
                                                JSONObject jsonUserIn = jsonUserInPhotos.getJSONObject(j)
                                                        .getJSONObject("user");
                                                User inUser = new User();
                                                inUser.setId(jsonUserIn.getString("pk"));
                                                inUser.setUsername(jsonUserIn.getString("username"));
                                                inUser.setProfilePicture(jsonUserIn.getString("profile_pic_url"));

                                                UsersInPhoto usersInPhoto = new UsersInPhoto();
                                                usersInPhoto.setUser(inUser);
                                                usersInPhotos.add(usersInPhoto);
                                                j++;
                                            }
                                        }

                                    }
                                }
                                si.isVideo = false;
                                if (type.equals("1")) {
                                    si.isVideo = false;
                                } else if (type.equals("2")) {
                                    si.isVideo = true;
                                    jsonVideo = jsonData.getJSONObject(i).getJSONArray("video_versions").getJSONObject(1);
                                    jsonVideoHq = jsonData.getJSONObject(i).getJSONArray("video_versions").getJSONObject(0);
                                }
                                String caption = "";
                                String userId = "";
                                if (!jsonData.getJSONObject(i).isNull("caption")) {
                                    caption = jsonData.getJSONObject(i).getJSONObject("caption").getString("text");
                                    userId = jsonData.getJSONObject(i).getJSONObject("caption").getString("user_id");
                                }
                                String createdTime = jsonData.getJSONObject(i).getString("taken_at");
                                si.photo = jsonPhoto.getString("url");
                                si.photoHQ = jsonPhotoHq.getString("url");
                                int withPhoto = 0, heightPhoto = 0;
                                int withVideo = 0, heighVideo = 0;
                                if (si.isVideo) {
                                    si.video = jsonVideo.getString("url");
                                    si.videoHQ = jsonVideoHq.getString("url");
                                    withVideo = jsonVideo.getInt("width");
                                    heighVideo = jsonVideo.getInt("height");
                                } else {
                                    withPhoto = jsonPhoto.getInt("width");
                                    heightPhoto = jsonPhoto.getInt("height");
                                }
                                si.likesCount = jsonLikes;
                                si.username = jsonUser.getString("username");
                                si.userId = userId;
                                si.caption = caption;
                                si.createdTime = createdTime;
                                si.id = maxId;
                                si.userPhoto = jsonUser.getString("profile_pic_url");
                                si.link = link;
                                si.isStory = true;

                                Media media = new Media();
                                media.setId(maxId);
                                media.setCreated_time(createdTime);
                                media.setLink(link);
                                media.setType(type.equals("1") ? "image" : "video");

                                Caption media_caption = new Caption();
                                media_caption.setText(caption);
                                media.setCaption(media_caption);

                                StandardResolution photo_standardResolution = new StandardResolution();
                                photo_standardResolution.setUrl(si.photo);
                                photo_standardResolution.setWidth(withPhoto);
                                photo_standardResolution.setHeight(heightPhoto);
                                Images images = new Images();
                                images.setStandardResolution(photo_standardResolution);
                                media.setImages(images);

                                StandardResolution video_standardResolution = new StandardResolution();
                                video_standardResolution.setUrl(si.video);
                                video_standardResolution.setWidth(withVideo);
                                video_standardResolution.setHeight(heighVideo);
                                Videos videos = new Videos();
                                videos.setStandardResolution(video_standardResolution);
                                media.setVideos(videos);

                                Likes likes = new Likes();
                                likes.setCount(Integer.parseInt(si.likesCount));
                                media.isLiked = isLiked;
                                media.setLikes(likes);

                                User user = new User();
                                user.setId(si.userId);
                                user.setUsername(si.username);
                                user.setProfilePicture(si.userPhoto);
                                media.setUser(user);

                                media.setUsersinphoto(usersInPhotos);


                                listMedia.add(media);
                                Log.e("loadPopular", "" + media.toJson());
                                i++;
                            }
                            /*Handler mainHandler = new Handler(Looper.getMainLooper());
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    // code to interact with UI
                                    callback.success(listMedia);
                                }
                            });*/
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                callback.success(listMedia);

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

    }

    public void loadPopularNewRefresh(final SuccessfullCallback callback) {
        String url = "https://i.instagram.com/api/v1/feed/timeline/";
        String cookie = RepostApplication.getInstance().getAppSettings().getInstagramCookie();
        Map<String, String> map = new HashMap<>();
        map.put("User-Agent", "Instagram 8.0.0 Android (18/4.3; 320dpi; 720x1280; Xiaomi; HM 1SW; armani; qcom; en_US)");
        map.put("Connection", "close");
        map.put("Accept-Language", "en-US");
        map.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        map.put("Accept", "*/*");
        map.put("Cookie2", "$Version=1");
        map.put("cookie",cookie);



        InstagramService.creatTestServiceApi().loadPopular(map,url).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> responses) {
                final ArrayList<Media> listMedia = new ArrayList<Media>();
                try {
                    String response = responses.body().toString();
                    Utils.writeToFile("", response);
                    if (!response.equals("")) {
                        JSONObject jsonObj = (JSONObject) new JSONTokener(response).nextValue();
                        JSONArray jsonData = jsonObj.getJSONArray("items");
                        if (jsonObj.getString("next_max_id") != null) {
                            mMinMediaId = jsonObj.getString("next_max_id");
                        } else {
                            mMinMediaId = "";
                        }
                        int length = jsonData.length();
                        String maxId = "";
                        if (length > 0) {
                            int max = length;
                            int i = 0;
                            while (i < max) {
                                StoryInfo si = new StoryInfo();
                                String type = jsonData.getJSONObject(i).getString("media_type");
                                String jsonLikes = jsonData.getJSONObject(i).getString("like_count");
                                boolean isLiked = jsonData.getJSONObject(i).getBoolean("has_liked");
                                JSONObject jsonUser = jsonData.getJSONObject(i).getJSONObject("user");
                                maxId = jsonData.getJSONObject(i).getString("id");
                                String link = "https://www.instagram.com/p/" + jsonData.getJSONObject(i).getString("code") + "/";
                                JSONObject jsonVideo = null;
                                JSONObject jsonVideoHq = null;
                                JSONObject jsonPhoto = jsonData.getJSONObject(i).getJSONObject("image_versions2").getJSONArray("candidates").getJSONObject(1);
                                JSONObject jsonPhotoHq = jsonData.getJSONObject(i).getJSONObject("image_versions2").getJSONArray("candidates").getJSONObject(0);


                                ArrayList<UsersInPhoto> usersInPhotos = new ArrayList<UsersInPhoto>();
                                if(jsonData.getJSONObject(i).has("usertags")) {
                                    JSONObject jsonUserInPhotoTag = jsonData.getJSONObject(i).getJSONObject("usertags");
                                    if (jsonUserInPhotoTag.has("in")) {
                                        JSONArray jsonUserInPhotos = jsonUserInPhotoTag.getJSONArray("in");
                                        int numbUser = jsonUserInPhotos.length();
                                        if (numbUser > 0) {
                                            int j = 0;
                                            while (j < numbUser) {
                                                JSONObject jsonUserIn = jsonUserInPhotos.getJSONObject(j)
                                                        .getJSONObject("user");
                                                User inUser = new User();
                                                inUser.setId(jsonUserIn.getString("pk"));
                                                inUser.setUsername(jsonUserIn.getString("username"));
                                                inUser.setProfilePicture(jsonUserIn.getString("profile_pic_url"));

                                                UsersInPhoto usersInPhoto = new UsersInPhoto();
                                                usersInPhoto.setUser(inUser);
                                                usersInPhotos.add(usersInPhoto);
                                                j++;
                                            }
                                        }

                                    }
                                }
                                si.isVideo = false;
                                if (type.equals("1")) {
                                    si.isVideo = false;
                                } else if (type.equals("2")) {
                                    si.isVideo = true;
                                    jsonVideo = jsonData.getJSONObject(i).getJSONArray("video_versions").getJSONObject(1);
                                    jsonVideoHq = jsonData.getJSONObject(i).getJSONArray("video_versions").getJSONObject(0);
                                }
                                String caption = "";
                                String userId = "";
                                if (!jsonData.getJSONObject(i).isNull("caption")) {
                                    caption = jsonData.getJSONObject(i).getJSONObject("caption").getString("text");
                                    userId = jsonData.getJSONObject(i).getJSONObject("caption").getString("user_id");
                                }
                                String createdTime = jsonData.getJSONObject(i).getString("taken_at");
                                si.photo = jsonPhoto.getString("url");
                                si.photoHQ = jsonPhotoHq.getString("url");
                                int withPhoto = 0, heightPhoto = 0;
                                int withVideo = 0, heighVideo = 0;
                                if (si.isVideo) {
                                    si.video = jsonVideo.getString("url");
                                    si.videoHQ = jsonVideoHq.getString("url");
                                    withVideo = jsonVideo.getInt("width");
                                    heighVideo = jsonVideo.getInt("height");
                                } else {
                                    withPhoto = jsonPhoto.getInt("width");
                                    heightPhoto = jsonPhoto.getInt("height");
                                }
                                si.likesCount = jsonLikes;
                                si.username = jsonUser.getString("username");
                                si.userId = userId;
                                si.caption = caption;
                                si.createdTime = createdTime;
                                si.id = maxId;
                                si.userPhoto = jsonUser.getString("profile_pic_url");
                                si.link = link;
                                si.isStory = true;

                                Media media = new Media();
                                media.setId(maxId);
                                media.setCreated_time(createdTime);
                                media.setLink(link);
                                media.setType(type.equals("1") ? "image" : "video");

                                Caption media_caption = new Caption();
                                media_caption.setText(caption);
                                media.setCaption(media_caption);

                                StandardResolution photo_standardResolution = new StandardResolution();
                                photo_standardResolution.setUrl(si.photo);
                                photo_standardResolution.setWidth(withPhoto);
                                photo_standardResolution.setHeight(heightPhoto);
                                Images images = new Images();
                                images.setStandardResolution(photo_standardResolution);
                                media.setImages(images);

                                StandardResolution video_standardResolution = new StandardResolution();
                                video_standardResolution.setUrl(si.video);
                                video_standardResolution.setWidth(withVideo);
                                video_standardResolution.setHeight(heighVideo);
                                Videos videos = new Videos();
                                videos.setStandardResolution(video_standardResolution);
                                media.setVideos(videos);

                                Likes likes = new Likes();
                                likes.setCount(Integer.parseInt(si.likesCount));
                                media.isLiked = isLiked;
                                media.setLikes(likes);

                                User user = new User();
                                user.setId(si.userId);
                                user.setUsername(si.username);
                                user.setProfilePicture(si.userPhoto);
                                media.setUser(user);

                                media.setUsersinphoto(usersInPhotos);


                                listMedia.add(media);
                                Log.e("loadPopular", "" + media.toJson());
                                i++;
                            }
                            /*Handler mainHandler = new Handler(Looper.getMainLooper());
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    // code to interact with UI
                                    callback.success(listMedia);
                                }
                            });*/
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                callback.success(listMedia);

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

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
                        Log.e("loadComments", "" + response.body().toJson());
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

    public void likeMedia(String media_id, final SimpleCallback callback) {
        InstagramService.createService().
                likeInstagram(media_id, RepostApplication.getInstance().getAppSettings().getInstagramAccessToken())
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

    public void removeLikeMedia(String media_id, final SimpleCallback callback) {
        InstagramService.createService().
                removeLikeInstagram(media_id, RepostApplication.getInstance().getAppSettings().getInstagramAccessToken())
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
