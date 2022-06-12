package com.faltenreich.diaguard.shared.data.database.dao;

import android.util.Log;

import com.faltenreich.diaguard.shared.Helper;
import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.data.database.entity.FoodEaten;
import com.j256.ormlite.stmt.SelectArg;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FoodOrmLiteDao extends BaseServerDao<Food> {

    private static final String TAG = FoodOrmLiteDao.class.getSimpleName();

    private static FoodOrmLiteDao instance;

    public static FoodOrmLiteDao getInstance() {
        if (instance == null) {
            instance = new FoodOrmLiteDao();
        }
        return instance;
    }

    private FoodOrmLiteDao() {
        super(Food.class);
    }

    public Food getByName(String name) {
        try {
            return getQueryBuilder()
                .where().eq(Food.Column.NAME, new SelectArg(name))
                .queryForFirst();
        } catch (SQLException exception) {
            Log.e(TAG, exception.toString());
            return null;
        }
    }

    @Override
    public List<Food> getAll() {
        try {
            return getQueryBuilder()
                .orderBy(Food.Column.NAME, true)
                .orderBy(Food.Column.UPDATED_AT, false)
                .where().eq(Food.Column.LANGUAGE_CODE, Helper.getLanguageCode())
                .query();
        } catch (SQLException exception) {
            Log.e(TAG, exception.toString());
            return new ArrayList<>();
        }
    }

    private void cascadeFoodEaten(Food food) {
        List<FoodEaten> foodEatenList = FoodEatenDao.getInstance().getAll(food);
        for (FoodEaten foodEaten : foodEatenList) {
            FoodEatenDao.getInstance().delete(foodEaten);
        }
    }

    @Override
    public void softDelete(Food entity) {
        cascadeFoodEaten(entity);
        super.softDelete(entity);
    }

    @Override
    public int delete(Food object) {
        cascadeFoodEaten(object);
        return super.delete(object);
    }

    @Override
    public int delete(List<Food> objects) {
        for (Food food : objects) {
            cascadeFoodEaten(food);
        }
        return super.delete(objects);
    }
}
