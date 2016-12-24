package com.fomdeveloper.planket.ui.view.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.fomdeveloper.planket.R;
import com.fomdeveloper.planket.data.model.PhotoItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


import butterknife.BindInt;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by Fernando on 28/08/16.
 */
public class ShowcasePhoto extends LinearLayout {

    private List<PhotoItem> photoItems = new ArrayList<>();

    @BindInt(R.integer.showcase_items)
    int showcaseItems;
    @BindInt(R.integer.showcase_rows)
    int showcaseRows;
    @BindInt(R.integer.showcase_item_height)
    int itemHeight;

    private Picasso picasso;
    private int rowsNedeed;

    @Nullable private OnShowcaseClickListener onShowcaseClickListener;

    public interface OnShowcaseClickListener{
        void onPhotoItemClick(int position);
        void onMoreClick();
    }

    public void setOnShowcaseClickListener(@Nullable OnShowcaseClickListener onShowcaseClickListener) {
        this.onShowcaseClickListener = onShowcaseClickListener;
    }

    public ShowcasePhoto(Context context, Picasso picasso) {
        super(context);
        this.picasso = picasso;
        init();
    }

    private void init(){
        ButterKnife.bind(this);
        setOrientation(VERTICAL);
    }

    public List<PhotoItem> getPhotoItems() {
        return photoItems;
    }

    public void setPhotoItems(List<PhotoItem> photoItems) {
        if (photoItems == null) return;
        this.photoItems = photoItems;
        this.rowsNedeed = (photoItems.size() / showcaseItems) + (photoItems.size() % showcaseItems == 0 ? 0 : 1);
        crateViews();
    }

    private void crateViews(){
        if (picasso == null){
            Timber.e("Picasso was null when trying to populate photos items for showcases in ShowcasePhoto");
            return;
        }
        if (photoItems == null){
            Timber.e("Photo items were null when trying to populate them in ShowcasePhoto");
            return;
        }


        int spots = showcaseItems * rowsNedeed;

        LinearLayout container = new LinearLayout(getContext());
        container.setOrientation(HORIZONTAL);
        for (int i=0; i < spots; i++) {
            View itemView = getItemView(i);
            container.addView(itemView);
            if (container.getChildCount() == showcaseItems){
                addView(container);
                if (getChildCount() < rowsNedeed) {
                    container = new LinearLayout(getContext());
                }
            }
        }


    }


    private View getItemView(final int position){

        if (position >=  photoItems.size()){
            return getInvisibleItemView();
        }

        PhotoItem photoItem = photoItems.get(position);
        PhotoItemViewLabel photoItemViewLabel;

        if (position == (showcaseItems*showcaseRows)-1){
            photoItemViewLabel = new PhotoItemViewLabel(getContext());
            photoItemViewLabel.setImageBackground(photoItem.getUrlSmall(),picasso);
            photoItemViewLabel.setSize(LayoutParams.MATCH_PARENT, itemHeight, 1);
            photoItemViewLabel.setLabel(getContext().getString(R.string.more_label));
            photoItemViewLabel.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if (onShowcaseClickListener!=null){
                        onShowcaseClickListener.onMoreClick();
                    }
                }
            });
            return photoItemViewLabel;
        }

        photoItemViewLabel = new PhotoItemViewLabel(getContext());
        photoItemViewLabel.setImageBackground(photoItem.getUrlSmall(),picasso);
        photoItemViewLabel.setSize(LayoutParams.MATCH_PARENT, itemHeight, 1);
        photoItemViewLabel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onShowcaseClickListener!=null){
                    onShowcaseClickListener.onPhotoItemClick(position);
                }
            }
        });

        return photoItemViewLabel;
    }

    private View getInvisibleItemView(){
        PhotoItemViewLabel photoItemViewLabel = new PhotoItemViewLabel(getContext());
        photoItemViewLabel.setSize(LayoutParams.MATCH_PARENT, itemHeight, 1);
        photoItemViewLabel.setVisibility(INVISIBLE);
        return photoItemViewLabel;
    }




}
