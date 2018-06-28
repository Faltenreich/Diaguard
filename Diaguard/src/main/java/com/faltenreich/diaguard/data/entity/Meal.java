package com.faltenreich.diaguard.data.entity;

import com.faltenreich.diaguard.data.PreferenceHelper;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.List;

@DatabaseTable
public class Meal extends Measurement {

    public class Column extends Measurement.Column {
        public static final String CARBOHYDRATES = "carbohydrates";
    }

    @DatabaseField(columnName = Column.CARBOHYDRATES)
    private float carbohydrates;

    @ForeignCollectionField
    private ForeignCollection<FoodEaten> foodEaten;

    private List<FoodEaten> foodEatenCache;

    public float getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(float carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public ForeignCollection<FoodEaten> getFoodEaten() {
        return foodEaten;
    }

    public void setFoodEaten(ForeignCollection<FoodEaten> foodEaten) {
        this.foodEaten = foodEaten;
    }

    private float getTotalCarbohydrates() {
        float carbohydrates = getCarbohydrates();
        if (foodEaten != null) {
            for (FoodEaten eaten : foodEaten) {
                carbohydrates += eaten.getCarbohydrates();
            }
        }
        return carbohydrates;
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

    public List<FoodEaten> getFoodEatenCache() {
        if (foodEatenCache == null) {
            foodEatenCache = new ArrayList<>();
        }
        return foodEatenCache;
    }

    public void setFoodEatenCache(List<FoodEaten> foodEatenCache) {
        this.foodEatenCache = foodEatenCache;
    }

    @Override
    public String toString() {
        return PreferenceHelper.getInstance().getMeasurementForUi(getCategory(), getTotalCarbohydrates());
    }
}
