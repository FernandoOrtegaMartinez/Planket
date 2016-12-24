package com.fomdeveloper.planket.ui.view;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Fernando on 08/07/16.
 */
public abstract class EndlessAdapter<T  extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T>{

    protected static final int TYPE_LOADING_MORE = -1;
    protected static final int TYPE_DATA = 0;

    protected boolean showLoadingMore = true;
    protected boolean showLoadingFinished;

    protected abstract int getDataCount();

    @Override
    public int getItemCount() {
        if (getDataCount()==0) return 0;
        return getDataCount() + (showLoadingMore ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position) {
        if (position < getDataCount()
                && getDataCount() > 0) {
            return TYPE_DATA;
        }
        return TYPE_LOADING_MORE;
    }

    public int getLoadingMoreItemPosition() {
        return showLoadingMore ? getItemCount() - 1 : RecyclerView.NO_POSITION;
    }

    public void showLoadingMore() {
        if (showLoadingMore) return;
        showLoadingMore = true;
        notifyItemInserted(getLoadingMoreItemPosition());
    }

    public void hideLoadingMore() {
        if (!showLoadingMore) return;
        final int loadingPos = getLoadingMoreItemPosition();
        showLoadingMore = false;
        notifyItemRemoved(loadingPos);
    }

    public void showNoMoreLoading() {
        showLoadingFinished = true;
        final int loadingPos = getLoadingMoreItemPosition();
        notifyItemChanged(loadingPos);
    }



}
