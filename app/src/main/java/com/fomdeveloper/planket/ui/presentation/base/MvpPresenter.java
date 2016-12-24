package com.fomdeveloper.planket.ui.presentation.base;

/**
 * Created by Fernando on 22/09/16.
 */
public interface MvpPresenter<V extends MvpView>{

    void attachView(V mvpView);

    void detachView();

}
