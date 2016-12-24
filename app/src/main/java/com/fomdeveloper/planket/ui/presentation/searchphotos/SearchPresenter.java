package com.fomdeveloper.planket.ui.presentation.searchphotos;

import com.fomdeveloper.planket.data.PaginatedDataManager;
import com.fomdeveloper.planket.data.model.PhotoItem;
import com.fomdeveloper.planket.data.model.transportmodel.PhotosContainer;
import com.fomdeveloper.planket.data.repository.FlickrRepository;
import com.fomdeveloper.planket.ui.presentation.base.BasePresenter;

import java.util.ArrayList;

import rx.Scheduler;
import rx.SingleSubscriber;

/**
 * Created by Fernando on 02/06/16.
 */
public class SearchPresenter extends BasePresenter<SearchView>{

    private FlickrRepository flickrRepository;
    private PaginatedDataManager paginatedDataManager;
    private Scheduler mainScheduler;
    private Scheduler ioScheduler;

    public SearchPresenter(FlickrRepository flickrRepository, PaginatedDataManager paginatedDataManager, Scheduler mainScheduler, Scheduler ioScheduler) {
        this.flickrRepository = flickrRepository;
        this.paginatedDataManager = paginatedDataManager;
        this.mainScheduler = mainScheduler;
        this.ioScheduler = ioScheduler;
    }


    public void loadInterestingness() {
        checkViewAttached();

        if (paginatedDataManager.isLoadingFirstPage()) {
            getView().showLoading(true);
        }

        if(paginatedDataManager.areTotalPagesLoaded()){
            getView().showNoMorePagesToLoad();
            return;
        }

        int nextPage = paginatedDataManager.getNextPageToLoad();

        addSubscription(
                flickrRepository.getInterestingness(nextPage)
                .observeOn(mainScheduler)
                .subscribeOn(ioScheduler)
                .subscribe(new SingleSubscriber<PhotosContainer>() {
                    @Override
                    public void onSuccess(PhotosContainer photosContainer) {
                        paginatedDataManager.setTotalPages(photosContainer.getTotalPages());
                        ArrayList<PhotoItem> photoItems = photosContainer.getPhotoItems();
                        if (photoItems!=null && photoItems.size()>0) {
                            getView().showPhotoItems(photoItems);
                        }else{
                            getView().showNoItems(!paginatedDataManager.isLoadingFirstPage());
                        }
                        getView().showLoading(false);
                        paginatedDataManager.addPageLoaded();
                    }

                    @Override
                    public void onError(Throwable error) {
                        getView().showLoading(false);
                        getView().showSearchError(!paginatedDataManager.isLoadingFirstPage());
                    }
                }));

    }

    public void searchByTag(final String tag) {
        checkViewAttached();

        if (paginatedDataManager.isLoadingFirstPage()) {
            getView().showLoading(true);
        }

        if(paginatedDataManager.areTotalPagesLoaded()){
            getView().showNoMorePagesToLoad();
            return;
        }

        int nextPage = paginatedDataManager.getNextPageToLoad();

        addSubscription(
                flickrRepository.getPhotosWithTag(tag, nextPage)
                .observeOn(mainScheduler)
                .subscribeOn(ioScheduler)
                .subscribe(new SingleSubscriber<PhotosContainer>() {
                    @Override
                    public void onSuccess(PhotosContainer photosContainer) {
                        paginatedDataManager.setTotalPages(photosContainer.getTotalPages());
                        ArrayList<PhotoItem> photoItems = photosContainer.getPhotoItems();
                        if (photoItems!=null && photoItems.size()>0) {
                            getView().showPhotoItems(photoItems);
                        }else{
                            getView().showNoItems(!paginatedDataManager.isLoadingFirstPage());
                        }
                        getView().showLoading(false);
                        paginatedDataManager.addPageLoaded();
                    }

                    @Override
                    public void onError(Throwable error) {
                        getView().showLoading(false);
                        getView().showSearchError(!paginatedDataManager.isLoadingFirstPage() );
                    }
                }));
    }

    public void searchByText(final String text) {
        checkViewAttached();

        if (paginatedDataManager.isLoadingFirstPage()) {
            getView().showLoading(true);
        }

        if(paginatedDataManager.areTotalPagesLoaded()){
            getView().showNoMorePagesToLoad();
            return;
        }

        int nextPage = paginatedDataManager.getNextPageToLoad();

        addSubscription(
                flickrRepository.getPhotosWithText(text, nextPage)
                .observeOn(mainScheduler)
                .subscribeOn(ioScheduler)
                .subscribe(new SingleSubscriber<PhotosContainer>() {
                    @Override
                    public void onSuccess(PhotosContainer photosContainer) {
                        paginatedDataManager.setTotalPages(photosContainer.getTotalPages());
                        ArrayList<PhotoItem> photoItems = photosContainer.getPhotoItems();
                        if (photoItems!=null && photoItems.size()>0) {
                            getView().showPhotoItems(photoItems);
                        }else{
                            getView().showNoItems(!paginatedDataManager.isLoadingFirstPage());
                        }
                        getView().showLoading(false);
                        paginatedDataManager.addPageLoaded();
                    }

                    @Override
                    public void onError(Throwable error) {
                        getView().showLoading(false);
                        getView().showSearchError(!paginatedDataManager.isLoadingFirstPage() );
                    }
                }));
    }

    public void searchByUserId(String userId) {
        checkViewAttached();

        if (paginatedDataManager.isLoadingFirstPage()) {
            getView().showLoading(true);
        }

        if(paginatedDataManager.areTotalPagesLoaded()){
            getView().showNoMorePagesToLoad();
            return;
        }

        int nextPage = paginatedDataManager.getNextPageToLoad();

        addSubscription(
                flickrRepository.getPhotosForUser(userId, nextPage)
                .observeOn(mainScheduler)
                .subscribeOn(ioScheduler)
                .subscribe(new SingleSubscriber<PhotosContainer>() {
                    @Override
                    public void onSuccess(PhotosContainer photosContainer) {
                        paginatedDataManager.setTotalPages(photosContainer.getTotalPages());
                        ArrayList<PhotoItem> photoItems = photosContainer.getPhotoItems();
                        if (photoItems!=null && photoItems.size()>0) {
                            getView().showPhotoItems(photoItems);
                        }else{
                            getView().showNoItems(!paginatedDataManager.isLoadingFirstPage());
                        }
                        getView().showLoading(false);
                        paginatedDataManager.addPageLoaded();
                    }

                    @Override
                    public void onError(Throwable error) {
                        getView().showLoading(false);
                        getView().showSearchError(!paginatedDataManager.isLoadingFirstPage());
                    }
                }));
    }

    public void searchFavsByUserId(final String userId) {
        checkViewAttached();

        if (paginatedDataManager.isLoadingFirstPage()) {
            getView().showLoading(true);
        }

        if(paginatedDataManager.areTotalPagesLoaded()){
            getView().showNoMorePagesToLoad();
            return;
        }

        int nextPage = paginatedDataManager.getNextPageToLoad();

        addSubscription(
                flickrRepository.getFavesForUser(userId, nextPage)
                        .observeOn(mainScheduler)
                        .subscribeOn(ioScheduler)
                        .subscribe(new SingleSubscriber<PhotosContainer>() {
                            @Override
                            public void onSuccess(PhotosContainer photosContainer) {
                                flickrRepository.saveUserFavPhotos(userId, photosContainer.getPhotoItems());
                                paginatedDataManager.setTotalPages(photosContainer.getTotalPages());
                                ArrayList<PhotoItem> photoItems = photosContainer.getPhotoItems();
                                if (photoItems!=null && photoItems.size()>0) {
                                    getView().showPhotoItems(photoItems);
                                }else{
                                    getView().showNoItems(!paginatedDataManager.isLoadingFirstPage());
                                }
                                getView().showLoading(false);
                                paginatedDataManager.addPageLoaded();
                            }

                            @Override
                            public void onError(Throwable error) {
                                getView().showLoading(false);
                                getView().showSearchError(!paginatedDataManager.isLoadingFirstPage() );
                            }
                        }));

    }
}
