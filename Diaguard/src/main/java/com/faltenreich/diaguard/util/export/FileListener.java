package com.faltenreich.diaguard.util.export;

import android.support.annotation.Nullable;

import java.io.File;

/**
 * Created by Filip on 07.09.2014.
 */
public interface FileListener {
    void onProgress(String message);
    void onComplete(@Nullable File file, String mimeType);
}
