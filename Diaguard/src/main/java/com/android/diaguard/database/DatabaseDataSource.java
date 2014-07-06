package com.android.diaguard.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.diaguard.helpers.Helper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Filip on 20.10.13.
 */
public class DatabaseDataSource {
    public static final String DB_FORMAT_DATE = "yyyy-MM-dd";
    public static final String DB_FORMAT_DATE_AND_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String DB_FORMAT_TIME = "HH:mm";

    public static final String FIRST_SECOND_OF_DAY = "00:00:00";
    public static final String LAST_SECOND_OF_DAY = "23:59:59";

    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public DatabaseDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    //region Database

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public boolean isOpen() {
        return dbHelper.getWritableDatabase() != null &&
                dbHelper.getWritableDatabase().isOpen();
    }

    // endregion

    //region Events

    public long insertEvent(Event event) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.VALUE, event.getValue());
        values.put(DatabaseHelper.DATE,
                Helper.getDateDatabaseFormat().format(event.getDate().getTime()));
        values.put(DatabaseHelper.NOTES, event.getNotes());
        values.put(DatabaseHelper.CATEGORY, event.getCategory().toString());

        return db.insertOrThrow(DatabaseHelper.EVENTS,
                null,
                values);
    }

    public int insertEvents(List<Event> events) {
        for(Event event: events)
            insertEvent(event);

        return events.size();
    }

    public long updateEvent(Event event) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.VALUE, event.getValue());
        values.put(DatabaseHelper.DATE, Helper.getDateDatabaseFormat().
                format(event.getDate().getTime()));
        values.put(DatabaseHelper.NOTES, event.getNotes());
        values.put(DatabaseHelper.CATEGORY, event.getCategory().toString());

        return db.update(DatabaseHelper.EVENTS,
                values,
                DatabaseHelper.ID + " = " + event.getId(),
                null);
    }

    public int deleteEvent(Event event) {
        return db.delete(DatabaseHelper.EVENTS,
                DatabaseHelper.ID + " = " + event.getId(),
                null);
    }

    public int deleteEventById(Long id) {
        return db.delete(DatabaseHelper.EVENTS,
                DatabaseHelper.ID + " = " + id,
                null);
    }

    public int deleteEventsBefore(Calendar calendar) {
        SimpleDateFormat format = new SimpleDateFormat(DB_FORMAT_DATE);
        String date = format.format(calendar.getTime());

        return db.delete(DatabaseHelper.EVENTS,
                DatabaseHelper.DATE + " <= Datetime('" + date + " " + LAST_SECOND_OF_DAY + "')",
                null);
    }

    public int countEvents() {
        String query = "SELECT * FROM " + DatabaseHelper.EVENTS;
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public int countEvents(Event.Category category) {
        String query = "SELECT * FROM " + DatabaseHelper.EVENTS + " WHERE " +
                DatabaseHelper.CATEGORY + " = '" + category.name() + "' ";
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public int countEvents(Event.Category category, Calendar day) {
        SimpleDateFormat format = new SimpleDateFormat(DB_FORMAT_DATE);
        String date = format.format(day.getTime());

        String query = "SELECT * FROM " + DatabaseHelper.EVENTS + " WHERE " +
                DatabaseHelper.DATE + " >= Datetime('" + date + " " + FIRST_SECOND_OF_DAY + "') AND " +
                DatabaseHelper.DATE + " <= Datetime('" + date + " " + LAST_SECOND_OF_DAY + "') AND " +
                DatabaseHelper.CATEGORY + " = '" + category.name() + "' ";
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public int countEventsBefore(Calendar calendar) {
        SimpleDateFormat format = new SimpleDateFormat(DB_FORMAT_DATE);
        String date = format.format(calendar.getTime());

        String query = "SELECT * FROM " + DatabaseHelper.EVENTS + " WHERE " +
                DatabaseHelper.DATE + " <= Datetime('" + date + " " + LAST_SECOND_OF_DAY + "')";
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public int countEventsAboveValue(Event.Category category, Calendar day, float limit) {
        SimpleDateFormat format = new SimpleDateFormat(DB_FORMAT_DATE);
        String date = format.format(day.getTime());

        String query = "SELECT * FROM " + DatabaseHelper.EVENTS + " WHERE " +
                DatabaseHelper.VALUE + " > " + limit + " AND " +
                DatabaseHelper.DATE + " >= Datetime('" + date + " " + FIRST_SECOND_OF_DAY + "') AND " +
                DatabaseHelper.DATE + " <= Datetime('" + date + " " + LAST_SECOND_OF_DAY + "') AND " +
                DatabaseHelper.CATEGORY + " = '" + category.name() + "' ";
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public int countEventsBelowValue(Event.Category category, Calendar day, float limit) {
        SimpleDateFormat format = new SimpleDateFormat(DB_FORMAT_DATE);
        String date = format.format(day.getTime());

        String query = "SELECT * FROM " + DatabaseHelper.EVENTS + " WHERE " +
                DatabaseHelper.VALUE + " < " + limit + " AND " +
                DatabaseHelper.DATE + " >= Datetime('" + date + " " + FIRST_SECOND_OF_DAY + "') AND " +
                DatabaseHelper.DATE + " <= Datetime('" + date + " " + LAST_SECOND_OF_DAY + "') AND " +
                DatabaseHelper.CATEGORY + " = '" + category.name() + "' ";
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public Event getEventById(long id) {
        String query = "SELECT * FROM " + DatabaseHelper.EVENTS + " WHERE " +
                DatabaseHelper.ID + " = " + id;
        Cursor cursor = db.rawQuery(query, null);

        Event event = new Event();

        if (cursor.moveToFirst()) {
            event.setId(Integer.parseInt(cursor.getString(0)));
            event.setValue(Float.parseFloat(cursor.getString(1)));
            event.setDate(cursor.getString(2));
            event.setNotes(cursor.getString(3));
            event.setCategory(Event.Category.valueOf(cursor.getString(4)));
        }
        return event;
    }

    public List<Event> getEvents() {
        String query = "SELECT * FROM " + DatabaseHelper.EVENTS + " ORDER BY " + DatabaseHelper.DATE;
        Cursor cursor = db.rawQuery(query, null);

        List<Event> events = new ArrayList<Event>();
        if (cursor.moveToFirst()) {
            while(!cursor.isAfterLast()) {
                Event event = new Event();
                event.setId(Integer.parseInt(cursor.getString(0)));
                event.setValue(Float.parseFloat(cursor.getString(1)));
                event.setDate(cursor.getString(2));
                event.setNotes(cursor.getString(3));
                event.setCategory(Event.Category.valueOf(cursor.getString(4)));
                events.add(event);

                cursor.moveToNext();
            }
        }
        return events;
    }

    public Event getLatestEvent(Event.Category category) {
        String query = "SELECT * FROM " + DatabaseHelper.EVENTS + " WHERE " +
                DatabaseHelper.CATEGORY + " = '" + category.name() + "' " +
                "ORDER BY " + DatabaseHelper.DATE + " DESC LIMIT 1;";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToLast()) {
            Event event = new Event();
            event.setId(Integer.parseInt(cursor.getString(0)));
            event.setValue(Float.parseFloat(cursor.getString(1)));
            event.setDate(cursor.getString(2));
            event.setNotes(cursor.getString(3));
            event.setCategory(Event.Category.valueOf(cursor.getString(4)));
            return event;
        }
        else
            return null;
    }

    public List<Event> getEventsOfDay(Calendar date) {
        String dateString = new SimpleDateFormat(DB_FORMAT_DATE).format(date.getTime());

        String query = "SELECT * FROM " + DatabaseHelper.EVENTS + " WHERE " +
                DatabaseHelper.DATE + " >= Datetime('" + dateString + " " + FIRST_SECOND_OF_DAY + "') AND " +
                DatabaseHelper.DATE + " <= Datetime('" + dateString + " " + LAST_SECOND_OF_DAY + "') " +
                "ORDER BY " + DatabaseHelper.DATE + ";";
        Cursor cursor = db.rawQuery(query, null);

        List<Event> events = new ArrayList<Event>();

        if (cursor.moveToFirst()) {
            while(!cursor.isAfterLast()) {

                Event event = new Event();
                event.setId(Integer.parseInt(cursor.getString(0)));
                event.setValue(Float.parseFloat(cursor.getString(1)));
                event.setDate(cursor.getString(2));
                event.setNotes(cursor.getString(3));
                event.setCategory(Event.Category.valueOf(cursor.getString(4)));

                events.add(event);

                cursor.moveToNext();
            }
        }
        return events;
    }

    public List<Event> getEventsOfDay(Calendar date, Event.Category category) {

        String dateString = new SimpleDateFormat(DB_FORMAT_DATE).format(date.getTime());

        String query = "SELECT * FROM " + DatabaseHelper.EVENTS + " WHERE " +
                DatabaseHelper.DATE + " >= Datetime('" + dateString + " " + FIRST_SECOND_OF_DAY + "') AND " +
                DatabaseHelper.DATE + " <= Datetime('" + dateString + " " + LAST_SECOND_OF_DAY + "') AND " +
                DatabaseHelper.CATEGORY + " = '" + category.name() + "' " +
                " ORDER BY " + DatabaseHelper.DATE;
        Cursor cursor = db.rawQuery(query, null);

        List<Event> events = new ArrayList<Event>();

        if (cursor.moveToFirst()) {

            while(!cursor.isAfterLast()) {

                Event event = new Event();
                event.setId(Integer.parseInt(cursor.getString(0)));
                event.setValue(Float.parseFloat(cursor.getString(1)));
                event.setDate(cursor.getString(2));
                event.setNotes(cursor.getString(3));
                event.setCategory(Event.Category.valueOf(cursor.getString(4)));

                events.add(event);

                cursor.moveToNext();
            }
        }
        return events;
    }

    public List<Event> getEventsOfDay(Calendar date, Event.Category[] categories) {

        String dateString = new SimpleDateFormat(DB_FORMAT_DATE).format(date.getTime());

        String whereCategory = "";
        for(Event.Category category : categories) {
            whereCategory = whereCategory + ",'" + category.name() + "'";
        }
        whereCategory = whereCategory.substring(1);

        String query = "SELECT * FROM " + DatabaseHelper.EVENTS + " WHERE " +
                DatabaseHelper.DATE + " >= Datetime('" + dateString + " " + FIRST_SECOND_OF_DAY + "') AND " +
                DatabaseHelper.DATE + " <= Datetime('" + dateString + " " + LAST_SECOND_OF_DAY + "') AND " +
                DatabaseHelper.CATEGORY + " in (" + whereCategory + ") " +
                " ORDER BY " + DatabaseHelper.DATE;
        Cursor cursor = db.rawQuery(query, null);

        List<Event> events = new ArrayList<Event>();

        if (cursor.moveToFirst()) {

            while(!cursor.isAfterLast()) {

                Event event = new Event();
                event.setId(Integer.parseInt(cursor.getString(0)));
                event.setValue(Float.parseFloat(cursor.getString(1)));
                event.setDate(cursor.getString(2));
                event.setNotes(cursor.getString(3));
                event.setCategory(Event.Category.valueOf(cursor.getString(4)));

                events.add(event);

                cursor.moveToNext();
            }
        }
        return events;
    }

    public float getBloodSugarAverage(int rangeInDays) {
        SimpleDateFormat format = new SimpleDateFormat(DB_FORMAT_DATE_AND_TIME);
        Calendar now = Calendar.getInstance();
        Calendar dateBefore = Calendar.getInstance();
        dateBefore.set(Calendar.HOUR_OF_DAY, now.get(Calendar.HOUR_OF_DAY) - (rangeInDays * 24));

        String query = "SELECT AVG(" + DatabaseHelper.VALUE + ") FROM " + DatabaseHelper.EVENTS + " WHERE " +
                DatabaseHelper.DATE + " >= Datetime('" + format.format(dateBefore.getTime()) + "') AND " +
                DatabaseHelper.DATE + " <= Datetime('" + format.format(now.getTime()) + "') AND " +
                DatabaseHelper.CATEGORY + " = '" + Event.Category.BloodSugar.toString() + "' ";
        Cursor cursor = db.rawQuery(query, null);

        float average = 0;
        if(cursor.moveToFirst())
            average = cursor.getFloat(0);

        return average;
    }

    public float[][] getAverageDataTable(Calendar date, Event.Category[] categories, int columns) {
        float[][] values = new float[categories.length][columns];
        List<Event> events = getEventsOfDay(date, categories);

        int counter = 1;
        for(Event event : events) {
            int category = Arrays.asList(categories).indexOf(event.getCategory());
            int hour = event.getDate().get(Calendar.HOUR_OF_DAY) / 2;
            float oldValue = values[category][hour];
            float newValue;

            // Sum of everything but Blood Sugar (average)
            if(event.getCategory() == Event.Category.BloodSugar ||
                    event.getCategory() == Event.Category.HbA1c ||
                    event.getCategory() == Event.Category.Weight ||
                    event.getCategory() == Event.Category.Pulse) {
                if(oldValue > 0) {
                    // Calculate Average per hour
                    newValue = ((oldValue * counter) + (event.getValue())) / (counter + 1);
                    counter++;
                }
                else {
                    newValue = event.getValue();
                    counter = 1;
                }
            }
            else
                newValue = oldValue + event.getValue();

            values[category][hour] = newValue;
        }
        return values;
    }

    // endregion
}
