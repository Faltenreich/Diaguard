package com.faltenreich.diaguard.database;

import com.faltenreich.diaguard.helpers.Helper;

import java.text.ParseException;
import java.util.Calendar;

/**
 * Created by Filip on 20.10.13.
 */
public class Event {

    private long id;
    private float value;
    private Calendar date;
    private String notes;
    private Category category;

    public enum Category {
        BloodSugar,
        Bolus,
        Meal,
        Activity,
        HbA1c,
        Weight,
        Pulse
    }

    public long getId() { return id; }

    public void setId(long id) {
        this.id = id;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
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

    public Category getCategory() { return category; }

    public void setCategory(Category category) { this.category = category; }

    public String getNotes() { return notes; }

    public void setNotes(String notes) { this.notes = notes; }
}
