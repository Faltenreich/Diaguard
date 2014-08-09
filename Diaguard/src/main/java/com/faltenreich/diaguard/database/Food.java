package com.faltenreich.diaguard.database;

import com.faltenreich.diaguard.helpers.Helper;

import java.text.ParseException;
import java.util.Calendar;

/**
 * Created by Filip on 09.08.14.
 */
public class Food {

    private long id;
    private float carbohydrates;
    private String name;
    private Calendar date;
    private long eventId;

    public long getId() { return id; }

    public void setId(long id) {
        this.id = id;
    }

    public float getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(float carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }
}
