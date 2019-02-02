package com.squareup.picasso;

public class PicassoUtils {

    // Workaround for accessing package-private cache
    public static void clearCache(Picasso picasso) {
        picasso.cache.clear();
    }
}
