package com.fomdeveloper.planket.data.model.transportmodel;


import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Fernando on 21/08/16.
 */
@Parcel
public class StringContent{

    @SerializedName("_content")
    public String content;
}