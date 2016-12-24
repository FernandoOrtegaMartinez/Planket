package com.fomdeveloper.planket.ui.view.widget;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;


/**
 * Created by Fernando on 25/09/16.
 */
public class PlanketButton extends Button{


    public PlanketButton(Context context) {
        super(context);
        init();
    }

    public PlanketButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PlanketButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/kitten_slant.ttf");
        setPaintFlags(getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG); //needed to render the font correctly
        setTypeface(tf);
        setAllCaps(false);
    }
}
