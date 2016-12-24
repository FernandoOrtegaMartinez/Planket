package com.fomdeveloper.planket.data.repository;

import android.support.annotation.NonNull;


import com.fomdeveloper.planket.data.model.FlickrUser;
import com.fomdeveloper.planket.data.model.PhotoItem;
import com.fomdeveloper.planket.data.model.transportmodel.CommentsContainer;
import com.fomdeveloper.planket.data.model.transportmodel.FavesContainer;
import com.fomdeveloper.planket.data.model.transportmodel.PhotosContainer;
import com.fomdeveloper.planket.data.model.transportmodel.SearchPhotosResponse;
import com.fomdeveloper.planket.data.model.transportmodel.StatusResponse;
import com.fomdeveloper.planket.data.model.transportmodel.UserShowcase;

import java.util.ArrayList;


import rx.Observable;
import rx.Single;

/**
 * Created by Fernando on 31/05/16.
 */
public interface FlickrRepository {

    Single<PhotosContainer> getInterestingness(int page);

    Single<PhotosContainer> getPhotosWithTag(@NonNull String tag, int page);

    Single<PhotosContainer> getPhotosWithText(@NonNull String text, int page);

    Single<PhotosContainer> getPhotosForUser(@NonNull String userId, int page);

    Single<PhotosContainer> getFavesForUser(@NonNull String userId, int page);

    Single<UserShowcase> getShowcases(@NonNull String userId, int count);

    Single<FavesContainer> getFaves(@NonNull String photoId, int page);

    Single<CommentsContainer> getComments(@NonNull String photoId, int page);

    Single<FlickrUser> getUserInfo(@NonNull String userId);

    Single<StatusResponse> addPhotoFavorite(@NonNull String photoId);

    Single<StatusResponse> removePhotoFavorite(@NonNull String photoId);

    Observable<SearchPhotosResponse> getAllFaves(@NonNull String userId);

    boolean isPhotoFavorite(@NonNull String photoId);
    void saveUserFavPhotos(@NonNull String userId, @NonNull ArrayList<PhotoItem> photoItems);
    void loadUserFaves();
    void saveUserFavPhoto(@NonNull String photoId);
    void deleteUserFavPhoto(@NonNull String photoId);

}
