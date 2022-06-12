package com.faltenreich.diaguard.shared.data.database.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import com.faltenreich.diaguard.feature.export.Backupable;
import com.faltenreich.diaguard.shared.data.database.Database;
import com.faltenreich.diaguard.shared.data.database.dao.MeasurementDao;

import org.joda.time.format.DateTimeFormat;

@Entity(
    foreignKeys = {
        /*
        @ForeignKey(
            entity = Meal.class,
            parentColumns = {BaseEntity.Column.ID},
            childColumns = {FoodEaten.Column.MEAL_ID},
            onDelete = ForeignKey.CASCADE
        ),
         */
        @ForeignKey(
            entity = Food.class,
            parentColumns = {BaseEntity.Column.ID},
            childColumns = {FoodEaten.Column.FOOD_ID},
            onDelete = ForeignKey.CASCADE
        )
    }
)
public class FoodEaten extends BaseEntity implements Backupable {

    public static final String BACKUP_KEY = "foodEaten";

    public class Column extends BaseEntity.Column {
        public static final String AMOUNT_IN_GRAMS = "amountInGrams";
        public static final String MEAL = "meal";
        public static final String MEAL_ID = "mealId";
        public static final String FOOD = "food";
        public static final String FOOD_ID = "foodId";
    }

    @ColumnInfo(name = Column.AMOUNT_IN_GRAMS)
    private float amountInGrams;

    @ColumnInfo(name = Column.MEAL_ID)
    private long mealId;

    @ColumnInfo(name = Column.FOOD_ID)
    private long foodId;

    public float getAmountInGrams() {
        return amountInGrams;
    }

    public void setAmountInGrams(float amountInGrams) {
        this.amountInGrams = amountInGrams;
    }

    public long getMealId() {
        return mealId;
    }

    public void setMealId(long mealId) {
        this.mealId = mealId;
    }

    @Deprecated
    public Meal getMeal() {
        return (Meal) MeasurementDao.getInstance(Meal.class).getById(mealId);
    }

    public long getFoodId() {
        return foodId;
    }

    public void setFoodId(long foodId) {
        this.foodId = foodId;
    }

    @Deprecated
    public Food getFood() {
        return Database.getInstance().getDatabase().foodDao().getById(foodId);
    }

    public float getCarbohydrates() {
        return getFood() != null ? getAmountInGrams() * getFood().getCarbohydrates() / 100 : 0;
    }

    public boolean isValid() {
        return amountInGrams > 0;
    }

    @Nullable
    public String print() {
        Food food = getFood();
        int amountEaten = (int) getAmountInGrams();
        if (food != null && amountEaten > 0) {
            return String.format("%d g %s", amountEaten, food.getName());
        } else {
            return null;
        }
    }

    @NonNull
    @Override
    public String toString() {
        Food food = getFood();
        return String.format("%s: %f grams (updated: %s)",
            food.getName(),
            amountInGrams,
            DateTimeFormat.mediumDateTime().print(getUpdatedAt())
        );
    }

    @Override
    public String getKeyForBackup() {
        return BACKUP_KEY;
    }

    @Override
    public String[] getValuesForBackup() {
        Food food = getFood();
        return new String[]{food.getName(), Float.toString(amountInGrams)};
    }
}
