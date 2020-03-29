package com.faltenreich.diaguard.shared.view.resource;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;

import com.faltenreich.diaguard.R;

public class ColorUtils {

    @ColorInt
    private static int getColor(Context context, @AttrRes int attrRes) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        boolean wasResolved = theme.resolveAttribute(attrRes, typedValue, true);
        return wasResolved ? typedValue.resourceId == 0 ? typedValue.data : ContextCompat.getColor(context, typedValue.resourceId) : -1;
    }

    @ColorInt
    public static int getPrimaryColor(Context context) {
        return getColor(context, R.attr.colorPrimary);
    }

    @ColorInt
    public static int getPrimaryDarkColor(Context context) {
        return getColor(context, R.attr.colorPrimaryDark);
    }

    @ColorInt
    public static int getAccentColor(Context context) {
        return getColor(context, R.attr.colorAccent);
    }

    @ColorInt
    public static int getTextColorPrimary(Context context) {
        return getColor(context, android.R.attr.textColorPrimary);
    }

    @ColorInt
    public static int getTextColorSecondary(Context context) {
        return getColor(context, android.R.attr.textColorSecondary);
    }

    @ColorInt
    public static int getBackgroundPrimary(Context context) {
        return getColor(context, R.attr.backgroundColorPrimary);
    }

    @ColorInt
    public static int getBackgroundSecondary(Context context) {
        return getColor(context, R.attr.backgroundColorSecondary);
    }

    @ColorInt
    public static int getBackgroundTertiary(Context context) {
        return getColor(context, R.attr.backgroundColorTertiary);
    }

    @ColorInt
    public static int getBackgroundPrimaryTranslucent(Context context) {
        return getColor(context, R.attr.backgroundColorPrimaryTranslucent);
    }
}
