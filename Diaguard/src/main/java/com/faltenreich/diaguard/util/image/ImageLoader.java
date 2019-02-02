package com.faltenreich.diaguard.util.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.faltenreich.diaguard.util.FileUtils;
import com.squareup.picasso.Picasso;

import java.io.File;

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

    public void clearCache(Context context) {
        File cache = new File(context.getApplicationContext().getCacheDir(), "picasso-cache");
        if (cache.exists() && cache.isDirectory()) {
            FileUtils.deleteDirectory(cache);
        }
    }


    public void load(@DrawableRes int imageRes, @DrawableRes int placeholderRes, ImageView imageView, Bitmap.Config config) {
        picasso.load(imageRes).placeholder(placeholderRes).config(config).into(imageView);
    }

    public void load(@DrawableRes int imageRes, ImageView imageView) {
        picasso.load(imageRes).into(imageView);
    }
}
