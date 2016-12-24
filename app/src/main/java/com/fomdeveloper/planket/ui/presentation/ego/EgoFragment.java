package com.fomdeveloper.planket.ui.presentation.ego;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fomdeveloper.planket.PlanketApplication;
import com.fomdeveloper.planket.R;
import com.fomdeveloper.planket.data.model.Ego;
import com.fomdeveloper.planket.ui.utils.SnackbarFactory;
import com.fomdeveloper.planket.ui.view.EndlessRecyclerViewScrollListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Fernando on 30/06/16.
 */
public abstract class EgoFragment extends Fragment implements EgoView{

    public static final int PER_PAGE_MAX_EGO = 50;

    @Inject Picasso picasso;

    @BindView(R.id.coordinator_layout)
    protected CoordinatorLayout coordinatorLayout;
    @BindView(R.id.recycler_view)
    protected RecyclerView recyclerView;
    @BindView(R.id.progress_bar)
    protected ProgressBar progressBar;
    @BindView(R.id.error_view)
    protected View errorView;
    @BindView(R.id.no_items_view)
    protected TextView noItems;

    protected EgoAdapter egoAdapter;

    protected abstract void showEgoItems(List<? extends Ego> egoItems);
    protected abstract void loadMore();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((PlanketApplication)getActivity().getApplication()).getAppComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ego,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager,PER_PAGE_MAX_EGO) {
            @Override
            public void onLoadMore() {
                loadMore();
            }
        });
        egoAdapter = new EgoAdapter(getActivity(),picasso);
        recyclerView.setAdapter(egoAdapter);

    }

    @Override
    public void showEgo(List<? extends Ego> egoItems) {
        recyclerView.setVisibility(View.VISIBLE);
        showEgoItems(egoItems);
    }

    @Override
    public void showErrorScreen(boolean isLoadingMore) {
        if (isLoadingMore){
            egoAdapter.hideLoadingMore();
            Snackbar snackbar = SnackbarFactory.makeSnackbar(coordinatorLayout, "Error loading more items", "TRY AGAIN", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    egoAdapter.showLoadingMore();
                    recyclerView.scrollToPosition(egoAdapter.getDataCount());
                    loadMore();
                }
            });
            snackbar.show();
        }else {
            recyclerView.setVisibility(View.GONE);
            errorView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showLoading(boolean visible) {
        if (visible) {
            progressBar.setVisibility(View.VISIBLE);
            errorView.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showNoMorePagesToLoad() {
        egoAdapter.hideLoadingMore();
    }

    @OnClick(R.id.retry_button)
    public void onRetryButtonClick(){
        loadMore();
    }
}
