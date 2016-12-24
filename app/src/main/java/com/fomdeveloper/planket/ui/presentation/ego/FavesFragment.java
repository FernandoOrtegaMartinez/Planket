package com.fomdeveloper.planket.ui.presentation.ego;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.fomdeveloper.planket.PlanketApplication;
import com.fomdeveloper.planket.R;
import com.fomdeveloper.planket.data.model.Ego;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Fernando on 26/06/16.
 */
public class FavesFragment extends EgoFragment{

    private static final String ARG_PHOTO_ID = "ARG.PHOTO_ID";

    @Inject
    EgoPresenter presenter;

    private String photoId;

    public static FavesFragment newInstance(String photoId) {
        
        Bundle args = new Bundle();
        args.putString(ARG_PHOTO_ID,photoId);

        FavesFragment fragment = new FavesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((PlanketApplication)getActivity().getApplication()).getAppComponent().inject(this);
        photoId = getArguments().getString(ARG_PHOTO_ID);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        presenter.loadFaves(photoId);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.detachView();
    }

    @Override
    public void showEgoItems(List<? extends Ego> egoItems) {
        egoAdapter.addEgo(egoItems);
    }

    @Override
    protected void loadMore() {
        presenter.loadFaves(photoId);
    }

    @Override
    public void showNoEgo() {
        noItems.setText(R.string.no_faves);
        noItems.setVisibility(View.VISIBLE);
    }
}
