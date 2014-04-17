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

    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public DatabaseDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public boolean isOpen() {
        return dbHelper.getWritableDatabase() != null && dbHelper.getWritableDatabase().isOpen();
    }

    public void close() {
        dbHelper.close();
    }

    // EVENTS

    /**
     * Insert one single Event
     * @param event The Event to insert
     * @return The ID of the inserted Event (-1 if something went wrong)
     */
    public long insertEvent(Event event) {

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.VALUE, event.getValue());
        values.put(DatabaseHelper.DATE, Helper.getDateDatabaseFormat().format(event.getDate().getTime()));
        values.put(DatabaseHelper.NOTES, event.getNotes());
        values.put(DatabaseHelper.CATEGORY, event.getCategory().toString());

        return db.insertOrThrow(DatabaseHelper.EVENTS, null, values);
    }

    /**
     * Insert multiple Events
     * @param events The Events to insert
     * @return The Number of inserted Events
     */
    public int insertEvents(List<Event> events) {

        for(Event event: events) {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.VALUE, event.getValue());
            values.put(DatabaseHelper.DATE, Helper.getDateDatabaseFormat().format(event.getDate().getTime()));
            values.put(DatabaseHelper.NOTES, event.getNotes());
            values.put(DatabaseHelper.CATEGORY, event.getCategory().toString());

            db.insertOrThrow(DatabaseHelper.EVENTS, null, values);
        }

        return events.size();
    }

    /**
     * Update an existing Event
     * @param event The Event to update
     * @return The ID of the updated Event (-1 if something went wrong)
     */
    public long updateEvent(Event event) {

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.VALUE, event.getValue());
        values.put(DatabaseHelper.DATE, Helper.getDateDatabaseFormat().format(event.getDate().getTime()));
        values.put(DatabaseHelper.NOTES, event.getNotes());
        values.put(DatabaseHelper.CATEGORY, event.getCategory().toString());

        return db.update(DatabaseHelper.EVENTS, values, DatabaseHelper.ID + " = " + event.getId(), null);
    }

    /**
     * Delete a single Event
     * @param event The Event to delete
     * @return How many Events did the WHERE-Clause affect
     */
    public int deleteEvent(Event event) {
        return db.delete(DatabaseHelper.EVENTS, DatabaseHelper.ID + " = " + event.getId(), null);
    }

    /**
     * Delete all Events before a specific Date
     * @param calendar The Date everything should be deleted before
     * @return How many Events did the WHERE-Clause affect
     */
    public int deleteEventsBefore(Calendar calendar) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(calendar.getTime());

        return db.delete(DatabaseHelper.EVENTS, DatabaseHelper.DATE + " <=Datetime('" + date + " 23:59:59')", null);
    }

    /**
     * Count all Events of a specific Category from a specific day
     * @return How many Events are affected
     */
    public int countEvents(Event.Category category) {

        String query = "SELECT * FROM " + DatabaseHelper.EVENTS + " WHERE " +
                DatabaseHelper.CATEGORY + " = '" + category.name() + "' ";
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    /**
     * Count all Events of a specific Category from a specific day
     * @return How many Events are affected
     */
    public int countEventsOfDay(Calendar day, Event.Category category) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(day.getTime());

        String query = "SELECT * FROM " + DatabaseHelper.EVENTS + " WHERE " +
                DatabaseHelper.DATE + " >= Datetime('" + date + " 00:00:00') AND " +
                DatabaseHelper.DATE + " <= Datetime('" + date + " 23:59:59') AND " +
                DatabaseHelper.CATEGORY + " = '" + category.name() + "' ";
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    /**
     * Count all Events before a specific Date (for Backup)
     * @param calendar The last Date to count Events
     * @return How many Events are affected
     */
    public int countEventsBefore(Calendar calendar) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(calendar.getTime());

        String query = "SELECT * FROM " + DatabaseHelper.EVENTS + " WHERE " +
                DatabaseHelper.DATE + " <=Datetime('" + date + " 23:59:59')";
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    /**
     * Get one specific Event
     * @param id The Id of the specific Event
     * @return The specific Event
     */
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

    /**
     * Get all Events
     * @return All Events
     */
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
    public List<Event> getEvents(Calendar timeStart, Calendar timeEnd, Event.Category category) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String query = "SELECT * FROM " + DatabaseHelper.EVENTS + " WHERE " +
                DatabaseHelper.DATE + " >= Datetime('" + format.format(timeStart.getTime()) + "') AND " +
                DatabaseHelper.DATE + " <= Datetime('" + format.format(timeEnd.getTime()) + "') AND " +
                DatabaseHelper.CATEGORY + " = '" + category.name() + "' " +
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

    /**
     * Get the latest event
     * @param category The specific Category to choose Events from
     * @return The latest event
     */
    public Event getLatestEvent(Event.Category category) {

        String query = "SELECT * FROM " + DatabaseHelper.EVENTS + " WHERE " +
                DatabaseHelper.CATEGORY + " = '" + category.name() + "' " +
                "ORDER BY " + DatabaseHelper.DATE + " DESC LIMIT 1;";
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

    /**
     * Get all Events of one Day
     * @param date The specific Day to choose Events from
     * @return Every Event from the specific Day
     */
    public List<Event> getEventsOfDay(Calendar date) {

        String dateString = new SimpleDateFormat("yyyy-MM-dd").format(date.getTime());

        String query = "SELECT * FROM " + DatabaseHelper.EVENTS + " WHERE " +
                DatabaseHelper.DATE + " >= Datetime('" + dateString + " 00:00:00') AND " +
                DatabaseHelper.DATE + " <= Datetime('" + dateString + " 23:59:59') " +
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

    /**
     * Get all Events of one Day
     * @param date The specific Day to choose Events from
     * @param category The specific Category to choose Events from
     * @return Every Event from the specific Day
     */
    public List<Event> getEventsOfDay(Calendar date, Event.Category category) {

        String dateString = new SimpleDateFormat("yyyy-MM-dd").format(date.getTime());

        String query = "SELECT * FROM " + DatabaseHelper.EVENTS + " WHERE " +
                DatabaseHelper.DATE + " >= Datetime('" + dateString + " 00:00:00') AND " +
                DatabaseHelper.DATE + " <= Datetime('" + dateString + " 23:59:59') AND " +
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

    /**
     * Get all Events of one Day
     * @param date The specific Day to choose Events from
     * @param categories The specific Categories to choose Events from
     * @return Every Event from the specific Day
     */
    public List<Event> getEventsOfDay(Calendar date, Event.Category[] categories) {

        String dateString = new SimpleDateFormat("yyyy-MM-dd").format(date.getTime());

        String whereCategory = "";
        for(Event.Category category : categories) {
            whereCategory = whereCategory + ",'" + category.name() + "'";
        }
        whereCategory = whereCategory.substring(1);

        String query = "SELECT * FROM " + DatabaseHelper.EVENTS + " WHERE " +
                DatabaseHelper.DATE + " >= Datetime('" + dateString + " 00:00:00') AND " +
                DatabaseHelper.DATE + " <= Datetime('" + dateString + " 23:59:59') AND " +
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

    /**
     * Calculate Average BloodSugar
     * @param rangeInDays Time span to calculate within
     * @return Average BloodSugar
     */
    public float getBloodSugarAverage(int rangeInDays) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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

    /**
     * Get a two-dimensional array with all values (average or sum) for graphical output
     */
    public float[][] getAverageDataTable(Calendar date, Event.Category[] categories, int columns) {

        float[][] values = new float[categories.length][columns];
        List<Event> events = getEventsOfDay(date, categories);

        int counter = 1;
        for(Event event : events) {

            int category = Arrays.asList(categories).indexOf(event.getCategory());
            int hour = event.getDate().get(Calendar.HOUR_OF_DAY) / 2;
            float oldValue = values[category][hour];
            float newValue;

            if(event.getCategory() == Event.Category.BloodSugar) {
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
}
