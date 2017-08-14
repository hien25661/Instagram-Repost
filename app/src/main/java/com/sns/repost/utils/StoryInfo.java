package com.sns.repost.utils;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class StoryInfo implements Parcelable {
    public static final Creator<StoryInfo> CREATOR = new C03141();
    public String caption = "";
    public String createdTime = "";
    public String id = "";
    public boolean isSmall = false;
    public boolean isStory = false;
    public boolean isVideo = false;
    public String likesCount = "";
    public String link = "";
    public String photo = "";
    public String photoHQ = "";
    public String userId = "";
    public String userPhoto = "";
    public String username = "";
    public String video = "";
    public String videoHQ = "";

    static class C03141 implements Creator<StoryInfo> {
        C03141() {
        }

        public StoryInfo createFromParcel(Parcel source) {
            return new StoryInfo(source);
        }

        public StoryInfo[] newArray(int size) {
            return new StoryInfo[size];
        }
    }
    public StoryInfo(){

    }
    public StoryInfo(Parcel in) {
        boolean z;
        boolean z2 = true;
        this.photo = in.readString();
        this.photoHQ = in.readString();
        this.likesCount = in.readString();
        this.username = in.readString();
        this.caption = in.readString();
        this.createdTime = in.readString();
        this.userPhoto = in.readString();
        this.id = in.readString();
        this.link = in.readString();
        this.video = in.readString();
        this.videoHQ = in.readString();
        this.userId = in.readString();
        this.isStory = in.readByte() != (byte) 0;
        if (in.readByte() != (byte) 0) {
            z = true;
        } else {
            z = false;
        }
        this.isVideo = z;
        if (in.readByte() == (byte) 0) {
            z2 = false;
        }
        this.isSmall = z2;
    }

    public static String GetTimeStr(Long time) {
        long longVal = time.longValue();
        int weeks = ((int) longVal) / 604800;
        int remainder = ((int) longVal) - (604800 * weeks);
        int days = remainder / 86400;
        remainder -= days * 86400;
        int hours = remainder / 3600;
        remainder -= hours * 3600;
        int mins = remainder / 60;
        int secs = remainder - (mins * 60);
        if (weeks > 0) {
            return weeks + " w.";
        }
        if (days > 0) {
            return days + " d.";
        }
        if (hours > 0) {
            return hours + " h.";
        }
        if (mins > 0) {
            return mins + " min.";
        }
        if (secs > 0) {
            return secs + " s.";
        }
        return "";
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        int i;
        int i2 = 1;
        dest.writeString(this.photo);
        dest.writeString(this.photoHQ);
        dest.writeString(this.likesCount);
        dest.writeString(this.username);
        dest.writeString(this.caption);
        dest.writeString(this.createdTime);
        dest.writeString(this.userPhoto);
        dest.writeString(this.id);
        dest.writeString(this.link);
        dest.writeString(this.video);
        dest.writeString(this.videoHQ);
        dest.writeString(this.userId);
        dest.writeByte((byte) (this.isStory ? 1 : 0));
        if (this.isVideo) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeByte((byte) i);
        if (!this.isSmall) {
            i2 = 0;
        }
        dest.writeByte((byte) i2);
    }

    public String getTime() {
        return GetTimeStr(Long.valueOf((System.currentTimeMillis() / 1000) - Long.parseLong(this.createdTime)));
    }
}
