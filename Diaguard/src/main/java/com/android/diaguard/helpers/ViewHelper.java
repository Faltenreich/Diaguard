package com.android.diaguard.helpers;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by Filip on 10.12.13.
 */
public class ViewHelper {

    public static void showToastError(Context context, String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void showToastMessage(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
