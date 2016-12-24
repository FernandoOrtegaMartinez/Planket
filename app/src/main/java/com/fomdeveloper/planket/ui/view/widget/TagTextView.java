package com.fomdeveloper.planket.ui.view.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fomdeveloper.planket.R;

/**
 * Created by Fernando on 30/07/16.
 */
public class TagTextView extends TextView implements View.OnTouchListener{

    public TagTextView(Context context) {
        super(context);

        setBackgroundResource(R.drawable.border_tag);

        setPadding(40, 20, 40, 20);

        setTextColor(ContextCompat.getColor(getContext(),R.color.colorDefaultText));
        RelativeLayout.LayoutParams llp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        llp.setMargins(0, 10, 15, 10);
        setLayoutParams(llp);

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction()==MotionEvent.ACTION_CANCEL || motionEvent.getAction()== MotionEvent.ACTION_UP){
            setTextColor(ContextCompat.getColor(getContext(),R.color.colorDefaultText));
        }else{
            setTextColor(Color.WHITE);
        }
        return false;
    }

}
