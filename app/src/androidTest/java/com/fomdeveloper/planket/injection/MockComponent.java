package com.fomdeveloper.planket.injection;

import com.fomdeveloper.planket.MainActivityTest;
import com.fomdeveloper.planket.SearchActivityTest;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Fernando on 24/12/2016.
 */
@Singleton
@Component(modules = {MockAppModule.class})
public interface MockComponent extends AppComponent {
    void inject(MainActivityTest mainActivityTest);
    void inject(SearchActivityTest searchActivityTest);
}
