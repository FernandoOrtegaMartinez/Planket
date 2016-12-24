package com.fomdeveloper.planket.data.model.transportmodel;

import com.fomdeveloper.planket.data.model.Fave;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Fernando on 25/06/16.
 */
public class FavesContainer {

    @Expose
    @SerializedName("id")
    private String photoId;

    @Expose
    private int page;

    @Expose
    @SerializedName("pages")
    private int totalPages;

    @Expose
    private int perpage;

    @Expose
    @SerializedName("total")
    private int numFaves;

    @Expose
    @SerializedName("person")
    private ArrayList<Fave> faves;

    public String getPhotoId() {
        return photoId;
    }

    public int getPage() {
        return page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getPerpage() {
        return perpage;
    }

    public int getNumFaves() {
        return numFaves;
    }

    public ArrayList<Fave> getFaves() {
        return faves;
    }
}
