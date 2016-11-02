package com.faltenreich.diaguard.data.dao;

import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.networking.openfoodfacts.dto.ProductDto;
import com.faltenreich.diaguard.networking.openfoodfacts.dto.SearchResponseDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Faltenreich on 23.09.2016.
 */

public class FoodDao extends BaseServerDao<Food> {

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

    public List<Food> createOrUpdate(SearchResponseDto dto) {
        List<Food> foodList = new ArrayList<>();
        for (ProductDto productDto : dto.products) {
            if (productDto.isValid()) {
                Food food = parseFromDto(productDto);
                createOrUpdate(food);
                foodList.add(food);
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
        return food;
    }
}
