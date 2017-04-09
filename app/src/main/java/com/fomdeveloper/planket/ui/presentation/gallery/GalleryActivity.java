package com.fomdeveloper.planket.ui.presentation.gallery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;

import com.fomdeveloper.planket.PlanketApplication;
import com.fomdeveloper.planket.R;
import com.fomdeveloper.planket.data.model.PhotoItem;
import com.fomdeveloper.planket.ui.presentation.base.BaseActivity;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by Fernando on 08/04/2017.
 */

public class GalleryActivity extends BaseActivity{

    public static final int REQUEST_CODE = 1001;
    public static final String EXTRA_RESULT_POSITION = "extra.result.POSITION";
    private static final String EXTRA_POSITION = "extra.POSITION";
    private static final String EXTRA_PHOTOS = "extra.PHOTOS";

    @Inject Picasso picasso;

    @BindView(R.id.gallery_view_pager) ViewPager viewPager;

    public static void start(Activity activity, int position, List<PhotoItem> photoItems) {
        Intent starter = new Intent(activity, GalleryActivity.class);
        starter.putExtra(EXTRA_POSITION,position);
        starter.putExtra(EXTRA_PHOTOS, Parcels.wrap(photoItems));
        activity.startActivityForResult(starter, REQUEST_CODE);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_gallery;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((PlanketApplication)getApplication()).getAppComponent().inject(this);

        final int position = getIntent().getIntExtra(EXTRA_POSITION, 0);
        final ArrayList<PhotoItem> photos = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_PHOTOS));

        if (photos != null) {
            viewPager.setAdapter(new GalleryPageAdapter(this, picasso, photos));
            viewPager.setCurrentItem(position);
        }
    }

    @Override
    public void finish() {
        setPositionResult();
        super.finish();
    }

    private void setPositionResult(){
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_RESULT_POSITION, viewPager.getCurrentItem());
        setResult(RESULT_OK, resultIntent);
    }

}
