package com.faltenreich.diaguard.shared.data.database.dao;

import com.faltenreich.diaguard.shared.data.database.entity.FoodEaten;

import org.joda.time.DateTime;

import java.util.List;

public interface FoodEatenDao {

    long createOrUpdate(FoodEaten foodEaten);

    void createOrUpdate(List<FoodEaten> foodEatenList);

    List<FoodEaten> getByMeal(long mealId);

    List<FoodEaten> getByFood(long foodId);

    List<FoodEaten> getBetween(DateTime start, DateTime end);

    List<FoodEaten> getLatest(long count);

    long countByFood(long foodId);

    // TODO: Replace with CASCADE DELETE via ForeignKey
    @Deprecated
    void delete(FoodEaten foodEaten);
}
