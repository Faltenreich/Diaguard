package com.faltenreich.diaguard.shared.data.database.dao;

import android.util.Log;

import com.faltenreich.diaguard.shared.data.database.entity.Entry;
import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.data.database.entity.FoodEaten;
import com.faltenreich.diaguard.shared.data.database.entity.Meal;
import com.j256.ormlite.stmt.QueryBuilder;

import org.joda.time.Interval;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Faltenreich on 23.09.2016.
 */

public class FoodEatenDao extends BaseDao<FoodEaten> {

    private static final String TAG = FoodEatenDao.class.getSimpleName();

    private static FoodEatenDao instance;

    public static FoodEatenDao getInstance() {
        if (instance == null) {
            instance = new FoodEatenDao();
        }
        return instance;
    }

    private FoodEatenDao() {
        super(FoodEaten.class);
    }

    public List<FoodEaten> getAllOrdered() {
        try {
            // TODO: Distinct
            // TODO: Order by Entry.getDateTime()
            return getQueryBuilder()
                    .orderBy(FoodEaten.Column.CREATED_AT, false)
                    .join(FoodDao.getInstance().getQueryBuilder())
                    .query();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getLocalizedMessage());
            return new ArrayList<>();
        }
    }

    public long count(Food food) {
        try {
            return getQueryBuilder()
                    .orderBy(FoodEaten.Column.CREATED_AT, false)
                    .where().eq(FoodEaten.Column.FOOD, food)
                    .countOf();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getLocalizedMessage());
            return 0;
        }
    }

    public List<FoodEaten> getAll(Food food) {
        try {
            return getQueryBuilder()
                    .orderBy(FoodEaten.Column.CREATED_AT, false)
                    .where().eq(FoodEaten.Column.FOOD, food)
                    .query();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getLocalizedMessage());
            return new ArrayList<>();
        }
    }

    public List<FoodEaten> getAll(Meal meal) {
        try {
            return getQueryBuilder()
                    .orderBy(FoodEaten.Column.CREATED_AT, false)
                    .where().eq(FoodEaten.Column.MEAL, meal)
                    .query();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getLocalizedMessage());
            return new ArrayList<>();
        }
    }

    public List<FoodEaten> getAll(Interval interval) {
        try {
            QueryBuilder<Entry, Long> queryBuilderEntry = EntryDao.getInstance().getQueryBuilder();
            queryBuilderEntry
                    .where().ge(Entry.Column.DATE, interval.getStart())
                    .and().le(Entry.Column.DATE, interval.getEnd());
            QueryBuilder<Meal, Long> queryBuilderMeal = MeasurementDao.getInstance(Meal.class).getQueryBuilder();
            return getQueryBuilder().join(queryBuilderMeal.join(queryBuilderEntry)).query();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getLocalizedMessage());
            return new ArrayList<>();
        }
    }
}
