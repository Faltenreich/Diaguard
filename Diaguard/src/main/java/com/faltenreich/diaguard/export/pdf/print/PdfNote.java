package com.faltenreich.diaguard.export.pdf.print;

import org.joda.time.DateTime;

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
