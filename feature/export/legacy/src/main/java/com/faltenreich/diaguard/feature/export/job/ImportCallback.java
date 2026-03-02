package com.faltenreich.diaguard.feature.export.job;

public interface ImportCallback {
    void onSuccess(String mimeType);
    void onError();
}
