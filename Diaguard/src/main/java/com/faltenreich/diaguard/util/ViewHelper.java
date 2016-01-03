package com.faltenreich.diaguard.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.support.design.widget.Snackbar;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Toast;

/**
 * Created by Filip on 10.12.13.
 */
public class ViewHelper {

    public static boolean isLargeScreen(Context context) {
        return context != null && (context.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) >=
                Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static boolean isLandscape(Activity activity) {
        if (activity != null) {
            Point size = new Point();
            activity.getWindowManager().getDefaultDisplay().getSize(size);
            int width = size.x;
            int height = size.y;
            return width > height;
        }
        return false;
    }

    public static void showSnackbar(View parentView, String text) {
        Snackbar.make(parentView, text, Snackbar.LENGTH_LONG).show();
    }

    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
}
