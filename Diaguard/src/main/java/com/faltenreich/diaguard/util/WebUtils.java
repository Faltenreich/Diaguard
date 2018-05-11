package com.faltenreich.diaguard.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class WebUtils {

    public static void openUri(Context context, Uri uri) {
        context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }
}
