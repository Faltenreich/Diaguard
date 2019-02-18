package com.faltenreich.diaguard.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import androidx.annotation.RawRes;

public class WebUtils {
    private static final String TAG = WebUtils.class.getSimpleName();

    public static void openUri(Context context, Uri uri) {
        context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    public static String loadHtml(Context context, @RawRes int resource) {
        InputStream inputStream = context.getResources().openRawResource(resource);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            int input = inputStream.read();
            while (input != -1) {
                byteArrayOutputStream.write(input);
                input = inputStream.read();
            }
            inputStream.close();
        } catch (IOException exception) {
            Log.e(TAG, exception.getMessage(), exception);
        }
        return byteArrayOutputStream.toString();
    }
}
