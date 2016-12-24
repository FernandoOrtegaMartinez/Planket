package com.fomdeveloper.planket.data.prefs;


/**
 * Created by Fernando on 20/08/16.
 */
public interface UserHelper {

    boolean isUserLoggedIn();
    void logoutUser();
    String getUserName();
    String getFullName();
    String getUserId();
    String getUserProfilePicUrl();

}
