package com.faltenreich.diaguard.shared.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.faltenreich.diaguard.R;
import com.google.android.material.snackbar.Snackbar;

public class ViewUtils {

    private static final String TAG = ViewUtils.class.getSimpleName();

    public static Point getPositionInParent(View view, ViewGroup parent) {
        Rect offsetViewBounds = new Rect();
        view.getDrawingRect(offsetViewBounds);
        parent.offsetDescendantRectToMyCoords(view, offsetViewBounds);
        return new Point(offsetViewBounds.left, offsetViewBounds.top);
    }

    public static void showKeyboard(View view) {
        view.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }

    public static void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            view.requestFocus();
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            view.clearFocus();
        }
    }

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

    private static Snackbar createSnackbar(View parentView, String text) {
        return Snackbar.make(parentView, text, Snackbar.LENGTH_LONG);
    }

    public static void showSnackbar(View parentView, String text) {
        createSnackbar(parentView, text).show();
    }

    public static void showSnackbar(View parentView, String text, View.OnClickListener onClickListener) {
        createSnackbar(parentView, text)
            .setAction(R.string.undo, onClickListener)
            .setActionTextColor(ContextCompat.getColor(parentView.getContext(), R.color.green_light))
            .show();
    }

    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    public static void setChecked(CheckBox checkBox, boolean isChecked, boolean animated) {
        checkBox.setChecked(isChecked);
        if (!animated) {
            try {
                // Workaround: Calling jumpDrawablesToCurrentState() after setChecked() skips the animation
                checkBox.jumpDrawablesToCurrentState();
            } catch (Exception exception) {
                Log.e(TAG, exception.toString());
            }
        }
    }
}
