package com.fomdeveloper.planket.data.model;

/**
 * Created by Fernando on 25/06/16.
 * An interface for model items that can get a user profile picture.
 */
public interface UserProfileInfo {

    String userProfileUrl = "http://farm%s.staticflickr.com/%s/buddyicons/%s.jpg";
    String getUserProfilePicUrl();
    String getBestName();

}
