package com.android.diaguard.helpers;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.android.diaguard.MainActivity;
import com.android.diaguard.R;
import com.android.diaguard.database.Event;
import com.android.diaguard.preferences.CategoryPreference;
import com.android.diaguard.preferences.FactorPreference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.EnumMap;
import java.util.List;

/**
 * Created by Filip on 04.11.13.
 */
public class PreferenceHelper {

    Activity activity;
    SharedPreferences sharedPreferences;

    public PreferenceHelper(Activity activity) {
        this.activity = activity;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
    }

    public MainActivity.FragmentType getStartFragment() {
        int startFragment = Integer.parseInt(sharedPreferences.getString("start_fragment", "0"));
        switch(startFragment) {
            case 0: return MainActivity.FragmentType.Home;
            case 1: return MainActivity.FragmentType.Timeline;
            case 2: return MainActivity.FragmentType.Log;
            default: return MainActivity.FragmentType.Home;
        }
    }

    // DATES

    public SimpleDateFormat getDateAndTimeFormat() {
        return new SimpleDateFormat(getDateFormat().toPattern() + " " + getTimeFormat().toPattern());
    }

    public SimpleDateFormat getDateFormat() {
        String dateString = sharedPreferences.getString("dateformat",
                activity.getResources().getString(R.string.dateformat_default));

        dateString = dateString.replace("YYYY", "yyyy");
        dateString = dateString.replace("mm", "MM");
        dateString = dateString.replace("DD", "dd");

        return new SimpleDateFormat(dateString);
    }

    public SimpleDateFormat getTimeFormat() {
        return new SimpleDateFormat("HH:mm");
    }

    // BLOOD SUGAR

    public float getTargetValue() {
        float targetValue = Float.valueOf(sharedPreferences.getString("target",
                activity.getString(R.string.pref_therapy_targets_target_default)));
        return formatDefaultToCustomUnit(Event.Category.BloodSugar, targetValue);
    }

    public boolean limitsAreHighlighted() {
        return sharedPreferences.getBoolean("targets_highlight", true);
    }

    public float getLimitHyperglycemia() {
        float limitHyperglycemia = Float.valueOf(sharedPreferences.getString("hyperclycemia",
                activity.getString(R.string.pref_therapy_targets_hyperclycemia_default)));
        return formatDefaultToCustomUnit(Event.Category.BloodSugar, limitHyperglycemia);
    }

    public float getLimitHypoglycemia() {
        float limitHypoglycemia = Float.valueOf(sharedPreferences.getString("hypoclycemia",
                activity.getString(R.string.pref_therapy_targets_hypoclycemia_default)));
        return formatDefaultToCustomUnit(Event.Category.BloodSugar, limitHypoglycemia);
    }

    // CATEGORIES

    public String getCategoryName(Event.Category category) {
        int position = Event.Category.valueOf(category.name()).ordinal();
        String[] categories = activity.getResources().getStringArray(R.array.categories);
        return categories[position];
    }

    public String getCategoryAcronym(Event.Category category) {
        int position = Event.Category.valueOf(category.name()).ordinal();
        String[] categories = activity.getResources().getStringArray(R.array.categories_acronyms);
        return categories[position];
    }

    // UNITS

    public String[] getUnitsNames(Event.Category category) {
        String categoryName = category.name().toLowerCase();
        int resourceIdUnits = activity.getResources().getIdentifier(categoryName +
                "_units", "array", activity.getPackageName());
        return activity.getResources().getStringArray(resourceIdUnits);
    }

    public String getUnitName(Event.Category category) {
        String sharedPref = sharedPreferences.
                getString("unit_" + category.name().toLowerCase(), "1");
        return getUnitsNames(category)[Arrays.asList(getUnitsValues(category)).indexOf(sharedPref)];
    }

    public String[] getUnitsAcronyms(Event.Category category) {
        String categoryName = category.name().toLowerCase();
        int resourceIdUnits = activity.getResources().getIdentifier(categoryName +
                "_units_acronyms", "array", activity.getPackageName());
        if(resourceIdUnits == 0)
            return null;
        else
            return activity.getResources().getStringArray(resourceIdUnits);
    }

    public String getUnitAcronym(Event.Category category) {
        String[] acronyms = getUnitsAcronyms(category);
        if(acronyms == null)
            return getUnitName(category);
        else {
            String sharedPref = sharedPreferences.
                    getString("unit_" + category.name().toLowerCase(), "1");
            String acronym = acronyms[Arrays.asList(getUnitsValues(category)).indexOf(sharedPref)];
            if(acronym == null)
                return getUnitName(category);
            else
                return acronym;
        }
    }

    public String[] getUnitsValues(Event.Category category) {
        String categoryName = category.name().toLowerCase();
        int resourceIdUnits = activity.getResources().getIdentifier(categoryName +
                "_units_values", "array", activity.getPackageName());
        return activity.getResources().getStringArray(resourceIdUnits);
    }

    public float getUnitValue(Event.Category category) {
        String sharedPref = sharedPreferences.
                getString("unit_" + category.name().toLowerCase(), "1");
        String value = getUnitsValues(category)[Arrays.asList(getUnitsValues(category)).indexOf(sharedPref)];
        return Float.valueOf(value);
    }

    public float formatCustomToDefaultUnit(Event.Category category, float value) {
        return value / getUnitValue(category);
    }

    public float formatDefaultToCustomUnit(Event.Category category, float value) {
        return value * getUnitValue(category);
    }

    // FACTORS

    public enum Daytime {
        Morning,
        Noon,
        Evening,
        Night
    }

    public Daytime getCurrentDaytime() {

        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);

        if(hour >= 4 && hour < 10)
            return Daytime.Morning;
        else if(hour >= 10 && hour < 16)
            return Daytime.Noon;
        else if(hour >= 16 && hour < 22)
            return Daytime.Evening;
        else
            return Daytime.Night;
    }

    public float getFactorValue(Daytime daytime) {
        return sharedPreferences.getFloat(FactorPreference.FACTOR +
                daytime.toString(), 0);
    }

    // CORRECTION VALUE

    public float getCorrectionValue() {
        float defaultValue = Float.parseFloat(sharedPreferences.getString("correction_value", "40"));
        return formatDefaultToCustomUnit(Event.Category.BloodSugar, defaultValue);
    }

    // CATEGORIES

    public boolean isCategoryActive(Event.Category category) {
        return sharedPreferences.getBoolean(category.name() + CategoryPreference.ACTIVE, true);
    }

    public Event.Category[] getActiveCategories() {
        List<Event.Category> activeCategories = new ArrayList<Event.Category>();

        for(int item = 0; item < Event.Category.values().length; item++)
            if(isCategoryActive(Event.Category.values()[item]))
                activeCategories.add(Event.Category.values()[item]);

        return activeCategories.toArray(new Event.Category[activeCategories.size()]);
    }

    public String[] getActiveCategoriesNames() {
        List<String> activeCategories = new ArrayList<String>();
        String[] categoryNames = activity.getResources().getStringArray(R.array.categories);

        for(int item = 0; item < Event.Category.values().length; item++)
            if(isCategoryActive(Event.Category.values()[item]))
                activeCategories.add(categoryNames[item]);

        return activeCategories.toArray(new String[activeCategories.size()]);
    }

    public EnumMap<Event.Category, Boolean> getActiveCategoriesHashMap() {

        EnumMap<Event.Category, Boolean> activeCategories =
                new  EnumMap<Event.Category, Boolean>(Event.Category.class);

        for(int item = 0; item < Event.Category.values().length; item++)
            activeCategories.put(Event.Category.values()[item], isCategoryActive(Event.Category.values()[item]));

        return activeCategories;
    }
}
