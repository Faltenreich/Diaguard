package com.faltenreich.diaguard.data;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.ui.view.preferences.CategoryPreference;
import com.faltenreich.diaguard.util.Helper;
import com.faltenreich.diaguard.util.NumberUtils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Filip on 04.11.13.
 */

/**
 * Parameters are of Default Value
 * Return Values are of Custom Value
 */
public class PreferenceHelper {

    private class Keys {
        static final String CATEGORY_PINNED = "categoryPinned%s";
        static final String ALARM_START_IN_MILLIS = "alarmStartInMillis";
        static final String INTERVAL_FACTOR = "intervalFactor";
        static final String INTERVAL_FACTOR_FOR_HOUR = "intervalFactor%d";
        final static String FACTOR_DEPRECATED = "factor_";
        final static String MEAL_IS_CALCULATED = "mealIsCalculated";
    }

    private static PreferenceHelper instance;
    private static SharedPreferences sharedPreferences;

    public static PreferenceHelper getInstance() {
        if(PreferenceHelper.instance == null) {
            PreferenceHelper.instance = new PreferenceHelper();
            PreferenceHelper.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(DiaguardApplication.getContext());
        }
        return PreferenceHelper.instance;
    }

    // GENERAL

    public long getAlarmStartInMillis() {
        return sharedPreferences.getLong(Keys.ALARM_START_IN_MILLIS, -1);
    }

    public void setAlarmStartInMillis(long alarmStartInMillis) {
        sharedPreferences.edit().putLong(Keys.ALARM_START_IN_MILLIS, alarmStartInMillis).apply();
    }

    public int getStartScreen() {
        String startScreen = sharedPreferences.getString("startscreen", "0");
        return Integer.parseInt(startScreen);
    }

    public boolean isSoundAllowed() {
        return sharedPreferences.getBoolean("sound", true);
    }

    public boolean isVibrationAllowed() {
        return sharedPreferences.getBoolean("vibration", true);
    }

    public int[] getExtrema(Measurement.Category category) {
        int resourceIdExtrema = DiaguardApplication.getContext().getResources().getIdentifier(category.name().toLowerCase() +
                "_extrema", "array", DiaguardApplication.getContext().getPackageName());

        if(resourceIdExtrema == 0) {
            throw new Resources.NotFoundException("Resource \"category_extrema\" not found: IntArray with event value extrema");
        }

        return DiaguardApplication.getContext().getResources().getIntArray(resourceIdExtrema);
    }

    public boolean validateEventValue(Measurement.Category category, float value) {
        int[] extrema = getExtrema(category);

        if(extrema.length != 2)
            throw new IllegalStateException("IntArray with event value extrema has to contain two values");

        return value > extrema[0] && value < extrema[1];
    }

    public void setExportNotes(boolean exportNotes) {
        sharedPreferences.edit().putBoolean("export_notes", exportNotes).apply();
    }

    public boolean exportNotes() {
        return sharedPreferences.getBoolean("export_notes", true);
    }

    // BLOOD SUGAR

    public float getTargetValue() {
        return NumberUtils.parseNumber(sharedPreferences.getString("target",
                DiaguardApplication.getContext().getString(R.string.pref_therapy_targets_target_default)));
    }

    public boolean limitsAreHighlighted() {
        return sharedPreferences.getBoolean("targets_highlight", true);
    }

    public float getLimitHyperglycemia() {
        return NumberUtils.parseNumber(sharedPreferences.getString("hyperclycemia",
                DiaguardApplication.getContext().getString(R.string.pref_therapy_targets_hyperclycemia_default)));
    }

    public float getLimitHypoglycemia() {
        return NumberUtils.parseNumber(sharedPreferences.getString("hypoclycemia",
                DiaguardApplication.getContext().getString(R.string.pref_therapy_targets_hypoclycemia_default)));
    }

    public float getCorrectionValue() {
        return NumberUtils.parseNumber(sharedPreferences.getString("correction_value", "40"));
    }

    // CATEGORIES

    public String getCategoryName(Measurement.Category category) {
        int position = Measurement.Category.valueOf(category.name()).ordinal();
        // TODO: Get resourceId by key
        String[] categories = DiaguardApplication.getContext().getResources().getStringArray(R.array.categories);
        return categories[position];
    }

    public int getCategoryImageResourceId(Measurement.Category category) {
        return DiaguardApplication.getContext().getResources().getIdentifier("ic_category_" + category.name().toLowerCase(),
                "drawable", DiaguardApplication.getContext().getPackageName());
    }

    public int getShowcaseImageResourceId(Measurement.Category category) {
        return DiaguardApplication.getContext().getResources().getIdentifier("ic_showcase_" + category.name().toLowerCase(),
                "drawable", DiaguardApplication.getContext().getPackageName());
    }

    public int getSeasonResourceId(DateTime daytime) {
        int monthOfYear = daytime.monthOfYear().get();
        String month;
        if (monthOfYear == 12 || monthOfYear <= 2) {
            month = "winter";
        } else if (monthOfYear > 2 && monthOfYear <= 5) {
            month = "spring";
        } else if (monthOfYear > 5 && monthOfYear <= 8) {
            month = "summer";
        } else {
            month = "fall";
        }
        return DiaguardApplication.getContext().getResources().getIdentifier("bg_" + month.toLowerCase(),
                "drawable", DiaguardApplication.getContext().getPackageName());
    }

    public boolean isCategoryActive(Measurement.Category category) {
        return sharedPreferences.getBoolean(category.name() + CategoryPreference.ACTIVE, true);
    }

    public Measurement.Category[] getActiveCategories() {
        List<Measurement.Category> activeCategories = new ArrayList<Measurement.Category>();
        for(int item = 0; item < Measurement.Category.values().length; item++) {
            if (isCategoryActive(Measurement.Category.values()[item])) {
                activeCategories.add(Measurement.Category.values()[item]);
            }
        }
        return activeCategories.toArray(new Measurement.Category[activeCategories.size()]);
    }

    private String getCategoryPinnedName(Measurement.Category category) {
        return String.format(Keys.CATEGORY_PINNED, category.name());
    }

    public boolean isCategoryPinned(Measurement.Category category) {
        return sharedPreferences.getBoolean(getCategoryPinnedName(category), false);
    }

    public void setCategoryPinned(Measurement.Category category, boolean isPinned) {
        sharedPreferences.edit().putBoolean(getCategoryPinnedName(category), isPinned).apply();
    }

    public boolean isMealCalculated() {
        return sharedPreferences.getBoolean(Keys.MEAL_IS_CALCULATED, false);
    }

    public void setIsMealCalculated(boolean isMealCalculated) {
        sharedPreferences.edit().putBoolean(Keys.MEAL_IS_CALCULATED, isMealCalculated).apply();
    }

    // UNITS

    public String[] getUnitsNames(Measurement.Category category) {
        String categoryName = category.name().toLowerCase();
        int resourceIdUnits = DiaguardApplication.getContext().getResources().getIdentifier(categoryName +
                "_units", "array", DiaguardApplication.getContext().getPackageName());
        return DiaguardApplication.getContext().getResources().getStringArray(resourceIdUnits);
    }

    public String getUnitName(Measurement.Category category) {
        String sharedPref = sharedPreferences.
                getString("unit_" + category.name().toLowerCase(), "1");
        return getUnitsNames(category)[Arrays.asList(getUnitsValues(category)).indexOf(sharedPref)];
    }

    public String[] getUnitsAcronyms(Measurement.Category category) {
        String categoryName = category.name().toLowerCase();
        int resourceIdUnits = DiaguardApplication.getContext().getResources().getIdentifier(categoryName +
                "_units_acronyms", "array", DiaguardApplication.getContext().getPackageName());
        if(resourceIdUnits == 0)
            return null;
        else
            return DiaguardApplication.getContext().getResources().getStringArray(resourceIdUnits);
    }

    public String getUnitAcronym(Measurement.Category category) {
        String[] acronyms = getUnitsAcronyms(category);
        if(acronyms == null)
            return getUnitName(category);
        else {
            String sharedPref = sharedPreferences.
                    getString("unit_" + category.name().toLowerCase(), "1");
            int indexOfAcronym = Arrays.asList(getUnitsValues(category)).indexOf(sharedPref);
            if(indexOfAcronym < acronyms.length) {
                return acronyms[indexOfAcronym];
            }
            else {
                return getUnitName(category);
            }
        }
    }

    public String[] getUnitsValues(Measurement.Category category) {
        String categoryName = category.name().toLowerCase();
        int resourceIdUnits = DiaguardApplication.getContext().getResources().getIdentifier(categoryName +
                "_units_values", "array", DiaguardApplication.getContext().getPackageName());
        return DiaguardApplication.getContext().getResources().getStringArray(resourceIdUnits);
    }

    public float getUnitValue(Measurement.Category category) {
        String sharedPref = sharedPreferences.
                getString("unit_" + category.name().toLowerCase(), "1");
        String value = getUnitsValues(category)[Arrays.asList(getUnitsValues(category)).indexOf(sharedPref)];
        return NumberUtils.parseNumber(value);
    }

    public float formatCustomToDefaultUnit(Measurement.Category category, float value) {
        return value / getUnitValue(category);
    }

    public float formatDefaultToCustomUnit(Measurement.Category category, float value) {
        return value * getUnitValue(category);
    }

    public String getMeasurementForUi(Measurement.Category category, float defaultValue) {
        return Helper.parseFloat(formatDefaultToCustomUnit(category, defaultValue));
    }

    // FACTORS

    public TimeInterval getFactorInterval() {
        int position = sharedPreferences.getInt(Keys.INTERVAL_FACTOR, TimeInterval.EVERY_SIX_HOURS.ordinal());
        TimeInterval[] timeIntervals = TimeInterval.values();
        return position >= 0 && position < timeIntervals.length ? timeIntervals[position] : TimeInterval.EVERY_FOUR_HOURS;
    }

    public void setFactorInterval(TimeInterval interval) {
        sharedPreferences.edit().putInt(Keys.INTERVAL_FACTOR, interval.ordinal()).apply();
    }

    public float getFactorForHour(int hourOfDay) {
        String key = String.format(Keys.INTERVAL_FACTOR_FOR_HOUR, hourOfDay);
        return sharedPreferences.getFloat(key, -1);
    }

    public void setFactorForHour(int hourOfDay, float factor) {
        String key = String.format(Keys.INTERVAL_FACTOR_FOR_HOUR, hourOfDay);
        sharedPreferences.edit().putFloat(key, factor).apply();
    }

    /**
     * Used to migrate from static to dynamic factors
     */
    public void migrateFactors() {
        for (Daytime daytime : Daytime.values()) {
            float factor = sharedPreferences.getFloat(Keys.FACTOR_DEPRECATED + daytime.toDeprecatedString(), -1);
            if (factor >= 0) {
                int step = 0;
                while (step < Daytime.INTERVAL_LENGTH) {
                    int hourOfDay = (daytime.startingHour + step) % DateTimeConstants.HOURS_PER_DAY;
                    setFactorForHour(hourOfDay, factor);
                    step++;
                }
                sharedPreferences.edit().putFloat(Keys.FACTOR_DEPRECATED + daytime, -1).apply();
            }
        }
    }
}
