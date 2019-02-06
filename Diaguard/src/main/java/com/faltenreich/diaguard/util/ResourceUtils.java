package com.faltenreich.diaguard.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

import com.faltenreich.diaguard.R;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;

public class ResourceUtils {

    @ColorInt
    private static int getAttribute(Context context, @AttrRes int attrRes) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        boolean wasResolved = theme.resolveAttribute(attrRes, typedValue, true);
        return wasResolved ? typedValue.resourceId == 0 ? typedValue.data : ContextCompat.getColor(context, typedValue.resourceId) : -1;
    }

    @ColorInt
    // Light: black, dark: white
    public static int getTextColorPrimary(Context context) {
        return getAttribute(context, android.R.attr.textColorPrimary);
    }

    @ColorInt
    // Light: dark-gray, dark: light-gray
    public static int getTextColorSecondary(Context context) {
        return getAttribute(context, android.R.attr.textColorSecondary);
    }

    @ColorInt
    public static int getBackgroundPrimary(Context context) {
        return getAttribute(context, R.attr.backgroundColorPrimary);
    }

    @ColorInt
    public static int getBackgroundSecondary(Context context) {
        return getAttribute(context, R.attr.backgroundColorSecondary);
    }

    @ColorInt
    public static int getBackgroundTertiary(Context context) {
        return getAttribute(context, R.attr.backgroundColorTertiary);
    }

    @ColorInt
    public static int getBackgroundPrimaryTranslucent(Context context) {
        return getAttribute(context, R.attr.backgroundColorPrimaryTranslucent);
    }
}
