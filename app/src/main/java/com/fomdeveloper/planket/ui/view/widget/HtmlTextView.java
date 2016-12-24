package com.fomdeveloper.planket.ui.view.widget;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Fernando on 05/11/2016.
 */

public class HtmlTextView extends TextView{
    public HtmlTextView(Context context) {
        super(context);
        init();
    }

    public HtmlTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HtmlTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void setHtmlText(String htmlText){
        setText(Html.fromHtml(htmlText));
    }

}
