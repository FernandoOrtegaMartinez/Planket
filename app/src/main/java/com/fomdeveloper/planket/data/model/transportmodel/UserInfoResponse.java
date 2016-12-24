package com.fomdeveloper.planket.data.model.transportmodel;

import com.fomdeveloper.planket.data.model.FlickrUser;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Fernando on 20/08/16.
 */
public class UserInfoResponse {

    @SerializedName("person")
    private FlickrUser flickrUser;

    public FlickrUser getFlickrUser() {
        return flickrUser;
    }
}
