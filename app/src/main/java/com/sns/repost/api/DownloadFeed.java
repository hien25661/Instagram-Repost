package com.sns.repost.api;

import android.os.AsyncTask;
import android.util.Log;

import com.sns.repost.RepostApplication;
import com.sns.repost.utils.Constants;
import com.sns.repost.utils.StoryInfo;
import com.sns.repost.utils.Utils;


import net.londatiga.android.instagram.InstagramSession;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request.Builder;
import okhttp3.Response;
import okhttp3.internal.Util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.net.URL;

public class DownloadFeed extends AsyncTask<URL, Integer, Long> {
    private int mCount = 0;
    String mMinMediaId;

    public DownloadFeed(InstagramSession is) {
    }


    protected Long doInBackground(URL... urls) {
        try {
            final int mode = 0;
            String url = "";

            url = Constants.getSelfFeedUrl();

            if (this.mMinMediaId.equals("")) {
                url = url + "?max_id=" + this.mMinMediaId;
            }
            RepostApplication.getInstance().getHttpClient().newCall(Constants.requestreformer(new Builder().url(url).get().header("User-Agent", Utils.userAgent).build())).enqueue(new Callback() {
                public void onFailure(Call paramAnonymousCall, IOException paramAnonymousIOException) {
                }

                public void onResponse(Call paramAnonymousCall, Response paramAnonymousResponse) throws IOException {
                    try {
                        String response = paramAnonymousResponse.body().string();
                        Utils.writeToFile("", response);
                        if (!response.equals("")) {
                            JSONObject jsonObj = (JSONObject) new JSONTokener(response).nextValue();
                            JSONArray jsonData = jsonObj.getJSONArray("items");
                            int length = jsonData.length();
                            String maxId = "";
                            if (length > 0) {
                                int max = length;
                                int i = 0;
                                while (i < max) {
                                    if (DownloadFeed.this.isCancelled()) {
                                        return;
                                    }
                                    StoryInfo si = new StoryInfo();
                                    String type = jsonData.getJSONObject(i).getString("media_type");
                                    String jsonLikes = jsonData.getJSONObject(i).getString("like_count");
                                    JSONObject jsonUser = jsonData.getJSONObject(i).getJSONObject("user");
                                    maxId = jsonData.getJSONObject(i).getString("id");
                                    String link = "https://www.instagram.com/p/" + jsonData.getJSONObject(i).getString("code") + "/";
                                    JSONObject jsonVideo = null;
                                    JSONObject jsonVideoHq = null;
                                    JSONObject jsonPhoto = jsonData.getJSONObject(i).getJSONObject("image_versions2").getJSONArray("candidates").getJSONObject(1);
                                    JSONObject jsonPhotoHq = jsonData.getJSONObject(i).getJSONObject("image_versions2").getJSONArray("candidates").getJSONObject(0);
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
                                    if (si.isVideo) {
                                        si.video = jsonVideo.getString("url");
                                        si.videoHQ = jsonVideoHq.getString("url");
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
                                    i++;
                                    Log.e("LoadPopular",""+si.username);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    DownloadFeed.this.postProcessing();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Long.valueOf(0);
    }

    protected void onProgressUpdate(Integer... progress) {
    }

    protected void onPostExecute(Long result) {
    }

    private void postProcessing() {
        if (isCancelled()) {
            return;
        }

    }
}
