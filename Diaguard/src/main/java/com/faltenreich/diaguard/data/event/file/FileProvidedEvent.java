package com.faltenreich.diaguard.data.event.file;

import android.net.Uri;

import com.faltenreich.diaguard.data.event.BaseContextEvent;

public class FileProvidedEvent extends BaseContextEvent<Uri> {

    public FileProvidedEvent(Uri context) {
        super(context);
    }
}
