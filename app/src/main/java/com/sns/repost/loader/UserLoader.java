package com.sns.repost.loader;

import android.util.Log;

import com.google.gson.Gson;
import com.sns.repost.RepostApplication;
import com.sns.repost.api.InstagramService;
import com.sns.repost.helpers.callback.SimpleCallback;
import com.sns.repost.models.User;
import com.sns.repost.services.response.GetRelationShipResponse;
import com.sns.repost.services.response.LoadMediaResponse;
import com.sns.repost.services.response.LoadSelfResponse;
import com.sns.repost.services.response.UsersResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hien.nv on 5/4/17.
 */

public class UserLoader {
    static UserLoader instance;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static UserLoader getInstance() {
        if (instance == null) {
            instance = new UserLoader();
        }
        return instance;
    }

    public void loadSelf(final SimpleCallback callback) {
        InstagramService.createService().getUserSelf(RepostApplication.getInstance().getAppSettings()
                .getInstagramAccessToken()).enqueue(new Callback<LoadSelfResponse>() {
            @Override
            public void onResponse(Call<LoadSelfResponse> call, Response<LoadSelfResponse> response) {
                if (response != null && response.body() != null && response.body() instanceof LoadSelfResponse) {
                    callback.success(response.body().getData());
                } else call.notify();
            }

            @Override
            public void onFailure(Call<LoadSelfResponse> call, Throwable t) {
                callback.failed();
            }
        });
    }

    public void loadUser(String user_id, final SimpleCallback callback) {
        InstagramService.createService().loadUser(user_id,
                RepostApplication.getInstance().getAppSettings().getInstagramAccessToken()).enqueue(new Callback<LoadSelfResponse>() {
            @Override
            public void onResponse(Call<LoadSelfResponse> call, Response<LoadSelfResponse> response) {
                if (response != null && response.body() != null && response.body() instanceof LoadSelfResponse) {
                    if (response.body().getData() != null) {
                        callback.success(response.body().getData());
                    } else {
                        callback.failed();
                    }
                } else {
                    callback.failed();
                }
            }

            @Override
            public void onFailure(Call<LoadSelfResponse> call, Throwable t) {
                callback.failed();
            }
        });
    }



    public void instagramUnFollow(User user) {
        InstagramService.createService().instagramUnFollow(user.getId(), RepostApplication.getInstance().getAppSettings().getInstagramAccessToken(), "unfollow");

    }

    public void instagramFollow(User user) {
        InstagramService.createService().instagramUnFollow(user.getId(), RepostApplication.getInstance().getAppSettings().getInstagramAccessToken(), "follow");
    }



    public void loadMedia(String media_id, final SimpleCallback callback) {
        InstagramService.createService().loadMedia(media_id,
                RepostApplication.getInstance().getAppSettings().getInstagramAccessToken()).enqueue(new Callback<LoadMediaResponse>() {
            @Override
            public void onResponse(Call<LoadMediaResponse> call, Response<LoadMediaResponse> response) {
                if (response != null && response.body() != null && response.body() instanceof LoadMediaResponse) {
                    if (response.body().getMedia() != null) {
                        Log.e("loadMedia", "" + response.body().getMedia().toJson());
                        callback.success(response.body().getMedia());
                    } else callback.failed();
                } else callback.failed();
            }

            @Override
            public void onFailure(Call<LoadMediaResponse> call, Throwable t) {
                callback.failed();
            }
        });
    }

    public void getRelationShipUser(String userId, final SimpleCallback callback) {
        InstagramService.createService().getRelationShipUser(userId,
                RepostApplication.getInstance().getAppSettings().getInstagramAccessToken()).enqueue(new Callback<GetRelationShipResponse>() {
            @Override
            public void onResponse(Call<GetRelationShipResponse> call, Response<GetRelationShipResponse> response) {
                if (response != null && response.body() != null && response.body() instanceof GetRelationShipResponse) {
                    if (response.body().getData() != null) {
                        callback.success(response.body().getData());
                    } else callback.failed();

                } else callback.failed();
            }

            @Override
            public void onFailure(Call<GetRelationShipResponse> call, Throwable t) {
                callback.failed();
            }
        });
    }

    public void changeRelationShipAction(String user_id, String action, final SimpleCallback callback) {
        InstagramService.createService().changeRelationShipUser(user_id, action,
                RepostApplication.getInstance().getAppSettings().getInstagramAccessToken())
                .enqueue(new Callback<GetRelationShipResponse>() {
                    @Override
                    public void onResponse(Call<GetRelationShipResponse> call, Response<GetRelationShipResponse> response) {
                        if (response != null && response.body() != null && response.body().getData() != null) {
                            callback.success(response.body().getData());
                        } else callback.failed();
                    }

                    @Override
                    public void onFailure(Call<GetRelationShipResponse> call, Throwable t) {
                        callback.failed();
                    }
                });
    }




    public void searchUser(String searchWord, final SimpleCallback callback){
        InstagramService.createService().searchPeople(searchWord,
                RepostApplication.getInstance().getAppSettings().getInstagramAccessToken())
                .enqueue(new Callback<UsersResponse>() {
                    @Override
                    public void onResponse(Call<UsersResponse> call, Response<UsersResponse> response) {
                        if(response!=null && response.body()!=null
                                && response.body() instanceof UsersResponse){
                            if(response.body().getUsers()!=null){
                                callback.success(response.body().getUsers());
                            }else {
                                callback.failed();
                            }
                        }else {
                            callback.failed();
                        }
                    }

                    @Override
                    public void onFailure(Call<UsersResponse> call, Throwable t) {
                        Log.e("VAVA",""+t.getMessage());
                        callback.failed();
                    }
                });
    }

}
