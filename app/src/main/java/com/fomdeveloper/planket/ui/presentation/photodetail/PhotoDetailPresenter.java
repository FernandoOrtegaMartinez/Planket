package com.fomdeveloper.planket.ui.presentation.photodetail;

import com.fomdeveloper.planket.data.model.transportmodel.StatusResponse;
import com.fomdeveloper.planket.data.repository.FlickrRepository;
import com.fomdeveloper.planket.ui.presentation.base.BasePresenter;

import rx.Scheduler;
import rx.SingleSubscriber;


/**
 * Created by Fernando on 25/06/16.
 */
public class PhotoDetailPresenter extends BasePresenter<PhotoDetailView> {

    private FlickrRepository flickrRepository;
    private Scheduler mainScheduler;
    private Scheduler ioScheduler;

    public PhotoDetailPresenter(FlickrRepository flickrRepository, Scheduler mainScheduler, Scheduler ioScheduler) {
        this.flickrRepository = flickrRepository;
        this.mainScheduler = mainScheduler;
        this.ioScheduler = ioScheduler;
    }

    public void setPhotoFavorite(final String photoId) {
        checkViewAttached();
        addSubscription(
                flickrRepository.addPhotoFavorite(photoId)
                .observeOn(mainScheduler)
                .subscribeOn(ioScheduler)
                .subscribe(new SingleSubscriber<StatusResponse>() {
                    @Override
                    public void onSuccess(StatusResponse statusResponse) {
                        if (statusResponse.getStatus().equalsIgnoreCase(StatusResponse.STATE_OK) || statusResponse.getCode() == StatusResponse.ERROR_CODE_ALREADY_FAV) {
                            flickrRepository.saveUserFavPhoto(photoId);
                            getView().showPhotoFavoriteSuccessful();
                        }else if (statusResponse.getCode() == StatusResponse.ERROR_CODE_NO_PERMISSIONS){
                            getView().showPhotoFavoriteError("Please log in to Flickr first.");
                        }else{
                            getView().showPhotoFavoriteError("There was an unknown error.");
                        }
                    }

                    @Override
                    public void onError(Throwable error) {
                        getView().showPhotoFavoriteError(error.getLocalizedMessage());
                    }
                }));
    }

    public void removePhotoFavorite(final String photoId) {
        checkViewAttached();
        addSubscription(
                flickrRepository.removePhotoFavorite(photoId)
                .observeOn(mainScheduler)
                .subscribeOn(ioScheduler)
                .subscribe(new SingleSubscriber<StatusResponse>() {
                    @Override
                    public void onSuccess(StatusResponse statusResponse) {
                        if (statusResponse.getStatus().equalsIgnoreCase(StatusResponse.STATE_OK) || statusResponse.getCode() == StatusResponse.ERROR_CODE_PHOTO_NOT_FAV) {
                            flickrRepository.deleteUserFavPhoto(photoId);
                            getView().showRemoveFavoriteSuccessful();
                        }else if (statusResponse.getCode() == StatusResponse.ERROR_CODE_NO_PERMISSIONS){
                            getView().showRemoveFavoriteError("Please log in to Flickr first.");
                        }else{
                            getView().showRemoveFavoriteError("There was an unknown error.");
                        }
                    }

                    @Override
                    public void onError(Throwable error) {
                        getView().showRemoveFavoriteError(error.getLocalizedMessage());
                    }
                }));
    }

    public void checkIfThisPhotoIsFav(String photoId) {
        checkViewAttached();
        getView().setFavButton(flickrRepository.isPhotoFavorite(photoId));
    }
}
