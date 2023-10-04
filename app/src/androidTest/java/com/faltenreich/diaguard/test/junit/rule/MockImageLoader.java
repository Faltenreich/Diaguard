package com.faltenreich.diaguard.test.junit.rule;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.faltenreich.diaguard.shared.view.image.ImageLoader;
import com.faltenreich.diaguard.shared.view.image.ImageLoading;

public class MockImageLoader extends TestRule {

    @Override
    public void applyBeforeTest() {
        ImageLoader.getInstance().init(new MockLoader());
    }

    @Override
    public void applyAfterTest() {}

    private static class MockLoader implements ImageLoading {

        @Override
        public void load(int imageRes, int placeholderRes, ImageView imageView, Bitmap.Config config) {}

        @Override
        public void load(int imageRes, ImageView imageView) {}

        @Override
        public void clearDiskCache(Context context) {}
    }
}
