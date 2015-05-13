package com.faltenreich.diaguard.database.measurements;

import com.faltenreich.diaguard.database.DatabaseHelper;

/**
 * Created by Filip on 11.05.2015.
 */
public class Meal extends Measurement {

    private long carbohydrates;
    private int foodId;

    public long getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(long carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    @Override
    public String getTableName() {
        return DatabaseHelper.MEAL;
    }
}
