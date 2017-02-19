package com.fomdeveloper.planket.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Fernando on 25/06/16.
 */
public class Comment extends Ego {

    @SerializedName("id")
    private String commentId;

    @SerializedName("author")
    private String userId;

    @SerializedName("authorname")
    private String userName;

    @SerializedName("realname")
    private String realName;

    @SerializedName("_content")
    private String comment;

    @SerializedName("datecreate")
    private String dateCreated;

    @SerializedName("iconserver")
    private String iconserver;

    @SerializedName("iconfarm")
    private String iconfarm;

    @Override
    protected String getProfilePicUrl() {
        return String.format(userProfileUrl,iconfarm,iconserver,userId);
    }

    @Override
    public String getBestName() {
        if (this.realName==null || this.realName.isEmpty()){
            return this.userName;
        }
        return this.realName;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

}
