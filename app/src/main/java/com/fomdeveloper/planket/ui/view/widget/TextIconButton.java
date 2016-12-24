package com.fomdeveloper.planket.ui.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fomdeveloper.planket.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Fernando on 24/06/16.
 */
public class TextIconButton extends LinearLayout{

    @BindView(R.id.icon)
    ImageView icon;
    @BindView(R.id.label)
    TextView label;

    private String textLabel;
    private int drawable;

    public TextIconButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.TextIconButton);
        textLabel = a.getString(R.styleable.TextIconButton_label);
        drawable = a.getResourceId(R.styleable.TextIconButton_buttonIcon,0);
        a.recycle();
        init();
    }

    private void init(){

        inflate(getContext(), R.layout.button_text_icon,this);
        ButterKnife.bind(this);

        label.setText(textLabel);
        icon.setImageResource(drawable);

    }

    public void setLabel(String newLabel) {
        this.textLabel = newLabel;
        label.setText(textLabel);

    }
}
