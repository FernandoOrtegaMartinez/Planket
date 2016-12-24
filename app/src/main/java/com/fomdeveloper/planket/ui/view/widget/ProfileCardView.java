package com.fomdeveloper.planket.ui.view.widget;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fomdeveloper.planket.R;
import com.fomdeveloper.planket.data.model.PhotoItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by Fernando on 28/08/16.
 */
public class ProfileCardView extends CardView implements ShowcasePhoto.OnShowcaseClickListener{

    @BindView(R.id.showcase_container)
    LinearLayout showcaseContainer;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.title)
    TextView titleTextView;
    @BindView(R.id.num_items)
    TextView numItemsTextView;
    @BindView(R.id.error_layout)
    View errorView;
    @BindView(R.id.error_message)
    TextView errorMessage;
    @BindInt(R.integer.showcase_items)
    int showcaseItems;

    private Picasso picasso;
    private List<PhotoItem> photoItems = new ArrayList<>();
    private OnCardClickListener onCardClickListener;

    public interface OnCardClickListener{
        void onPhotoItemClick(ProfileCardView profileCardView,int position);
        void onMoreClick(ProfileCardView profileCardView);
    }

    public ProfileCardView(Context context) {
        super(context);
        init();
    }

    private void init(){
        inflate(getContext(), R.layout.cardview_profile,this);
        ButterKnife.bind(this);

        setCardElevation(0);
        setCardBackgroundColor(ContextCompat.getColor(getContext(),R.color.colorwindowBackground));

    }

    public List<PhotoItem> getPhotoItems() {
        return photoItems;
    }

    public void setPhotoItems(List<PhotoItem> photoItems, String title, int totalItems, Picasso picasso) {
        this.photoItems = photoItems;
        this.picasso = picasso;
        populatePhotoItems(title,totalItems);
    }

    private void populatePhotoItems(String title, int totalItems){
        if (picasso == null){
            Timber.e("Picasso was null when trying to populate photos items for showcases in ProfileCardView");
            return;
        }
        if (photoItems.size() > 0){
            this.numItemsTextView.setText(String.valueOf(totalItems));
            this.titleTextView.setText(title);
            this.errorView.setVisibility(GONE);
            ShowcasePhoto showcasePhoto = new ShowcasePhoto(getContext(),picasso);
            showcasePhoto.setPhotoItems(photoItems);
            showcasePhoto.setOnShowcaseClickListener(this);
            showcaseContainer.addView(showcasePhoto);
        }else{
            this.errorView.setVisibility(VISIBLE);
        }
        this.progressBar.setVisibility(GONE);
    }

    public void showLoading(boolean visible){
        progressBar.setVisibility(visible?VISIBLE:GONE);
    }

    public void showErrorView(String errorMessage){
        this.progressBar.setVisibility(GONE);
        this.errorView.setVisibility(VISIBLE);
        this.errorMessage.setText(errorMessage);
    }

    public void setOnCardClickListener(OnCardClickListener onCardClickListener) {
        this.onCardClickListener = onCardClickListener;
    }

    @Override
    public void onPhotoItemClick(int position) {
        if (this.onCardClickListener!=null){
            this.onCardClickListener.onPhotoItemClick(this, position);
        }
    }

    @Override
    public void onMoreClick() {
        if (this.onCardClickListener!=null){
            this.onCardClickListener.onMoreClick(this);
        }
    }
}
