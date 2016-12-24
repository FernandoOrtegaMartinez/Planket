package com.fomdeveloper.planket.ui.presentation.base.oauth;

import com.fomdeveloper.planket.data.api.oauth.OAuthManager;
import com.fomdeveloper.planket.data.api.oauth.OAuthUtil;
import com.fomdeveloper.planket.ui.presentation.base.BasePresenter;

import rx.Scheduler;
import rx.SingleSubscriber;

/**
 * Created by Fernando on 22/09/16.
 */
public class OauthPresenter<T extends OauthView> extends BasePresenter<T> {

    private OAuthManager oAuthManager;
    private Scheduler mainScheduler;
    private Scheduler ioScheduler;

    public OauthPresenter(OAuthManager oAuthManager, Scheduler mainScheduler, Scheduler ioScheduler) {
        this.oAuthManager = oAuthManager;
        this.mainScheduler = mainScheduler;
        this.ioScheduler = ioScheduler;
    }

    public void requestFlickrLogin() {
        checkViewAttached();
        getView().setLoading(true);
        addSubscription(
                oAuthManager.getRequestToken()
                .observeOn(mainScheduler)
                .subscribeOn(ioScheduler)
                .subscribe(new SingleSubscriber<String>() {
                    @Override
                    public void onSuccess(String requestToken) {
                        String authUrl = OAuthUtil.buildAuthenticationUrl(requestToken);
                        getView().openFlickrLoginScreen(authUrl);
                    }

                    @Override
                    public void onError(Throwable error) {
                        getView().setLoading(false);
                        getView().showErrorRequestingFlickrLogin(error.getLocalizedMessage());
                    }
                }));
    }

    public void loginFlickr(String oauthToken, String oauthVerifier) {
        checkViewAttached();
        getView().setLoading(true);
        addSubscription(oAuthManager.getAccessToken(oauthToken,oauthVerifier).observeOn(mainScheduler)
                .subscribeOn(ioScheduler).subscribe(new SingleSubscriber<String>() {
                    @Override
                    public void onSuccess(String userId) {
                        getView().setLoading(false);
                        getView().showFlickrLoginSuccessful(userId);
                    }

                    @Override
                    public void onError(Throwable error) {
                        getView().setLoading(false);
                        getView().showErrorLoginFlickr(error.getLocalizedMessage());
                    }
                }));
    }
}
