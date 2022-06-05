package com.faltenreich.diaguard.shared.data.database.dao;

import android.util.Log;

import com.faltenreich.diaguard.shared.Helper;
import com.faltenreich.diaguard.shared.data.database.entity.BaseServerEntity;
import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.data.database.entity.FoodEaten;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FoodOrmLiteDao extends BaseServerDao<Food> implements FoodDao {

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

    public List<Food> getAllFromUser() {
        try {
            return getQueryBuilder()
                .orderBy(Food.Column.NAME, true)
                .orderBy(Food.Column.UPDATED_AT, false)
                .where().eq(Food.Column.LANGUAGE_CODE, Helper.getLanguageCode())
                .and().isNull(BaseServerEntity.Column.SERVER_ID)
                .query();
        } catch (SQLException exception) {
            Log.e(TAG, exception.toString());
            return new ArrayList<>();
        }
    }

    public List<Food> getAllCommon() {
        try {
            return getQueryBuilder()
                .orderBy(Food.Column.UPDATED_AT, true)
                .where().isNotNull(Food.Column.LABELS)
                .and().isNull(BaseServerEntity.Column.SERVER_ID)
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

    public List<Food> search(
        String query,
        long page,
        boolean showCustomFood,
        boolean showCommonFood,
        boolean showBrandedFood
    ) {
        if (!showCustomFood && !showCommonFood && !showBrandedFood) {
            return new ArrayList<>();
        }

        try {
            QueryBuilder<Food, Long> queryBuilder = getQueryBuilder()
                .orderByRaw(String.format("%s COLLATE NOCASE", Food.Column.NAME))
                .orderBy(Food.Column.UPDATED_AT, false)
                .offset(page * BaseDao.PAGE_SIZE)
                .limit(BaseDao.PAGE_SIZE);

            Where<Food, Long> where = queryBuilder.where();
            where.isNull(Food.Column.DELETED_AT);

            if (query != null && query.length() > 0) {
                where.and();
                where.like(Food.Column.NAME, new SelectArg("%" + query + "%"));
            }

            int whereTypeCount = 0;

            if (showCustomFood) {
                where.isNull(Food.Column.LABELS);
                where.isNull(Food.Column.SERVER_ID);
                where.and(2);
                whereTypeCount++;
            }

            if (showCommonFood) {
                where.isNotNull(Food.Column.LABELS);
                where.isNull(Food.Column.SERVER_ID);
                where.and(2);
                whereTypeCount++;
            }

            if (showBrandedFood) {
                where.isNotNull(Food.Column.SERVER_ID);
                whereTypeCount++;
            }

            where.or(whereTypeCount);
            where.and(2);

            return where.query();

        } catch (SQLException exception) {
            Log.e(TAG, exception.toString());
            return new ArrayList<>();
        }
    }
}
