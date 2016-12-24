package com.fomdeveloper.planket.ui.presentation.ego;


import com.fomdeveloper.planket.data.PaginatedDataManager;
import com.fomdeveloper.planket.data.model.Comment;
import com.fomdeveloper.planket.data.model.Fave;
import com.fomdeveloper.planket.data.model.transportmodel.CommentsContainer;
import com.fomdeveloper.planket.data.model.transportmodel.FavesContainer;
import com.fomdeveloper.planket.data.repository.FlickrRepository;
import com.fomdeveloper.planket.ui.presentation.base.BasePresenter;

import java.util.ArrayList;

import rx.Scheduler;
import rx.SingleSubscriber;

/**
 * Created by Fernando on 30/06/16.
 */
public class EgoPresenter extends BasePresenter<EgoView> {

    private FlickrRepository flickrRepository;
    private PaginatedDataManager paginatedDataManager;
    private Scheduler mainScheduler;
    private Scheduler ioScheduler;

    public EgoPresenter(FlickrRepository flickrRepository, PaginatedDataManager paginatedDataManager, Scheduler mainScheduler, Scheduler ioScheduler) {
        this.flickrRepository = flickrRepository;
        this.paginatedDataManager = paginatedDataManager;
        this.mainScheduler = mainScheduler;
        this.ioScheduler = ioScheduler;
    }

    public void loadComments(String photoId) {
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
                flickrRepository.getComments(photoId, nextPage)
                .observeOn(mainScheduler)
                .subscribeOn(ioScheduler)
                .subscribe(new SingleSubscriber<CommentsContainer>() {
                    @Override
                    public void onSuccess(CommentsContainer commentsContainer) {
                        paginatedDataManager.setTotalPages(commentsContainer.getTotalPages());
                        getView().showLoading(false);
                        paginatedDataManager.addPageLoaded();
                        ArrayList<Comment> comments = commentsContainer.getComments();
                        if (comments!=null && comments.size()>0) {
                            getView().showEgo(comments);
                        }else{
                            getView().showNoEgo();
                        }
                    }

                    @Override
                    public void onError(Throwable error) {
                        getView().showLoading(false);
                        getView().showErrorScreen(!paginatedDataManager.isLoadingFirstPage() );
                    }
                }));
    }

    public void loadFaves(String photoId) {
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
                flickrRepository.getFaves(photoId, nextPage)
                .observeOn(mainScheduler)
                .subscribeOn(ioScheduler)
                .subscribe(new SingleSubscriber<FavesContainer>() {
                    @Override
                    public void onSuccess(FavesContainer favesContainer) {
                        paginatedDataManager.setTotalPages(favesContainer.getTotalPages());
                        getView().showLoading(false);
                        paginatedDataManager.addPageLoaded();
                        ArrayList<Fave> faves = favesContainer.getFaves();
                        if (faves!=null && faves.size()>0) {
                            getView().showEgo(faves);
                        }else{
                            getView().showNoEgo();
                        }
                    }

                    @Override
                    public void onError(Throwable error) {
                        getView().showLoading(false);
                        getView().showErrorScreen(!paginatedDataManager.isLoadingFirstPage() );
                    }
                }));
    }
}
