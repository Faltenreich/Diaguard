package com.faltenreich.diaguard.database;

import com.faltenreich.diaguard.helpers.Helper;

import java.text.ParseException;
import java.util.Calendar;

/**
 * Created by Filip on 09.08.2014.
 */
public class Entry {

    private long id;
    private Calendar date;
    private String note;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public void setDate(String dateString) {
        try{
            this.date = Calendar.getInstance();
            this.date.setTime(Helper.getDateDatabaseFormat().parse(dateString));
        }
        catch (ParseException ex) {
            ex.printStackTrace();
        }
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
