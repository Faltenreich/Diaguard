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

    private static final String TAG = EntryDao.class.getSimpleName();

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

    public List<Food> getAllFoodEaten() {
        return new ArrayList<>();
    }

    public List<Food> createOrUpdate(SearchResponseDto dto) {
        List<Food> foodList = new ArrayList<>();
        for (ProductDto productDto : dto.products) {
            Food food = parseFromDto(productDto);
            createOrUpdate(food);
            foodList.add(food);
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
        food.setImageUrl(dto.imageUrl);
        food.setBrand(dto.brand);
        food.setCarbohydrates(dto.nutriments.carbohydrates);
        food.setEnergy(dto.nutriments.energy);
        food.setFat(dto.nutriments.fat);
        food.setFatSaturated(dto.nutriments.fatSaturated);
        food.setFiber(dto.nutriments.fiber);
        food.setProteins(dto.nutriments.proteins);
        food.setSalt(dto.nutriments.salt);
        food.setSodium(dto.nutriments.sodium);
        food.setSugar(dto.nutriments.sugar);
        return food;
    }
}
