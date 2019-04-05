package com.faltenreich.diaguard.data.event;

import android.net.Uri;

public class FileProvidedEvent extends BaseContextEvent<Uri> {

    public FileProvidedEvent(Uri context) {
        super(context);
    }
}
