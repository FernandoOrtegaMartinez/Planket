package com.fomdeveloper.planket.data.model.transportmodel;

/**
 * Created by Fernando on 10/10/2016.
 */

public class UserShowcase {

    private PhotosContainer userPhotosContainer;
    private PhotosContainer userFavesContainer;

    public UserShowcase(PhotosContainer userPhotosContainer, PhotosContainer userFavesContainer) {
        this.userPhotosContainer = userPhotosContainer;
        this.userFavesContainer = userFavesContainer;
    }

    public PhotosContainer getUserPhotosContainer() {
        return userPhotosContainer;
    }

    public PhotosContainer getUserFavesContainer() {
        return userFavesContainer;
    }

}
