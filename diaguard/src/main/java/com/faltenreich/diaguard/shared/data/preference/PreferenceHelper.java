package com.faltenreich.diaguard.shared.data.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.preference.PreferenceManager;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.category.CategoryComparatorFactory;
import com.faltenreich.diaguard.feature.export.job.pdf.meta.PdfExportStyle;
import com.faltenreich.diaguard.feature.navigation.MainFragmentType;
import com.faltenreich.diaguard.feature.preference.factor.Daytime;
import com.faltenreich.diaguard.feature.preference.factor.TimeInterval;
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

@SuppressWarnings({
    "WeakerAccess",
    "SameParameterValue",
    "BooleanMethodIsAlwaysInverted",
    "SwitchStatementWithTooFewBranches",
    "DefaultLocale"
})
public class PreferenceHelper {

    private static final String TAG = PreferenceHelper.class.getSimpleName();
    private static final String INPUT_QUERIES_SEPARATOR = ";";
    private static final int INPUT_QUERIES_MAXIMUM_COUNT = 10;

    public static class Keys {
        public static final String VERSION_CODE = "versionCode";
        public static final String CATEGORY_PINNED = "categoryPinned%s";
        public final static String CATEGORY_ACTIVE = "%s_active";
        public final static String CATEGORY_SORT_INDEX = "%s_sortIndex";
        public static final String ALARM_START_IN_MILLIS = "alarmStartInMillis";
        public static final String INTERVAL_FACTOR = "intervalFactor";
        public static final String INTERVAL_FACTOR_FOR_HOUR = "intervalFactor%d";
        public static final String FACTOR_DEPRECATED = "factor_";
        public static final String INTERVAL_CORRECTION = "intervalCorrection";
        public static final String INTERVAL_CORRECTION_FOR_HOUR = "intervalCorrection%d";
        public static final String CORRECTION_DEPRECATED = "correction_value";
        public static final String INPUT_QUERIES = "inputQueries";
        public static final String DID_IMPORT_COMMON_FOOD_FOR_LANGUAGE = "didImportCommonFoodForLanguage";
        public static final String CHART_STYLE = "chart_style";
        public static final String EXPORT_PDF_STYLE = "exportPdfStyle";
        public static final String EXPORT_HEADER = "exportHeader";
        public static final String EXPORT_FOOTER = "exportFooter";
        public static final String EXPORT_NOTES = "export_notes";
        public static final String EXPORT_TAGS = "export_tags";
        public static final String EXPORT_FOOD = "export_food";
        public static final String EXPORT_CATEGORIES = "exportCategories";
        public static final String EXPORT_INSULIN_SPLIT = "exportInsulinSplit";
        public static final String DID_IMPORT_TAGS_FOR_LANGUAGE = "didImportTagsForLanguage";
        public static final String FOOD_SHOW_BRANDED = "showBrandedFood";
        public static final String THEME = "theme";
    }

    public enum FactorUnit {
        CARBOHYDRATES_UNIT(0, R.string.unit_factor_carbohydrates_unit, .1f),
        BREAD_UNITS(1, R.string.unit_factor_bread_unit, .0833f);

        public int index;
        public @StringRes
        int titleResId;
        public float factor;

        FactorUnit(int index, @StringRes int titleResId, float factor) {
            this.index = index;
            this.titleResId = titleResId;
            this.factor = factor;
        }
    }

    private static PreferenceHelper instance;

    public static PreferenceHelper getInstance() {
        if (instance == null) {
            instance = new PreferenceHelper();
        }
        return instance;
    }

    private SharedPreferences sharedPreferences;
    private Context context;

    private PreferenceHelper() {

    }

    public void init(Context context) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.context = context;
    }

    private Context getContext() {
        return context;
    }

    // GENERAL

    public void migrate() {
        migrateFactors();
        migrateCorrection();
        migrateStartScreen();
    }

    public int getVersionCode() {
        return sharedPreferences.getInt(Keys.VERSION_CODE, 0);
    }

    public void setVersionCode(int versionCode) {
        sharedPreferences.edit().putInt(Keys.VERSION_CODE, versionCode).apply();
    }

    public boolean didImportCommonFood(Locale locale) {
        return sharedPreferences.getBoolean(Keys.DID_IMPORT_COMMON_FOOD_FOR_LANGUAGE + locale.getLanguage(), false);
    }

    public void setDidImportCommonFood(Locale locale, boolean didImport) {
        sharedPreferences.edit().putBoolean(Keys.DID_IMPORT_COMMON_FOOD_FOR_LANGUAGE + locale.getLanguage(), didImport).apply();
    }

    public boolean didImportTags(Locale locale) {
        return sharedPreferences.getBoolean(Keys.DID_IMPORT_TAGS_FOR_LANGUAGE + locale.getLanguage(), false);
    }

    public void setDidImportTags(Locale locale, boolean didImport) {
        sharedPreferences.edit().putBoolean(Keys.DID_IMPORT_TAGS_FOR_LANGUAGE + locale.getLanguage(), didImport).apply();
    }

    public long getAlarmStartInMillis() {
        return sharedPreferences.getLong(Keys.ALARM_START_IN_MILLIS, -1);
    }

    public void setAlarmStartInMillis(long alarmStartInMillis) {
        sharedPreferences.edit().putLong(Keys.ALARM_START_IN_MILLIS, alarmStartInMillis).apply();
    }

    public Theme getTheme() {
        Theme themeDefault = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P ? Theme.SYSTEM : Theme.LIGHT;
        String themeKey = sharedPreferences.getString(Keys.THEME, null);
        if (themeKey != null) {
            Theme theme = Theme.fromKey(themeKey);
            return theme != null ? theme : themeDefault;
        } else {
            setTheme(themeDefault);
            return themeDefault;
        }
    }

    public void setTheme(Theme theme) {
        sharedPreferences.edit().putString(Keys.THEME, theme.getKey()).apply();
    }

    public int getStartScreen() {
        String startScreen = sharedPreferences.getString("startscreen", "0");
        return Integer.parseInt(startScreen);
    }

    public void setStartScreen(int startScreen) {
        sharedPreferences.edit().putString("startscreen", Integer.toString(startScreen)).apply();
    }

    private void migrateStartScreen() {
        int startScreen = getStartScreen();
        if (MainFragmentType.valueOf(startScreen) == null) {
            setStartScreen(0);
        }
    }

    public boolean isSoundAllowed() {
        return sharedPreferences.getBoolean("sound", true);
    }

    public boolean isVibrationAllowed() {
        return sharedPreferences.getBoolean("vibration", true);
    }

    public TimelineStyle getTimelineStyle() {
        String preference = sharedPreferences.getString(Keys.CHART_STYLE, null);
        if (!TextUtils.isEmpty(preference)) {
            try {
                int chartStyle = Integer.parseInt(preference);
                TimelineStyle[] chartStyles = TimelineStyle.values();
                return chartStyle >= 0 && chartStyle < chartStyles.length ? chartStyles[chartStyle] : TimelineStyle.SCATTER_CHART;
            } catch (NumberFormatException exception) {
                Log.e(TAG, exception.getMessage() != null ? exception.getMessage() : "Failed to getChartStyle");
            }
        } else {
            Log.e(TAG, "Failed to find shared preference for key: " + Keys.CHART_STYLE);
        }
        return TimelineStyle.SCATTER_CHART;
    }

    public void setTimelineStyle(TimelineStyle style) {
        int stableId = style.getStableId();
        String stableIdString = Integer.toString(stableId);
        sharedPreferences.edit().putString(Keys.CHART_STYLE, stableIdString).apply();
    }

    public int[] getExtrema(Category category) {
        int resourceIdExtrema = getContext().getResources().getIdentifier(category.name().toLowerCase() + "_extrema", "array", getContext().getPackageName());
        if (resourceIdExtrema == 0) {
            throw new Resources.NotFoundException("Resource \"category_extrema\" not found: IntArray with event value extrema");
        }
        return getContext().getResources().getIntArray(resourceIdExtrema);
    }

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
            float value = PreferenceHelper.getInstance().formatCustomToDefaultUnit(category, FloatUtils.parseNumber(textView.getText().toString()));
            if (allowNegativeValues) {
                value = Math.abs(value);
            }
            if (!PreferenceHelper.getInstance().validateEventValue(category, value)) {
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
        sharedPreferences.edit().putInt(Keys.EXPORT_PDF_STYLE, style.stableId).apply();
    }

    public PdfExportStyle getPdfExportStyle() {
        PdfExportStyle defaultStyle = PdfExportStyle.TABLE;
        int stableId = sharedPreferences.getInt(Keys.EXPORT_PDF_STYLE, defaultStyle.stableId);
        PdfExportStyle style = PdfExportStyle.valueOf(stableId);
        return style != null ? style : defaultStyle;
    }

    public void setExportHeader(boolean exportHeader) {
        sharedPreferences.edit().putBoolean(Keys.EXPORT_HEADER, exportHeader).apply();
    }

    public boolean exportHeader() {
        return sharedPreferences.getBoolean(Keys.EXPORT_HEADER, true);
    }

    public void setExportFooter(boolean exportFooter) {
        sharedPreferences.edit().putBoolean(Keys.EXPORT_FOOTER, exportFooter).apply();
    }

    public boolean exportFooter() {
        return sharedPreferences.getBoolean(Keys.EXPORT_FOOTER, true);
    }

    public void setExportNotes(boolean exportNotes) {
        sharedPreferences.edit().putBoolean(Keys.EXPORT_NOTES, exportNotes).apply();
    }

    public boolean exportNotes() {
        return sharedPreferences.getBoolean(Keys.EXPORT_NOTES, true);
    }

    public void setExportTags(boolean exportTags) {
        sharedPreferences.edit().putBoolean(Keys.EXPORT_TAGS, exportTags).apply();
    }

    public boolean exportTags() {
        return sharedPreferences.getBoolean(Keys.EXPORT_TAGS, true);
    }

    public void setExportFood(boolean exportFood) {
        sharedPreferences.edit().putBoolean(Keys.EXPORT_FOOD, exportFood).apply();
    }

    public boolean exportFood() {
        return sharedPreferences.getBoolean(Keys.EXPORT_FOOD, true);
    }

    public void setExportInsulinSplit(boolean splitInsulin) {
        sharedPreferences.edit().putBoolean(Keys.EXPORT_INSULIN_SPLIT, splitInsulin).apply();
    }

    public boolean exportInsulinSplit() {
        return sharedPreferences.getBoolean(Keys.EXPORT_INSULIN_SPLIT, false);
    }

    public void setExportCategories(Category[] categories) {
        String preference = new CategorySerializer().serialize(categories);
        sharedPreferences.edit().putString(Keys.EXPORT_CATEGORIES, preference).apply();
    }

    public Category[] getExportCategories() {
        CategorySerializer serializer = new CategorySerializer();
        String preference = sharedPreferences.getString(Keys.EXPORT_CATEGORIES, serializer.serialize(Category.values()));
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
        sharedPreferences.edit().putString(Keys.INPUT_QUERIES, inputQueries).apply();
    }

    private String getInputQueriesString() {
        return sharedPreferences.getString(Keys.INPUT_QUERIES, "");
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

    public String getValueForKey(String key) {
        return sharedPreferences.getString(key, null);
    }

    public float getTargetValue() {
        return FloatUtils.parseNumber(sharedPreferences.getString("target",
            getContext().getString(R.string.pref_therapy_targets_target_default)));
    }

    public boolean limitsAreHighlighted() {
        return sharedPreferences.getBoolean("targets_highlight", true);
    }

    public void setLimitsAreHighlighted(boolean isHighlighted) {
        sharedPreferences.edit().putBoolean("targets_highlight", isHighlighted).apply();
    }

    public float getLimitHyperglycemia() {
        return FloatUtils.parseNumber(sharedPreferences.getString("hyperclycemia",
            getContext().getString(R.string.pref_therapy_targets_hyperclycemia_default)));
    }

    public float getLimitHypoglycemia() {
        return FloatUtils.parseNumber(sharedPreferences.getString("hypoclycemia",
            getContext().getString(R.string.pref_therapy_targets_hypoclycemia_default)));
    }

    public int getMonthResourceId(DateTime daytime) {
        int monthOfYear = daytime.monthOfYear().get();
        String identifier = String.format("bg_month_%d", monthOfYear - 1);
        return getContext().getResources().getIdentifier(identifier,
            "drawable", getContext().getPackageName());
    }

    public int getMonthSmallResourceId(DateTime daytime) {
        int monthOfYear = daytime.monthOfYear().get();
        String identifier = String.format("bg_month_%d_small", monthOfYear - 1);
        return getContext().getResources().getIdentifier(identifier,
            "drawable", getContext().getPackageName());
    }

    public boolean isCategoryActive(Category category) {
        return sharedPreferences.getBoolean(String.format(Keys.CATEGORY_ACTIVE, category.name()), true);
    }

    public void setCategoryActive(Category category, boolean isActive) {
        sharedPreferences.edit().putBoolean(String.format(Keys.CATEGORY_ACTIVE, category.name()), isActive).apply();
    }

    public int getCategorySortIndex(Category category) {
        return sharedPreferences.getInt(String.format(Keys.CATEGORY_SORT_INDEX, category.name()), category.ordinal());
    }

    public void setCategorySortIndex(Category category, int sortIndex) {
        sharedPreferences.edit().putInt(String.format(Keys.CATEGORY_SORT_INDEX, category.name()), sortIndex).apply();
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

    private String getCategoryPinnedName(Category category) {
        return String.format(Keys.CATEGORY_PINNED, category.name());
    }

    public boolean isCategoryPinned(Category category) {
        return sharedPreferences.getBoolean(getCategoryPinnedName(category), false);
    }

    public void setCategoryPinned(Category category, boolean isPinned) {
        sharedPreferences.edit().putBoolean(getCategoryPinnedName(category), isPinned).apply();
    }

    // UNITS

    public String[] getUnitsNames(Category category) {
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

    public String[] getUnitsAcronyms(Category category) {
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
        switch (category) {
            case HBA1C:
                // Workaround for calculating HbA1c with formula
                float unitValue = getUnitValue(category);
                return unitValue != 1 ? (value * 0.0915f) + 2.15f : value;
            default:
                return value / getUnitValue(category);
        }
    }

    public float formatDefaultToCustomUnit(Category category, float value) {
        switch (category) {
            case HBA1C:
                // Workaround for calculating HbA1c with formula
                float unitValue = getUnitValue(category);
                return unitValue != 1 ? (value - 2.15f) / 0.0915f : value;
            default:
                return value * getUnitValue(category);
        }
    }

    public String getMeasurementForUi(Category category, float value) {
        float customValue = formatDefaultToCustomUnit(category, value);
        return FloatUtils.parseFloat(customValue);
    }

    // FACTORS

    public TimeInterval getFactorInterval() {
        int position = sharedPreferences.getInt(Keys.INTERVAL_FACTOR, TimeInterval.EVERY_SIX_HOURS.ordinal());
        TimeInterval[] timeIntervals = TimeInterval.values();
        return position >= 0 && position < timeIntervals.length ? timeIntervals[position] : TimeInterval.EVERY_SIX_HOURS;
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
    private void migrateFactors() {
        if (getFactorForHour(0) < 0) {
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

    public FactorUnit getFactorUnit() {
        FactorUnit defaultValue = FactorUnit.CARBOHYDRATES_UNIT;
        String value = sharedPreferences.getString("unit_meal_factor", "0");
        int index = 0;
        try {
            index = Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            Log.e(TAG, exception.getMessage() != null ? exception.getMessage() : "Failed to getFactorUnit()");
        }
        return index >= 0 && index < FactorUnit.values().length ? FactorUnit.values()[index] : defaultValue;
    }

    // CORRECTION

    public TimeInterval getCorrectionInterval() {
        int position = sharedPreferences.getInt(Keys.INTERVAL_CORRECTION, TimeInterval.CONSTANT.ordinal());
        TimeInterval[] timeIntervals = TimeInterval.values();
        return position >= 0 && position < timeIntervals.length ? timeIntervals[position] : TimeInterval.CONSTANT;
    }

    public void setCorrectionInterval(TimeInterval interval) {
        sharedPreferences.edit().putInt(Keys.INTERVAL_CORRECTION, interval.ordinal()).apply();
    }

    public float getCorrectionForHour(int hourOfDay) {
        String key = String.format(Keys.INTERVAL_CORRECTION_FOR_HOUR, hourOfDay);
        return sharedPreferences.getFloat(key, -1);
    }

    public void setCorrectionForHour(int hourOfDay, float factor) {
        String key = String.format(Keys.INTERVAL_CORRECTION_FOR_HOUR, hourOfDay);
        sharedPreferences.edit().putFloat(key, factor).apply();
    }

    private void migrateCorrection() {
        if (getCorrectionForHour(0) < 0) {
            float oldValue = FloatUtils.parseNumber(sharedPreferences.getString(Keys.CORRECTION_DEPRECATED, "40"));
            int hourOfDay = 0;
            while (hourOfDay < DateTimeConstants.HOURS_PER_DAY) {
                setCorrectionForHour(hourOfDay, oldValue);
                hourOfDay++;
            }
        }
    }

    // FOOD

    public boolean showBrandedFood() {
        return sharedPreferences.getBoolean(Keys.FOOD_SHOW_BRANDED, true);
    }

    public void setShowBrandedFood(boolean showBrandedFood) {
        sharedPreferences.edit().putBoolean(Keys.FOOD_SHOW_BRANDED, showBrandedFood).apply();
    }
}
