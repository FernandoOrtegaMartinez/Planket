package com.fomdeveloper.planket.ui.presentation.main;


import android.support.annotation.NonNull;

import com.fomdeveloper.planket.bus.RxEventBus;
import com.fomdeveloper.planket.bus.events.FlickrUserLoggedInEvent;
import com.fomdeveloper.planket.data.model.FlickrUser;
import com.fomdeveloper.planket.data.prefs.PlanketBoxPreferences;
import com.fomdeveloper.planket.data.repository.FlickrRepository;
import com.fomdeveloper.planket.ui.presentation.base.BasePresenter;



import rx.Scheduler;
import rx.SingleSubscriber;
import rx.functions.Action1;
import timber.log.Timber;

/**
 * Created by Fernando on 31/05/16.
 */
public class MainPresenter extends BasePresenter<MainView> {

    private FlickrRepository flickrRepository;
    private PlanketBoxPreferences planketBoxPreferences;
    private RxEventBus rxEventBus;
    private Scheduler mainScheduler;
    private Scheduler ioScheduler;

    public MainPresenter(FlickrRepository flickrRepository, PlanketBoxPreferences planketBoxPreferences, RxEventBus rxEventBus, Scheduler mainScheduler, Scheduler ioScheduler) {
        this.flickrRepository = flickrRepository;
        this.planketBoxPreferences = planketBoxPreferences;
        this.rxEventBus = rxEventBus;
        this.mainScheduler = mainScheduler;
        this.ioScheduler = ioScheduler;
    }

    @Override
    public void attachView(@NonNull MainView mvpView) {
        super.attachView(mvpView);
        addSubscription(rxEventBus.filteredObservable(FlickrUserLoggedInEvent.class)
                .observeOn(mainScheduler)
                .subscribe(new Action1<FlickrUserLoggedInEvent>() {
                    @Override
                    public void call(FlickrUserLoggedInEvent event) {

                                getView().updateUserViews();
                                getUserInfo(event.getUserId());
                                loadUserFavesFromDB();

                    }
                }));
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
                        if (flickrUser!=null) {
                            String userProfilePicUrl = flickrUser.getUserProfilePicUrl();
                            planketBoxPreferences.putUserProfilePicUrl(userProfilePicUrl);
                            getView().updateUserProfilePic(userProfilePicUrl);
                        }
                    }

                    @Override
                    public void onError(Throwable error) {
                        Timber.e(error.getLocalizedMessage());
                    }
                }));
    }

    public void loadUserFavesFromDB() {
        if (planketBoxPreferences.isUserLoggedIn()) {
            flickrRepository.loadUserFaves();
        }
    }

}
