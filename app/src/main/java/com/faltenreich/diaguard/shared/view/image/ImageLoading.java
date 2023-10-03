package com.faltenreich.diaguard.shared.view.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;

public interface ImageLoading {

    void load(@DrawableRes int imageRes, @DrawableRes int placeholderRes, ImageView imageView, Bitmap.Config config);

    void load(@DrawableRes int imageRes, ImageView imageView);

    void clearDiskCache(Context context);
}
