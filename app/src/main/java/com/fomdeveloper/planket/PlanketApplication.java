package com.fomdeveloper.planket;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.fomdeveloper.planket.injection.AppComponent;
import com.fomdeveloper.planket.injection.AppModule;
import com.fomdeveloper.planket.injection.DaggerAppComponent;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

/**
 * Created by Fernando on 09/05/16.
 */
public class PlanketApplication extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        Crashlytics crashlyticsKit = new Crashlytics.Builder()
                .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                .build();

        Fabric.with(this, crashlyticsKit);

        if (BuildConfig.DEBUG){
            Timber.plant(new Timber.DebugTree());
        }

        appComponent = createComponent();

    }

    public AppComponent createComponent() {
        return DaggerAppComponent.builder().appModule(new AppModule(this)).build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

}
