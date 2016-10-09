package com.faltenreich.diaguard.data.dao;

import com.faltenreich.diaguard.data.entity.FoodEaten;

/**
 * Created by Faltenreich on 23.09.2016.
 */

public class FoodEatenDao extends BaseDao<FoodEaten> {

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
}
