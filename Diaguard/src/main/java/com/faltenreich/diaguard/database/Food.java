package com.faltenreich.diaguard.database;

import com.faltenreich.diaguard.helpers.Helper;

import org.joda.time.DateTime;

/**
 * Created by Filip on 09.08.14.
 */
public class Food extends Model {

    private float carbohydrates;
    private String name;
    private DateTime date;
    private long measurementId;

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

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public void setDate(String dateString) {
        this.date = Helper.getDateDatabaseFormat().parseDateTime(dateString);
    }

    public long getMeasurementId() {
        return measurementId;
    }

    public void setMeasurementId(long measurementId) {
        this.measurementId = measurementId;
    }

    @Override
    public String getTableName() {
        return DatabaseHelper.FOOD;
    }
}
