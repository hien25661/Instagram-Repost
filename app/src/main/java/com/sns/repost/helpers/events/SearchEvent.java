package com.sns.repost.helpers.events;

/**
 * Created by nguyenvanhien on 7/10/17.
 */

public class SearchEvent {
    private String keyWord;

    public SearchEvent(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }
}
