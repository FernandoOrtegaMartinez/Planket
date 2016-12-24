package com.fomdeveloper.planket.injection;

import com.fomdeveloper.planket.ui.presentation.login.FlickrLoginActivity;
import com.fomdeveloper.planket.ui.presentation.ego.CommentsFragment;
import com.fomdeveloper.planket.ui.presentation.ego.EgoActivity;
import com.fomdeveloper.planket.ui.presentation.ego.EgoFragment;
import com.fomdeveloper.planket.ui.presentation.ego.FavesFragment;
import com.fomdeveloper.planket.ui.presentation.main.MainActivity;
import com.fomdeveloper.planket.ui.presentation.profile.ProfileActivity;
import com.fomdeveloper.planket.ui.presentation.searchphotos.SearchFragment;
import com.fomdeveloper.planket.ui.presentation.photodetail.PhotoDetailFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Fernando on 09/05/16.
 */

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(SearchFragment searchFragment);
    void inject(PhotoDetailFragment photoDetailFragment);
    void inject(CommentsFragment commentsFragment);
    void inject(FavesFragment favesFragment);
    void inject(EgoActivity egoActivity);
    void inject(EgoFragment egoFragment);
    void inject(MainActivity mainActivity);
    void inject(ProfileActivity profileActivity);
    void inject(FlickrLoginActivity flickrLoginActivity);
}
