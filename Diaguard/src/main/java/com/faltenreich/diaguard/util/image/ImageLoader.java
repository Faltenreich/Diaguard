package com.faltenreich.diaguard.util.image;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.PicassoUtils;

import androidx.annotation.DrawableRes;

public class ImageLoader {

    private static ImageLoader instance;

    public static ImageLoader getInstance() {
        if (instance == null) {
            instance = new ImageLoader();
        }
        return instance;
    }

    private Picasso picasso;

    private ImageLoader() {
        picasso = Picasso.get();
    }

    public void clearCache() {
        PicassoUtils.clearCache(picasso);
    }


    public void load(@DrawableRes int imageRes, @DrawableRes int placeholderRes, ImageView imageView, Bitmap.Config config) {
        picasso.load(imageRes).placeholder(placeholderRes).config(config).into(imageView);
    }

    public void load(@DrawableRes int imageRes, ImageView imageView) {
        picasso.load(imageRes).into(imageView);
    }
}
