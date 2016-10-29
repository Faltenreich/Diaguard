package com.faltenreich.diaguard.data.entity;

import android.support.annotation.StringRes;

import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.util.Helper;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

import static com.faltenreich.diaguard.data.entity.Food.Column.FAT_SATURATED;
import static com.faltenreich.diaguard.data.entity.Food.Column.PROTEINS;
import static com.faltenreich.diaguard.data.entity.Food.Column.SUGAR;

/**
 * Created by Faltenreich on 11.09.2016.
 */
public class Food extends BaseServerEntity {

    private static final String IMAGE_SUFFIX = ".jpg";
    private static final String KEYWORD_FULL_RESOLUTION = "full";

    public class Column extends BaseServerEntity.Column {
        public static final String NAME = "name";
        public static final String IMAGE_URL = "imageUrl";
        public static final String BRAND = "brand";
        public static final String CARBOHYDRATES = "carbohydrates";
        public static final String ENERGY = "energy";
        public static final String FAT = "fat";
        public static final String FAT_SATURATED = "fatSaturated";
        public static final String FIBER = "fiber";
        public static final String PROTEINS = "proteins";
        public static final String SALT = "salt";
        public static final String SODIUM = "sodium";
        public static final String SUGAR = "sugar";
        public static final String FOOD_EATEN = "foodEaten";
    }

    public enum Nutrient {

        ENERGY(R.string.nutrient_energy),
        FAT(R.string.nutrient_fat),
        FAT_SATURATED(R.string.nutrient_fat_saturated),
        CARBOHYDRATES(R.string.nutrient_carbohydrates),
        SUGAR(R.string.nutrient_sugar),
        FIBER(R.string.nutrient_fiber),
        PROTEINS(R.string.nutrient_proteins),
        SALT(R.string.nutrient_salt),
        SODIUM(R.string.nutrient_sodium);

        private int textResId;

        Nutrient(@StringRes int textResId) {
            this.textResId = textResId;
        }

        public String getLabel() {
            return DiaguardApplication.getContext().getString(textResId);
        }

        public @StringRes int getUnit() {
            switch (this) {
                case ENERGY: return R.string.energy_acronym;
                default: return R.string.grams_acronym;
            }
        }

        public float getValue(Food food) {
            switch (this) {
                case CARBOHYDRATES: return food.getCarbohydrates();
                case ENERGY: return food.getEnergy();
                case FAT: return food.getFat();
                case FAT_SATURATED: return food.getFatSaturated();
                case FIBER: return food.getFiber();
                case PROTEINS: return food.getProteins();
                case SALT: return food.getSalt();
                case SODIUM: return food.getSodium();
                case SUGAR: return food.getSugar();
                default: return -1;
            }
        }
    }

    @DatabaseField(columnName = Column.NAME)
    private String name;

    @DatabaseField(columnName = Column.IMAGE_URL)
    private String imageUrl;

    @DatabaseField(columnName = Column.BRAND)
    private String brand;

    @DatabaseField(columnName = Column.CARBOHYDRATES)
    private float carbohydrates;

    @DatabaseField(columnName = Column.ENERGY)
    private float energy;

    @DatabaseField(columnName = Column.FAT)
    private float fat;

    @DatabaseField(columnName = FAT_SATURATED)
    private float fatSaturated;

    @DatabaseField(columnName = Column.FIBER)
    private float fiber;

    @DatabaseField(columnName = PROTEINS)
    private float proteins;

    @DatabaseField(columnName = Column.SALT)
    private float salt;

    @DatabaseField(columnName = Column.SODIUM)
    private float sodium;

    @DatabaseField(columnName = SUGAR)
    private float sugar;

    @ForeignCollectionField(columnName = Column.FOOD_EATEN)
    private ForeignCollection<FoodEaten> foodEaten;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getFullImageUrl() {
        if (imageUrl != null && imageUrl.endsWith(IMAGE_SUFFIX)) {
            String fullImageUrl = imageUrl.substring(0, imageUrl.length() - IMAGE_SUFFIX.length());
            int indexOfLastDot = fullImageUrl.lastIndexOf('.');
            fullImageUrl = fullImageUrl.substring(0, indexOfLastDot + 1);
            fullImageUrl = fullImageUrl + KEYWORD_FULL_RESOLUTION + IMAGE_SUFFIX;
            return fullImageUrl;
        } else {
            return imageUrl;
        }
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public float getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(float carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public float getEnergy() {
        return energy;
    }

    public void setEnergy(float energy) {
        this.energy = energy;
    }

    public float getFat() {
        return fat;
    }

    public void setFat(float fat) {
        this.fat = fat;
    }

    public float getFatSaturated() {
        return fatSaturated;
    }

    public void setFatSaturated(float fatSaturated) {
        this.fatSaturated = fatSaturated;
    }

    public float getFiber() {
        return fiber;
    }

    public void setFiber(float fiber) {
        this.fiber = fiber;
    }

    public float getProteins() {
        return proteins;
    }

    public void setProteins(float proteins) {
        this.proteins = proteins;
    }

    public float getSalt() {
        return salt;
    }

    public void setSalt(float salt) {
        this.salt = salt;
    }

    public float getSodium() {
        return sodium;
    }

    public void setSodium(float sodium) {
        this.sodium = sodium;
    }

    public float getSugar() {
        return sugar;
    }

    public void setSugar(float sugar) {
        this.sugar = sugar;
    }

    public ForeignCollection<FoodEaten> getFoodEaten() {
        return foodEaten;
    }

    public void setFoodEaten(ForeignCollection<FoodEaten> foodEaten) {
        this.foodEaten = foodEaten;
    }

    public String getValueForUi() {
        float valueFormatted = PreferenceHelper.getInstance().formatDefaultToCustomUnit(Measurement.Category.MEAL, carbohydrates);
        return Helper.parseFloat(valueFormatted);
    }

    @Override
    public String toString() {
        return name;
    }
}
