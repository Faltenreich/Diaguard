package com.faltenreich.diaguard.shared.view.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.faltenreich.diaguard.shared.data.file.FileUtils;
import com.squareup.picasso.Picasso;

import java.io.File;

public class PicassoLoader implements ImageLoading {

    private static final String TAG = PicassoLoader.class.getSimpleName();

    private final Picasso picasso;

    public PicassoLoader(Context context) {
        picasso = new Picasso.Builder(context).build();
    }

    @Override
    public void load(int imageRes, int placeholderRes, ImageView imageView, Bitmap.Config config) {
        try {
            picasso.load(imageRes).placeholder(placeholderRes).config(config).into(imageView);
        } catch (Exception exception) {
            Log.e(TAG, exception.toString());
        }
    }

    @Override
    public void load(int imageRes, ImageView imageView) {
        try {
            picasso.load(imageRes).into(imageView);
        } catch (Exception exception) {
            Log.e(TAG, exception.toString());
        }
    }

    public void clearDiskCache(Context context) {
        File cache = new File(context.getApplicationContext().getCacheDir(), "picasso-cache");
        if (cache.exists() && cache.isDirectory()) {
            FileUtils.deleteDirectory(cache);
        }
    }
}
