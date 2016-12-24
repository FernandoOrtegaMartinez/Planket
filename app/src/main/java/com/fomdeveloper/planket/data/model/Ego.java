package com.fomdeveloper.planket.data.model;

/**
 * Created by Fernando on 30/06/16.
 */
public abstract class Ego implements UserProfileInfo {

    protected abstract String getProfilePicUrl();

    @Override
    public String getUserProfilePicUrl() {
        return getProfilePicUrl();
    }
}
