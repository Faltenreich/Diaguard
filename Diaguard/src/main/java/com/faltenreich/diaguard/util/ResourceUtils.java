package com.faltenreich.diaguard.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

import com.faltenreich.diaguard.R;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorRes;

public class ResourceUtils {

    private static int getAttribute(Context context, @AttrRes int attrRes) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        boolean wasResolved = theme.resolveAttribute(attrRes, typedValue, true);
        return wasResolved ? typedValue.resourceId : -1;
    }

    @ColorRes
    // Light: black, dark: white
    public static int getTextColorPrimary(Context context) {
        return getAttribute(context, android.R.attr.textColorPrimary);
    }

    @ColorRes
    // Light: dark-gray, dark: light-gray
    public static int getTextColorSecondary(Context context) {
        return getAttribute(context, android.R.attr.textColorSecondary);
    }

    @ColorRes
    public static int getBackgroundPrimary(Context context) {
        return getAttribute(context, R.attr.backgroundColorPrimary);
    }

    @ColorRes
    public static int getBackgroundSecondary(Context context) {
        return getAttribute(context, R.attr.backgroundColorSecondary);
    }

    @ColorRes
    public static int getBackgroundTertiary(Context context) {
        return getAttribute(context, R.attr.backgroundColorTertiary);
    }

    @ColorRes
    public static int getBackgroundPrimaryTranslucent(Context context) {
        return getAttribute(context, R.attr.backgroundColorPrimaryTranslucent);
    }
}
