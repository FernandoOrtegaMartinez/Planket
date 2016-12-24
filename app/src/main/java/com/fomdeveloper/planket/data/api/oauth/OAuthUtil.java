package com.fomdeveloper.planket.data.api.oauth;

/**
 * Created by Fernando on 20/08/16.
 */
public class OAuthUtil {

    public static String buildAuthenticationUrl(String requestToken) {
        return "https://www.flickr.com/services/oauth/authorize?oauth_token="+requestToken;
    }

}
