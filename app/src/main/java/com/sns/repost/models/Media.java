package com.sns.repost.models;


import com.sns.repost.services.response.LoadLikedResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hien on 5/4/2017.
 */

public class Media extends BaseModel{
    private String type = "";
    private ArrayList<UsersInPhoto> usersinphoto = new ArrayList<>();
    private ArrayList<String> tags = new ArrayList<>();
    private Comments comments;
    private Caption caption;
    private Likes likes;
    private String link = "";
    private String created_time = "";

    private Images images;
    private Videos videos;
    private String id;
    private String location = "";

    private User user;
    public boolean isLiked = false;
    private List<Media> carouselMedia = new ArrayList<>();

    public List<Media> getCarouselMedia() {
        return carouselMedia;
    }

    public void setCarouselMedia(List<Media> carouselMedia) {
        this.carouselMedia = carouselMedia;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<UsersInPhoto> getUsersinphoto() {
        return usersinphoto;
    }

    public void setUsersinphoto(ArrayList<UsersInPhoto> usersinphoto) {
        this.usersinphoto = usersinphoto;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public Comments getComments() {
        return comments;
    }

    public void setComments(Comments comments) {
        this.comments = comments;
    }


    public Caption getCaption() {
        return caption;
    }

    public void setCaption(Caption caption) {
        this.caption = caption;
    }


    public Likes getLikes() {
        return likes;
    }

    public void setLikes(Likes likes) {
        this.likes = likes;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    class MediaList {
        ArrayList<Media> medias = new ArrayList<>();
        String next_url;
        Media media;
    }

}
