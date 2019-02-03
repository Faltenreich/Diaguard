package com.faltenreich.diaguard.util.export;

import androidx.annotation.Nullable;

import java.io.File;

/**
 * Created by Filip on 07.09.2014.
 */
public interface FileListener {
    void onProgress(String message);
    void onSuccess(@Nullable File file, String mimeType);
    void onError();
}
