package com.faltenreich.diaguard.data;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.dao.FoodDao;
import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.util.NumberUtils;
import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by Faltenreich on 22.11.2016.
 */

public class ImportHelper {

    private static final String TAG = ImportHelper.class.getSimpleName();
    private static final String FOOD_CSV_FILE_NAME = "food_common.csv";
    private static final char FOOD_CSV_FILE_SEPARATOR = ';';

    public static void importCommonFood(Context context, Locale locale) {
        new ImportFoodTask(context, locale).execute();
    }

    private static class ImportFoodTask extends AsyncTask<Void, Void, Void> {

        private Context context;
        private Locale locale;

        // TODO: Localization
        // TODO: Bulk insert
        // TODO: UTF-8
        ImportFoodTask(Context context, Locale locale) {
            this.context = context;
            this.locale = locale;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                AssetManager assetManager = context.getAssets();
                InputStream inputStream = assetManager.open(FOOD_CSV_FILE_NAME);
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                CSVReader reader = new CSVReader(bufferedReader, FOOD_CSV_FILE_SEPARATOR);

                // Detect language
                String[] nextLine = reader.readNext();
                String systemLanguageCode = locale.getLanguage();
                int languageRow = 0;
                for (int languagePosition = 1; languagePosition < 4; languagePosition++) {
                    String availableLanguageCode = nextLine[languagePosition];
                    if (systemLanguageCode.startsWith(availableLanguageCode.substring(0, 1))) {
                        languageRow = languagePosition;
                        break;
                    }
                }

                // Store common food in database
                List<Food> foodList = new ArrayList<>();
                while ((nextLine = reader.readNext()) != null) {

                    if (nextLine.length >= 13) {
                        Food food = new Food();
                        food.setName(nextLine[languageRow]);
                        food.setIngredients(food.getName());
                        food.setLabels(context.getString(R.string.food_common));
                        food.setLanguageCode(systemLanguageCode);

                        food.setCarbohydrates(NumberUtils.parseNullableNumber(nextLine[4]));
                        food.setEnergy(NumberUtils.parseNullableNumber(nextLine[5]));
                        food.setFat(NumberUtils.parseNullableNumber(nextLine[6]));
                        food.setFatSaturated(NumberUtils.parseNullableNumber(nextLine[7]));
                        food.setFiber(NumberUtils.parseNullableNumber(nextLine[8]));
                        food.setProteins(NumberUtils.parseNullableNumber(nextLine[9]));
                        food.setSalt(NumberUtils.parseNullableNumber(nextLine[10]));
                        food.setSodium(NumberUtils.parseNullableNumber(nextLine[11]));
                        food.setSugar(NumberUtils.parseNullableNumber(nextLine[12]));
                        foodList.add(food);

                        Log.d(TAG, "Importing " + food.getName());
                    }
                }

                Collections.reverse(foodList);
                FoodDao.getInstance().bulkCreateOrUpdate(foodList);

                Log.i(TAG, String.format("Imported: %d common food items from csv table into database", foodList.size()));

            } catch (IOException exception) {
                Log.e(TAG, exception.getLocalizedMessage());
            }
            return null;
        }
    }
}
