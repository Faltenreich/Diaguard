package com.faltenreich.diaguard.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.faltenreich.diaguard.database.measurements.Activity;
import com.faltenreich.diaguard.database.measurements.BloodSugar;
import com.faltenreich.diaguard.database.measurements.Bolus;
import com.faltenreich.diaguard.database.measurements.HbA1c;
import com.faltenreich.diaguard.database.measurements.Meal;
import com.faltenreich.diaguard.database.measurements.Pressure;
import com.faltenreich.diaguard.database.measurements.Pulse;
import com.faltenreich.diaguard.database.measurements.Weight;
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
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public DatabaseDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
        // To enable ON CASCADE
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys = ON;");
        }
    }

    public void close() {
        dbHelper.close();
    }

    public boolean isOpen() {
        return dbHelper.getWritableDatabase() != null &&
                dbHelper.getWritableDatabase().isOpen();
    }

    public int getVersion() {
        return db.getVersion();
    }

    // region Helper

    private ContentValues getContentValues(Model model) {
        ContentValues values = null;

        if(model.getClass() == Entry.class) {
            values = getContentValuesEntry((Entry) model);
        }
        else if(model.getClass() == Food.class) {
            values = getContentValuesFood((Food) model);
        }
        else if(model.getClass() == BloodSugar.class) {
            values = getContentValuesBloodSugar((BloodSugar) model);
        }
        else if(model.getClass() == Bolus.class) {
            values = getContentValuesBolus((Bolus) model);
        }
        else if(model.getClass() == Meal.class) {
            values = getContentValuesMeal((Meal) model);
        }
        else if(model.getClass() == Activity.class) {
            values = getContentValuesActivity((Activity) model);
        }
        else if(model.getClass() == HbA1c.class) {
            values = getContentValuesHbA1c((HbA1c) model);
        }
        else if(model.getClass() == Weight.class) {
            values = getContentValuesWeight((Weight) model);
        }
        else if(model.getClass() == Pulse.class) {
            values = getContentValuesPulse((Pulse) model);
        }
        else if(model.getClass() == Pressure.class) {
            values = getContentValuesPressure((Pressure) model);
        }

        if(values == null) {
            throw new IllegalArgumentException("getContentValues() missing for model '" +
                    model.getClass().getName() + "'");
        }

        return values;
    }

    private ContentValues getContentValuesEntry(Entry entry) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.CREATED_AT, Helper.getDateDatabaseFormat().print(entry.getCreatedAt()));
        values.put(DatabaseHelper.UPDATED_AT, Helper.getDateDatabaseFormat().print(entry.getUpdatedAt()));
        values.put(DatabaseHelper.DATE, Helper.getDateDatabaseFormat().print(entry.getDate()));
        values.put(DatabaseHelper.NOTE, entry.getNote());
        values.put(DatabaseHelper.IS_VISIBLE, entry.isVisible() ? 1 : 0);
        return values;
    }

    private ContentValues getContentValuesFood(Food food) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.CREATED_AT, Helper.getDateDatabaseFormat().print(food.getCreatedAt()));
        values.put(DatabaseHelper.UPDATED_AT, Helper.getDateDatabaseFormat().print(food.getUpdatedAt()));
        values.put(DatabaseHelper.CARBOHYDRATES, food.getCarbohydrates());
        values.put(DatabaseHelper.NAME, food.getName());
        values.put(DatabaseHelper.IMAGE, food.getImage());
        return values;
    }

    private ContentValues getContentValuesBloodSugar(BloodSugar bloodSugar) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.CREATED_AT, Helper.getDateDatabaseFormat().print(bloodSugar.getCreatedAt()));
        values.put(DatabaseHelper.UPDATED_AT, Helper.getDateDatabaseFormat().print(bloodSugar.getUpdatedAt()));
        values.put(DatabaseHelper.MGDL, bloodSugar.getMgDl());
        values.put(DatabaseHelper.ENTRY_ID, bloodSugar.getEntryId());
        return values;
    }

    private ContentValues getContentValuesBolus(Bolus bolus) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.CREATED_AT, Helper.getDateDatabaseFormat().print(bolus.getCreatedAt()));
        values.put(DatabaseHelper.UPDATED_AT, Helper.getDateDatabaseFormat().print(bolus.getUpdatedAt()));
        values.put(DatabaseHelper.MILLILITER, bolus.getMilliliter());
        values.put(DatabaseHelper.ATC_CODE, bolus.getAtcCode());
        values.put(DatabaseHelper.IS_CORRECTION, bolus.isCorrection() ? 1 : 0);
        values.put(DatabaseHelper.ENTRY_ID, bolus.getEntryId());
        return values;
    }

    private ContentValues getContentValuesMeal(Meal meal) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.CREATED_AT, Helper.getDateDatabaseFormat().print(meal.getCreatedAt()));
        values.put(DatabaseHelper.UPDATED_AT, Helper.getDateDatabaseFormat().print(meal.getUpdatedAt()));
        values.put(DatabaseHelper.CARBOHYDRATES, meal.getCarbohydrates());
        values.put(DatabaseHelper.FOOD_ID, meal.getFoodId());
        values.put(DatabaseHelper.ENTRY_ID, meal.getEntryId());
        return values;
    }

    private ContentValues getContentValuesActivity(Activity activity) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.CREATED_AT, Helper.getDateDatabaseFormat().print(activity.getCreatedAt()));
        values.put(DatabaseHelper.UPDATED_AT, Helper.getDateDatabaseFormat().print(activity.getUpdatedAt()));
        values.put(DatabaseHelper.MINUTES, activity.getMinutes());
        values.put(DatabaseHelper.TYPE, activity.getType());
        values.put(DatabaseHelper.ENTRY_ID, activity.getEntryId());
        return values;
    }

    private ContentValues getContentValuesHbA1c(HbA1c hbA1c) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.CREATED_AT, Helper.getDateDatabaseFormat().print(hbA1c.getCreatedAt()));
        values.put(DatabaseHelper.UPDATED_AT, Helper.getDateDatabaseFormat().print(hbA1c.getUpdatedAt()));
        values.put(DatabaseHelper.PERCENT, hbA1c.getPercent());
        values.put(DatabaseHelper.ENTRY_ID, hbA1c.getEntryId());
        return values;
    }

    private ContentValues getContentValuesWeight(Weight weight) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.CREATED_AT, Helper.getDateDatabaseFormat().print(weight.getCreatedAt()));
        values.put(DatabaseHelper.UPDATED_AT, Helper.getDateDatabaseFormat().print(weight.getUpdatedAt()));
        values.put(DatabaseHelper.KILOGRAM, weight.getKilogram());
        values.put(DatabaseHelper.ENTRY_ID, weight.getEntryId());
        return values;
    }

    private ContentValues getContentValuesPulse(Pulse pulse) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.CREATED_AT, Helper.getDateDatabaseFormat().print(pulse.getCreatedAt()));
        values.put(DatabaseHelper.UPDATED_AT, Helper.getDateDatabaseFormat().print(pulse.getUpdatedAt()));
        values.put(DatabaseHelper.FREQUENCY, pulse.getFrequency());
        values.put(DatabaseHelper.ENTRY_ID, pulse.getEntryId());
        return values;
    }

    private ContentValues getContentValuesPressure(Pressure pressure) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.CREATED_AT, Helper.getDateDatabaseFormat().print(pressure.getCreatedAt()));
        values.put(DatabaseHelper.UPDATED_AT, Helper.getDateDatabaseFormat().print(pressure.getUpdatedAt()));
        values.put(DatabaseHelper.SYSTOLIC, pressure.getSystolic());
        values.put(DatabaseHelper.DIASTOLIC, pressure.getDiastolic());
        values.put(DatabaseHelper.ENTRY_ID, pressure.getEntryId());
        return values;
    }

    public Model get(String tableName, Cursor cursor) {
        if(cursor.moveToFirst()) {
            switch (tableName) {
                case DatabaseHelper.ENTRY:
                    return getEntry(cursor);
                case DatabaseHelper.FOOD:
                    return getFood(cursor);
                case DatabaseHelper.BLOODSUGAR:
                    return getBloodSugar(cursor);
                case DatabaseHelper.BOLUS:
                    return getBolus(cursor);
                case DatabaseHelper.MEAL:
                    return getMeal(cursor);
                case DatabaseHelper.ACTIVITY:
                    return getActivity(cursor);
                case DatabaseHelper.HBA1C:
                    return getHbA1c(cursor);
                case DatabaseHelper.WEIGHT:
                    return getWeight(cursor);
                case DatabaseHelper.PULSE:
                    return getPulse(cursor);
                case DatabaseHelper.PRESSURE:
                    return getPressure(cursor);
                default:
                    return null;
            }
        }
        else return null;
    }

    public Entry getEntry(Cursor cursor) {
        Entry entry = new Entry();
        entry.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ID))));
        entry.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.CREATED_AT)));
        entry.setUpdatedAt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.UPDATED_AT)));
        entry.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.DATE)));
        entry.setNote(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.NOTE)));
        entry.setIsVisible(cursor.getString(cursor.getColumnIndex(DatabaseHelper.IS_VISIBLE)).equals("1"));
        return entry;
    }

    public Food getFood(Cursor cursor) {
        Food food = new Food();
        food.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ID))));
        food.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.CREATED_AT)));
        food.setUpdatedAt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.UPDATED_AT)));
        food.setCarbohydrates(Float.parseFloat(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.CARBOHYDRATES))));
        food.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.NAME)));
        food.setImage(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.IMAGE)));
        return food;
    }

    public BloodSugar getBloodSugar(Cursor cursor) {
        BloodSugar bloodSugar = new BloodSugar();
        bloodSugar.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ID))));
        bloodSugar.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.CREATED_AT)));
        bloodSugar.setUpdatedAt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.UPDATED_AT)));
        bloodSugar.setMgDl(Long.parseLong(cursor.getString(cursor.getColumnIndex(DatabaseHelper.MGDL))));
        bloodSugar.setEntryId(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ENTRY_ID))));
        return bloodSugar;
    }

    public Bolus getBolus(Cursor cursor) {
        Bolus bolus = new Bolus();
        bolus.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ID))));
        bolus.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.CREATED_AT)));
        bolus.setUpdatedAt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.UPDATED_AT)));
        bolus.setMilliliter(Long.parseLong(cursor.getString(cursor.getColumnIndex(DatabaseHelper.MILLILITER))));
        bolus.setAtcCode(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ATC_CODE))));
        bolus.setIsCorrection(cursor.getString(cursor.getColumnIndex(DatabaseHelper.IS_CORRECTION)).equals("1"));
        bolus.setEntryId(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ENTRY_ID))));
        return bolus;
    }

    public Meal getMeal(Cursor cursor) {
        Meal meal = new Meal();
        meal.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ID))));
        meal.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.CREATED_AT)));
        meal.setUpdatedAt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.UPDATED_AT)));
        meal.setCarbohydrates(Long.parseLong(cursor.getString(cursor.getColumnIndex(DatabaseHelper.MILLILITER))));
        meal.setFoodId(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.FOOD_ID))));
        meal.setEntryId(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ENTRY_ID))));
        return meal;
    }

    public Activity getActivity(Cursor cursor) {
        Activity activity = new Activity();
        activity.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ID))));
        activity.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.CREATED_AT)));
        activity.setUpdatedAt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.UPDATED_AT)));
        activity.setMinutes(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.MINUTES))));
        activity.setType(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TYPE))));
        activity.setEntryId(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ENTRY_ID))));
        return activity;
    }

    public HbA1c getHbA1c(Cursor cursor) {
        HbA1c hbA1c = new HbA1c();
        hbA1c.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ID))));
        hbA1c.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.CREATED_AT)));
        hbA1c.setUpdatedAt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.UPDATED_AT)));
        hbA1c.setPercent(Long.parseLong(cursor.getString(cursor.getColumnIndex(DatabaseHelper.HBA1C))));
        hbA1c.setEntryId(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ENTRY_ID))));
        return hbA1c;
    }

    public Weight getWeight(Cursor cursor) {
        Weight weight = new Weight();
        weight.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ID))));
        weight.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.CREATED_AT)));
        weight.setUpdatedAt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.UPDATED_AT)));
        weight.setKilogram(Long.parseLong(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KILOGRAM))));
        weight.setEntryId(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ENTRY_ID))));
        return weight;
    }

    public Pulse getPulse(Cursor cursor) {
        Pulse pulse = new Pulse();
        pulse.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ID))));
        pulse.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.CREATED_AT)));
        pulse.setUpdatedAt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.UPDATED_AT)));
        pulse.setFrequency(Long.parseLong(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FREQUENCY))));
        pulse.setEntryId(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ENTRY_ID))));
        return pulse;
    }

    public Pressure getPressure(Cursor cursor) {
        Pressure pressure = new Pressure();
        pressure.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ID))));
        pressure.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.CREATED_AT)));
        pressure.setUpdatedAt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.UPDATED_AT)));
        pressure.setSystolic(Long.parseLong(cursor.getString(cursor.getColumnIndex(DatabaseHelper.SYSTOLIC))));
        pressure.setDiastolic(Long.parseLong(cursor.getString(cursor.getColumnIndex(DatabaseHelper.DIASTOLIC))));
        pressure.setEntryId(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ENTRY_ID))));
        return pressure;
    }

    // endregion

    // region Read

    public Model get(String table, long id) {
        Cursor cursor = db.query(table, null,
                DatabaseHelper.ID + "=?",
                new String[] { String.valueOf(id) },
                null, null, null, null);
        return get(table, cursor);
    }

    public List<Model> get(String table) {
        Cursor cursor = db.query(table, null, null, null, null, null, null, null);
        List<Model> objects = new ArrayList<Model>();
        if (cursor.moveToFirst()) {
            while(!cursor.isAfterLast()) {
                objects.add(get(table, cursor));
                cursor.moveToNext();
            }
        }
        return objects;
    }

    public List<Model> get(String table, String[] columns, String selection, String[] selectionArgs,
                           String groupBy, String having, String orderBy, String limit) {
        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
        List<Model> objects = new ArrayList<Model>();
        if (cursor.moveToFirst()) {
            while(!cursor.isAfterLast()) {
                objects.add(get(table, cursor));
                cursor.moveToNext();
            }
        }
        return objects;
    }

    public Entry getLatestBloodSugar() {
        String query = "SELECT * FROM " + DatabaseHelper.ENTRY +
                        " INNER JOIN " + DatabaseHelper.MEASUREMENT +
                        " ON " + DatabaseHelper.MEASUREMENT + "." + DatabaseHelper.ENTRY_ID +
                        " = " + DatabaseHelper.ENTRY + "." + DatabaseHelper.ID +
                        " AND " + DatabaseHelper.MEASUREMENT + "." + DatabaseHelper.CATEGORY +
                        " = '" + Measurement.Category.BloodSugar + "'" +
                        " ORDER BY " + DatabaseHelper.ENTRY + "." + DatabaseHelper.DATE +
                        " DESC LIMIT 1";
        Cursor cursor = db.rawQuery(query, null);
        Entry entry = null;
        if(cursor.moveToFirst()) {
            entry = getEntry(cursor);
            entry.getMeasurements().add(getBloodSugar(cursor));
        }
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
                Entry entry = getEntry(cursor);
                // TODO entry.getMeasurements().add(get(cursor));
                entries.add(entry);
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

    public float getBloodSugarAverageOfDay(DateTime day) {
        DateTimeFormatter format = Helper.getDateDatabaseFormat();
        String query = "SELECT AVG(" + DatabaseHelper.VALUE + ") FROM " + DatabaseHelper.MEASUREMENT +
                " INNER JOIN " + DatabaseHelper.ENTRY +
                " ON " + DatabaseHelper.MEASUREMENT + "." + DatabaseHelper.ENTRY_ID +
                " = " + DatabaseHelper.ENTRY + "." + DatabaseHelper.ID +
                " AND " + DatabaseHelper.ENTRY + "." + DatabaseHelper.DATE +
                " >= Datetime('" + format.print(day.withTimeAtStartOfDay()) + "')" +
                " AND " + DatabaseHelper.ENTRY + "." + DatabaseHelper.DATE +
                " <= Datetime('" + format.print(day.withTime(23, 59, 59, 999)) + "')" +
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
                // TODO
                /*
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
                */
            }
        }
        return values;
    }

    public int count(String table) {
        String query = "SELECT COUNT(*) FROM " + table + ";";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
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

    public int countEntriesBefore(DateTime lastDay) {
        String query = "SELECT COUNT(*) FROM " + DatabaseHelper.ENTRY +
                " WHERE " + DatabaseHelper.DATE +
                " <= Datetime('" + lastDay.withTime(23, 59, 59, 999) + "');";
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

    public int delete(String table, String selection, String[] selectionArgs) {
        return db.delete(table, selection, selectionArgs);
    }

    // endregion
}
