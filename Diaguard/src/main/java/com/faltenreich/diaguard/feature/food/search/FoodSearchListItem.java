package com.faltenreich.diaguard.feature.food.search;

import androidx.annotation.NonNull;

import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.data.database.entity.FoodEaten;

/**
 * Created by Filip on 10.07.2015.
 */
public class FoodSearchListItem {

    private Food food;
    private FoodEaten foodEaten;

    FoodSearchListItem(FoodEaten foodEaten) {
        this.food = foodEaten.getFood();
        this.foodEaten = foodEaten;
    }

    FoodSearchListItem(Food food) {
        this.food = food;
        this.foodEaten = null;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public FoodEaten getFoodEaten() {
        return foodEaten;
    }

    public void setFoodEaten(FoodEaten foodEaten) {
        this.foodEaten = foodEaten;
    }

    @Override
    public boolean equals(Object item) {
        return item instanceof FoodSearchListItem && food.equals(((FoodSearchListItem) item).getFood());
    }

    @NonNull
    @Override
    public String toString() {
        return food.toString();
    }
}
