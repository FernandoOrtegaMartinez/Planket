package com.fomdeveloper.planket.ui.presentation.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fomdeveloper.planket.PlanketApplication;
import com.fomdeveloper.planket.R;
import com.fomdeveloper.planket.data.model.FlickrUser;
import com.fomdeveloper.planket.data.model.PhotoItem;
import com.fomdeveloper.planket.ui.presentation.base.BaseActivity;
import com.fomdeveloper.planket.ui.presentation.photodetail.PhotoDetailActivity;
import com.fomdeveloper.planket.ui.presentation.searchphotos.SearchActivity;
import com.fomdeveloper.planket.ui.presentation.searchphotos.SearchType;
import com.fomdeveloper.planket.ui.view.CircleTransformation;
import com.fomdeveloper.planket.ui.view.widget.ProfileCardView;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindDimen;
import butterknife.BindInt;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Fernando on 20/08/16.
 */
public class ProfileActivity extends BaseActivity implements ProfileView, ProfileCardView.OnCardClickListener {

    public static final String EXTRA_USER_ID = "extra.USER_ID";
    private static final int[] CARD_VIEW_TITLE = {R.string.photos_title, R.string.faves_title};

    @Inject ProfilePresenter presenter;
    @Inject Picasso picasso;

    @BindView(R.id.real_name)
    TextView realNameTextView;
    @BindView(R.id.user_name)
    TextView userNameTextView;
    @BindView(R.id.location)
    TextView locationTextView;
    @BindView(R.id.profile_pic)
    ImageView profilePic;
    @BindView(R.id.cover_img)
    ImageView coverImage;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.profile_container)
    LinearLayout profileContainer;
    @BindView(R.id.error_view)
    View errorView;
    @BindView(R.id.no_results_view)
    View noResultsView;
    @BindView(R.id.no_results_message)
    TextView noResultsTextView;

    @BindDimen(R.dimen.profile_pic_size_big)
    int profilePicSize;
    @BindInt(R.integer.showcase_items)
    int showcaseItems;
    @BindInt(R.integer.showcase_rows)
    int showcaseRows;

    private String userId;
    private FlickrUser flickrUser;
    private List<PhotoItem> userPhotos;
    private List<PhotoItem> userFaves;

    public static void start(Context context, String userId) {
        Intent starter = new Intent(context, ProfileActivity.class);
        starter.putExtra(EXTRA_USER_ID,userId);
        context.startActivity(starter);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_profile;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((PlanketApplication)getApplication()).getAppComponent().inject(this);

        presenter.attachView(this);

        userId = getIntent().getStringExtra(EXTRA_USER_ID);
        getUserInfo();
        getShowcases();
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    private void getUserInfo(){
        presenter.getUserInfo(userId);
    }

    private void getShowcases(){
        presenter.getShowcases(userId, showcaseItems*showcaseRows);
    }

    @Override
    public void showUserDetails(FlickrUser flickrUser) {
        this.flickrUser = flickrUser;
        realNameTextView.setText(flickrUser.getRealname());
        userNameTextView.setText(flickrUser.getUsername());
        locationTextView.setText(flickrUser.getLocation());
        removeEmptyTextViews();

        picasso.load(flickrUser.getUserProfilePicUrl())
                .resize(profilePicSize, profilePicSize)
                .centerCrop()
                .transform(new CircleTransformation())
                .placeholder(R.drawable.profile)
                .into(profilePic);
    }

    @Override
    public void showUserPhotos(List<PhotoItem> photoItems, int totalItems) {
        this.userPhotos = photoItems;
        picasso.load(photoItems.get(0).getUrlMedium()).into(coverImage);
        addProfileCardView(R.id.user_photos_cardview, getString(CARD_VIEW_TITLE[0]),photoItems, totalItems);
    }

    @Override
    public void showUserFaves(List<PhotoItem> photoItems, int totalItems) {
        this.userFaves = photoItems;
        addProfileCardView(R.id.user_faves_cardview, getString(CARD_VIEW_TITLE[1]),photoItems, totalItems);
    }

    private void addProfileCardView(int cardViewId, String title, List<PhotoItem> photoItems, int totalItems){
        ProfileCardView userFavesCardView = getUserCardView(cardViewId);
        if (userFavesCardView==null) {
            userFavesCardView = new ProfileCardView(this);
            userFavesCardView.setId(cardViewId);
            userFavesCardView.setPhotoItems(photoItems, title, totalItems, picasso);
            userFavesCardView.setOnCardClickListener(this);
            if (cardViewId == R.id.user_photos_cardview) {
                profileContainer.addView(userFavesCardView,0);
            }else{
                profileContainer.addView(userFavesCardView);
            }
        }else{
            userFavesCardView.setPhotoItems(photoItems, title, totalItems, picasso);
        }
    }

    private ProfileCardView getUserCardView(int cardViewId){
        for (int i=0; i< profileContainer.getChildCount();i++){
            ProfileCardView profileCardView = (ProfileCardView)profileContainer.getChildAt(i);
            if (profileCardView.getId() == cardViewId){
                return profileCardView;
            }
        }
        return null;
    }

    @Override
    public void showNoItems() {
        noResultsTextView.setText(R.string.empty_box);
        noResultsView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
    }

    @Override
    public void showErrorScreen() {
        noResultsView.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.retry_button)
    public void onRetryClick(){
        errorView.setVisibility(View.GONE);
        noResultsView.setVisibility(View.GONE);
        getUserInfo();
        getShowcases();
    }

    @Override
    public void showLoading(boolean visible) {
        progressBar.setVisibility(visible? View.VISIBLE:View.GONE);
    }

    @Override
    public void onPhotoItemClick(ProfileCardView profileCardView, int position) {
        switch (profileCardView.getId()){
            case R.id.user_photos_cardview:
                if (userPhotos==null) return;
                PhotoDetailActivity.start(this,position,userPhotos);
                break;
            case R.id.user_faves_cardview:
                if (userFaves==null) return;
                PhotoDetailActivity.start(this,position,userFaves);
                break;
            default:break;
        }
    }

    @Override
    public void onMoreClick(ProfileCardView profileCardView) {
        switch (profileCardView.getId()){
            case R.id.user_photos_cardview:
                SearchActivity.start(this,userId, buildExtraSearchTitle(SearchType.USER_ID), SearchType.USER_ID);
                break;
            case R.id.user_faves_cardview:
                SearchActivity.start(this,userId, buildExtraSearchTitle(SearchType.FAV), SearchType.FAV);
                break;
            default:break;
        }

    }

    private String buildExtraSearchTitle(SearchType searchType){
        String defaultTitle = "";
        switch (searchType){
            case USER_ID:defaultTitle = " photos";
                break;
            case FAV:defaultTitle = " faves";
                break;
        }
        return flickrUser!=null?flickrUser.getBestName()+defaultTitle:"User"+defaultTitle;
    }

    private void removeEmptyTextViews() {
        if (realNameTextView.getText().toString().isEmpty()){
            realNameTextView.setVisibility(View.GONE);
        }
        if (userNameTextView.getText().toString().isEmpty()){
            userNameTextView.setVisibility(View.GONE);
        }
        if (locationTextView.getText().toString().isEmpty()){
            locationTextView.setVisibility(View.GONE);
        }
    }
}
