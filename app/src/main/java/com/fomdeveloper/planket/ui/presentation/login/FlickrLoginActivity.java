package com.fomdeveloper.planket.ui.presentation.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fomdeveloper.planket.BuildConfig;
import com.fomdeveloper.planket.PlanketApplication;
import com.fomdeveloper.planket.NetworkManager;
import com.fomdeveloper.planket.R;
import com.fomdeveloper.planket.bus.RxEventBus;
import com.fomdeveloper.planket.bus.events.FlickrUserLoggedInEvent;
import com.fomdeveloper.planket.ui.presentation.base.oauth.OauthActivity;
import com.fomdeveloper.planket.ui.presentation.base.oauth.OauthPresenter;
import com.fomdeveloper.planket.ui.presentation.base.oauth.OauthView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Fernando on 23/09/16.
 */
public class FlickrLoginActivity extends OauthActivity implements OauthView{

    @Inject
    OauthPresenter presenter;
    @Inject
    RxEventBus rxEventBus;
    @Inject
    NetworkManager networkManager;

    @BindView(R.id.login_button)
    Button loginButton;
    @BindView(R.id.error_textview)
    TextView errorTextView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private boolean isLoading;

    public static void start(Context context) {
        Intent starter = new Intent(context, FlickrLoginActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((PlanketApplication)getApplication()).getAppComponent().inject(this);
        setContentView(R.layout.activity_flickr_login);
        ButterKnife.bind(this);
        presenter.attachView(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isLoading){
            setLoading(false);
        }
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    protected OauthPresenter getOauthPresenter() {
        return presenter;
    }

    @Override
    protected void showLoginSuccessful(String userId) {
        Toast.makeText(this, R.string.login_success_message,Toast.LENGTH_LONG).show();
        rxEventBus.post(new FlickrUserLoggedInEvent(userId));
        finish();
    }

    @Override
    protected void showLoginError(String errorMessage) {
        if (BuildConfig.DEBUG){
            errorTextView.setText(errorMessage);
        }else {
            errorTextView.setText(R.string.error_login);
        }
        errorTextView.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.login_button)
    public void onLoginClick(){
        if (networkManager.isConnectedToInternet()) {
            presenter.requestFlickrLogin();
        }else{
            showLoginError(getString(R.string.no_internet));
        }
    }

    // called from the layout
    public void dismiss(View view) {
        finish();
    }

    @Override
    public void setLoading(boolean isLoading){
        this.isLoading = isLoading;
        if (isLoading){
            loginButton.setEnabled(false);
            errorTextView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }else{
            loginButton.setEnabled(true);
            progressBar.setVisibility(View.GONE);
        }
    }

}
