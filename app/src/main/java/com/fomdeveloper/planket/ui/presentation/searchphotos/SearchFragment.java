package com.fomdeveloper.planket.ui.presentation.searchphotos;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fomdeveloper.planket.PlanketApplication;
import com.fomdeveloper.planket.NetworkManager;
import com.fomdeveloper.planket.R;
import com.fomdeveloper.planket.data.model.PhotoItem;
import com.fomdeveloper.planket.ui.presentation.main.ToolbarInterface;
import com.fomdeveloper.planket.ui.presentation.photodetail.PhotoDetailActivity;
import com.fomdeveloper.planket.ui.utils.SnackbarFactory;
import com.fomdeveloper.planket.ui.view.EndlessRecyclerViewScrollListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Fernando on 02/06/16.
 */
public class SearchFragment extends Fragment implements SearchView {

    public static final String TAG = SearchFragment.class.getSimpleName();
    private static final String ARG_SEARCH_ID = "ARG.SEARCH_ID";
    private static final String ARG_SEARCH_TYPE = "ARG.SEARCH_TYPE";
    private static final String ARG_SEARCH_TITLE = "ARG.SEARCH_TITLE";
    private static final int PER_PAGE_MAX_QUERY = 100;


    @Inject
    SearchPresenter presenter;
    @Inject
    Picasso picasso;
    @Inject
    NetworkManager networkManager;

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_View)
    RecyclerView recyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.error_view)
    View errorView;
    @BindView(R.id.no_results_view)
    View noResultsView;
    @BindView(R.id.no_results_message)
    TextView noResultsTextView;
    @BindView(R.id.toolbar_logo)
    View toolbarLogo;

    @Nullable private ToolbarInterface toolbarInterface;
    @Nullable private String searchId;
    @Nullable private String searchTitle;
    @NonNull private SearchType searchType;

    private SearchAdapter adapter;

    public static SearchFragment newInstance(String searchId, SearchType searchType) {

        Bundle args = new Bundle();
        args.putString(ARG_SEARCH_ID, searchId);
        args.putString(ARG_SEARCH_TYPE, searchType.name());

        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);

        return fragment;
    }

    public static SearchFragment newInstance(String searchId, String searchTitle, String searchType) {

        Bundle args = new Bundle();
        args.putString(ARG_SEARCH_ID, searchId);
        args.putString(ARG_SEARCH_TYPE, searchType);
        args.putString(ARG_SEARCH_TITLE, searchTitle);

        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ToolbarInterface){
            toolbarInterface = (ToolbarInterface) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        toolbarInterface = null;
        presenter.detachView();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((PlanketApplication)getActivity().getApplication()).getAppComponent().inject(this);
        if (getArguments()!=null) {
            this.searchId = getArguments().getString(ARG_SEARCH_ID);
            this.searchTitle = getArguments().getString(ARG_SEARCH_TITLE);
            this.searchType = SearchType.valueOf(getArguments().getString(ARG_SEARCH_TYPE));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search,container,false);
        ButterKnife.bind(this,view);
        setupToolbar();
        return view;
    }

    private void setupToolbar(){
        if (toolbarInterface != null) {
            toolbarInterface.setToolbar(toolbar);
        }
        final ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        setActionBar(actionBar);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager,PER_PAGE_MAX_QUERY) {
            @Override
            public void onLoadMore() {
                loadData();
            }
        });

        adapter = new SearchAdapter(picasso, photoClickListener);
        recyclerView.setAdapter(adapter);

        loadData();

    }

    @Override
    public void showPhotoItems(List<PhotoItem> photoItems) {
        recyclerView.setVisibility(View.VISIBLE);
        adapter.addMoreItems(photoItems);
    }

    @Override
    public void showSearchError(boolean isLoadingMore) {
        if (isLoadingMore){
            adapter.hideLoadingMore();
            Snackbar snackbar = SnackbarFactory.makeSnackbar(coordinatorLayout, "Error loading more photos", "TRY AGAIN", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.showLoadingMore();
                    loadData();
                }
            });
            snackbar.show();
        }else {
            recyclerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
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
        adapter.showNoMoreLoading();
    }

    @Override
    public void showNoItems(boolean isLoadingMore) {
        if (isLoadingMore) {
            adapter.hideLoadingMore();
        }else{
            noResultsTextView.setText(R.string.error_no_results);
            noResultsView.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.retry_button)
    public void onRetryButtonClick(){
        if (networkManager.isConnectedToInternet()){
            loadData();
        }else{
            // TODO: 10/21/16 show message error internet
        }
    }

    private View.OnClickListener photoClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int)v.getTag();
            PhotoDetailActivity.start(getActivity(),position,adapter.getPhotoItems());
        }
    };

    private void loadData(){

        switch (searchType){
            case INTERESTINGNESS:
                presenter.loadInterestingness();
                break;
            case TAG:
                presenter.searchByTag(searchId);
                break;
            case TEXT:
                presenter.searchByText(searchId);
                break;
            case USER_ID:
                presenter.searchByUserId(searchId);
                break;
            case FAV:
                presenter.searchFavsByUserId(searchId);
                break;
            default:
                showSearchError(false);
                break;
        }

    }

    private void setActionBar(ActionBar actionBar){
       if (actionBar==null)return;

       actionBar.setDisplayHomeAsUpEnabled(true);
       if (searchType==SearchType.INTERESTINGNESS) {
           actionBar.setDisplayShowTitleEnabled(false);
           toolbarLogo.setVisibility(View.VISIBLE);
           actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
       }else if (searchTitle!=null){
           actionBar.setTitle(searchTitle);
           toolbarLogo.setVisibility(View.GONE);
        }else {
           actionBar.setTitle(searchId);
           toolbarLogo.setVisibility(View.GONE);
        }
    }

}
