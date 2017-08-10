
package com.sns.repost.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User extends BaseModel implements Parcelable{

    @SerializedName("id")
    @Expose()
    private String id;
    @SerializedName("username")
    @Expose
    private String username = "";
    @SerializedName("profile_picture")
    @Expose
    private String profilePicture;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("bio")
    @Expose
    private String bio;
    @SerializedName("website")
    @Expose
    private String website;
    @SerializedName("counts")
    @Expose
    private Counts counts;

    private transient boolean isInfluencer = false;
    public User(){}

    protected User(Parcel in) {
        id = in.readString();
        username = in.readString();
        profilePicture = in.readString();
        fullName = in.readString();
        bio = in.readString();
        website = in.readString();
        counts = in.readParcelable(Counts.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(username);
        dest.writeString(profilePicture);
        dest.writeString(fullName);
        dest.writeString(bio);
        dest.writeString(website);
        dest.writeParcelable(counts, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Counts getCounts() {
        return counts;
    }

    public void setCounts(Counts counts) {
        this.counts = counts;
    }

    public boolean isInfluencer() {
        return isInfluencer;
    }

    public void setInfluencer(boolean influencer) {
        isInfluencer = influencer;
    }

//    public String toJson(){
//        Gson gson = new Gson();
//        return gson.toJson(this);
//    }
}
