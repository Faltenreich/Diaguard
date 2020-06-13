package com.faltenreich.diaguard.shared.view.resource;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

import androidx.annotation.AttrRes;
import androidx.core.content.ContextCompat;

@SuppressWarnings("SameParameterValue")
public class DrawableUtils {

    private static Drawable getDrawable(Context context, @AttrRes int attributeResourceId) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attributeResourceId, typedValue, true);
        int drawableResourceId = typedValue.resourceId;
        return ContextCompat.getDrawable(context, drawableResourceId);
    }

    public static Drawable getDivider(Context context) {
        return getDrawable(context, android.R.attr.listDivider);
    }
}
