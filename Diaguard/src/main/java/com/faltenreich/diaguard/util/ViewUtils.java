package com.faltenreich.diaguard.util;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Build;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.numberpicker.NumberPickerBuilder;
import com.codetroopers.betterpickers.numberpicker.NumberPickerDialogFragment;
import com.faltenreich.diaguard.R;

import java.math.BigDecimal;

@SuppressWarnings({"WeakerAccess", "unused"})
public class ViewUtils {

    private static final int REVEAL_DURATION = 400;
    private static final int UNREVEAL_DURATION = 300;

    public static void showKeyboard(View view) {
        view.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }

    public static void hideKeyboard(View view) {
        view.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        view.clearFocus();
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getCurrentFocus() != null) {
            hideKeyboard(activity.getCurrentFocus());
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

    public static void showToast(Context context, @StringRes int stringResId) {
        showToast(context, context.getString(stringResId));
    }

    public static int getDefaultTextColor(Context context) {
        return new TextView(context).getTextColors().getDefaultColor();
    }

    public static void showNumberPicker(AppCompatActivity activity, @StringRes int labelResId, int initialValue, int minValue, int maxValue, NumberPickerDialogFragment.NumberPickerDialogHandlerV2 listener) {
        new NumberPickerBuilder()
                .setFragmentManager(activity.getSupportFragmentManager())
                .setStyleResId(R.style.NumberPicker)
                .setLabelText(activity.getString(labelResId))
                .setPlusMinusVisibility(View.GONE)
                .setDecimalVisibility(View.GONE)
                .setMaxNumber(BigDecimal.valueOf(maxValue))
                .setMinNumber(BigDecimal.valueOf(minValue))
                .addNumberPickerDialogHandler(listener)
                .setCurrentNumber(initialValue > 0 ? initialValue : null)
                .show();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void reveal(final View view, int positionX, int positionY, final boolean reveal, int duration, Animator.AnimatorListener listener) {
        int radius = (int) Math.hypot(view.getWidth(), view.getHeight());
        int startRadius = reveal ? 0 : radius;
        int endRadius = reveal ? radius : 0;

        Animator animator = ViewAnimationUtils.createCircularReveal(view, positionX, positionY, startRadius, endRadius);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.setDuration(duration);

        if (listener != null) {
            animator.addListener(listener);
        }

        animator.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void reveal(View view, int positionX, int positionY, boolean reveal, Animator.AnimatorListener listener) {
        reveal(view, positionX, positionY, reveal, reveal ? REVEAL_DURATION : UNREVEAL_DURATION, listener);
    }

    public static Vector2D getPositionOnScreen(View view) {
        int[] screenLocation = new int[2];
        view.getLocationOnScreen(screenLocation);
        return new Vector2D(screenLocation[0], screenLocation[1]);
    }

    @Nullable
    public static CoordinatorLayout.Behavior getBehavior(View view) {
        if (view.getLayoutParams() instanceof CoordinatorLayout.LayoutParams) {
            return  ((CoordinatorLayout.LayoutParams) view.getLayoutParams()).getBehavior();
        }
        return null;
    }
}
