package com.fomdeveloper.planket.ui.utils;

import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.fomdeveloper.planket.R;

/**
 * Created by Fernando on 27/10/2016.
 */
public class SnackbarFactory {

    public static Snackbar makeSnackbar(View parentView, String message, String action, View.OnClickListener onClickListener){
        Snackbar snackbar = Snackbar
                .make(parentView, message, Snackbar.LENGTH_INDEFINITE)
                .setAction(action,onClickListener);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(parentView.getContext(), R.color.colorPrimaryDark));
        return snackbar;
    }
}
