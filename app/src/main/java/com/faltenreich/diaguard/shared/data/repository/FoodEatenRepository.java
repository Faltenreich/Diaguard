package com.faltenreich.diaguard.shared.data.repository;

import com.faltenreich.diaguard.shared.data.database.dao.FoodEatenOrmLiteDao;
import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.data.database.entity.FoodEaten;
import com.faltenreich.diaguard.shared.data.database.entity.Meal;

import org.joda.time.Interval;

import java.util.List;

public class FoodEatenRepository {

    private static FoodEatenRepository instance;

    public static FoodEatenRepository getInstance() {
        if (instance == null) {
            instance = new FoodEatenRepository();
        }
        return instance;
    }

    private final FoodEatenOrmLiteDao dao = FoodEatenOrmLiteDao.getInstance();

    public FoodEaten createOrUpdate(FoodEaten foodEaten) {
        return dao.createOrUpdate(foodEaten);
    }

    public List<FoodEaten> getByMeal(Meal meal) {
        return dao.getAll(meal);
    }

    public List<FoodEaten> getByFood(Food food) {
        return dao.getAll(food);
    }

    public List<FoodEaten> getBetween(Interval interval) {
        return dao.getAll(interval);
    }

    public List<FoodEaten> getLatest(long count) {
        return dao.getLatest(count);
    }

    public long countByFood(Food food) {
        return dao.count(food);
    }

    // TODO: Replace with CASCADE DELETE via ForeignKey
    @Deprecated
    public void delete(FoodEaten foodEaten) {
        dao.delete(foodEaten);
    }
}
