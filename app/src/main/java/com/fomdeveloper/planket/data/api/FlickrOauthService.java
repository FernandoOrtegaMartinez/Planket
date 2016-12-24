package com.fomdeveloper.planket.data.api;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Single;

/**
 * Created by Fernando on 09/05/16.
 * Flickr Auth API (a different endpoint)
 */
public interface FlickrOauthService {

    String ENDPOINT = "https://www.flickr.com/services/";

    @POST("oauth/request_token")
    Single<ResponseBody> getRequestToken(@Query("oauth_callback") String oauthCallback);

    @GET("oauth/access_token")
    Single<ResponseBody> getAccessToken(@Query("oauth_token")String oauthToken, @Query("oauth_verifier")String oauthVerifier);

}
