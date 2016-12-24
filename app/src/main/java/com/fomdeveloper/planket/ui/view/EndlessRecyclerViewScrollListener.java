package com.fomdeveloper.planket.ui.view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * * Borrowed from gist.github.com/nesquena/d09dc68ff07e845cc622
 */
public abstract class EndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {

    private final int visibleThreshold = 1; // The minimum amount of items to have below your current scroll position before loading more.
    private int previousTotal; // The total number of items in the dataset after the last load
    private boolean loading = false; // True if we are still waiting for the last set of data to load.
    private LinearLayoutManager mLinearLayoutManager;

    public EndlessRecyclerViewScrollListener(LinearLayoutManager linearLayoutManager, int itemsPerPage) {
        this.mLinearLayoutManager = linearLayoutManager;
        this.previousTotal = itemsPerPage + 1;
    }


    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int totalItemCount = mLinearLayoutManager.getItemCount();
        int lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();

        if (totalItemCount>0) {

            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;
                }
            }

            if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                // End has been reached
                onLoadMore();
                loading = true;
            }

        }

    }

    public abstract void onLoadMore();


}