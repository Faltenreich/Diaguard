package com.faltenreich.diaguard.data.entity;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

/**
 * Created by Faltenreich on 11.09.2016.
 */
public class Food extends BaseServerEntity {

    public class Column extends BaseServerEntity.Column {
        public static final String NAME = "name";
        public static final String IMAGE_URL = "imageUrl";
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

    @DatabaseField(columnName = Column.NAME)
    private String name;

    @DatabaseField(columnName = Column.IMAGE_URL)
    private String imageUrl;

    @DatabaseField(columnName = Column.CARBOHYDRATES)
    private float carbohydrates;

    @DatabaseField(columnName = Column.ENERGY)
    private float energy;

    @DatabaseField(columnName = Column.FAT)
    private float fat;

    @DatabaseField(columnName = Column.FAT_SATURATED)
    private float fatSaturated;

    @DatabaseField(columnName = Column.FIBER)
    private float fiber;

    @DatabaseField(columnName = Column.PROTEINS)
    private float proteins;

    @DatabaseField(columnName = Column.SALT)
    private float salt;

    @DatabaseField(columnName = Column.SODIUM)
    private float sodium;

    @DatabaseField(columnName = Column.SUGAR)
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

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
}
