package com.fomdeveloper.planket.data.model.transportmodel;

import android.support.annotation.VisibleForTesting;

import com.fomdeveloper.planket.data.model.PhotoItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Fernando on 3/6/15.
 */
public class PhotosContainer {

    @Expose
    @SerializedName("page")
    private int page;

    @Expose
    @SerializedName("pages")
    private int pages;

    @Expose
    @SerializedName("perpage")
    private int perpage;

    @Expose
    @SerializedName("total")
    private int total;


    @Expose
    @SerializedName("photo")
    private ArrayList<PhotoItem> photoItems;


    public int getPage() {
        return page;
    }

    public int getTotalPages() {
        return pages;
    }

    public int getPerpage() {
        return perpage;
    }

    public int getTotal() {
        return total;
    }

    public ArrayList<PhotoItem> getPhotoItems() {
        return photoItems;
    }

    @VisibleForTesting
    public void setPhotoItems(ArrayList<PhotoItem> photoItems) {
        this.photoItems = photoItems;
    }
}
