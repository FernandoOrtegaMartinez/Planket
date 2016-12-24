package com.fomdeveloper.planket.data.model.transportmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Fernando on 25/06/16.
 */
public class CommentsResponse {

    @Expose
    @SerializedName("comments")
    private CommentsContainer commentsContainer;

    public CommentsContainer getCommentsContainer() {
        return commentsContainer;
    }
}
