package com.fomdeveloper.planket.data.model.transportmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Fernando on 25/06/16.
 */
public class FavesResponse {

    @Expose
    @SerializedName("photo")
    private FavesContainer favesContainer;

    public FavesContainer getFavesContainer() {
        return favesContainer;
    }
}
