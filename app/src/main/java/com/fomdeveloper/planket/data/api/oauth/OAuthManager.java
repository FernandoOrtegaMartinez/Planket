package com.fomdeveloper.planket.data.api.oauth;


import rx.Single;

/**
 * Created by Fernando on 13/08/16.
 */
public interface OAuthManager {

    Single<String> getRequestToken();

    Single<String> getAccessToken(String oauthToken, String oauthVerifier);

}
