package com.faltenreich.diaguard.shared.data.repository;

import com.faltenreich.diaguard.shared.data.database.Database;
import com.faltenreich.diaguard.shared.data.database.dao.FoodEatenDao;
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

    private final FoodEatenDao dao = Database.getInstance().getDatabase().foodEatenDao();

    public FoodEaten createOrUpdate(FoodEaten foodEaten) {
        long id = dao.createOrUpdate(foodEaten);
        foodEaten.setId(id);
        return foodEaten;
    }

    public List<FoodEaten> getByMeal(Meal meal) {
        return dao.getByMeal(meal.getId());
    }

    public List<FoodEaten> getByFood(Food food) {
        return dao.getByFood(food.getId());
    }

    public List<FoodEaten> getBetween(Interval interval) {
        return dao.getBetween(interval.getStart(), interval.getEnd());
    }

    public List<FoodEaten> getLatest(long count) {
        return dao.getLatest(count);
    }

    public long countByFood(Food food) {
        return dao.countByFood(food.getId());
    }

    public void delete(FoodEaten foodEaten) {
        dao.delete(foodEaten);
    }
}
