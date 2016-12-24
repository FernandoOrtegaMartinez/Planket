package com.fomdeveloper.planket.ui.presentation.ego;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;
import android.widget.ImageView;

import com.fomdeveloper.planket.PlanketApplication;
import com.fomdeveloper.planket.R;
import com.fomdeveloper.planket.data.model.PhotoItem;
import com.fomdeveloper.planket.ui.presentation.base.BaseActivity;
import com.fomdeveloper.planket.ui.utils.ImageBlurryUtils;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import javax.inject.Inject;

import butterknife.BindView;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Fernando on 25/06/16.
 */
public class EgoActivity extends BaseActivity{

    private static final String EXTRA_PHOTO_ITEM = "com.fomdeveloper.planket.EXTRA_PHOTO_ITEM";

    @Inject Picasso picasso;

    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.sliding_tabs)
    TabLayout tabLayout;
    @BindView(R.id.background_image)
    ImageView imgBackground;

    private Subscription subscription;
    private PhotoItem photoItem;

    public static void start(Context context, PhotoItem photoItem) {
        Intent starter = new Intent(context, EgoActivity.class);
        starter.putExtra(EXTRA_PHOTO_ITEM, Parcels.wrap(photoItem));
        context.startActivity(starter);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_ego;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((PlanketApplication)getApplication()).getAppComponent().inject(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        photoItem = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_PHOTO_ITEM));
        if (photoItem.getPhotoTitle()!=null) {
            setToolbarTitle(photoItem.getPhotoTitle());
        }

        setupViewpager();
        getImageBlurry();
    }

    private void setupViewpager(){

        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            private Fragment[] egoFragments = {FavesFragment.newInstance(photoItem.getPhotoId()),
                                                CommentsFragment.newInstance(photoItem.getPhotoId())};
            private final CharSequence[] titles = {getString(R.string.faves_title),getString(R.string.comments_title)};

            @Override
            public Fragment getItem(int position) {
                return egoFragments[position];
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }

            @Override
            public int getCount() {
                return egoFragments.length;
            }
        });

        tabLayout.setupWithViewPager(viewPager);
    }


    private void getImageBlurry(){
        subscription = ImageBlurryUtils.getBitmapImage(picasso, photoItem.getUrlMedium())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<Bitmap, Single<Bitmap>>() {
                    @Override
                    public Single<Bitmap> call(Bitmap scaledBitmap) {
                        return ImageBlurryUtils.getBlurryImage(EgoActivity.this, scaledBitmap);
                    }
                })
                .subscribe(new SingleSubscriber<Bitmap>() {
                    @Override
                    public void onSuccess(Bitmap blurryBitmap) {
                        imgBackground.setImageBitmap(blurryBitmap);
                    }

                    @Override
                    public void onError(Throwable error) {
                        Timber.e(error.getLocalizedMessage());
                        picasso.load(photoItem.getUrlMedium()).into(imgBackground);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscription.unsubscribe();
    }
}
