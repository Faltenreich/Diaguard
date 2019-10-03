package com.faltenreich.diaguard.export.pdf;

import org.joda.time.DateTime;

/**
 * Created by Faltenreich on 25.08.2018
 */
class PdfNote {
    private DateTime dateTime;
    private String note;

    PdfNote(DateTime dateTime, String note) {
        this.dateTime = dateTime;
        this.note = note;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public String getNote() {
        return note;
    }
}
