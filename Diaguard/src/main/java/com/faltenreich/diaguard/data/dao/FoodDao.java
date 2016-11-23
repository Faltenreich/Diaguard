package com.faltenreich.diaguard.data.dao;

import android.util.Log;

import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.networking.openfoodfacts.dto.ProductDto;
import com.faltenreich.diaguard.networking.openfoodfacts.dto.SearchResponseDto;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Faltenreich on 23.09.2016.
 */

public class FoodDao extends BaseServerDao<Food> {

    private static final String TAG = FoodDao.class.getSimpleName();

    private static FoodDao instance;

    public static FoodDao getInstance() {
        if (instance == null) {
            instance = new FoodDao();
        }
        return instance;
    }

    private FoodDao() {
        super(Food.class);
    }

    @Override
    public List<Food> getAll() {
        try {
            return getDao().queryBuilder()
                    .orderBy(Food.Column.UPDATED_AT, false)
                    .query();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getLocalizedMessage());
            return new ArrayList<>();
        }
    }

    public List<Food> createOrUpdate(SearchResponseDto dto) {
        List<Food> foodList = new ArrayList<>();
        Collections.reverse(dto.products);
        for (ProductDto productDto : dto.products) {
            if (productDto.isValid()) {
                Food food = parseFromDto(productDto);
                createOrUpdate(food);
                foodList.add(0, food);
            }
        }
        return foodList;
    }

    private Food parseFromDto(ProductDto dto) {
        String serverId = Integer.toString(dto.identifier);
        Food food = getByServerId(serverId);
        if (food == null) {
            food = new Food();
        }
        food.setServerId(serverId);
        food.setName(dto.name);
        food.setFullName(dto.fullName);
        food.setImageUrl(dto.imageUrl);
        food.setBrand(dto.brand);
        food.setIngredients(dto.ingredients != null ? dto.ingredients.replaceAll("_", "") : null);
        food.setLabels(dto.labels);
        food.setCarbohydrates(dto.nutrients.carbohydrates);
        food.setEnergy(dto.nutrients.energy);
        food.setFat(dto.nutrients.fat);
        food.setFatSaturated(dto.nutrients.fatSaturated);
        food.setFiber(dto.nutrients.fiber);
        food.setProteins(dto.nutrients.proteins);
        food.setSalt(dto.nutrients.salt);
        food.setSodium(dto.nutrients.sodium);
        food.setSugar(dto.nutrients.sugar);
        food.setSugarLevel(dto.nutrientLevels.sugar);
        food.setCountryCode(PreferenceHelper.getInstance().getCountryCode());
        food.setLanguageCode(food.getCountryCode());
        return food;
    }

    public List<Food> search(String query, long page) {
        try {
            QueryBuilder<Food, Long> queryBuilder = getDao().queryBuilder()
                    .orderBy(Food.Column.UPDATED_AT, false)
                    .offset(page * BaseDao.PAGE_SIZE)
                    .limit(BaseDao.PAGE_SIZE);
            if (query != null && query.length() > 0) {
                String like = "%" + query + "%";
                queryBuilder
                        .where().like(Food.Column.NAME, like)
                        .or().like(Food.Column.FULL_NAME, like);
            }
            return queryBuilder.query();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getLocalizedMessage());
            return new ArrayList<>();
        }
    }
}
