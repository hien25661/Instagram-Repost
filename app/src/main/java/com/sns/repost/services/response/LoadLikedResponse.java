package com.sns.repost.services.response;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sns.repost.models.BaseModel;
import com.sns.repost.models.Caption;
import com.sns.repost.models.Comments;
import com.sns.repost.models.Images;
import com.sns.repost.models.Likes;
import com.sns.repost.models.Location;
import com.sns.repost.models.LowResolution;
import com.sns.repost.models.Media;
import com.sns.repost.models.StandardResolution;
import com.sns.repost.models.Thumbnail;
import com.sns.repost.models.User;
import com.sns.repost.models.UsersInPhoto;
import com.sns.repost.models.Videos;

/**
 * Created by Hien on 5/4/2017.
 */

public class LoadLikedResponse extends BaseModel {

    @SerializedName("pagination")
    @Expose
    private Pagination pagination;
    @SerializedName("data")
    @Expose
    private List<MediaData> data = null;
    @SerializedName("meta")
    @Expose
    private Meta meta;

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public List<MediaData> getData() {
        return data;
    }

    public void setData(List<MediaData> data) {
        this.data = data;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public class Meta {

        @SerializedName("code")
        @Expose
        private Integer code;

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

    }
    public class Pagination {

        @SerializedName("next_max_like_id")
        @Expose
        private String nextMaxLikeId;
        @SerializedName("next_url")
        @Expose
        private String nextUrl = "";

        public String getNextMaxLikeId() {
            return nextMaxLikeId;
        }

        public void setNextMaxLikeId(String nextMaxLikeId) {
            this.nextMaxLikeId = nextMaxLikeId;
        }

        public String getNextUrl() {
            return nextUrl;
        }

        public void setNextUrl(String nextUrl) {
            this.nextUrl = nextUrl;
        }

    }
    public class Position {

        @SerializedName("x")
        @Expose
        private Double x;
        @SerializedName("y")
        @Expose
        private Double y;

        public Double getX() {
            return x;
        }

        public void setX(Double x) {
            this.x = x;
        }

        public Double getY() {
            return y;
        }

        public void setY(Double y) {
            this.y = y;
        }

    }

    public class MediaData {

        //    private User user;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("user")
        @Expose
        private User user;
        @SerializedName("images")
        @Expose
        private Images images;
        @SerializedName("created_time")
        @Expose
        private String createdTime;
        @SerializedName("caption")
        @Expose
        private Caption caption;
        @SerializedName("user_has_liked")
        @Expose
        private Boolean userHasLiked;
        @SerializedName("likes")
        @Expose
        private Likes likes;
        @SerializedName("tags")
        @Expose
        private List<String> tags = null;
        @SerializedName("filter")
        @Expose
        private String filter;
        @SerializedName("comments")
        @Expose
        private Comments comments;
        @SerializedName("type")
        @Expose
        private String type = "";
        @SerializedName("link")
        @Expose
        private String link;
        @SerializedName("location")
        @Expose
        private Location location;
        @SerializedName("attribution")
        @Expose
        private Object attribution;
        @SerializedName("users_in_photo")
        @Expose
        private List<UsersInPhoto> usersInPhoto = new ArrayList<>();

        @SerializedName("videos")
        @Expose
        private Videos videos;

        @SerializedName("carousel_media")
        @Expose
        private List<MediaData> carouselMedia = new ArrayList<>();

        public Videos getVideos() {
            return videos;
        }

        public void setVideos(Videos videos) {
            this.videos = videos;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public Images getImages() {
            return images;
        }

        public void setImages(Images images) {
            this.images = images;
        }

        public String getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(String createdTime) {
            this.createdTime = createdTime;
        }

        public Caption getCaption() {
            return caption;
        }

        public void setCaption(Caption caption) {
            this.caption = caption;
        }

        public Boolean getUserHasLiked() {
            return userHasLiked;
        }

        public void setUserHasLiked(Boolean userHasLiked) {
            this.userHasLiked = userHasLiked;
        }

        public Likes getLikes() {
            return likes;
        }

        public void setLikes(Likes likes) {
            this.likes = likes;
        }

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }

        public String getFilter() {
            return filter;
        }

        public void setFilter(String filter) {
            this.filter = filter;
        }

        public Comments getComments() {
            return comments;
        }

        public void setComments(Comments comments) {
            this.comments = comments;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        public Object getAttribution() {
            return attribution;
        }

        public void setAttribution(Object attribution) {
            this.attribution = attribution;
        }

        public List<UsersInPhoto> getUsersInPhoto() {
            return usersInPhoto;
        }

        public void setUsersInPhoto(List<UsersInPhoto> usersInPhoto) {
            this.usersInPhoto = usersInPhoto;
        }

        public List<MediaData> getCarouselMedia() {
            return carouselMedia;
        }

        public void setCarouselMedia(List<MediaData> carouselMedia) {
            this.carouselMedia = carouselMedia;
        }



        public Media getMedia(){
            Media media = new Media();
            media.setType(getType());
            ArrayList<UsersInPhoto> userInPhotoString = new ArrayList<>();
            for (UsersInPhoto item : getUsersInPhoto()){
                if(item!=null){
                    userInPhotoString.add(item);
                }
            }
            media.setUsersinphoto(userInPhotoString);
            media.setTags((ArrayList<String>) getTags());
            media.setComments(getComments());

            Caption mCaption = new Caption();
            if(getCaption() !=null){
                mCaption = getCaption();
            }
            media.setCaption(mCaption);


            media.setLikes(getLikes());
            media.setLink(getLink());
            media.setCreated_time(getCreatedTime());
            media.setImages(getImages());

            Videos videos = new Videos();
            StandardResolution standardResolution = new StandardResolution();
            standardResolution.setHeight(0);
            standardResolution.setUrl("");
            standardResolution.setWidth(0);
            videos.setStandardResolution(standardResolution);

            if(getVideos() != null) {
                videos = getVideos();
            }
            media.setVideos(videos);

            media.setId(getId());
            if(getLocation()!= null && getLocation().getName()!=null) {
                media.setLocation(getLocation().getName());
            }
            media.setUser(getUser());
            media.isLiked = getUserHasLiked();
            if(getCarouselMedia()!=null && getCarouselMedia().size()>0){
                ArrayList<Media> carouselListMedia = new ArrayList<>();
                for (MediaData mediaData : getCarouselMedia()){
                    carouselListMedia.add(getMedia(mediaData));
                }
                media.setCarouselMedia(carouselListMedia);
            }
            return media;
        }


        public Media getMedia(MediaData mediaData){
            Media media = new Media();
            media.setType(mediaData.getType());
            media.setImages(mediaData.getImages());

            Videos videos = new Videos();
            StandardResolution standardResolution = new StandardResolution();
            standardResolution.setHeight(0);
            standardResolution.setUrl("");
            standardResolution.setWidth(0);
            videos.setStandardResolution(standardResolution);

            if(getVideos() != null) {
                videos = mediaData.getVideos();
            }
            media.setVideos(videos);


            return media;
        }
    }
}
