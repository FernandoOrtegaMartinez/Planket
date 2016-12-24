package com.fomdeveloper.planket.ui.presentation.ego;

import com.fomdeveloper.planket.data.model.Ego;
import com.fomdeveloper.planket.ui.presentation.base.MvpView;

import java.util.List;

/**
 * Created by Fernando on 30/06/16.
 */
public interface EgoView extends MvpView{

        void showEgo(List<? extends Ego> egoItems);
        void showLoading(boolean visible);
        void showNoMorePagesToLoad();
        void showErrorScreen(boolean isLoadingMore);
        void showNoEgo();


}
