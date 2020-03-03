package com.faltenreich.diaguard.data.dao;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.entity.BaseServerEntity;
import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.data.entity.FoodEaten;
import com.faltenreich.diaguard.networking.openfoodfacts.dto.ProductDto;
import com.faltenreich.diaguard.networking.openfoodfacts.dto.SearchResponseDto;
import com.faltenreich.diaguard.util.DateTimeUtils;
import com.faltenreich.diaguard.util.Helper;
import com.faltenreich.diaguard.util.StringUtils;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;

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

    public List<Food> getAllFromUser() {
        try {
            return getDao().queryBuilder()
                    .orderBy(Food.Column.NAME, true)
                    .orderBy(Food.Column.UPDATED_AT, false)
                    .where().eq(Food.Column.LANGUAGE_CODE, Helper.getLanguageCode())
                    .and().isNull(BaseServerEntity.Column.SERVER_ID)
                    .query();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getLocalizedMessage());
            return new ArrayList<>();
        }
    }

    public List<Food> getAllCommon(Context context) {
        try {
            return getDao().queryBuilder()
                .orderBy(Food.Column.UPDATED_AT, true)
                .where().eq(Food.Column.LABELS, context.getString(R.string.food_common))
                .and().isNull(BaseServerEntity.Column.SERVER_ID)
                .query();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getLocalizedMessage());
            return new ArrayList<>();
        }
    }

    private void cascadeFoodEaten(Food food) {
        List<FoodEaten> foodEatenList = FoodEatenDao.getInstance().getAll(food);
        for (FoodEaten foodEaten : foodEatenList) {
            FoodEatenDao.getInstance().delete(foodEaten);
        }
    }

    @Override
    public void softDelete(Food entity) {
        cascadeFoodEaten(entity);
        super.softDelete(entity);
    }

    @Override
    public int delete(Food object) {
        cascadeFoodEaten(object);
        return super.delete(object);
    }

    @Override
    public int delete(List<Food> objects) {
        for (Food food : objects) {
            cascadeFoodEaten(food);
        }
        return super.delete(objects);
    }

    public Food get(String name) {
        try {
            return getDao().queryBuilder()
                    .where().eq(Food.Column.NAME, name)
                    .queryForFirst();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getLocalizedMessage());
            return null;
        }
    }

    public List<Food> search(String query, long page) {
        try {
            QueryBuilder<Food, Long> queryBuilder = getDao().queryBuilder()
                    .orderBy(Food.Column.NAME, true)
                    .orderBy(Food.Column.UPDATED_AT, false)
                    .offset(page * BaseDao.PAGE_SIZE)
                    .limit(BaseDao.PAGE_SIZE);

            boolean hasQuery = query != null && query.length() > 0;
            if (hasQuery) {
                return queryBuilder
                        .where().eq(Food.Column.LANGUAGE_CODE, Helper.getLanguageCode())
                        .and().like(Food.Column.NAME, new SelectArg("%" + query + "%"))
                        .and().isNull(Food.Column.DELETED_AT)
                        .query();

            } else {
                return queryBuilder
                        .where().eq(Food.Column.LANGUAGE_CODE, Helper.getLanguageCode())
                        .and().isNull(Food.Column.DELETED_AT)
                        .query();
            }
            
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
                if (food != null) {
                    foodList.add(0, food);
                }
            }
        }
        FoodDao.getInstance().bulkCreateOrUpdate(foodList);
        return foodList;
    }

    @Nullable
    private Food parseFromDto(ProductDto dto) {
        if (!dto.identifier.isJsonPrimitive()) {
            return null;
        }
        String serverId = dto.identifier.getAsJsonPrimitive().getAsString();
        if (StringUtils.isBlank(serverId)) {
            return null;
        }

        Food food = getByServerId(serverId);
        boolean isNew = food == null;
        if (isNew) {
            food = new Food();
        }

        if (isNew || needsUpdate(food, dto)) {
            food.setServerId(serverId);
            food.setName(dto.name);
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
            Locale locale = dto.languageCode != null ? new Locale(dto.languageCode) : Helper.getLocale();
            food.setLanguageCode(locale.getLanguage());
        }

        return food;
    }

    private boolean needsUpdate(Food food, ProductDto dto) {
        String lastEditDateString = dto.lastEditDates != null && dto.lastEditDates.length > 0 ? dto.lastEditDates[0] : null;
        DateTime lastEditDate = DateTimeUtils.parseFromString(lastEditDateString, ProductDto.DATE_FORMAT);
        return lastEditDate != null && food.getUpdatedAt() != null && food.getUpdatedAt().isBefore(lastEditDate);
    }
}
