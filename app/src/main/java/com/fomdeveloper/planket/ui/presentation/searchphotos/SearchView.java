package com.fomdeveloper.planket.ui.presentation.searchphotos;

import com.fomdeveloper.planket.data.model.PhotoItem;
import com.fomdeveloper.planket.ui.presentation.base.MvpView;

import java.util.List;

/**
 * Created by Fernando on 02/06/16.
 */
public interface SearchView extends MvpView{

        void showPhotoItems(List<PhotoItem> photoItems);
        void showLoading(boolean visible);
        void showNoMorePagesToLoad();
        void showSearchError(boolean isLoadingMore);
        void showNoItems(boolean isLoadingMore);

}
