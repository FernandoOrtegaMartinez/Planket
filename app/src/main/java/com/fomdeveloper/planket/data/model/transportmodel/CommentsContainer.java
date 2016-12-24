package com.fomdeveloper.planket.data.model.transportmodel;

import com.fomdeveloper.planket.data.model.Comment;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Fernando on 25/06/16.
 */
public class CommentsContainer {

    @Expose
    @SerializedName("photo_id")
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
    private int numComments;

    @Expose
    @SerializedName("comment")
    private ArrayList<Comment> comments;

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

    public int getNumComments() {
        return numComments;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }
}
