package com.fomdeveloper.planket.ui.presentation.profile;

import com.fomdeveloper.planket.data.model.FlickrUser;
import com.fomdeveloper.planket.data.model.PhotoItem;
import com.fomdeveloper.planket.data.model.transportmodel.UserShowcase;
import com.fomdeveloper.planket.data.repository.FlickrRepository;
import com.fomdeveloper.planket.ui.presentation.base.BasePresenter;

import java.util.ArrayList;

import rx.Scheduler;
import rx.SingleSubscriber;

/**
 * Created by Fernando on 20/08/16.
 */
public class ProfilePresenter extends BasePresenter<ProfileView> {

    private FlickrRepository flickrRepository;
    private Scheduler mainScheduler;
    private Scheduler ioScheduler;

    public ProfilePresenter(FlickrRepository flickrRepository, Scheduler mainScheduler, Scheduler ioScheduler) {
        this.flickrRepository = flickrRepository;
        this.mainScheduler = mainScheduler;
        this.ioScheduler = ioScheduler;
    }

    public void getUserInfo(String userId) {
        checkViewAttached();
        addSubscription(
                flickrRepository.getUserInfo(userId)
                .observeOn(mainScheduler)
                .subscribeOn(ioScheduler)
                .subscribe(new SingleSubscriber<FlickrUser>() {
                    @Override
                    public void onSuccess(FlickrUser flickrUser) {
                        if (flickrUser!=null){
                            getView().showUserDetails(flickrUser);
                        }
                    }

                    @Override
                    public void onError(Throwable error) {
                        // TODO: 21/08/16
                    }
                }));
    }

    public void getShowcases(final String userId, int count) {
        checkViewAttached();
        getView().showLoading(true);
        addSubscription(
                flickrRepository.getShowcases(userId, count)
                        .observeOn(mainScheduler)
                        .subscribeOn(ioScheduler)
                        .subscribe(new SingleSubscriber<UserShowcase>() {
                            @Override
                            public void onSuccess(UserShowcase userShowcase) {
                                // Photos
                                boolean isPhotosEmpty = true;
                                ArrayList<PhotoItem> userPhotos = userShowcase.getUserPhotosContainer().getPhotoItems();
                                if (userPhotos!=null && userPhotos.size()>0) {
                                    getView().showUserPhotos(userPhotos, userShowcase.getUserPhotosContainer().getTotal());
                                    isPhotosEmpty = false;
                                }
                                // Faves
                                boolean isFavesEmpty = true;
                                ArrayList<PhotoItem> userFaves = userShowcase.getUserFavesContainer().getPhotoItems();
                                if (userFaves!=null && userFaves.size()>0) {
                                    flickrRepository.saveUserFavPhotos(userId, userFaves);
                                    getView().showUserFaves(userFaves, userShowcase.getUserFavesContainer().getTotal());
                                    isFavesEmpty = false;
                                }

                                if (isPhotosEmpty && isFavesEmpty){
                                    getView().showNoItems();
                                }

                                getView().showLoading(false);
                            }

                            @Override
                            public void onError(Throwable error) {
                                getView().showLoading(false);
                                getView().showErrorScreen();
                            }
                        }));
    }

}
