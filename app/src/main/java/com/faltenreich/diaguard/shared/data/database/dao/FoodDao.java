package com.faltenreich.diaguard.shared.data.database.dao;

import androidx.annotation.Nullable;

import com.faltenreich.diaguard.shared.data.database.entity.Food;

import java.util.List;

public interface FoodDao {

    Food createOrUpdate(Food food);

    void createOrUpdate(List<Food> foodList);

    @Nullable
    Food getById(long id);

    @Nullable
    Food getByServerId(String serverId);

    @Nullable
    Food getByName(String name);

    List<Food> getAllCommon();

    List<Food> getAllFromUser();

    List<Food> search(
        String query,
        long page,
        boolean showCustomFood,
        boolean showCommonFood,
        boolean showBrandedFood
    );

    void softDelete(Food food);

    int delete(Food object);

    int delete(List<Food> objects);

    // TODO: Check if necessary
    void deleteAll();
}
