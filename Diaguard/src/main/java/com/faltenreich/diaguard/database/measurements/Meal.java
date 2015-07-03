package com.faltenreich.diaguard.database.measurements;

import com.faltenreich.diaguard.helpers.PreferenceHelper;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Filip on 11.05.2015.
 */
@DatabaseTable
public class Meal extends Measurement implements ICustomizable {

    public static final String CARBOHYDRATES = "carbohydrates";
    public static final String FOOD_ID = "food_id";

    @DatabaseField
    private float carbohydrates;

    @DatabaseField
    private int foodId;

    public float getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(float carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public Category getMeasurementType() {
        return Category.Meal;
    }

    @Override
    public float getValueForUser() {
        return PreferenceHelper.getInstance().formatDefaultToCustomUnit(Category.Meal, carbohydrates);
    }
}
