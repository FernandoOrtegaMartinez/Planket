package com.fomdeveloper.planket.data.model;



import com.fomdeveloper.planket.data.model.transportmodel.StringContent;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;


/**
 * Created by Fernando on 09/05/16.
 */
@Parcel
public class PhotoItem implements UserProfileInfo {

    @SerializedName("owner")
    String userId;

    @SerializedName("ownername")
    String realName;

    @SerializedName("id")
    String photoId;

    @SerializedName("title")
    String photoTitle;

    @SerializedName("url_s")
    String urlSmall;

    @SerializedName("url_z")
    String urlMedium;

    @SerializedName("count_faves")
    String numFaves;

    @SerializedName("count_comments")
    String numComments;

    StringContent description;

    String tags;
    String latitude;
    String longitude;

    String iconfarm;
    String iconserver;

    @Override
    public String getUserProfilePicUrl() {
        return String.format(userProfileUrl,iconfarm,iconserver,userId);
    }

    @Override
    public String getBestName() {
        if (this.realName==null) return "";
        return this.realName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRealname() {
        return realName;
    }

    public void setRealname(String realname) {
        this.realName = realname;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public String getPhotoTitle() {
        return photoTitle;
    }

    public void setPhotoTitle(String photoTitle) {
        this.photoTitle = photoTitle;
    }

    public String getUrlSmall() {
        return urlSmall;
    }

    public void setUrlSmall(String urlSmall) {
        this.urlSmall = urlSmall;
    }

    public String getUrlMedium() {
        return urlMedium;
    }

    public void setUrlMedium(String urlMedium) {
        this.urlMedium = urlMedium;
    }

    public String getNumFaves() {
        return numFaves;
    }

    public void setNumFaves(String numFaves) {
        this.numFaves = numFaves;
    }

    public String getNumComments() {
        return numComments;
    }

    public void setNumComments(String numComments) {
        this.numComments = numComments;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description.content;
    }

}
