package com.fomdeveloper.planket.data;

import android.content.Context;

import com.fomdeveloper.planket.data.model.transportmodel.PhotosContainer;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import rx.Single;

/**
 * Created by Fernando on 24/12/2016.
 */

public class ResponseHelper {

    public static Single<PhotosContainer> interestingnessResponse(String jsonResponse){
        PhotosContainer photosContainer = new Gson().fromJson(jsonResponse, PhotosContainer.class);
        return Single.just(photosContainer);
    }

    public static String getStringFromFile(Context context, String filePath) throws Exception {
        final InputStream stream = context.getResources().getAssets().open(filePath);
        String result = streamToString(stream);
        stream.close();
        return result;
    }

    private static String streamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

}
