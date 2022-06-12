package com.faltenreich.diaguard.shared.data.database.entity;

import androidx.annotation.NonNull;

import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.data.database.Database;
import com.faltenreich.diaguard.shared.data.primitive.FloatUtils;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.apache.commons.lang3.ArrayUtils;

import java.util.List;

@DatabaseTable
public class Meal extends Measurement {

    public class Column extends Measurement.Column {
        public static final String CARBOHYDRATES = "carbohydrates";
    }

    @DatabaseField(columnName = Column.CARBOHYDRATES)
    private float carbohydrates;

    public float getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(float carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public List<FoodEaten> getFoodEaten() {
        return Database.getInstance().getDatabase().foodEatenDao().getByMeal(getId());
    }

    private float getTotalCarbohydrates() {
        float carbohydrates = getCarbohydrates();
        for (FoodEaten eaten : getFoodEaten()) {
            carbohydrates += eaten.getCarbohydrates();
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

    @NonNull
    @Override
    public String toString() {
        return PreferenceStore.getInstance().getMeasurementForUi(getCategory(), getTotalCarbohydrates());
    }

    @Override
    public String[] getValuesForExport() {
        float value = carbohydrates;
        for (FoodEaten foodEaten : getFoodEaten()) {
            value += foodEaten.getCarbohydrates();
        }
        float valueFormatted = PreferenceStore.getInstance().formatDefaultToCustomUnit(getCategory(), value);
        String valueForUi = FloatUtils.parseFloat(valueFormatted);
        return ArrayUtils.addAll(new String[]{getCategory().name().toLowerCase()}, valueForUi);
    }
}
