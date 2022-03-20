package com.faltenreich.diaguard.feature.preference.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.annotation.XmlRes;
import androidx.preference.PreferenceManager;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.category.CategoryComparatorFactory;
import com.faltenreich.diaguard.feature.export.job.pdf.meta.PdfExportStyle;
import com.faltenreich.diaguard.feature.navigation.MainFragmentType;
import com.faltenreich.diaguard.feature.timeline.TimelineStyle;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.primitive.FloatUtils;
import com.faltenreich.diaguard.shared.data.serialization.CategorySerializer;
import com.faltenreich.diaguard.shared.view.theme.Theme;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class PreferenceStore {

    private static final String TAG = PreferenceStore.class.getSimpleName();
    private static final String INPUT_QUERIES_SEPARATOR = ";";
    private static final int INPUT_QUERIES_MAXIMUM_COUNT = 10;

    private static PreferenceStore instance;

    public static PreferenceStore getInstance() {
        if (instance == null) {
            instance = new PreferenceStore();
        }
        return instance;
    }

    private SharedPreferences sharedPreferences;
    private Context context;

    private PreferenceStore() {

    }

    public void init(Context context) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.context = context;
    }

    public void setDefaultValues(Context context) {
        setDefaultValues(context, R.xml.preferences_overview);
        setDefaultValues(context, R.xml.preferences_food);
        setDefaultValues(context, R.xml.preferences_limit);
        setDefaultValues(context, R.xml.preferences_unit);
    }

    private void setDefaultValues(Context context, @XmlRes int preferenceResource) {
        PreferenceManager.setDefaultValues(context, preferenceResource, false);
    }

    private Context getContext() {
        return context;
    }

    private String getKey(@StringRes int stringResource, Object... arguments) {
        return context.getString(stringResource, arguments);
    }

    // GENERAL

    public void migrate() {
        migrateMealFactors();
        migrateCorrectionFactors();
        migrateStartScreen();
    }

    public int getVersionCode() {
        return sharedPreferences.getInt(getKey(R.string.preference_version_code), 0);
    }

    public void setVersionCode(int versionCode) {
        sharedPreferences.edit().putInt(getKey(R.string.preference_version_code), versionCode).apply();
    }

    public boolean didImportCommonFood(Locale locale) {
        return sharedPreferences.getBoolean(getKey(R.string.preference_food_imported) + locale.getLanguage(), false);
    }

    public void setDidImportCommonFood(Locale locale, boolean didImport) {
        sharedPreferences.edit().putBoolean(getKey(R.string.preference_food_imported) + locale.getLanguage(), didImport).apply();
    }

    public boolean didImportTags(Locale locale) {
        return sharedPreferences.getBoolean(getKey(R.string.preference_tags_imported) + locale.getLanguage(), false);
    }

    public void setDidImportTags(Locale locale, boolean didImport) {
        sharedPreferences.edit().putBoolean(getKey(R.string.preference_tags_imported) + locale.getLanguage(), didImport).apply();
    }

    public long getAlarmStartInMillis() {
        return sharedPreferences.getLong(getKey(R.string.preference_alarm_start), -1);
    }

    public void setAlarmStartInMillis(long alarmStartInMillis) {
        sharedPreferences.edit().putLong(getKey(R.string.preference_alarm_start), alarmStartInMillis).apply();
    }

    public Theme getTheme() {
        Theme themeDefault = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P ? Theme.SYSTEM : Theme.LIGHT;
        String themeKey = sharedPreferences.getString(getKey(R.string.preference_theme), null);
        if (themeKey != null) {
            Theme theme = Theme.fromKey(themeKey);
            return theme != null ? theme : themeDefault;
        } else {
            setTheme(themeDefault);
            return themeDefault;
        }
    }

    public void setTheme(Theme theme) {
        sharedPreferences.edit().putString(getKey(R.string.preference_theme), theme.getKey()).apply();
    }

    public int getStartScreen() {
        String startScreen = sharedPreferences.getString(getKey(R.string.preference_start_screen), "0");
        return Integer.parseInt(startScreen);
    }

    @SuppressWarnings("SameParameterValue")
    private void setStartScreen(int startScreen) {
        sharedPreferences.edit().putString(getKey(R.string.preference_start_screen), Integer.toString(startScreen)).apply();
    }

    private void migrateStartScreen() {
        int startScreen = getStartScreen();
        if (MainFragmentType.valueOf(startScreen) == null) {
            setStartScreen(0);
        }
    }

    public boolean isSoundAllowed() {
        return sharedPreferences.getBoolean(getKey(R.string.preference_alarm_sound), true);
    }

    public boolean isVibrationAllowed() {
        return sharedPreferences.getBoolean(getKey(R.string.preference_alarm_vibration), true);
    }

    public TimelineStyle getTimelineStyle() {
        String key = getKey(R.string.preference_chart_style);
        String preference = sharedPreferences.getString(key, null);
        if (!TextUtils.isEmpty(preference)) {
            try {
                int chartStyle = Integer.parseInt(preference);
                TimelineStyle[] chartStyles = TimelineStyle.values();
                return chartStyle >= 0 && chartStyle < chartStyles.length ? chartStyles[chartStyle] : TimelineStyle.SCATTER_CHART;
            } catch (NumberFormatException exception) {
                Log.e(TAG, exception.getMessage() != null ? exception.getMessage() : "Failed to getChartStyle");
            }
        } else {
            Log.e(TAG, "Failed to find shared preference for key: " + key);
        }
        return TimelineStyle.SCATTER_CHART;
    }

    public void setTimelineStyle(TimelineStyle style) {
        int stableId = style.getStableId();
        String stableIdString = Integer.toString(stableId);
        sharedPreferences.edit().putString(getKey(R.string.preference_chart_style), stableIdString).apply();
    }

    public int[] getExtrema(Category category) {
        int resourceIdExtrema = getContext().getResources().getIdentifier(category.name().toLowerCase() + "_extrema", "array", getContext().getPackageName());
        if (resourceIdExtrema == 0) {
            throw new Resources.NotFoundException("Resource \"category_extrema\" not found: IntArray with event value extrema");
        }
        return getContext().getResources().getIntArray(resourceIdExtrema);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean validateEventValue(Category category, float value) {
        int[] extrema = getExtrema(category);

        if (extrema.length != 2)
            throw new IllegalStateException("IntArray with event value extrema has to contain two values");

        return value > extrema[0] && value < extrema[1];
    }

    public boolean isValueValid(TextView textView, Category category) {
        return isValueValid(textView, category, false);
    }

    public boolean isValueValid(TextView textView, Category category, boolean allowNegativeValues) {
        boolean isValid = true;
        textView.setError(null);
        try {
            float value = PreferenceStore.getInstance().formatCustomToDefaultUnit(category, FloatUtils.parseNumber(textView.getText().toString()));
            if (allowNegativeValues) {
                value = Math.abs(value);
            }
            if (!PreferenceStore.getInstance().validateEventValue(category, value)) {
                textView.setError(textView.getContext().getString(R.string.validator_value_unrealistic));
                isValid = false;
            }
        } catch (NumberFormatException exception) {
            textView.setError(textView.getContext().getString(R.string.validator_value_number));
            isValid = false;
        }
        return isValid;
    }

    public void setPdfExportStyle(PdfExportStyle style) {
        sharedPreferences.edit().putInt(getKey(R.string.preference_export_pdf_style), style.stableId).apply();
    }

    public PdfExportStyle getPdfExportStyle() {
        PdfExportStyle defaultStyle = PdfExportStyle.TABLE;
        int stableId = sharedPreferences.getInt(getKey(R.string.preference_export_pdf_style), defaultStyle.stableId);
        PdfExportStyle style = PdfExportStyle.valueOf(stableId);
        return style != null ? style : defaultStyle;
    }

    public void setExportHeader(boolean exportHeader) {
        sharedPreferences.edit().putBoolean(getKey(R.string.preference_export_header), exportHeader).apply();
    }

    public boolean exportHeader() {
        return sharedPreferences.getBoolean(getKey(R.string.preference_export_header), true);
    }

    public void setExportFooter(boolean exportFooter) {
        sharedPreferences.edit().putBoolean(getKey(R.string.preference_export_footer), exportFooter).apply();
    }

    public boolean exportFooter() {
        return sharedPreferences.getBoolean(getKey(R.string.preference_export_footer), true);
    }

    public void setExportNotes(boolean exportNotes) {
        sharedPreferences.edit().putBoolean(getKey(R.string.preference_export_notes), exportNotes).apply();
    }

    public boolean exportNotes() {
        return sharedPreferences.getBoolean(getKey(R.string.preference_export_notes), true);
    }

    public void setExportTags(boolean exportTags) {
        sharedPreferences.edit().putBoolean(getKey(R.string.preference_export_tags), exportTags).apply();
    }

    public boolean exportTags() {
        return sharedPreferences.getBoolean(getKey(R.string.preference_export_tags), true);
    }

    public void setExportFood(boolean exportFood) {
        sharedPreferences.edit().putBoolean(getKey(R.string.preference_export_food), exportFood).apply();
    }

    public boolean exportFood() {
        return sharedPreferences.getBoolean(getKey(R.string.preference_export_food), true);
    }

    public void setSkipEmptyDays(boolean skipEmptyDays) {
        sharedPreferences.edit().putBoolean(getKey(R.string.preference_export_skip_empty_days), skipEmptyDays).apply();
    }

    public boolean skipEmptyDays() {
        return sharedPreferences.getBoolean(getKey(R.string.preference_export_skip_empty_days), false);
    }

    public void setExportInsulinSplit(boolean splitInsulin) {
        sharedPreferences.edit().putBoolean(getKey(R.string.preference_export_insulin_split), splitInsulin).apply();
    }

    public boolean exportInsulinSplit() {
        return sharedPreferences.getBoolean(getKey(R.string.preference_export_insulin_split), false);
    }

    public void setExportCategories(Category[] categories) {
        String preference = new CategorySerializer().serialize(categories);
        sharedPreferences.edit().putString(getKey(R.string.preference_export_categories), preference).apply();
    }

    public Category[] getExportCategories() {
        CategorySerializer serializer = new CategorySerializer();
        String preference = sharedPreferences.getString(getKey(R.string.preference_export_categories), serializer.serialize(Category.values()));
        return serializer.deserialize(preference);
    }

    public void addInputQuery(String query) {
        String inputQueries = getInputQueriesString();
        if (inputQueries.length() > 0) {
            inputQueries = inputQueries + INPUT_QUERIES_SEPARATOR;
        }
        inputQueries = inputQueries + query;
        // Prevent history from gaining weight
        String[] inputQueriesArray = inputQueries.split(INPUT_QUERIES_SEPARATOR);
        if (inputQueriesArray.length > INPUT_QUERIES_MAXIMUM_COUNT) {
            int endIndex = inputQueriesArray.length;
            int startIndex = endIndex - INPUT_QUERIES_MAXIMUM_COUNT;
            String[] newInputQueries = Arrays.copyOfRange(inputQueriesArray, startIndex, endIndex);
            inputQueries = TextUtils.join(INPUT_QUERIES_SEPARATOR, newInputQueries);
        }
        sharedPreferences.edit().putString(getKey(R.string.preference_input_queries), inputQueries).apply();
    }

    private String getInputQueriesString() {
        return sharedPreferences.getString(getKey(R.string.preference_input_queries), "");
    }

    public ArrayList<String> getInputQueries() {
        ArrayList<String> inputQueries = new ArrayList<>();
        for (String inputQuery : getInputQueriesString().split(INPUT_QUERIES_SEPARATOR)) {
            if (!TextUtils.isEmpty(inputQuery)) {
                inputQueries.removeAll(Collections.singleton(inputQuery));
                inputQueries.add(0, inputQuery);
            }
        }
        return inputQueries;
    }

    // BLOOD SUGAR

    public float getTargetValue() {
        return FloatUtils.parseNumber(sharedPreferences.getString(getKey(R.string.preference_extrema_target),
            getContext().getString(R.string.pref_therapy_targets_target_default)));
    }

    public boolean limitsAreHighlighted() {
        return sharedPreferences.getBoolean(getKey(R.string.preference_extrema_highlight), true);
    }

    public void setLimitsAreHighlighted(boolean isHighlighted) {
        sharedPreferences.edit().putBoolean(getKey(R.string.preference_extrema_highlight), isHighlighted).apply();
    }

    public float getLimitHyperglycemia() {
        return FloatUtils.parseNumber(sharedPreferences.getString(getKey(R.string.preference_extrema_hyperclycemia),
            getContext().getString(R.string.pref_therapy_targets_hyperclycemia_default)));
    }

    public float getLimitHypoglycemia() {
        return FloatUtils.parseNumber(sharedPreferences.getString(getKey(R.string.preference_extrema_hypoclycemia),
            getContext().getString(R.string.pref_therapy_targets_hypoclycemia_default)));
    }

    public int getMonthResourceId(DateTime daytime) {
        int monthOfYear = daytime.monthOfYear().get();
        String identifier = String.format(Locale.getDefault(), "bg_month_%d", monthOfYear - 1);
        return getContext().getResources().getIdentifier(identifier,
            "drawable", getContext().getPackageName());
    }

    public int getMonthSmallResourceId(DateTime daytime) {
        int monthOfYear = daytime.monthOfYear().get();
        String identifier = String.format(Locale.getDefault(), "bg_month_%d_small", monthOfYear - 1);
        return getContext().getResources().getIdentifier(identifier,
            "drawable", getContext().getPackageName());
    }

    public boolean isCategoryActive(Category category) {
        return sharedPreferences.getBoolean(getKey(R.string.preference_categories_active, category.name()), true);
    }

    public void setCategoryActive(Category category, boolean isActive) {
        sharedPreferences.edit().putBoolean(getKey(R.string.preference_categories_active, category.name()), isActive).apply();
    }

    public int getCategorySortIndex(Category category) {
        return sharedPreferences.getInt(getKey(R.string.preference_categories_sort_index, category.name()), category.ordinal());
    }

    public void setCategorySortIndex(Category category, int sortIndex) {
        sharedPreferences.edit().putInt(getKey(R.string.preference_categories_sort_index, category.name()), sortIndex).apply();
    }

    public List<Category> getSortedCategories(Comparator<Category> comparator) {
        List<Category> activeCategories = new ArrayList<>(Arrays.asList(Category.values()));
        Collections.sort(activeCategories, comparator);
        return activeCategories;
    }

    public List<Category> getSortedCategories() {
        return getSortedCategories(CategoryComparatorFactory.getInstance().createComparatorFromCategories());
    }

    public Category[] getActiveCategories(@Nullable Category excluded) {
        List<Category> sortedCategories = getSortedCategories();
        List<Category> activeCategories = new ArrayList<>();
        for (Category category : sortedCategories) {
            if (category != excluded && isCategoryActive(category)) {
                activeCategories.add(category);
            }
        }
        return activeCategories.toArray(new Category[0]);
    }

    public Category[] getActiveCategories() {
        return getActiveCategories(null);
    }

    public Category[] getPinnedCategories() {
        List<Category> pinnedCategories = new ArrayList<>();
        for (Category category : getActiveCategories()) {
            if (isCategoryPinned(category)) {
                pinnedCategories.add(category);
            }
        }
        return pinnedCategories.toArray(new Category[0]);
    }

    private String getCategoryPinnedName(Category category) {
        return getKey(R.string.preference_categories_pinned, category.name());
    }

    public boolean isCategoryPinned(Category category) {
        return sharedPreferences.getBoolean(getCategoryPinnedName(category), false);
    }

    public void setCategoryPinned(Category category, boolean isPinned) {
        sharedPreferences.edit().putBoolean(getCategoryPinnedName(category), isPinned).apply();
    }

    // UNITS

    private String[] getUnitsNames(Category category) {
        String categoryName = category.name().toLowerCase();
        int resourceIdUnits = getContext().getResources().getIdentifier(categoryName +
            "_units", "array", getContext().getPackageName());
        return getContext().getResources().getStringArray(resourceIdUnits);
    }

    public String getUnitName(Category category) {
        String sharedPref = sharedPreferences.
            getString("unit_" + category.name().toLowerCase(), "1");
        return getUnitsNames(category)[Arrays.asList(getUnitsValues(category)).indexOf(sharedPref)];
    }

    private String[] getUnitsAcronyms(Category category) {
        String categoryName = category.name().toLowerCase();
        int resourceIdUnits = getContext().getResources().getIdentifier(categoryName +
            "_units_acronyms", "array", getContext().getPackageName());
        if (resourceIdUnits == 0)
            return null;
        else
            return getContext().getResources().getStringArray(resourceIdUnits);
    }

    public String getUnitAcronym(Category category) {
        String[] acronyms = getUnitsAcronyms(category);
        if (acronyms == null)
            return getUnitName(category);
        else {
            String sharedPref = sharedPreferences.
                getString("unit_" + category.name().toLowerCase(), "1");
            int indexOfAcronym = Arrays.asList(getUnitsValues(category)).indexOf(sharedPref);
            if (indexOfAcronym < acronyms.length) {
                return acronyms[indexOfAcronym];
            } else {
                return getUnitName(category);
            }
        }
    }

    public String getLabelForMealPer100g(Context context) {
        return String.format("%s %s 100 g", getUnitAcronym(Category.MEAL), context.getString(R.string.per));
    }

    private String[] getUnitsValues(String unitName) {
        int resourceIdUnits = getContext().getResources().getIdentifier(unitName +
            "_units_values", "array", getContext().getPackageName());
        return getContext().getResources().getStringArray(resourceIdUnits);
    }

    private String[] getUnitsValues(Category category) {
        String categoryName = category.name().toLowerCase();
        return getUnitsValues(categoryName);
    }

    private float getUnitValue(Category category) {
        String sharedPref = sharedPreferences.getString("unit_" + category.name().toLowerCase(), "1");
        String value = getUnitsValues(category)[Arrays.asList(getUnitsValues(category)).indexOf(sharedPref)];
        return FloatUtils.parseNumber(value);
    }

    public float formatCustomToDefaultUnit(Category category, float value) {
        // Workaround for calculating HbA1c with formula
        if (category == Category.HBA1C) {
            float unitValue = getUnitValue(category);
            return unitValue != 1 ? (value * 0.0915f) + 2.15f : value;
        }
        return value / getUnitValue(category);
    }

    public float formatDefaultToCustomUnit(Category category, float value) {
        // Workaround for calculating HbA1c with formula
        if (category == Category.HBA1C) {
            float unitValue = getUnitValue(category);
            return unitValue != 1 ? (value - 2.15f) / 0.0915f : value;
        }
        return value * getUnitValue(category);
    }

    public String getMeasurementForUi(Category category, float value) {
        float customValue = formatDefaultToCustomUnit(category, value);
        return FloatUtils.parseFloat(customValue);
    }

    public int getDecimalPlaces() {
        return sharedPreferences.getInt(
            getKey(R.string.preference_decimal_places),
            context.getResources().getInteger(R.integer.decimal_places_default)
        );
    }

    public void setDecimalPlaces(int decimalPlaces) {
        sharedPreferences
            .edit()
            .putInt(getKey(R.string.preference_decimal_places), decimalPlaces)
            .apply();
    }

    // FACTOR

    // MEAL FACTOR

    private static final float DEFAULT_FACTOR_MEAL = 1;

    public TimeInterval getMealFactorInterval() {
        int position = sharedPreferences.getInt(getKey(R.string.preference_factor_meal_interval), TimeInterval.EVERY_SIX_HOURS.ordinal());
        TimeInterval[] timeIntervals = TimeInterval.values();
        return position >= 0 && position < timeIntervals.length ? timeIntervals[position] : TimeInterval.EVERY_SIX_HOURS;
    }

    public void setMealFactorInterval(TimeInterval interval) {
        sharedPreferences.edit().putInt(getKey(R.string.preference_factor_meal_interval), interval.ordinal()).apply();
    }

    public float getMealFactorForHour(int hourOfDay) {
        String key = getKey(R.string.preference_factor_meal_interval_for_hour, hourOfDay);
        return sharedPreferences.getFloat(key, DEFAULT_FACTOR_MEAL);
    }

    public void setMealFactorForHour(int hourOfDay, float factor) {
        String key = getKey(R.string.preference_factor_meal_interval_for_hour, hourOfDay);
        sharedPreferences.edit().putFloat(key, factor).apply();
    }

    public MealFactorUnit getMealFactorUnit() {
        MealFactorUnit defaultValue = MealFactorUnit.CARBOHYDRATES_UNIT;
        String value = sharedPreferences.getString(getKey(R.string.preference_factor_meal), "0");
        int index = 0;
        try {
            index = Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            Log.e(TAG, exception.toString());
        }
        return index >= 0 && index < MealFactorUnit.values().length ? MealFactorUnit.values()[index] : defaultValue;
    }

    /**
     * Used to migrate from static to dynamic factors
     */
    private void migrateMealFactors() {
        if (getMealFactorForHour(0) < 0) {
            for (Daytime daytime : Daytime.values()) {
                float factor = sharedPreferences.getFloat(getKey(R.string.preference_factor_deprecated) + daytime.toDeprecatedString(), -1);
                if (factor >= 0) {
                    int step = 0;
                    while (step < Daytime.INTERVAL_LENGTH) {
                        int hourOfDay = (daytime.startingHour + step) % DateTimeConstants.HOURS_PER_DAY;
                        setMealFactorForHour(hourOfDay, factor);
                        step++;
                    }
                    sharedPreferences.edit().putFloat(getKey(R.string.preference_factor_deprecated) + daytime, -1).apply();
                }
            }
        }
    }

    // CORRECTION FACTOR

    private static final float DEFAULT_FACTOR_CORRECTION_FACTOR = 40;

    public TimeInterval getCorrectionFactorInterval() {
        int position = sharedPreferences.getInt(getKey(R.string.preference_factor_correction_interval), TimeInterval.CONSTANT.ordinal());
        TimeInterval[] timeIntervals = TimeInterval.values();
        return position >= 0 && position < timeIntervals.length ? timeIntervals[position] : TimeInterval.CONSTANT;
    }

    public void setCorrectionFactorInterval(TimeInterval interval) {
        sharedPreferences.edit().putInt(getKey(R.string.preference_factor_correction_interval), interval.ordinal()).apply();
    }

    public float getCorrectionFactorForHour(int hourOfDay) {
        String key = getKey(R.string.preference_factor_correction_interval_for_hour, hourOfDay);
        return sharedPreferences.getFloat(key, DEFAULT_FACTOR_CORRECTION_FACTOR);
    }

    public void setCorrectionFactorForHour(int hourOfDay, float factor) {
        String key = getKey(R.string.preference_factor_correction_interval_for_hour, hourOfDay);
        sharedPreferences.edit().putFloat(key, factor).apply();
    }

    private void migrateCorrectionFactors() {
        if (getCorrectionFactorForHour(0) < 0) {
            float oldValue = FloatUtils.parseNumber(sharedPreferences.getString(getKey(R.string.preference_correction_deprecated), Float.toString(DEFAULT_FACTOR_CORRECTION_FACTOR)));
            int hourOfDay = 0;
            while (hourOfDay < DateTimeConstants.HOURS_PER_DAY) {
                setCorrectionFactorForHour(hourOfDay, oldValue);
                hourOfDay++;
            }
        }
    }

    // BASAL RATE FACTOR

    private static final float DEFAULT_FACTOR_BASAL_RATE = 1;

    public TimeInterval getBasalRateFactorInterval() {
        int position = sharedPreferences.getInt(getKey(R.string.preference_factor_basal_rate_interval), TimeInterval.CONSTANT.ordinal());
        TimeInterval[] timeIntervals = TimeInterval.values();
        return position >= 0 && position < timeIntervals.length ? timeIntervals[position] : TimeInterval.CONSTANT;
    }

    public void setBasalRateFactorInterval(TimeInterval interval) {
        sharedPreferences.edit().putInt(getKey(R.string.preference_factor_basal_rate_interval), interval.ordinal()).apply();
    }

    public float getBasalRateFactorForHour(int hourOfDay) {
        String key = getKey(R.string.preference_factor_basal_rate_interval_for_hour, hourOfDay);
        return sharedPreferences.getFloat(key, DEFAULT_FACTOR_BASAL_RATE);
    }

    public void setBasalRateFactorForHour(int hourOfDay, float factor) {
        String key = getKey(R.string.preference_factor_basal_rate_interval_for_hour, hourOfDay);
        sharedPreferences.edit().putFloat(key, factor).apply();
    }

    // FOOD

    public boolean showCustomFood() {
        return sharedPreferences.getBoolean(getKey(R.string.preference_food_show_custom), true);
    }

    public boolean showCommonFood() {
        return sharedPreferences.getBoolean(getKey(R.string.preference_food_show_common), true);
    }

    public boolean showBrandedFood() {
        return sharedPreferences.getBoolean(getKey(R.string.preference_food_show_branded), true);
    }
}
