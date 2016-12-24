package com.fomdeveloper.planket.ui.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fomdeveloper.planket.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Fernando on 28/08/16.
 */
public class PhotoItemViewLabel extends RelativeLayout{

    @BindView(R.id.background_image)
    ImageView backgroundImage;
    @BindView(R.id.shadow)
    View shadow;
    @BindView(R.id.label)
    TextView label;


    public PhotoItemViewLabel(Context context) {
        super(context);
        init();
    }

    public PhotoItemViewLabel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PhotoItemViewLabel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        inflate(getContext(), R.layout.photo_item_view_text,this);
        ButterKnife.bind(this);
    }

    public void setImageBackground(String urlImage, Picasso picasso) {
        picasso.load(urlImage).into(backgroundImage);
    }

    public void setLabel(String textLabel) {
        if (!textLabel.isEmpty()) {
            label.setText(textLabel);
            label.setVisibility(VISIBLE);
            shadow.setVisibility(VISIBLE);
        }else{
            label.setVisibility(GONE);
            shadow.setVisibility(GONE);
        }
    }

    public void setSize(int w, int h, int weight){
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(w,h,weight);
        layoutParams.setMargins(6,6,6,6);
        setLayoutParams(layoutParams);

        LayoutParams imageparams = new LayoutParams(w,h);
        backgroundImage.setLayoutParams(imageparams);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size;
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        if (width > height) {
            size = height;
        } else {
            size = width;
        }
        setMeasuredDimension(size, size);

    }


}
