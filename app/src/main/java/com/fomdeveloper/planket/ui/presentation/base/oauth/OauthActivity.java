package com.fomdeveloper.planket.ui.presentation.base.oauth;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;

import com.fomdeveloper.planket.R;

/**
 * Created by Fernando on 20/08/16.
 */
public abstract class OauthActivity extends AppCompatActivity implements OauthView {

    private final String OAUTH_TOKEN_PARAM = "oauth_token";
    private final String OAUTH_VERIFIER_PARAM = "oauth_verifier";

    protected abstract void showLoginSuccessful(String userId);
    protected abstract void showLoginError(String errorMessage);
    protected abstract OauthPresenter getOauthPresenter();

    @Override
    public void showErrorRequestingFlickrLogin(String error) {
        showLoginError(error);
    }

    @Override
    public void openFlickrLoginScreen(String url) {
        Uri uri = Uri.parse(url);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(browserIntent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        final String myScheme = intent.getScheme();
        if (myScheme != null && myScheme.equals(getString(R.string.callback_scheme))) {
            logUser(intent.getDataString());
        }
    }

    private void logUser(String url) {
        Uri uri = Uri.parse(url);
        String oauthToken = uri.getQueryParameter(OAUTH_TOKEN_PARAM);
        String oauthVerifier = uri.getQueryParameter(OAUTH_VERIFIER_PARAM);
        getOauthPresenter().loginFlickr(oauthToken,oauthVerifier);
    }

    @Override
    public void showFlickrLoginSuccessful(String userId) {
        showLoginSuccessful(userId);
    }

    @Override
    public void showErrorLoginFlickr(String error) {
        showLoginError(error);
    }

}
