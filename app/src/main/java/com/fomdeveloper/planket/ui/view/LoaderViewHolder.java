package com.fomdeveloper.planket.ui.view;

import android.view.View;
import android.widget.ProgressBar;

import com.fomdeveloper.planket.R;

import butterknife.BindView;

/**
 * Created by Fernando on 11/06/16.
 */
public class LoaderViewHolder extends FlickrViewHolder{

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.no_more_load)
    View noMoreToLoadView;

    public LoaderViewHolder(View itemView) {
        super(itemView);
    }

    public void bindItem(boolean finishLoading){
        if (finishLoading){
            noMoreToLoadView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }else{
            noMoreToLoadView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

}