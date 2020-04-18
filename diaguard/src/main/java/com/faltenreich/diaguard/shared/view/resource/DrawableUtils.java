package com.faltenreich.diaguard.shared.view.resource;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.annotation.Dimension;

public class DrawableUtils {

    public static Drawable resize(Context context, Drawable image, @Dimension int size) {
        Bitmap bitmap = ((BitmapDrawable) image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(bitmap, size, size, false);
        return new BitmapDrawable(context.getResources(), bitmapResized);
    }
}
