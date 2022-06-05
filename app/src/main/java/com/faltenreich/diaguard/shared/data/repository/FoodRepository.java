package com.faltenreich.diaguard.shared.data.repository;

import android.content.Context;

import androidx.annotation.Nullable;

import com.faltenreich.diaguard.feature.food.networking.OpenFoodFactsService;
import com.faltenreich.diaguard.feature.food.search.FoodSearchListItem;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.data.async.DataCallback;
import com.faltenreich.diaguard.shared.data.async.DataLoader;
import com.faltenreich.diaguard.shared.data.async.DataLoaderListener;
import com.faltenreich.diaguard.shared.data.database.dao.BaseDao;
import com.faltenreich.diaguard.shared.data.database.dao.FoodOrmLiteDao;
import com.faltenreich.diaguard.shared.data.database.dao.FoodEatenDao;
import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.data.database.entity.FoodEaten;
import com.faltenreich.diaguard.shared.data.primitive.StringUtils;
import com.faltenreich.diaguard.shared.networking.NetworkingUtils;

import java.util.ArrayList;
import java.util.List;

public class FoodRepository {

    private static final long LATEST_FOOD_EATEN_COUNT = BaseDao.PAGE_SIZE;

    private static FoodRepository instance;

    public static FoodRepository getInstance() {
        if (instance == null) {
            instance = new FoodRepository();
        }
        return instance;
    }

    private final FoodOrmLiteDao dao = FoodOrmLiteDao.getInstance();

    private FoodRepository() {}

    public Food createOrUpdate(Food food) {
        return dao.createOrUpdate(food);
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
                    return FoodOrmLiteDao.getInstance().createOrUpdate(dto);
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

                List<Food> foodList = FoodOrmLiteDao.getInstance().search(query, page, showCustomFood, showCommonFood, showBrandedFood);
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

    public void softDelete(Food food) {
        dao.softDelete(food);
    }

    // TODO: Check if necessary
    public void deleteAll() {
        dao.deleteAll();
    }
}
