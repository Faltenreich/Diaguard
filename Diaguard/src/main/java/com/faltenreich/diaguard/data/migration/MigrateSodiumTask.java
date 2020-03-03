package com.faltenreich.diaguard.data.migration;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.faltenreich.diaguard.data.dao.FoodDao;
import com.faltenreich.diaguard.data.entity.Food;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Version 3.4.2:
 * Fixes amount of sodium in common food which was wrongly imported in grams instead of milligrams
 */
public class MigrateSodiumTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = MigrateSodiumTask.class.getSimpleName();

    private WeakReference<Context> context;

    MigrateSodiumTask(Context context) {
        this.context = new WeakReference<>(context);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        List<Food> foodList = FoodDao.getInstance().getAllCommon(context.get());
        for (Food food : foodList) {
            Float sodium = food.getSodium();
            food.setSodium(sodium != null ? sodium / 1000 : null);
        }
        FoodDao.getInstance().bulkCreateOrUpdate(foodList);
        Log.i(TAG, String.format("Fixed sodium of %d common food items", foodList.size()));
        return null;
    }
}