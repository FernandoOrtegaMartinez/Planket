package com.fomdeveloper.planket.ui.view.widget;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import com.fomdeveloper.planket.R;

/**
 * Created by Fernando on 31/07/16.
 */
public class SmallGreyTextView extends TextView {

    public SmallGreyTextView(Context context) {
        super(context);
        init();
    }

    public SmallGreyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SmallGreyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        setTextColor(ContextCompat.getColor(getContext(), R.color.colorDefaultText));
        setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
    }
}
