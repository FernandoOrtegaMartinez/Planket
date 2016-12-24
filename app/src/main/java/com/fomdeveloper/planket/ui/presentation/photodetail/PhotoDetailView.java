package com.fomdeveloper.planket.ui.presentation.photodetail;


import com.fomdeveloper.planket.ui.presentation.base.MvpView;

/**
 * Created by Fernando on 25/06/16.
 */
public interface PhotoDetailView extends MvpView{

        void showPhotoFavoriteSuccessful();
        void showPhotoFavoriteError(String errorMessage);
        void showRemoveFavoriteSuccessful();
        void showRemoveFavoriteError(String errorMessage);
        void setFavButton(boolean isFavorite);

}
