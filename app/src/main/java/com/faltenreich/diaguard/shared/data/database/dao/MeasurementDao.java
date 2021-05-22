package com.faltenreich.diaguard.shared.data.database.dao;

import android.util.Log;

import com.faltenreich.diaguard.shared.data.database.entity.Activity;
import com.faltenreich.diaguard.shared.data.database.entity.BaseEntity;
import com.faltenreich.diaguard.shared.data.database.entity.BloodSugar;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Entry;
import com.faltenreich.diaguard.shared.data.database.entity.FoodEaten;
import com.faltenreich.diaguard.shared.data.database.entity.HbA1c;
import com.faltenreich.diaguard.shared.data.database.entity.Insulin;
import com.faltenreich.diaguard.shared.data.database.entity.Meal;
import com.faltenreich.diaguard.shared.data.database.entity.Measurement;
import com.faltenreich.diaguard.shared.data.database.entity.OxygenSaturation;
import com.faltenreich.diaguard.shared.data.database.entity.Pressure;
import com.faltenreich.diaguard.shared.data.database.entity.Pulse;
import com.faltenreich.diaguard.shared.data.database.entity.Weight;
import com.faltenreich.diaguard.shared.data.primitive.FloatUtils;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.DatabaseTableConfig;

import org.joda.time.DateTime;
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
        if (entity.getCategory() == Category.MEAL) {
            createOrUpdate((Meal) entity);
        }
        return entity;
    }

    @Override
    public int delete(M object) {
        if (object.getCategory() == Category.MEAL) {
            Meal meal = (Meal) object;
            for (FoodEaten foodEaten : meal.getFoodEaten()) {
                meal.getFoodEatenCache().add(foodEaten);
                FoodEatenDao.getInstance().delete(foodEaten);
            }
        }
        return super.delete(object);
    }

    private void createOrUpdate(Meal meal) {
        for (FoodEaten foodEatenOld : FoodEatenDao.getInstance().getAll(meal)) {
            int indexInCache = meal.getFoodEatenCache().indexOf(foodEatenOld);
            if (indexInCache != -1) {
                FoodEaten foodEatenNew = meal.getFoodEatenCache().get(indexInCache);
                if (!foodEatenNew.isValid()) {
                    FoodEatenDao.getInstance().delete(foodEatenOld);
                }
            } else {
                FoodEatenDao.getInstance().delete(foodEatenOld);
            }
        }
        for (FoodEaten foodEaten : meal.getFoodEatenCache()) {
            if (foodEaten.isValid()) {
                foodEaten.setMeal(meal);
                FoodEatenDao.getInstance().createOrUpdate(foodEaten);
            }
        }
    }

    public float function(SqlFunction sqlFunction, String column, Interval interval) {
        String classNameEntry = DatabaseTableConfig.extractTableName(null, Entry.class);
        String classNameMeasurement = DatabaseTableConfig.extractTableName(null, getClazz());
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

        List<String[]> results = queryRaw(query);
        if (results == null) {
            return 0;
        }

        String[] resultArray = results.get(0);
        String result = resultArray[0];
        try {
            return FloatUtils.parseNumber(result);
        } catch (NumberFormatException exception) {
            return 0;
        } catch (NullPointerException exception) {
            return 0;
        }
    }

    public Measurement getAvgMeasurement(Category category, Interval interval) {
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
                float avg = function(SqlFunction.SUM, Meal.Column.CARBOHYDRATES, interval) / daysBetween;
                float foodEatenSum = 0;
                List<FoodEaten> foodEatenList = FoodEatenDao.getInstance().getAll(interval);
                for (FoodEaten foodEaten : foodEatenList) {
                    foodEatenSum += foodEaten.getCarbohydrates();
                }
                avg = avg + (foodEatenSum / daysBetween);
                meal.setCarbohydrates(avg);
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
            case OXYGEN_SATURATION:
                OxygenSaturation oxygenSaturation = new OxygenSaturation();
                oxygenSaturation.setPercent(function(SqlFunction.AVG, OxygenSaturation.Column.PERCENT, interval));
                return oxygenSaturation;
            default:
                return null;
        }
    }

    public M getMeasurement(Entry entry) {
        try {
            return getQueryBuilder().where().eq(Measurement.Column.ENTRY, entry).queryForFirst();
        } catch (SQLException exception) {
            Log.e(TAG, exception.toString());
            return null;
        }
    }

    public List<M> getMeasurements(DateTime day) {
        try {
            if (day == null) {
                return new ArrayList<>();
            }
            DateTime start = day.withTimeAtStartOfDay();
            DateTime end = day.withTime(DateTimeConstants.HOURS_PER_DAY - 1,
                    DateTimeConstants.MINUTES_PER_HOUR - 1,
                    DateTimeConstants.SECONDS_PER_MINUTE - 1,
                    DateTimeConstants.MILLIS_PER_SECOND - 1);
            QueryBuilder<M, Long> measurementQb = getQueryBuilder();
            QueryBuilder<Entry, Long> entryQb = EntryDao.getInstance().getQueryBuilder();
            entryQb
                    .orderBy(Entry.Column.DATE, true)
                    .where().gt(Entry.Column.DATE, start).and().lt(Entry.Column.DATE, end);
            return measurementQb.join(entryQb).query();
        } catch (SQLException exception) {
            Log.e(TAG, exception.toString());
            return new ArrayList<>();
        }
    }
}
