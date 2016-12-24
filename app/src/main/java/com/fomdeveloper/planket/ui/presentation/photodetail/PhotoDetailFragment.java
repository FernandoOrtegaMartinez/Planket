package com.fomdeveloper.planket.ui.presentation.photodetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fomdeveloper.planket.PlanketApplication;
import com.fomdeveloper.planket.R;
import com.fomdeveloper.planket.data.model.PhotoItem;
import com.fomdeveloper.planket.data.prefs.UserHelper;
import com.fomdeveloper.planket.ui.presentation.login.FlickrLoginActivity;
import com.fomdeveloper.planket.ui.presentation.ego.EgoActivity;
import com.fomdeveloper.planket.ui.presentation.profile.ProfileActivity;
import com.fomdeveloper.planket.ui.presentation.searchphotos.SearchActivity;
import com.fomdeveloper.planket.ui.presentation.searchphotos.SearchType;
import com.fomdeveloper.planket.ui.view.CircleTransformation;
import com.fomdeveloper.planket.ui.view.FlowLayout;
import com.fomdeveloper.planket.ui.view.widget.HtmlTextView;
import com.fomdeveloper.planket.ui.view.widget.SmallGreyTextView;
import com.fomdeveloper.planket.ui.view.widget.TagTextView;
import com.fomdeveloper.planket.ui.view.widget.TextIconButton;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import javax.inject.Inject;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Fernando on 09/06/16.
 */
public class PhotoDetailFragment extends Fragment implements PhotoDetailView {

    private static final String ARG_PHOTO = "ARG.PHOTO";
    private PhotoItem photoItem;

    @BindView(R.id.photo)
    ImageView photoImageView;
    @BindView(R.id.faves)
    TextIconButton favesButton;
    @BindView(R.id.comments)
    TextIconButton commentsButton;
    @BindView(R.id.profile_pic)
    ImageView profilePicImageView;
    @BindView(R.id.user_name)
    TextView userNameTextView;
    @BindView(R.id.pic_name)
    TextView picNameTextView;
    @BindView(R.id.location_name)
    TextView locationNameTextView;
    @BindView(R.id.description)
    HtmlTextView descriptionTextView;
    @BindView(R.id.tags_container)
    FlowLayout tagsContainer;
    @BindView(R.id.fab)
    FloatingActionButton favButton;


    @Inject
    PhotoDetailPresenter presenter;
    @Inject
    UserHelper userHelper;
    @Inject
    Picasso picasso;

    private boolean isPhotoFavorite;


    public static PhotoDetailFragment newInstance(PhotoItem photoItem) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_PHOTO, Parcels.wrap(photoItem));

        PhotoDetailFragment fragment = new PhotoDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((PlanketApplication)getActivity().getApplication()).getAppComponent().inject(this);
        photoItem = Parcels.unwrap(getArguments().getParcelable(ARG_PHOTO));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_detail,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter.attachView(this);

        picNameTextView.setText(photoItem.getPhotoTitle());
//        locationNameTextView.setText(photoItem.getLatitude()+","+photoItem.getLongitude()); // TODO: 27/10/2016 Show country and city with reverse geocoding
        favesButton.setLabel(photoItem.getNumFaves());
        commentsButton.setLabel(photoItem.getNumComments());
        String description = photoItem.getDescription().isEmpty() ? getResources().getString(R.string.no_description) : photoItem.getDescription();
        descriptionTextView.setHtmlText(description);

        userNameTextView.setText(photoItem.getRealname());
        picasso.load(photoItem.getUrlMedium()).into(photoImageView);

        final int profilePicSize = getResources().getDimensionPixelSize(R.dimen.profile_pic_size_big);
        picasso.load(photoItem.getUserProfilePicUrl())
                .resize(profilePicSize, profilePicSize)
                .centerCrop()
                .transform(new CircleTransformation())
                .placeholder(R.drawable.profile)
                .into(profilePicImageView);


        presenter.checkIfThisPhotoIsFav(photoItem.getPhotoId());
        populateTags();

    }

    @Override
    public void onDetach() {
        presenter.detachView();
        super.onDetach();
    }

    private void populateTags() {
        if(!photoItem.getTags().isEmpty()) {
            String[] tags = photoItem.getTags().split(" ");
            TagTextView tagTextView;
            for (int i = 0; i < tags.length; i++) {
                tagTextView = new TagTextView(getActivity());
                tagTextView.setText(tags[i]);
                tagTextView.setOnClickListener(onTagListener);
                tagsContainer.addView(tagTextView);
            }
        }else {
            SmallGreyTextView noTagsTextView = new SmallGreyTextView(getActivity());
            noTagsTextView.setText(R.string.no_tags);
            tagsContainer.addView(noTagsTextView);
        }
    }

    @OnClick(R.id.faves_comments_button)
    public void onClickFavesAndComments() {
        EgoActivity.start(getActivity(), photoItem);
    }

    @OnClick(R.id.fab)
    public void onClickFabButton(FloatingActionButton floatingActionButton){
        if (userHelper.isUserLoggedIn()) {
            if (isPhotoFavorite){
                setFavButton(false);
                presenter.removePhotoFavorite(photoItem.getPhotoId());
            }else {
                setFavButton(true);
                presenter.setPhotoFavorite(photoItem.getPhotoId());
            }
        }else{
            startActivity(new Intent(getActivity(), FlickrLoginActivity.class));
        }
    }

    @Override
    public void showPhotoFavoriteSuccessful() {
        Toast.makeText(getActivity(),"Added to favorites",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showPhotoFavoriteError(String errorMessage) {
        Toast.makeText(getActivity(),errorMessage,Toast.LENGTH_SHORT).show();
        setFavButton(false);
    }

    @Override
    public void showRemoveFavoriteSuccessful() {
        Toast.makeText(getActivity(),"Removed from your favorites",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showRemoveFavoriteError(String errorMessage) {
        Toast.makeText(getActivity(),errorMessage,Toast.LENGTH_SHORT).show();
        setFavButton(true);
    }

    @Override
    public void setFavButton(boolean isFavorite) {
        if (isFavorite){
            favButton.setImageResource(R.drawable.ic_faves_red);
            isPhotoFavorite = true;
        }else{
            favButton.setImageResource(R.drawable.ic_faves);
            isPhotoFavorite = false;
        }
    }

    @OnClick(R.id.user_layout)
    public void onClickUserLayout(View view){
        ProfileActivity.start(getActivity(),photoItem.getUserId());
    }

    private View.OnClickListener onTagListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SearchActivity.start(getActivity(),((TagTextView)v).getText().toString(), SearchType.TAG);
        }
    };




}
