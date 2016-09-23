package com.faltenreich.diaguard.data.dao;

import com.faltenreich.diaguard.data.entity.Food;

/**
 * Created by Faltenreich on 23.09.2016.
 */

public class FoodDao extends BaseDao<Food> {

    private static final String TAG = EntryDao.class.getSimpleName();

    private static FoodDao instance;

    public static FoodDao getInstance() {
        if (instance == null) {
            instance = new FoodDao();
        }
        return instance;
    }

    private FoodDao() {
        super(Food.class);
    }
}
