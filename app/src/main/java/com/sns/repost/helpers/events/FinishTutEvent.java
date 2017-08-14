package com.sns.repost.helpers.events;

/**
 * Created by hien.nv on 5/18/17.
 */

public class FinishTutEvent {
    boolean isFromUser = false;

    public boolean isFromUser() {
        return isFromUser;
    }

    public void setFromUser(boolean fromUser) {
        isFromUser = fromUser;
    }

    public FinishTutEvent(boolean isFromUser) {
        this.isFromUser = isFromUser;
    }
}
