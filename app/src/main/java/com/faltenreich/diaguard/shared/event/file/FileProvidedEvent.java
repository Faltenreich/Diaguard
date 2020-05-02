package com.faltenreich.diaguard.shared.event.file;

import android.net.Uri;

import com.faltenreich.diaguard.shared.event.BaseContextEvent;

public class FileProvidedEvent extends BaseContextEvent<Uri> {

    public FileProvidedEvent(Uri context) {
        super(context);
    }
}
