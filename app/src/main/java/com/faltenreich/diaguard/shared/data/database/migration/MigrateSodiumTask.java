package com.faltenreich.diaguard.shared.data.database.migration;

import android.os.AsyncTask;
import android.util.Log;

import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.data.repository.FoodRepository;

import java.util.List;

/**
 * Version 3.4.2:
 * Fixes amount of sodium in common food which was wrongly imported in grams instead of milligrams
 */
@Deprecated
public class MigrateSodiumTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = MigrateSodiumTask.class.getSimpleName();

    MigrateSodiumTask() {

    }

    @Override
    protected Void doInBackground(Void... voids) {
        List<Food> foodList = FoodRepository.getInstance().getAllCommon();
        for (Food food : foodList) {
            Float sodium = food.getSodium();
            food.setSodium(sodium != null && sodium > 0 ? sodium / 1000 : null);
        }
        FoodRepository.getInstance().createOrUpdate(foodList);
        Log.i(TAG, String.format("Fixed sodium of %d common food items", foodList.size()));
        return null;
    }
}