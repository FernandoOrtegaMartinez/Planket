package com.fomdeveloper.planket.data.model.transportmodel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Fernando on 21/08/16.
 */
public class StatusResponse {

    public static final String STATE_OK = "ok";
    public static final String STATE_FAIL = "fail";

    public static final int ERROR_CODE_PHOTO_NOT_FAV = 1;
    public static final int ERROR_CODE_ALREADY_FAV = 3;
    public static final int ERROR_CODE_NO_PERMISSIONS = 99;

    @SerializedName("stat")
    private String status;

    @SerializedName("code")
    private Integer code;

    @SerializedName("message")
    private String message;

    public String getStatus() {
        return status;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
