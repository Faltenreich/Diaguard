package com.faltenreich.diaguard.shared.data.database.dao;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.datetime.DateTimeUtils;
import com.faltenreich.diaguard.feature.food.networking.dto.ProductDto;
import com.faltenreich.diaguard.feature.food.networking.dto.SearchResponseDto;
import com.faltenreich.diaguard.shared.Helper;
import com.faltenreich.diaguard.shared.data.database.entity.BaseServerEntity;
import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.data.database.entity.FoodEaten;
import com.faltenreich.diaguard.shared.data.primitive.StringUtils;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.stmt.Where;

import org.joda.time.DateTime;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

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
            return getQueryBuilder()
                .orderBy(Food.Column.NAME, true)
                .orderBy(Food.Column.UPDATED_AT, false)
                .where().eq(Food.Column.LANGUAGE_CODE, Helper.getLanguageCode())
                .query();
        } catch (SQLException exception) {
            Log.e(TAG, exception.toString());
            return new ArrayList<>();
        }
    }

    public List<Food> getAllFromUser() {
        try {
            return getQueryBuilder()
                .orderBy(Food.Column.NAME, true)
                .orderBy(Food.Column.UPDATED_AT, false)
                .where().eq(Food.Column.LANGUAGE_CODE, Helper.getLanguageCode())
                .and().isNull(BaseServerEntity.Column.SERVER_ID)
                .query();
        } catch (SQLException exception) {
            Log.e(TAG, exception.toString());
            return new ArrayList<>();
        }
    }

    public List<Food> getAllCommon(Context context) {
        try {
            return getQueryBuilder()
                .orderBy(Food.Column.UPDATED_AT, true)
                .where().eq(Food.Column.LABELS, context.getString(R.string.food_common_old))
                .and().isNull(BaseServerEntity.Column.SERVER_ID)
                .query();
        } catch (SQLException exception) {
            Log.e(TAG, exception.toString());
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
            return getQueryBuilder()
                .where().eq(Food.Column.NAME, name)
                .queryForFirst();
        } catch (SQLException exception) {
            Log.e(TAG, exception.toString());
            return null;
        }
    }

    public List<Food> search(
        Context context,
        String query,
        long page,
        boolean showCustomFood,
        boolean showCommonFood,
        boolean showBrandedFood
    ) {
        if (!showCustomFood && !showCommonFood && !showBrandedFood) {
            return new ArrayList<>();
        }

        try {
            QueryBuilder<Food, Long> queryBuilder = getQueryBuilder()
                .orderByRaw(String.format("%s COLLATE NOCASE", Food.Column.NAME))
                .orderBy(Food.Column.UPDATED_AT, false)
                .offset(page * BaseDao.PAGE_SIZE)
                .limit(BaseDao.PAGE_SIZE);

            Where<Food, Long> where = queryBuilder.where();
            where.isNull(Food.Column.DELETED_AT);

            if (query != null && query.length() > 0) {
                where.and();
                where.like(Food.Column.NAME, new SelectArg("%" + query + "%"));
            }

            int whereTypeCount = 0;

            if (showCustomFood) {
                where.ne(Food.Column.LABELS, context.getString(R.string.food_common));
                where.ne(Food.Column.LABELS, context.getString(R.string.food_common_old));
                where.and(2);
                where.isNull(Food.Column.LABELS);
                where.or(2);
                where.isNull(Food.Column.SERVER_ID);
                where.and(2);
                whereTypeCount++;
            }

            if (showCommonFood) {
                where.eq(Food.Column.LABELS, context.getString(R.string.food_common));
                where.eq(Food.Column.LABELS, context.getString(R.string.food_common_old));
                where.or(2);
                whereTypeCount++;
            }

            if (showBrandedFood) {
                where.isNotNull(Food.Column.SERVER_ID);
                whereTypeCount++;
            }

            where.or(whereTypeCount);
            where.and(2);

            return where.query();

        } catch (SQLException exception) {
            Log.e(TAG, exception.toString());
            return new ArrayList<>();
        }
    }

    public List<Food> createOrUpdate(@Nullable SearchResponseDto dto) {
        if (dto == null || dto.products == null ||dto.products.isEmpty()) {
            return new ArrayList<>();
        }
        String languageCode = Helper.getLanguageCode();
        List<Food> foodList = new ArrayList<>();
        Collections.reverse(dto.products);
        for (ProductDto productDto : dto.products) {
            // Workaround: API returns products in other languages even though defined otherwise through GET parameters
            boolean isSameLanguage = languageCode.equals(productDto.languageCode);
            if (isSameLanguage && productDto.isValid()) {
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
