package com.sns.repost.models;


/**
 * Created by nguyenvanhien on 7/10/17.
 */

public class Tag {
    private long timeAdded;
    private String name;
    private long mediaCount;

    public Tag() {
    }

    public long getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(long timeAdded) {
        this.timeAdded = timeAdded;
    }

    public Tag(String name, long mediaCount) {
        this.name = name;
        this.mediaCount = mediaCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getMediaCount() {
        return mediaCount;
    }

    public void setMediaCount(long mediaCount) {
        this.mediaCount = mediaCount;
    }
}
