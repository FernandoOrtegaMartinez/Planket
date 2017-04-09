package com.fomdeveloper.planket.ui.presentation.gallery;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.fomdeveloper.planket.R;
import com.fomdeveloper.planket.data.model.PhotoItem;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Fernando on 08/04/2017.
 */

public class GalleryPageAdapter extends PagerAdapter{

    private Context context;
    private Picasso picasso;
    private ArrayList<PhotoItem> photoItems;

    public GalleryPageAdapter(Context context, Picasso picasso, ArrayList<PhotoItem> photoItems) {
        this.context = context;
        this.picasso = picasso;
        this.photoItems = photoItems;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.item_gallery_pager, collection, false);
        final ImageView img = (ImageView)layout.findViewById(R.id.zoom_image);
        final ProgressBar progressBar = (ProgressBar)layout.findViewById(R.id.progress_bar);
        final PhotoItem photoItem = photoItems.get(position);
        final String urlImage = TextUtils.isEmpty(photoItem.getUrlLarge()) ? photoItem.getUrlMedium() : photoItem.getUrlLarge();
        picasso.load(urlImage).into(img, new Callback.EmptyCallback(){
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
            }
        });
        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return photoItems.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
