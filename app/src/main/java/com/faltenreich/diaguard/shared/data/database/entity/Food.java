package com.faltenreich.diaguard.shared.data.database.entity;

import static com.faltenreich.diaguard.shared.data.database.entity.Food.Column.FAT_SATURATED;
import static com.faltenreich.diaguard.shared.data.database.entity.Food.Column.PROTEINS;
import static com.faltenreich.diaguard.shared.data.database.entity.Food.Column.SUGAR;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.export.Backupable;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.data.primitive.FloatUtils;

@Entity
public class Food extends BaseServerEntity implements Backupable {

    public static final String BACKUP_KEY = "food";

    @SuppressWarnings("WeakerAccess")
    public class Column extends BaseServerEntity.Column {
        public static final String NAME = "name";
        public static final String BRAND = "brand";
        public static final String INGREDIENTS = "ingredients";
        public static final String LABELS = "labels";
        public static final String CARBOHYDRATES = "carbohydrates";
        public static final String ENERGY = "energy";
        public static final String FAT = "fat";
        public static final String FAT_SATURATED = "fatSaturated";
        public static final String FIBER = "fiber";
        public static final String PROTEINS = "proteins";
        public static final String SALT = "salt";
        public static final String SODIUM = "sodium";
        public static final String SUGAR = "sugar";
        public static final String LANGUAGE_CODE = "languageCode";
        public static final String FOOD_EATEN = "foodEaten";
    }

    public enum Nutrient {
        CARBOHYDRATES(R.string.carbohydrates),
        SUGAR(R.string.sugar),
        ENERGY(R.string.energy),
        FAT(R.string.fat),
        FAT_SATURATED(R.string.fat_saturated),
        FIBER(R.string.fiber),
        PROTEINS(R.string.proteins),
        SALT(R.string.salt),
        SODIUM(R.string.sodium);

        private final int textResId;

        Nutrient(@StringRes int textResId) {
            this.textResId = textResId;
        }

        public String getLabel(Context context) {
            return context.getString(textResId);
        }

        public @StringRes int getUnitResId() {
            switch (this) {
                case ENERGY: return R.string.energy_acronym;
                case SODIUM: return R.string.milligrams_acronym;
                default: return R.string.grams_acronym;
            }
        }

        public Float getValue(Food food) {
            switch (this) {
                case CARBOHYDRATES: return food.getCarbohydrates();
                case ENERGY: return food.getEnergy();
                case FAT: return food.getFat();
                case FAT_SATURATED: return food.getFatSaturated();
                case FIBER: return food.getFiber();
                case PROTEINS: return food.getProteins();
                case SALT: return food.getSalt();
                case SODIUM: return food.getSodium() != null ? food.getSodium() * 1000 : null;
                case SUGAR: return food.getSugar();
                default: return null;
            }
        }

        public void setValue(Food food, float value) {
            switch (this) {
                case CARBOHYDRATES: food.setCarbohydrates(value); break;
                case ENERGY: food.setEnergy(value); break;
                case FAT: food.setFat(value); break;
                case FAT_SATURATED: food.setFatSaturated(value); break;
                case FIBER: food.setFiber(value); break;
                case PROTEINS: food.setProteins(value); break;
                case SALT: food.setSalt(value); break;
                case SODIUM: food.setSodium(value > 0 ? value / 1000 : value); break;
                case SUGAR: food.setSugar(value); break;
                default: Log.e(Nutrient.class.getSimpleName(), "Unsupported nutrient for applyValue(): " + this);
            }
        }
    }

    @ColumnInfo(name = Tag.Column.NAME)
    private String name;

    @ColumnInfo(name = Column.BRAND)
    private String brand;

    @ColumnInfo(name = Column.INGREDIENTS)
    private String ingredients;

    @ColumnInfo(name = Column.LABELS)
    private String labels;

    @ColumnInfo(name = Column.CARBOHYDRATES, defaultValue = "-1")
    private Float carbohydrates;

    @ColumnInfo(name = Column.ENERGY, defaultValue = "-1")
    private Float energy;

    @ColumnInfo(name = Column.FAT, defaultValue = "-1")
    private Float fat;

    @ColumnInfo(name = FAT_SATURATED, defaultValue = "-1")
    private Float fatSaturated;

    @ColumnInfo(name = Column.FIBER, defaultValue = "-1")
    private Float fiber;

    @ColumnInfo(name = PROTEINS, defaultValue = "-1")
    private Float proteins;

    @ColumnInfo(name = Column.SALT, defaultValue = "-1")
    private Float salt;

    @ColumnInfo(name = Column.SODIUM, defaultValue = "-1")
    private Float sodium;

    @ColumnInfo(name = SUGAR, defaultValue = "-1")
    private Float sugar;

    @ColumnInfo(name = Column.LANGUAGE_CODE)
    private String languageCode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public Float getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(Float carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public Float getEnergy() {
        return energy;
    }

    public void setEnergy(Float energy) {
        this.energy = energy;
    }

    public Float getFat() {
        return fat;
    }

    public void setFat(Float fat) {
        this.fat = fat;
    }

    public Float getFatSaturated() {
        return fatSaturated;
    }

    public void setFatSaturated(Float fatSaturated) {
        this.fatSaturated = fatSaturated;
    }

    public Float getFiber() {
        return fiber;
    }

    public void setFiber(Float fiber) {
        this.fiber = fiber;
    }

    public Float getProteins() {
        return proteins;
    }

    public void setProteins(Float proteins) {
        this.proteins = proteins;
    }

    public Float getSalt() {
        return salt;
    }

    public void setSalt(Float salt) {
        this.salt = salt;
    }

    public Float getSodium() {
        return sodium;
    }

    public void setSodium(Float sodium) {
        this.sodium = sodium;
    }

    public Float getSugar() {
        return sugar;
    }

    public void setSugar(Float sugar) {
        this.sugar = sugar;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public boolean isCustomFood(Context context) {
        return !isBrandedFood() && !isCommonFood(context);
    }

    public boolean isCommonFood(Context context) {
        return labels != null && labels.contains(context.getString(R.string.food_common));
    }

    public boolean isBrandedFood() {
        return getServerId() != null;
    }

    public FoodType getFoodType(Context context) {
        if (isBrandedFood()) {
            return FoodType.BRANDED;
        } else if (isCommonFood(context)) {
            return FoodType.COMMON;
        } else {
            return FoodType.CUSTOM;
        }
    }

    public String getValueForUi() {
        if (carbohydrates != null) {
            float valueFormatted = PreferenceStore.getInstance().formatDefaultToCustomUnit(Category.MEAL, carbohydrates);
            return FloatUtils.parseFloat(valueFormatted);
        } else {
            return "";
        }
    }

    @Override
    public String getKeyForBackup() {
        return BACKUP_KEY;
    }

    @Override
    public String[] getValuesForBackup() {
        return new String[]{name, brand, ingredients, Float.toString(carbohydrates)};
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
