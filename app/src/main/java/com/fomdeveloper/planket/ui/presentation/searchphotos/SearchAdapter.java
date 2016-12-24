package com.fomdeveloper.planket.ui.presentation.searchphotos;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fomdeveloper.planket.R;
import com.fomdeveloper.planket.data.model.PhotoItem;
import com.fomdeveloper.planket.ui.view.EndlessAdapter;
import com.fomdeveloper.planket.ui.view.FlickrViewHolder;
import com.fomdeveloper.planket.ui.view.LoaderViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Fernando on 02/06/16.
 */
public class SearchAdapter extends EndlessAdapter<FlickrViewHolder> {

    private ArrayList<PhotoItem> photoItems;
    private Picasso picasso;
    private View.OnClickListener onPhotoClickListener;

    public SearchAdapter(Picasso picasso, View.OnClickListener onClickListener) {
        this.photoItems = new ArrayList<>();
        this.picasso = picasso;
        this.onPhotoClickListener = onClickListener;
    }

    @Override
    protected int getDataCount() {
        return photoItems.size();
    }

    @Override
    public FlickrViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_DATA) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.viewholder_search_item, parent, false);
            return new PhotoViewHolder(v);
        }else{
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.viewholder_loader, parent, false);
            return new LoaderViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(FlickrViewHolder holder, int position) {
        if (holder instanceof PhotoViewHolder){
            ((PhotoViewHolder)holder).bindItem(position);
        }else{
            ((LoaderViewHolder)holder).bindItem(showLoadingFinished);
        }
    }

    class PhotoViewHolder extends FlickrViewHolder{

        @BindView(R.id.image_item)
        ImageView imageView;

        public PhotoViewHolder(View itemView) {
            super(itemView);
        }

        public void bindItem(int position){
            PhotoItem photoItem = photoItems.get(position);
            picasso.load(photoItem.getUrlSmall()).into(this.imageView);
            this.imageView.setTag(position);
            this.imageView.setOnClickListener(onPhotoClickListener);

        }
    }

    public void addMoreItems(@NonNull List<PhotoItem> morePhotoItems){
        photoItems.addAll(morePhotoItems);
        notifyItemRangeInserted(getLoadingMoreItemPosition(),morePhotoItems.size());
    }

    public ArrayList<PhotoItem> getPhotoItems() {
        return photoItems;
    }

}
