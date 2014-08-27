package com.faltenreich.diaguard.database;

import com.faltenreich.diaguard.helpers.Helper;

import org.joda.time.DateTime;

/**
 * Created by Filip on 09.08.2014.
 */
public class Entry extends Model {
    private DateTime date;
    private String note;

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public void setDate(String dateString) {
        this.date = new DateTime(Helper.getDateDatabaseFormat().parseDateTime(dateString));
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String getTableName() {
        return DatabaseHelper.ENTRY;
    }
}
