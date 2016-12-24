package com.fomdeveloper.planket.ui.presentation.base.oauth;


import com.fomdeveloper.planket.ui.presentation.base.MvpView;

/**
 * Created by Fernando on 20/08/16.
 */
public interface OauthView extends MvpView{

        void openFlickrLoginScreen(String url);
        void showErrorRequestingFlickrLogin(String error);
        void showFlickrLoginSuccessful(String userId);
        void showErrorLoginFlickr(String error);
        void setLoading(boolean isLoading);

}
