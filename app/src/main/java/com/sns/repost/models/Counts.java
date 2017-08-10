package com.sns.repost.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nguyenvanhien on 8/10/17.
 */

public class Counts  implements Parcelable{
    @SerializedName("media")
    @Expose
    private int media;
    @SerializedName("follows")
    @Expose
    private int follows;
    @SerializedName("followed_by")
    @Expose
    private int followedBy;

    protected Counts(Parcel in) {
        media = in.readInt();
        follows = in.readInt();
        followedBy = in.readInt();
    }

    public final Parcelable.Creator<Counts> CREATOR = new Parcelable.Creator<Counts>() {
        @Override
        public Counts createFromParcel(Parcel in) {
            return new Counts(in);
        }

        @Override
        public Counts[] newArray(int size) {
            return new Counts[size];
        }
    };

    public int getMedia() {
        return media;
    }

    public void setMedia(int media) {
        this.media = media;
    }

    public int getFollows() {
        return follows;
    }

    public void setFollows(int follows) {
        this.follows = follows;
    }

    public int getFollowedBy() {
        return followedBy;
    }

    public void setFollowedBy(int followedBy) {
        this.followedBy = followedBy;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(media);
        dest.writeInt(follows);
        dest.writeInt(followedBy);
    }
}
