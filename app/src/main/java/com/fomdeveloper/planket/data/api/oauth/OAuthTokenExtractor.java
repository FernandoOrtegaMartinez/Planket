package com.fomdeveloper.planket.data.api.oauth;

import com.google.gdata.util.common.base.Preconditions;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Fernando on 13/08/16.
 */
public class OAuthTokenExtractor{

    private static final String CHARSET = "UTF-8";

    private static final String OAUTH_TOKEN_REGEXP = "oauth_token=([^&]+)";
    private static final String OAUTH_TOKEN_SECRET_REGEXP = "oauth_token_secret=([^&]*)";
    private static final String OAUTH_FULLNAME_REGEXP = "fullname=([^&]*)";
    private static final String OAUTH_USER_NAME_REGEXP = "username=([^&]*)";
    private static final String OAUTH_USER_ID_REGEXP = "user_nsid=([^&]*)";

    public static OAuthToken extractRequestToken(String response) {

        if (response.isEmpty()){
            throw new IllegalArgumentException("Response body is incorrect. Can't extract a token from an empty string");
        }

        final String token = extract(response, Pattern.compile(OAUTH_TOKEN_REGEXP));
        final String secret = extract(response, Pattern.compile(OAUTH_TOKEN_SECRET_REGEXP));

        return new OAuthToken.Builder(token, secret).build();
    }

    public static OAuthToken extractAccessToken(String response) {

        if (response.isEmpty()){
            throw new IllegalArgumentException("Response body is incorrect. Can't extract a token from an empty string");
        }

        final String token = extract(response, Pattern.compile(OAUTH_TOKEN_REGEXP));
        final String secret = extract(response, Pattern.compile(OAUTH_TOKEN_SECRET_REGEXP));
        final String fullName = extract(response, Pattern.compile(OAUTH_FULLNAME_REGEXP));
        final String userName = extract(response, Pattern.compile(OAUTH_USER_NAME_REGEXP));
        final String userId = extract(response, Pattern.compile(OAUTH_USER_ID_REGEXP));

        return new OAuthToken.Builder(token, secret).fullName(fullName).userName(userName).userId(userId).build();
    }

    private static String extract(String response, Pattern p) {
        final Matcher matcher = p.matcher(response);
        if (matcher.find() && matcher.groupCount() >= 1) {
            return decode(matcher.group(1));
        } else {
            throw new RuntimeException("Response body is incorrect. Can't extract token and secret from this: '"
                    + response + "'", null);
        }
    }

    private static String decode(String encoded) {
        Preconditions.checkNotNull(encoded, "Cannot decode null object");
        try {
            return URLDecoder.decode(encoded, CHARSET);
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Charset not found while decoding string: " + CHARSET, uee);
        }
    }

}
