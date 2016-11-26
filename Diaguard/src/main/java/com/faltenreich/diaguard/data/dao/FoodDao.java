package com.faltenreich.diaguard.data.dao;

import android.util.Log;

import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.event.Events;
import com.faltenreich.diaguard.event.networking.FoodSearchSucceededEvent;
import com.faltenreich.diaguard.networking.openfoodfacts.dto.ProductDto;
import com.faltenreich.diaguard.networking.openfoodfacts.dto.SearchResponseDto;
import com.faltenreich.diaguard.util.DateTimeUtils;
import com.faltenreich.diaguard.util.Helper;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import org.joda.time.DateTime;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

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
                    .orderBy(Food.Column.NAME, true)
                    .orderBy(Food.Column.UPDATED_AT, false)
                    .where().eq(Food.Column.LANGUAGE_CODE, Helper.getLanguageCode())
                    .query();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getLocalizedMessage());
            return new ArrayList<>();
        }
    }

    public List<Food> search(String query, long page) {
        try {
            QueryBuilder<Food, Long> queryBuilder = getDao().queryBuilder()
                    .orderBy(Food.Column.NAME, true)
                    .orderBy(Food.Column.UPDATED_AT, false)
                    .offset(page * BaseDao.PAGE_SIZE)
                    .limit(BaseDao.PAGE_SIZE);

            Where<Food, Long> where = queryBuilder.where();
            where.eq(Food.Column.LANGUAGE_CODE, Helper.getLanguageCode());

            boolean hasQuery = query != null && query.length() > 0;
            if (hasQuery) {
                String like = "%" + query + "%";
                where.like(Food.Column.NAME, like).or().like(Food.Column.FULL_NAME, like);
                return where.and(where, where).query();

            } else {
                return where.query();
            }
            
        } catch (SQLException exception) {
            Log.e(TAG, exception.getLocalizedMessage());
            return new ArrayList<>();
        }
    }

    public void createOrUpdate(SearchResponseDto dto) {
        List<Food> foodList = new ArrayList<>();
        Collections.reverse(dto.products);
        for (ProductDto productDto : dto.products) {
            if (productDto.isValid()) {
                foodList.add(0, parseFromDto(productDto));
            }
        }
        FoodDao.getInstance().bulkCreateOrUpdate(foodList);
        Events.post(new FoodSearchSucceededEvent(foodList));
    }

    private Food parseFromDto(ProductDto dto) {
        String serverId = Integer.toString(dto.identifier);

        Food food = getByServerId(serverId);
        boolean isNew = food == null;
        if (isNew) {
            food = new Food();
        }

        if (isNew || needsUpdate(food, dto)) {
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
            food.setLanguageCode(new Locale(dto.languageCode).getLanguage());
        }

        return food;
    }

    private boolean needsUpdate(Food food, ProductDto dto) {
        String lastEditDateString = dto.lastEditDates != null && dto.lastEditDates.length > 0 ? dto.lastEditDates[0] : null;
        DateTime lastEditDate = DateTimeUtils.parseFromString(lastEditDateString, ProductDto.DATE_FORMAT);
        return lastEditDate != null && food.getUpdatedAt().isBefore(lastEditDate);
    }
}
