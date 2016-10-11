package com.faltenreich.diaguard.data.dao;

import android.util.Log;

import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.data.entity.FoodEaten;
import com.faltenreich.diaguard.data.entity.Meal;

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
            return getDao().queryBuilder()
                    .orderBy(FoodEaten.Column.CREATED_AT, false)
                    .join(FoodDao.getInstance().getQueryBuilder())
                    .query();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getLocalizedMessage());
            return new ArrayList<>();
        }
    }

    public List<FoodEaten> getAll(Food food) {
        try {
            return getDao().queryBuilder()
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
            return getDao().queryBuilder()
                    .orderBy(FoodEaten.Column.CREATED_AT, false)
                    .where().eq(FoodEaten.Column.MEAL, meal)
                    .query();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getLocalizedMessage());
            return new ArrayList<>();
        }
    }
}
