package com.faltenreich.diaguard.util;

import java.io.File;

/**
 * Created by Filip on 07.09.2014.
 */
public interface IFileListener {
    void onProgress(String message);
    void onComplete(File file, String mimeType);
}
