package com.faltenreich.diaguard.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.faltenreich.diaguard.helpers.Helper;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Arrays;
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
        values.put(DatabaseHelper.ENTRY_ID, food.getMeasurementId());
        return values;
    }


    public Entry getEntry(Cursor cursor) {
        Entry entry = new Entry();
        entry.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ID))));
        entry.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.DATE)));
        entry.setNote(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.NOTE)));
        return entry;
    }

    public Entry getEntryWithMeasurement(Cursor cursor) {
        Entry entry = new Entry();
        entry.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ID))));
        entry.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.DATE)));
        entry.setNote(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.NOTE)));

        Measurement measurement = new Measurement();
        measurement.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ID))));
        measurement.setValue(Float.parseFloat(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.VALUE))));
        measurement.setCategory(Measurement.Category.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.CATEGORY))));
        measurement.setEntryId(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ENTRY_ID))));
        entry.getMeasurements().add(measurement);

        return entry;
    }

    public Measurement getMeasurement(Cursor cursor) {
        Measurement measurement = new Measurement();
        measurement.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ID))));
        measurement.setValue(Float.parseFloat(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.VALUE))));
        measurement.setCategory(Measurement.Category.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.CATEGORY))));
        measurement.setEntryId(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ENTRY_ID))));
        return measurement;
    }

    public Food getFood(Cursor cursor) {
        Food food = new Food();
        food.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ID))));
        food.setCarbohydrates(Float.parseFloat(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.CARBOHYDRATES))));
        food.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.NAME)));
        food.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.DATE)));
        food.setMeasurementId(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.MEASUREMENT_ID))));
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

    public List<Entry> getEntriesOfDay(DateTime day) {
        String startOfDay = Helper.getDateDatabaseFormat().print(day.withTimeAtStartOfDay());
        String endOfDay = Helper.getDateDatabaseFormat().print(day.withTime(23, 59, 59, 999));
        String query = "SELECT * FROM " + DatabaseHelper.ENTRY +
                " WHERE " + DatabaseHelper.DATE + " >= Datetime('" + startOfDay + "') " +
                " AND " + DatabaseHelper.DATE + " <= Datetime('" + endOfDay + "');";
        Cursor cursor = db.rawQuery(query, null);

        List<Entry> entries = new ArrayList<Entry>();
        if (cursor.moveToFirst()) {
            while(!cursor.isAfterLast()) {
                entries.add(getEntry(cursor));
                cursor.moveToNext();
            }
        }

        cursor.close();
        return entries;
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

    public float getBloodSugarAverage(int rangeInDays) {
        DateTimeFormatter format = Helper.getDateDatabaseFormat();
        DateTime now = new DateTime();
        String query = "SELECT AVG(" + DatabaseHelper.VALUE + ") FROM " + DatabaseHelper.MEASUREMENT +
                " INNER JOIN " + DatabaseHelper.ENTRY +
                " ON " + DatabaseHelper.MEASUREMENT + "." + DatabaseHelper.ENTRY_ID +
                " = " + DatabaseHelper.ENTRY + "." + DatabaseHelper.ID +
                " AND " + DatabaseHelper.ENTRY + "." + DatabaseHelper.DATE +
                " >= Datetime('" + format.print(now.minusDays(rangeInDays).withTimeAtStartOfDay()) + "')" +
                " AND " + DatabaseHelper.ENTRY + "." + DatabaseHelper.DATE +
                " <= Datetime('" + format.print(now.withTime(23, 59, 59, 999)) + "')" +
                " AND " + DatabaseHelper.MEASUREMENT + "." + DatabaseHelper.CATEGORY +
                " = '" + Measurement.Category.BloodSugar.toString() + "';";
        Cursor cursor = db.rawQuery(query, null);

        float average = 0;
        if(cursor.moveToFirst())
            average = cursor.getFloat(0);

        return average;
    }

    public float[][] getAverageDataTable(DateTime day, Measurement.Category[] categories, int columns) {
        float[][] values = new float[categories.length][columns];
        int counter = 1;
        for(Measurement.Category category : categories) {
            List<Entry> entriesOfDay = getEntriesOfDay(day, category);
            int row = Arrays.asList(categories).indexOf(category);

            for(Entry entry : entriesOfDay) {
                Measurement measurement = entry.getMeasurements().get(0);
                int hour = entry.getDate().getHourOfDay() / 2;
                float oldValue = values[row][hour];
                float newValue;

                // Sum of everything but Blood Sugar (average)
                if (measurement.getCategory() == Measurement.Category.BloodSugar ||
                        measurement.getCategory() == Measurement.Category.HbA1c ||
                        measurement.getCategory() == Measurement.Category.Weight ||
                        measurement.getCategory() == Measurement.Category.Pulse) {
                    if (oldValue > 0) {
                        // Calculate Average per hour
                        newValue = ((oldValue * counter) + (measurement.getValue())) / (counter + 1);
                        counter++;
                    } else {
                        newValue = measurement.getValue();
                        counter = 1;
                    }
                } else
                    newValue = oldValue + measurement.getValue();

                values[row][hour] = newValue;
            }
        }
        return values;
    }

    public int count(String table, String selection, String selectionArg) {
        String query = "SELECT COUNT(*) FROM " + table + " WHERE " + selection + " = '" + selectionArg + "';";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    public int countMeasurements(DateTime day, Measurement.Category category) {
        String query = "SELECT COUNT(*) FROM " + DatabaseHelper.ENTRY +
                " INNER JOIN " + DatabaseHelper.MEASUREMENT +
                " ON " + DatabaseHelper.MEASUREMENT + "." + DatabaseHelper.ENTRY_ID +
                " = " + DatabaseHelper.ENTRY + "." + DatabaseHelper.ID +
                " AND " + DatabaseHelper.ENTRY + "." + DatabaseHelper.DATE +
                " >= Datetime('" + day.withTimeAtStartOfDay() + "')" +
                " AND " + DatabaseHelper.ENTRY + "." + DatabaseHelper.DATE +
                " <= Datetime('" + day.withTime(23, 59, 59, 999) + "')" +
                " AND " + DatabaseHelper.MEASUREMENT + "." + DatabaseHelper.CATEGORY +
                " = '" + category + "';";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    public int countMeasurements(DateTime day, Measurement.Category category, float limit, boolean countHigherValues) {
        String comparisonSymbol = countHigherValues ? " >= " : " <= ";
        String query = "SELECT COUNT(*) FROM " + DatabaseHelper.ENTRY +
                " INNER JOIN " + DatabaseHelper.MEASUREMENT +
                " ON " + DatabaseHelper.MEASUREMENT + "." + DatabaseHelper.ENTRY_ID +
                " = " + DatabaseHelper.ENTRY + "." + DatabaseHelper.ID +
                " AND " + DatabaseHelper.ENTRY + "." + DatabaseHelper.DATE +
                " >= Datetime('" + day.withTimeAtStartOfDay() + "')" +
                " AND " + DatabaseHelper.ENTRY + "." + DatabaseHelper.DATE +
                " <= Datetime('" + day.withTime(23, 59, 59, 999) + "')" +
                " AND " + DatabaseHelper.MEASUREMENT + "." + DatabaseHelper.CATEGORY +
                " = '" + category + "' " +
                " AND " + DatabaseHelper.MEASUREMENT + "." + DatabaseHelper.VALUE +
                comparisonSymbol + limit + ";";
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
}
