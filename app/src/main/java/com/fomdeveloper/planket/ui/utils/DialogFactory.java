package com.fomdeveloper.planket.ui.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;

/**
 * Created by Fernando on 19/09/16.
 */
public class DialogFactory {

    public static Dialog createOkDialog(Context context, String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null);
        return alertDialog.create();
    }

}
