package com.faltenreich.diaguard.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.R;

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

    public static void showSnackbar(View parentView, String text, View.OnClickListener onClickListener) {
        int actionTextColor = ContextCompat.getColor(DiaguardApplication.getContext(), R.color.green_light);
        Snackbar.make(parentView, text, Snackbar.LENGTH_LONG)
                .setAction(R.string.undo, onClickListener)
                .setActionTextColor(actionTextColor)
                .show();
    }

    public static void showToast(Context context, String text) {
        showToast(context, text, Toast.LENGTH_LONG);
    }

    public static void showToast(Context context, @StringRes int stringResId) {
        showToast(context, context.getString(stringResId), Toast.LENGTH_SHORT);
    }

    public static void showToast(Context context, String text, int duration) {
        Toast.makeText(context, text, duration).show();
    }

    public static int getDefaultTextColor() {
        return new TextView(DiaguardApplication.getContext()).getTextColors().getDefaultColor();
    }
}
