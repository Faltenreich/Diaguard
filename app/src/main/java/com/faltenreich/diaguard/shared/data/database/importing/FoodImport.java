package com.faltenreich.diaguard.shared.data.database.importing;

import android.content.Context;
import android.util.Log;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.data.database.dao.FoodDao;
import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.data.primitive.FloatUtils;
import com.opencsv.CSVReader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

class FoodImport implements Importing {

    private static final String TAG = FoodImport.class.getSimpleName();
    private static final String FOOD_CSV_FILE_NAME = "food_common.csv";

    private final Context context;
    private final Locale locale;

    FoodImport(Context context, Locale locale) {
        this.context = context;
        this.locale = locale;
    }

    @Override
    public boolean requiresImport() {
        return !PreferenceStore.getInstance().didImportCommonFood(locale);
    }

    @Override
    public void importData() {
        try {
            CSVReader reader = CsvImport.getCsvReader(context, FOOD_CSV_FILE_NAME);

            String languageCode = locale.getLanguage();
            String[] nextLine = reader.readNext();
            int languageRow = CsvImport.getLanguageColumn(languageCode, nextLine);

            List<Food> foodList = new ArrayList<>();
            while ((nextLine = reader.readNext()) != null) {

                if (nextLine.length >= 13) {
                    Food food = new Food();

                    food.setName(nextLine[languageRow]);
                    food.setIngredients(food.getName());
                    food.setLabels(context.getString(R.string.food_common));
                    food.setLanguageCode(languageCode);

                    // Main nutrients are given in grams, so we take them as they are
                    food.setCarbohydrates(FloatUtils.parseNullableNumber(nextLine[4]));
                    food.setEnergy(FloatUtils.parseNullableNumber(nextLine[5]));
                    food.setFat(FloatUtils.parseNullableNumber(nextLine[6]));
                    food.setFatSaturated(FloatUtils.parseNullableNumber(nextLine[7]));
                    food.setFiber(FloatUtils.parseNullableNumber(nextLine[8]));
                    food.setProteins(FloatUtils.parseNullableNumber(nextLine[9]));
                    food.setSalt(FloatUtils.parseNullableNumber(nextLine[10]));
                    food.setSugar(FloatUtils.parseNullableNumber(nextLine[12]));

                    // Mineral nutrients are given in milligrams, so we divide them by 1.000
                    Float sodium = FloatUtils.parseNullableNumber(nextLine[11]);
                    sodium = sodium != null ? sodium / 1000 : null;
                    food.setSodium(sodium);

                    foodList.add(food);
                }
            }

            Collections.reverse(foodList);
            FoodDao.getInstance().deleteAll();
            FoodDao.getInstance().bulkCreateOrUpdate(foodList);

            Log.i(TAG, String.format("Imported %d common food items from csv", foodList.size()));
            PreferenceStore.getInstance().setDidImportCommonFood(locale, true);

        } catch (Exception exception) {
            Log.e(TAG, exception.toString());
        }
    }
}
