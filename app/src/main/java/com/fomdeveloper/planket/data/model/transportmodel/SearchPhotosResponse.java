package com.fomdeveloper.planket.data.model.transportmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Fernando on 09/05/16.
 */
public class SearchPhotosResponse {

    @Expose
    @SerializedName("photos")
    private PhotosContainer photosContainer;

    public PhotosContainer getPhotosContainer() {
        return photosContainer;
    }

}
