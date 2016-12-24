package com.fomdeveloper.planket.ui.presentation.profile;

import com.fomdeveloper.planket.data.model.FlickrUser;
import com.fomdeveloper.planket.data.model.PhotoItem;
import com.fomdeveloper.planket.ui.presentation.base.MvpView;

import java.util.List;

/**
 * Created by Fernando on 20/08/16.
 */
public interface ProfileView extends MvpView{

        void showUserDetails(FlickrUser flickrUser);
        void showUserPhotos(List<PhotoItem> photoItems, int totalItems);
        void showUserFaves(List<PhotoItem> photoItems, int totalItems);
        void showErrorScreen();
        void showLoading(boolean visible);
        void showNoItems();

}
