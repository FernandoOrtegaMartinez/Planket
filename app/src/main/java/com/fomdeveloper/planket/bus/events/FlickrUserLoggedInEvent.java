package com.fomdeveloper.planket.bus.events;

/**
 * Created by Fernando on 23/09/16.
 */
public class FlickrUserLoggedInEvent {

    private final String userId;

    public FlickrUserLoggedInEvent(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
