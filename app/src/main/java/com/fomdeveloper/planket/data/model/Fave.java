package com.fomdeveloper.planket.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Fernando on 25/06/16.
 */
public class Fave extends Ego{

    @SerializedName("nsid")
    private String userId;

    @SerializedName("username")
    private String userName;

    @SerializedName("realname")
    private String realName;

    @SerializedName("favedate")
    private String favedate;

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

    public String getFavedate() {
        return favedate;
    }

    public void setFavedate(String favedate) {
        this.favedate = favedate;
    }

}
