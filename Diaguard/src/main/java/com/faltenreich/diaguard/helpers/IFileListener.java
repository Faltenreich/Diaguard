package com.faltenreich.diaguard.helpers;

import java.io.File;

/**
 * Created by Filip on 07.09.2014.
 */
public interface IFileListener {
    public void handleFile(File file, String mimeType);
}
