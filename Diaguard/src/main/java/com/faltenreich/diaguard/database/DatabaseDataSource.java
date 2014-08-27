package com.faltenreich.diaguard.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.faltenreich.diaguard.helpers.Helper;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Filip on 20.10.13.
 */
public class DatabaseDataSource {

    public static final String DB_FORMAT_DATE_AND_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String DB_FORMAT_DATE = "yyyy-MM-dd";
    public static final String DB_FORMAT_TIME = "HH:mm";

    public static final String FIRST_SECOND_OF_DAY = "00:00:00";
    public static final String LAST_SECOND_OF_DAY = "23:59:59";

    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public DatabaseDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

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

    // region Helper

    private ContentValues getContentValues(Model model) {
        ContentValues values = null;

        if(model.getClass() == Entry.class)
            values = getContentValuesEntry((Entry)model);
        else if(model.getClass() == Measurement.class)
            values = getContentValuesMeasurement((Measurement)model);
        else if(model.getClass() == Food.class)
            values = getContentValuesFood((Food)model);

        if(values == null)
            throw new IllegalArgumentException("getContentValues() missing for model '" +
                    model.getClass().getName() + "'");

        return values;
    }

    private ContentValues getContentValuesEntry(Entry entry) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.DATE, Helper.getDateDatabaseFormat().print(entry.getDate()));
        values.put(DatabaseHelper.NOTE, entry.getNote());
        return values;
    }

    private ContentValues getContentValuesMeasurement(Measurement measurement) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.VALUE, measurement.getValue());
        values.put(DatabaseHelper.CATEGORY, measurement.getCategory().toString());
        values.put(DatabaseHelper.ENTRY_ID, measurement.getEntryId());
        return values;
    }

    private ContentValues getContentValuesFood(Food food) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.CARBOHYDRATES, food.getCarbohydrates());
        values.put(DatabaseHelper.NAME, food.getName());
        values.put(DatabaseHelper.DATE, Helper.getDateDatabaseFormat().print(food.getDate()));
        values.put(DatabaseHelper.ENTRY_ID, food.getEventId());
        return values;
    }


    public Entry getEntry(Cursor cursor) {
        Entry entry = new Entry();
        entry.setId(Integer.parseInt(cursor.getString(0)));
        entry.setDate(cursor.getString(1));
        entry.setNote(cursor.getString(2));
        return entry;
    }

    public Entry getEntryWithMeasurement(Cursor cursor) {
        Entry entry = new Entry();
        entry.setId(Integer.parseInt(cursor.getString(0)));
        entry.setDate(cursor.getString(1));
        entry.setNote(cursor.getString(2));

        Measurement measurement = new Measurement();
        measurement.setId(Integer.parseInt(cursor.getString(3)));
        measurement.setValue(Float.parseFloat(cursor.getString(4)));
        measurement.setCategory(Measurement.Category.valueOf(cursor.getString(5)));
        measurement.setEntryId(Integer.parseInt(cursor.getString(6)));
        entry.getMeasurements().add(measurement);

        return entry;
    }

    public Measurement getMeasurement(Cursor cursor) {
        Measurement measurement = new Measurement();
        measurement.setId(Integer.parseInt(cursor.getString(0)));
        measurement.setValue(Float.parseFloat(cursor.getString(1)));
        measurement.setCategory(Measurement.Category.valueOf(cursor.getString(2)));
        measurement.setEntryId(Integer.parseInt(cursor.getString(3)));
        return measurement;
    }

    public Food getFood(Cursor cursor) {
        Food food = new Food();
        food.setId(Integer.parseInt(cursor.getString(0)));
        food.setCarbohydrates(Float.parseFloat(cursor.getString(1)));
        food.setName(cursor.getString(2));
        food.setDate(cursor.getString(3));
        food.setEventId(Integer.parseInt(cursor.getString(4)));
        return food;
    }

    // endregion

    // region Read

    public Model get(String table, long id) {
        String where = DatabaseHelper.ID + "=?";
        Cursor cursor = db.query(table, null, where, new String[] { String.valueOf(id) },
                null, null, null, null);
        cursor.moveToFirst();

        if(table.equals(DatabaseHelper.ENTRY))
            return getEntry(cursor);
        else if(table.equals(DatabaseHelper.MEASUREMENT))
            return getMeasurement(cursor);
        else if(table.equals(DatabaseHelper.FOOD))
            return getFood(cursor);
        else
            throw new Resources.NotFoundException();
    }

    public List<Model> get(String table, String[] columns, String selection, String[] selectionArgs,
                           String groupBy, String having, String orderBy, String limit) {
        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);

        List<Model> objects = new ArrayList<Model>();
        if (cursor.moveToFirst()) {
            while(!cursor.isAfterLast()) {
                if(table.equals(DatabaseHelper.ENTRY))
                    objects.add(getEntry(cursor));
                else if(table.equals(DatabaseHelper.MEASUREMENT))
                    objects.add(getMeasurement(cursor));
                else if(table.equals(DatabaseHelper.FOOD))
                    objects.add(getFood(cursor));
                cursor.moveToNext();
            }
        }
        return objects;
    }

    public Entry getLatestBloodSugar() {
        String query = String.format("SELECT * FROM %2$s INNER JOIN %5$s ON %5$s.%3$s = %2$s.%1$s ORDER BY %2$s.%4$s DESC LIMIT 1",
                DatabaseHelper.ID, DatabaseHelper.ENTRY, DatabaseHelper.ENTRY_ID, DatabaseHelper.DATE, DatabaseHelper.MEASUREMENT);
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        Entry entry = getEntryWithMeasurement(cursor);
        cursor.close();
        return entry;
    }

    public List<Entry> getEntriesOfDay(DateTime day, Measurement.Category category) {
        String startOfDay = Helper.getDateDatabaseFormat().print(day.withTimeAtStartOfDay());
        String endOfDay = Helper.getDateDatabaseFormat().print(day.withTime(23, 59, 59, 999));
        String query = "SELECT * FROM " + DatabaseHelper.ENTRY +
                        " INNER JOIN " + DatabaseHelper.MEASUREMENT +
                        " ON " + DatabaseHelper.MEASUREMENT + "." + DatabaseHelper.ENTRY_ID +
                        " = " + DatabaseHelper.ENTRY + "." + DatabaseHelper.ID +
                        " AND " + DatabaseHelper.ENTRY + "." + DatabaseHelper.DATE +
                        " >= Datetime('" + startOfDay + "') " +
                        " AND " + DatabaseHelper.ENTRY + "." + DatabaseHelper.DATE +
                        " <= Datetime('" + endOfDay + "') " +
                        " AND " + DatabaseHelper.MEASUREMENT + "." + DatabaseHelper.CATEGORY +
                        " = '" + category + "';";
        Cursor cursor = db.rawQuery(query, null);

        List<Entry> entries = new ArrayList<Entry>();
        if (cursor.moveToFirst()) {
            while(!cursor.isAfterLast()) {
                entries.add(getEntryWithMeasurement(cursor));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return entries;
    }

    public int count(String table, String selection, String selectionArg) {
        String query = "SELECT COUNT(*) FROM " + table + " WHERE " + selection + " = '" + selectionArg + "';";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    // endregion

    // region Write

    public long insert(Model model){
        return db.insertOrThrow(model.getTableName(), null, getContentValues(model));
    }

    public long[] insert(List<? extends Model> models){
        long[] ids = new long[models.size()];
        for(int position = 0; position < models.size(); position++) {
            Model model = models.get(position);
            ids[position] = db.insertOrThrow(model.getTableName(), null, getContentValues(model));
        }
        return ids;
    }

    public long update(Model model) {
        return db.update(model.getTableName(), getContentValues(model),
                DatabaseHelper.ID + " = " + model.getId(), null);
    }

    public int delete(Model model) {
        return db.delete(model.getTableName(),
                DatabaseHelper.ID + "=?",
                new String[]{Long.toString(model.getId())});
    }

    public int delete(Model model, String selection, String[] selectionArgs) {
        return db.delete(model.getTableName(), selection + "=?", selectionArgs);
    }

    // endregion

    /*

    public Measurement getLatestMeasurement(Measurement.Category category) {
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
                DatabaseHelper.CATEGORY + " = '" + Measurement.Category.BloodSugar.toString() + "' ";
        Cursor cursor = db.rawQuery(query, null);

        float average = 0;
        if(cursor.moveToFirst())
            average = cursor.getFloat(0);

        return average;
    }


    public float[][] getAverageDataTable(Calendar date, Measurement.Category[] categories, int columns) {
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
    */
}
