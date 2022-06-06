package com.faltenreich.diaguard.shared.data.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.faltenreich.diaguard.feature.datetime.DateTimeUtils;
import com.faltenreich.diaguard.feature.food.networking.OpenFoodFactsService;
import com.faltenreich.diaguard.feature.food.networking.dto.ProductDto;
import com.faltenreich.diaguard.feature.food.networking.dto.SearchResponseDto;
import com.faltenreich.diaguard.feature.food.search.FoodSearchListItem;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.Helper;
import com.faltenreich.diaguard.shared.data.async.DataCallback;
import com.faltenreich.diaguard.shared.data.async.DataLoader;
import com.faltenreich.diaguard.shared.data.async.DataLoaderListener;
import com.faltenreich.diaguard.shared.data.database.Database;
import com.faltenreich.diaguard.shared.data.database.dao.BaseDao;
import com.faltenreich.diaguard.shared.data.database.dao.FoodDao;
import com.faltenreich.diaguard.shared.data.database.dao.FoodEatenDao;
import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.data.database.entity.FoodEaten;
import com.faltenreich.diaguard.shared.data.primitive.StringUtils;
import com.faltenreich.diaguard.shared.networking.NetworkingUtils;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class FoodRepository {

    private static final long LATEST_FOOD_EATEN_COUNT = BaseDao.PAGE_SIZE;

    private static FoodRepository instance;

    public static FoodRepository getInstance() {
        if (instance == null) {
            instance = new FoodRepository();
        }
        return instance;
    }

    private final FoodDao dao = Database.getInstance().getDatabase().foodDao();

    private FoodRepository() {}

    public Food createOrUpdate(Food food) {
        long id = dao.createOrUpdate(food);
        food.setId(id);
        return food;
    }

    public void createOrUpdate(List<Food> foodList) {
        dao.createOrUpdate(foodList);
    }

    @Nullable
    public Food getById(long id) {
        return dao.getById(id);
    }

    @Nullable
    public Food getByName(String name) {
        return dao.getByName(name);
    }

    public List<Food> getAllCommon() {
        return dao.getAllCommon();
    }

    public List<Food> getAllFromUser() {
        return dao.getAllFromUser();
    }

    public void search(Context context, String query, int page, DataCallback<List<FoodSearchListItem>> callback) {
        if (page == 0
            && !StringUtils.isBlank(query)
            && PreferenceStore.getInstance().showBrandedFood()
            && NetworkingUtils.isOnline(context)
        ) {
            searchOnline(context, query, page, result -> searchOffline(context, query, page, callback));
        } else {
            searchOffline(context, query, page, callback);
        }
    }

    private void searchOnline(Context context, String query, int page, DataCallback<List<Food>> callback) {
        OpenFoodFactsService.getInstance().search(query, page, (dto) -> {
            if (dto == null || dto.products == null || dto.products.isEmpty()) {
                callback.onResult(new ArrayList<>());
                return;
            }
            DataLoader.getInstance().load(context, new DataLoaderListener<List<Food>>() {
                @Override
                public List<Food> onShouldLoad(Context context) {
                    return createOrUpdate(dto);
                }
                @Override
                public void onDidLoad(List<Food> data) {
                    callback.onResult(data);
                }
            });
        });
    }

    private void searchOffline(Context context, String query, int page, DataCallback<List<FoodSearchListItem>> callback) {
        DataLoader.getInstance().load(context, new DataLoaderListener<List<FoodSearchListItem>>() {
            @Override
            public List<FoodSearchListItem> onShouldLoad(Context context) {
                List<FoodSearchListItem> items = new ArrayList<>();

                boolean showCustomFood = PreferenceStore.getInstance().showCustomFood();
                boolean showCommonFood = PreferenceStore.getInstance().showCommonFood();
                boolean showBrandedFood = PreferenceStore.getInstance().showBrandedFood();

                boolean hasQuery = query != null && query.length() > 0;
                if (page == 0 && !hasQuery) {
                    List<FoodEaten> foodEatenList = FoodEatenDao.getInstance().getLatest(LATEST_FOOD_EATEN_COUNT);
                    for (FoodEaten foodEaten : foodEatenList) {
                        items.add(new FoodSearchListItem(foodEaten));
                    }
                }

                List<Food> foodList = dao.search(query, page, showCustomFood, showCommonFood, showBrandedFood);
                for (Food food : foodList) {
                    items.add(new FoodSearchListItem(food));
                }
                return items;
            }
            @Override
            public void onDidLoad(List<FoodSearchListItem> data) {
                callback.onResult(data);
            }
        });
    }

    public List<Food> createOrUpdate(@NonNull SearchResponseDto dto) {
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
        createOrUpdate(foodList);
        return foodList;
    }

    @Nullable
    private Food parseFromDto(ProductDto dto) {
        if (dto == null || dto.identifier == null || !dto.identifier.isJsonPrimitive()) {
            return null;
        }
        String serverId = dto.identifier.getAsJsonPrimitive().getAsString();
        if (StringUtils.isBlank(serverId)) {
            return null;
        }

        Food food = dao.getByServerId(serverId);
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

    public void softDelete(Food food) {
        food.setDeletedAt(DateTime.now());
        dao.createOrUpdate(food);
    }

    // TODO: Check if necessary
    public void deleteAll() {
        dao.deleteAll();
    }
}
