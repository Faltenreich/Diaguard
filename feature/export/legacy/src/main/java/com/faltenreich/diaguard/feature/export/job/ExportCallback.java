package com.faltenreich.diaguard.feature.export.job;

import androidx.annotation.NonNull;

import java.io.File;

public interface ExportCallback {
    void onProgress(@NonNull String message);
    void onSuccess(@NonNull File file, @NonNull String mimeType);
    void onError(@NonNull String message);
}
