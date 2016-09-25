package com.faltenreich.diaguard.data.dao;

import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.networking.openfoodfacts.dto.ProductDto;
import com.faltenreich.diaguard.networking.openfoodfacts.dto.SearchResponseDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Faltenreich on 23.09.2016.
 */

public class FoodDao extends BaseDao<Food> {

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

    public List<Food> parseFromDto(SearchResponseDto dto) {
        List<Food> foodList = new ArrayList<>();
        for (ProductDto productDto : dto.products) {
            Food food = new Food();
            food.setServerId(Integer.toString(productDto.identifier));
            food.setName(productDto.name);
            food.setImageUrl(productDto.imageUrl);
            food.setBrand(productDto.brand);
            food.setCarbohydrates(productDto.nutriments.carbohydrates);
            food.setEnergy(productDto.nutriments.energy);
            food.setFat(productDto.nutriments.fat);
            food.setFatSaturated(productDto.nutriments.fatSaturated);
            food.setFiber(productDto.nutriments.fiber);
            food.setProteins(productDto.nutriments.proteins);
            food.setSalt(productDto.nutriments.salt);
            food.setSodium(productDto.nutriments.sodium);
            food.setSugar(productDto.nutriments.sugar);
            foodList.add(food);
        }
        return foodList;
    }
}
