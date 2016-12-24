package com.fomdeveloper.planket.ui.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by Fernando on 11/06/16.
 */
public class FlickrViewHolder extends RecyclerView.ViewHolder{

    public FlickrViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

}
