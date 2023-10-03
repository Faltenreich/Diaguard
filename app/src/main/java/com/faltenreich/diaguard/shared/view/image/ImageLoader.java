package com.faltenreich.diaguard.shared.view.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;

public class ImageLoader implements ImageLoading {

    private static ImageLoader instance;

    public static ImageLoader getInstance() {
        if (instance == null) {
            instance = new ImageLoader();
        }
        return instance;
    }

    private ImageLoading proxy;

    private ImageLoader() {}

    public void init(ImageLoading proxy) {
        this.proxy = proxy;
    }

    @Override
    public void load(@DrawableRes int imageRes, @DrawableRes int placeholderRes, ImageView imageView, Bitmap.Config config) {
        proxy.load(imageRes, placeholderRes, imageView, config);
    }

    @Override
    public void load(@DrawableRes int imageRes, ImageView imageView) {
        proxy.load(imageRes, imageView);
    }

    @Override
    public void clearDiskCache(Context context) {
        proxy.clearDiskCache(context);
    }
}
