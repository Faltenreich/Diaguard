package com.faltenreich.diaguard.shared.view.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;

import com.faltenreich.diaguard.shared.data.file.FileUtils;
import com.squareup.picasso.Picasso;

import java.io.File;

public class ImageLoader {

    private static final String TAG = ImageLoader.class.getSimpleName();

    private static ImageLoader instance;

    public static ImageLoader getInstance() {
        if (instance == null) {
            instance = new ImageLoader();
        }
        return instance;
    }

    private Picasso picasso;

    private ImageLoader() {

    }

    public void init(Context context) {
        picasso = new Picasso.Builder(context).build();
    }

    public void clearDiskCache(Context context) {
        File cache = new File(context.getApplicationContext().getCacheDir(), "picasso-cache");
        if (cache.exists() && cache.isDirectory()) {
            FileUtils.deleteDirectory(cache);
        }
    }


    public void load(@DrawableRes int imageRes, @DrawableRes int placeholderRes, ImageView imageView, Bitmap.Config config) {
        try {
            picasso.load(imageRes).placeholder(placeholderRes).config(config).into(imageView);
        } catch (Exception exception) {
            Log.e(TAG, exception.toString());
        }
    }

    public void load(@DrawableRes int imageRes, ImageView imageView) {
        try {
            picasso.load(imageRes).into(imageView);
        } catch (Exception exception) {
            Log.e(TAG, exception.toString());
        }
    }
}
