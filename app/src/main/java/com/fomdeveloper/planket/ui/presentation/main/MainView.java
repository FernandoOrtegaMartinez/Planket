package com.fomdeveloper.planket.ui.presentation.main;


import com.fomdeveloper.planket.ui.presentation.base.MvpView;

/**
 * Created by Fernando on 31/05/16.
 */
public interface MainView extends MvpView {

        void updateUserViews();
        void updateUserProfilePic(String UserProfilePicUrl);

}
