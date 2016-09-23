package com.faltenreich.diaguard.data.entity;

import com.faltenreich.diaguard.data.PreferenceHelper;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Filip on 11.05.2015.
 */
@DatabaseTable
public class Meal extends Measurement {

    public class Column extends Measurement.Column {
        public static final String CARBOHYDRATES = "carbohydrates";
        public static final String FOOD_EATEN = "foodEaten";
    }

    @DatabaseField(columnName = Column.CARBOHYDRATES)
    private float carbohydrates;

    @DatabaseField(columnName = Column.FOOD_EATEN, foreign = true, foreignAutoRefresh = true)
    private FoodEaten foodEaten;

    public float getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(float carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public FoodEaten getFoodEaten() {
        return foodEaten;
    }

    public void setFoodEaten(FoodEaten foodEaten) {
        this.foodEaten = foodEaten;
    }

    @Override
    public Category getCategory() {
        return Category.MEAL;
    }

    @Override
    public float[] getValues() {
        return new float[] { carbohydrates };
    }

    @Override
    public void setValues(float... values) {
        if (values.length > 0) {
            carbohydrates = values[0];
        }
    }

    @Override
    public String toString() {
        return PreferenceHelper.getInstance().getMeasurementForUi(getCategory(), carbohydrates);
    }
}
