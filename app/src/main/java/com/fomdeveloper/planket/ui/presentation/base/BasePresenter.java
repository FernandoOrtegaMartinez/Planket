package com.fomdeveloper.planket.ui.presentation.base;

import android.support.annotation.NonNull;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Fernando on 22/09/16.
 */
public class BasePresenter<T extends MvpView> implements MvpPresenter<T> {

    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    private T mvpView;

    @Override
    public void attachView(@NonNull T mvpView) {
        this.mvpView = mvpView;
    }

    @Override
    public void detachView() {
        compositeSubscription.unsubscribe();
        mvpView = null;
    }

    public boolean isViewAttached() {
        return mvpView != null;
    }

    public T getView() {
        return mvpView;
    }


    public void checkViewAttached() {
        if (!isViewAttached()) throw new RuntimeException("View wasn't attached to the presenter. Call presenter.attachView(MvpView)");
    }

    protected void addSubscription(Subscription subscription) {
        this.compositeSubscription.add(subscription);
    }

}
