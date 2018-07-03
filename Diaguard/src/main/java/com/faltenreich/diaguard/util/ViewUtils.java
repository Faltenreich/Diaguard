package com.faltenreich.diaguard.util;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.numberpicker.NumberPickerBuilder;
import com.codetroopers.betterpickers.numberpicker.NumberPickerDialogFragment;
import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.R;

import java.math.BigDecimal;

import io.codetail.animation.ViewAnimationUtils;

@SuppressWarnings({"WeakerAccess", "unused"})
public class ViewUtils {

    private static final int ANIMATION_ROLL_DURATION = 400;
    private static final int REVEAL_DURATION = 800;
    private static final int UNREVEAL_DURATION = 600;

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
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    public static void showToast(Context context, @StringRes int stringResId) {
        showToast(context, context.getString(stringResId));
    }

    public static int getDefaultTextColor(Context context) {
        return new TextView(context).getTextColors().getDefaultColor();
    }

    public static void requestFocusShowKeyboard(View view) {
        view.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }

    public static void showNumberPicker(AppCompatActivity activity, @StringRes int labelResId, int initialValue, int minValue, int maxValue, NumberPickerDialogFragment.NumberPickerDialogHandlerV2 listener) {
        NumberPickerBuilder numberPicker = new NumberPickerBuilder()
                .setFragmentManager(activity.getSupportFragmentManager())
                .setStyleResId(R.style.NumberPicker)
                .setLabelText(activity.getString(labelResId))
                .setPlusMinusVisibility(View.GONE)
                .setDecimalVisibility(View.GONE)
                .setMaxNumber(BigDecimal.valueOf(maxValue))
                .setMinNumber(BigDecimal.valueOf(minValue))
                .addNumberPickerDialogHandler(listener);
        numberPicker.setCurrentNumber(initialValue);
        numberPicker.show();
    }


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

    public static void reveal(View view, int positionX, int positionY, boolean reveal, Animator.AnimatorListener listener) {
        reveal(view, positionX, positionY, reveal, reveal ? REVEAL_DURATION : UNREVEAL_DURATION, listener);
    }

    public static Vector2D getPositionOnScreen(View view) {
        int[] screenLocation = new int[2];
        view.getLocationOnScreen(screenLocation);
        return new Vector2D(screenLocation[0], screenLocation[1]);
    }
}
