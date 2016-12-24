package com.fomdeveloper.planket.data.model;


import com.fomdeveloper.planket.data.model.transportmodel.StringContent;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Fernando on 20/08/16.
 */
public class FlickrUser implements UserProfileInfo {

    @SerializedName("nsid")
    private String userId;

    @SerializedName("iconserver")
    private String iconserver;

    @SerializedName("iconfarm")
    private Integer iconfarm;

    @SerializedName("username")
    private StringContent userName = new StringContent();

    @SerializedName("realname")
    private StringContent realName = new StringContent();

    @SerializedName("location")
    private StringContent location = new StringContent();

    @SerializedName("description")
    private StringContent description = new StringContent();

    @SerializedName("mobileurl")
    private StringContent mobileurl = new StringContent();

    @SerializedName("photos")
    private PhotosCount photosCount = new PhotosCount();

    private class IntContent {

        @SerializedName("_content")
        public int content;

    }

    private class PhotosCount {

        @SerializedName("count")
        public IntContent count = new IntContent();

    }

    @Override
    public String getUserProfilePicUrl() {
        return String.format(userProfileUrl,iconfarm,iconserver,userId);
    }

    @Override
    public String getBestName() {
        if (this.getRealname()==null || this.getRealname().isEmpty()){
            return getUsername();
        }
        return getRealname();
    }

    public String getUserId() {
        return userId;
    }

    public String getIconserver() {
        return iconserver;
    }

    public Integer getIconfarm() {
        return iconfarm;
    }

    public String getUsername() {
        return userName.content;
    }

    public String getRealname() {
            return realName.content;
    }

    public String getLocation() {
            return location.content;
    }

    public String getDescription() {
        return description.content;
    }

    public String getMobileurl() {
        return mobileurl.content;
    }

    public int getPhotosCount() {
        return photosCount.count.content;
    }

    @Override
    public String toString() {
        return "FlickrUser{" +
                "userId='" + userId + '\'' +
                ", iconserver='" + iconserver + '\'' +
                ", iconfarm=" + iconfarm +
                ", username=" + userName.content +
                ", realname=" + realName.content +
                ", location=" + location.content +
                ", description=" + description.content +
                ", mobileurl=" + mobileurl.content +
                ", photosCount=" + photosCount.count.content +
                '}';
    }
}
