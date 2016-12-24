package com.fomdeveloper.planket.data.api.oauth;


import android.content.Context;

import com.fomdeveloper.planket.R;
import com.fomdeveloper.planket.data.api.FlickrOauthService;
import com.fomdeveloper.planket.data.prefs.PlanketBoxPreferences;

import java.io.IOException;

import okhttp3.ResponseBody;
import rx.Single;
import rx.exceptions.Exceptions;
import rx.functions.Func1;
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer;
import timber.log.Timber;

/**
 * Created by Fernando on 13/08/16.
 */
public class OAuthManagerImpl implements OAuthManager{

    private FlickrOauthService flickrOauthService;
    private OkHttpOAuthConsumer okHttpOAuthConsumer;
    private OAuthToken requestToken;
    private PlanketBoxPreferences planketBoxPreferences;
    private String callbackScheme;

    public OAuthManagerImpl(FlickrOauthService flickrOauthService, OkHttpOAuthConsumer okHttpOAuthConsumer, PlanketBoxPreferences planketBoxPreferences, Context context) {
        this.flickrOauthService = flickrOauthService;
        this.okHttpOAuthConsumer = okHttpOAuthConsumer;
        this.planketBoxPreferences = planketBoxPreferences;
        this.callbackScheme = context.getString(R.string.callback_scheme).concat("://oauth?");

    }

    @Override
    public Single<String> getRequestToken() {
        clearOauthSignature();
        return flickrOauthService.getRequestToken(callbackScheme).map(new Func1<ResponseBody, String>() {
            @Override
            public String call(ResponseBody response) {
                try {
                    String responseBody = response.string();
                    Timber.d(responseBody);
                    requestToken = OAuthTokenExtractor.extractRequestToken(responseBody);
                    return requestToken.getToken();
                } catch (IOException e) {
                    Timber.e(e.getLocalizedMessage());
                    throw Exceptions.propagate(e);
                }
            }
        });
    }

    @Override
    public Single<String> getAccessToken(String oauthToken, String oauthVerifier) {
        okHttpOAuthConsumer.setTokenWithSecret(requestToken.getToken(),requestToken.getTokenSecret());
        return flickrOauthService.getAccessToken(oauthToken,oauthVerifier).map(new Func1<ResponseBody, String>() {
            @Override
            public String call(ResponseBody response) {
                try {
                    String responseBody = response.string();
                    Timber.d(responseBody);
                    final OAuthToken accessToken = OAuthTokenExtractor.extractAccessToken(responseBody);
                    planketBoxPreferences.putAccessToken(accessToken);
                    okHttpOAuthConsumer.setTokenWithSecret(accessToken.getToken(),accessToken.getTokenSecret());
                    return accessToken.getUserId();
                } catch (IOException e) {
                    Timber.e(e.getLocalizedMessage());
                    throw Exceptions.propagate(e);
                }
            }
        });
    }

    private void clearOauthSignature(){
        okHttpOAuthConsumer.setTokenWithSecret("","");
    }
}
