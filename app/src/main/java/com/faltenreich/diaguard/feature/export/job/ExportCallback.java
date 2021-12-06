package com.faltenreich.diaguard.feature.export.job;

import java.io.File;

public interface ExportCallback {
    void onProgress(String message);
    void onSuccess(File file, String mimeType);
    void onError();
}
