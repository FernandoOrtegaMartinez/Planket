package com.fomdeveloper.planket.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.fomdeveloper.planket.data.api.oauth.OAuthToken;
import com.google.gson.Gson;

/**
 * Created by Fernando on 20/08/16.
 */
public class PlanketBoxPreferences implements UserHelper {


    public static final String PREF_FILE_NAME = "planket-preferences";
    private static final String PREF_KEY_ACCESS_TOKEN = "PREF_KEY_ACCESS_TOKEN";
    private static final String PREF_KEY_USER_PROFILE_PIC= "PREF_KEY_USER_PROFILE_PIC";

    private final SharedPreferences sharedPreferences;
    private Gson gson;

    public PlanketBoxPreferences(Context context, Gson gson) {
        this.sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        this.gson = gson;
    }

    public void clearAll() {
        sharedPreferences.edit().clear().apply();
    }

    public void putAccessToken(OAuthToken oAuthToken) {
        sharedPreferences.edit().putString(PREF_KEY_ACCESS_TOKEN, gson.toJson(oAuthToken)).apply();
    }

    public void putUserProfilePicUrl(String userProfilePicUrl){
        sharedPreferences.edit().putString(PREF_KEY_USER_PROFILE_PIC, userProfilePicUrl).apply();
    }


    @Nullable
    public OAuthToken getAccessToken() {
        String token = sharedPreferences.getString(PREF_KEY_ACCESS_TOKEN, null);
        if (token != null) {
            return gson.fromJson(token, OAuthToken.class);
        }
        return null;
    }

    @Nullable
    public String getUserProfilePicUrl() {
        return sharedPreferences.getString(PREF_KEY_USER_PROFILE_PIC, null);
    }

    @Override
    public boolean isUserLoggedIn() {
        if (getAccessToken()!=null) {
            return true;
        }
        return false;
    }

    @Override
    public void logoutUser() {
        clearAll();
    }

    @Override
    public String getUserName() {
        return getAccessToken().getUserName();
    }

    @Override
    public String getFullName() {
        return getAccessToken().getFullName();
    }

    @Override
    public String getUserId() {
        return getAccessToken().getUserId();
    }


}
