package com.faltenreich.diaguard.data.dao;

import android.util.Log;

import com.faltenreich.diaguard.data.SqlFunction;
import com.faltenreich.diaguard.data.entity.Activity;
import com.faltenreich.diaguard.data.entity.BaseEntity;
import com.faltenreich.diaguard.data.entity.BloodSugar;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.FoodEaten;
import com.faltenreich.diaguard.data.entity.HbA1c;
import com.faltenreich.diaguard.data.entity.Insulin;
import com.faltenreich.diaguard.data.entity.Meal;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.data.entity.Pressure;
import com.faltenreich.diaguard.data.entity.Pulse;
import com.faltenreich.diaguard.data.entity.Weight;
import com.faltenreich.diaguard.util.NumberUtils;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.table.DatabaseTableConfig;

import org.joda.time.DateTimeConstants;
import org.joda.time.Interval;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Faltenreich on 06.09.2015.
 */
public class MeasurementDao <M extends Measurement> extends BaseDao<M> {

    private static final String TAG = MeasurementDao.class.getSimpleName();

    private static HashMap<Class, MeasurementDao> instances;

    public static <M extends Measurement> MeasurementDao getInstance(Class<M> clazz) {
        if (instances == null) {
            instances = new HashMap<>();
        }
        MeasurementDao measurementDao = instances.get(clazz);
        if (measurementDao == null) {
            instances.put(clazz, new MeasurementDao<>(clazz));
            measurementDao = instances.get(clazz);
        }
        return measurementDao;
    }

    private MeasurementDao(Class<M> clazz) {
        super(clazz);
    }

    @Override
    public M createOrUpdate(M object) {
        M entity = super.createOrUpdate(object);
        switch (entity.getCategory()) {
            case MEAL:
                createOrUpdate((Meal) entity);
                break;
        }
        return entity;
    }

    @Override
    public int delete(M object) {
        switch (object.getCategory()) {
            case MEAL:
                Meal meal = (Meal) object;
                List<FoodEaten> foodEatenCache = new ArrayList<>();
                for (FoodEaten foodEaten : meal.getFoodEaten()) {
                    foodEatenCache.add(foodEaten);
                }
                meal.setFoodEatenCache(foodEatenCache);
                break;
        }
        return super.delete(object);
    }

    private void createOrUpdate(Meal meal) {

        for (FoodEaten foodEatenOld : FoodEatenDao.getInstance().getAll(meal)) {
            if (!meal.getFoodEatenCache().contains(foodEatenOld)) {
                FoodEatenDao.getInstance().delete(foodEatenOld);
            }
        }

        for (FoodEaten foodEaten : meal.getFoodEatenCache()) {
            foodEaten.setMeal(meal);
            FoodEatenDao.getInstance().createOrUpdate(foodEaten);
        }
    }

    public float function(SqlFunction sqlFunction, String column, Interval interval) {
        String classNameEntry = DatabaseTableConfig.extractTableName(Entry.class);
        String classNameMeasurement = DatabaseTableConfig.extractTableName(getClazz());
        long intervalStart = interval.getStart().withTimeAtStartOfDay().getMillis();
        long intervalEnd = interval.getEnd().withTime(
                DateTimeConstants.HOURS_PER_DAY - 1,
                DateTimeConstants.MINUTES_PER_HOUR - 1,
                DateTimeConstants.SECONDS_PER_MINUTE - 1,
                DateTimeConstants.MILLIS_PER_SECOND - 1)
                .getMillis();
        String query = "SELECT " + sqlFunction.function + "(" + column + ")" +
                " FROM " + classNameMeasurement +
                " INNER JOIN " + classNameEntry +
                " ON " + classNameMeasurement + "." + Measurement.Column.ENTRY +
                " = " + classNameEntry + "." + BaseEntity.Column.ID +
                " AND " + classNameEntry + "." + Entry.Column.DATE +
                " >= " + intervalStart +
                " AND " + classNameEntry + "." + Entry.Column.DATE +
                " <= " + intervalEnd + ";";

        List<String[]> results;
        try {
            GenericRawResults<String[]> rawResults = getDao().queryRaw(query);
            results = rawResults.getResults();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getMessage());
            return 0;
        }

        String[] resultArray = results.get(0);
        String result = resultArray[0];
        try {
            return NumberUtils.parseNumber(result);
        } catch (NumberFormatException exception) {
            return 0;
        } catch (NullPointerException exception) {
            return 0;
        }
    }

    public Measurement getAvgMeasurement(Measurement.Category category, Interval interval) {
        long daysBetween = interval.toDuration().getStandardDays() + 1;
        switch (category) {
            case BLOODSUGAR:
                BloodSugar bloodSugar = new BloodSugar();
                bloodSugar.setMgDl(function(SqlFunction.AVG, BloodSugar.Column.MGDL, interval));
                return bloodSugar;
            case INSULIN:
                Insulin insulin = new Insulin();
                insulin.setBolus(function(SqlFunction.SUM, Insulin.Column.BOLUS, interval) / daysBetween);
                insulin.setBasal(function(SqlFunction.SUM, Insulin.Column.BASAL, interval) / daysBetween);
                insulin.setCorrection(function(SqlFunction.SUM, Insulin.Column.CORRECTION, interval) / daysBetween);
                return insulin;
            case MEAL:
                Meal meal = new Meal();
                // TODO: Include carbohydrates from FoodEaten
                meal.setCarbohydrates(function(SqlFunction.SUM, Meal.Column.CARBOHYDRATES, interval) / daysBetween);
                return meal;
            case ACTIVITY:
                Activity activity = new Activity();
                activity.setMinutes((int) (function(SqlFunction.SUM, Activity.Column.MINUTES, interval) / daysBetween));
                return activity;
            case HBA1C:
                HbA1c hbA1c = new HbA1c();
                hbA1c.setPercent(function(SqlFunction.AVG, HbA1c.Column.PERCENT, interval));
                return hbA1c;
            case WEIGHT:
                Weight weight = new Weight();
                weight.setKilogram(function(SqlFunction.AVG, Weight.Column.KILOGRAM, interval));
                return weight;
            case PULSE:
                Pulse pulse = new Pulse();
                pulse.setFrequency(function(SqlFunction.AVG, Pulse.Column.FREQUENCY, interval));
                return pulse;
            case PRESSURE:
                Pressure pressure = new Pressure();
                pressure.setSystolic(function(SqlFunction.AVG, Pressure.Column.SYSTOLIC, interval));
                pressure.setDiastolic(function(SqlFunction.AVG, Pressure.Column.DIASTOLIC, interval));
                return pressure;
            default:
                return null;
        }
    }

    public M getMeasurement(Entry entry) {
        try {
            return getDao().queryBuilder().where().eq(Measurement.Column.ENTRY, entry).queryForFirst();
        } catch (SQLException exception) {
            Log.e(TAG, String.format("Could not fetch measurement of category '%s'", getClazz().toString()));
            return null;
        }
    }

    public int deleteMeasurements(Entry entry) {
        try {
            DeleteBuilder deleteBuilder = getDao().deleteBuilder();
            deleteBuilder.where().eq(Measurement.Column.ENTRY, entry);
            return deleteBuilder.delete();
        } catch (SQLException exception) {
            Log.e(TAG, String.format("Could not delete '%s' measurements of entry with id %d", getClazz().toString(), entry.getId()));
            return 0;
        }
    }
}
