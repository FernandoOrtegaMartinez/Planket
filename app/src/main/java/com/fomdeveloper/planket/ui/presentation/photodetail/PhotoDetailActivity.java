package com.fomdeveloper.planket.ui.presentation.photodetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.fomdeveloper.planket.R;
import com.fomdeveloper.planket.data.model.PhotoItem;
import com.fomdeveloper.planket.ui.presentation.base.BaseActivity;
import com.fomdeveloper.planket.ui.presentation.gallery.GalleryActivity;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Fernando on 09/06/16.
 */
public class PhotoDetailActivity extends BaseActivity implements PhotoDetailListener{

    private static final String EXTRA_POSITION = "extra.POSITION";
    private static final String EXTRA_PHOTOS = "extra.PHOTOS";

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.view_pager) ViewPager viewPager;

    private int position;
    private ArrayList<PhotoItem> photos;

    public static void start(Context context,int position, List<PhotoItem> photoItems) {
        Intent starter = new Intent(context, PhotoDetailActivity.class);
        starter.putExtra(EXTRA_POSITION,position);
        starter.putExtra(EXTRA_PHOTOS, Parcels.wrap(photoItems));
        context.startActivity(starter);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_photo_detail;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        position = getIntent().getIntExtra(EXTRA_POSITION,0);
        photos = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_PHOTOS));
        if (photos != null){

            viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
                @Override
                public Fragment getItem(int position) {
                    return PhotoDetailFragment.newInstance(photos.get(position));
                }

                @Override
                public int getCount() {
                    return photos.size();
                }
            });

            viewPager.setCurrentItem(position);
        }

    }

    @Override
    public void openGallery() {
        GalleryActivity.start(this, viewPager.getCurrentItem(), photos);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == GalleryActivity.REQUEST_CODE){
            position = data.getIntExtra(GalleryActivity.EXTRA_RESULT_POSITION, 0);
            viewPager.setCurrentItem(position, false);
        }
    }
}
