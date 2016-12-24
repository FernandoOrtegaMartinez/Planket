package com.fomdeveloper.planket.ui.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.fomdeveloper.planket.R;

/**
 * Created by Fernando on 30/08/16.
 */
public class ShadowView extends View {

    private final float ALPHA = 0.5f;

    public ShadowView(Context context) {
        super(context);
        init();
    }

    public ShadowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ShadowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        setBackgroundResource(R.color.black);
        setAlpha(ALPHA);
    }

}
