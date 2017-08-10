package com.sns.repost.api;


import com.sns.repost.services.response.GetCommentResponse;
import com.sns.repost.services.response.GetFollowStatusResponse;
import com.sns.repost.services.response.GetRelationShipResponse;
import com.sns.repost.services.response.InstagramGetTokenResponse;
import com.sns.repost.services.response.LikeResponse;
import com.sns.repost.services.response.LoadFollowsResponse;
import com.sns.repost.services.response.LoadLikedResponse;
import com.sns.repost.services.response.LoadMediaResponse;
import com.sns.repost.services.response.LoadSelfResponse;
import com.sns.repost.services.response.TagsResponse;
import com.sns.repost.services.response.UsersResponse;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by hien.nv on 5/3/17.
 */

public interface InstagramApi {
    @FormUrlEncoded
    @POST("oauth/access_token")
    Call<InstagramGetTokenResponse> getInstagramAccessToken(@Field("client_id") String client_id, @Field("client_secret") String client_secret,
                                                            @Field("redirect_uri") String redirect_uri, @Field("grant_type") String grant_type,
                                                            @Field("code") String code);
    @GET("users/self")
    Call<LoadSelfResponse> getUserSelf(@Query("access_token") String accessToken);

    @GET("users/self/media/liked")
    Call<LoadLikedResponse> loadLiked(@Query("access_token") String accessToken);

    @GET
    Call<LoadLikedResponse> loadMoreLike(@Url String url);

    @GET("users/{user-id}")
    Call<LoadSelfResponse> loadUser(@Path("user-id") String user_id, @Query("access_token") String accessToken);

    @FormUrlEncoded
    @POST("users/{user.id}/relationship")
    Call<String> instagramUnFollow(@Path("user-id") String user_id, @Field("access_token") String accessToken, @Field("action") String action);

    @FormUrlEncoded
    @POST("/users/{user.id}/relationship")
    Call<String> instagramFollow(@Path("user-id") String user_id, @Field("access_token") String accessToken, @Field("action") String action);

    @GET("users/self/follows")
    Call<LoadFollowsResponse> loadFollows(@Query("access_token") String accessToken);

    @GET
    Call<LoadFollowsResponse> loadMoreFollow(@Url String url);

    @GET("users/{user-id}/relationship")
    Call<GetFollowStatusResponse> getFollowStatus(@Path("user-id") String user_id, @Query("access_token") String accessToken);

    @GET("users/{user-id}/media/recent")
    Call<LoadLikedResponse> getMediaFollowUser(@Path("user-id") String user_id, @Query("access_token") String accessToken);

    @GET("media/{media-id}")
    Call<LoadMediaResponse> loadMedia(@Path("media-id") String media_id, @Query("access_token") String accessToken);

    @GET("media/{media-id}/comments")
    Call<GetCommentResponse> loadComments(@Path("media-id") String media_id, @Query("access_token") String accessToken);

    @FormUrlEncoded
    @POST("media/{media-id}/likes")
    Call<LikeResponse> likeInstagram(@Path("media-id") String media_id, @Field("access_token") String accessToken);

    @DELETE("media/{media-id}/likes")
    Call<LikeResponse> removeLikeInstagram(@Path("media-id") String media_id, @Query("access_token") String accessToken);

    @GET("users/{user-id}/relationship")
    Call<GetRelationShipResponse> getRelationShipUser(@Path("user-id") String user_id,
                                                      @Query("access_token") String accessToken);
    @FormUrlEncoded
    @POST("users/{user-id}/relationship")
    Call<GetRelationShipResponse> changeRelationShipUser(@Path("user-id") String user_id, @Field("action") String action,
                                                         @Query("access_token") String accessToken);

    @GET("tags/{tag-name}/media/recent")
    Call<LoadLikedResponse> loadMediaFromTag(@Path("tag-name") String tagname,
                                             @Query("access_token") String accessToken);

    @GET
    Call<LoadLikedResponse> loadMoreMediaFromTag(@Url String url);

    @GET("tags/search")
    Call<TagsResponse> searchTag(@Query("q") String searchWord,
                                 @Query("access_token") String token);

    @GET("users/search")
    Call<UsersResponse> searchPeople(@Query("q") String searchWord,
                                     @Query("access_token") String token);



    //popular
    @GET("media/popular")
    Call<String> loadPopular(@Query("client_id") String accessToken);


}
