package com.faltenreich.diaguard.shared.data.database.entity;

import android.content.res.Resources;

import androidx.annotation.ArrayRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import com.faltenreich.diaguard.R;

public enum Category {

    BLOODSUGAR(BloodSugar.class, 1, R.string.bloodsugar, R.string.bloodsugar_acronym, R.drawable.ic_category_bloodsugar, R.drawable.ic_showcase_bloodsugar),
    INSULIN(Insulin.class, 2, R.string.insulin, R.drawable.ic_category_insulin, R.drawable.ic_showcase_insulin),
    MEAL(Meal.class, 3, R.string.meal, R.drawable.ic_category_meal, R.drawable.ic_showcase_meal),
    ACTIVITY(Activity.class, 4, R.string.activity, R.drawable.ic_category_activity, R.drawable.ic_showcase_activity),
    HBA1C(HbA1c.class, 5, R.string.hba1c, R.drawable.ic_category_hba1c, R.drawable.ic_showcase_hba1c),
    WEIGHT(Weight.class, 6, R.string.weight, R.drawable.ic_category_weight, R.drawable.ic_showcase_weight),
    PULSE(Pulse.class, 7, R.string.pulse, R.drawable.ic_category_pulse, R.drawable.ic_showcase_pulse),
    PRESSURE(Pressure.class, 8, R.string.pressure, R.string.pressure_acronym, R.drawable.ic_category_pressure, R.drawable.ic_showcase_pressure),
    OXYGEN_SATURATION(OxygenSaturation.class, 9, R.string.oxygen_saturation, R.string.oxygen_saturation_acronym, R.drawable.ic_category_oxygen_saturation, R.drawable.ic_showcase_oxygen_saturation);

    private final Class clazz;
    private final int stableId;
    private final int stringResId;
    private final int stringAcronymResId;
    private final int iconImageResId;
    private final int showcaseImageResId;

    Category(
        Class clazz,
        int stableId,
        @StringRes int stringResId,
        @StringRes int stringAcronymResId,
        @DrawableRes int iconImageResId,
        @DrawableRes int showcaseImageResId
    ) {
        this.clazz = clazz;
        this.stableId = stableId;
        this.stringResId = stringResId;
        this.stringAcronymResId = stringAcronymResId;
        this.iconImageResId = iconImageResId;
        this.showcaseImageResId = showcaseImageResId;
    }

    Category(
            Class clazz,
            int stableId,
            @StringRes int stringResId,
            @DrawableRes int iconImageResId,
            @DrawableRes int showcaseImageResId
    ) {
        this(clazz, stableId, stringResId, stringResId, iconImageResId, showcaseImageResId);
    }

    public <M extends Measurement> Class<M> toClass() {
        return clazz;
    }

    public boolean stackValues() {
        return this == INSULIN || this == MEAL || this == ACTIVITY;
    }

    public int getStableId() {
        return stableId;
    }

    public int getStringResId() {
        return stringResId;
    }

    public int getStringAcronymResId() {
        return stringAcronymResId;
    }

    @DrawableRes
    public int getIconImageResourceId() {
        return iconImageResId;
    }

    @DrawableRes
    public int getShowcaseImageResourceId() {
        return showcaseImageResId;
    }

    public boolean isOptional() {
        return this != BLOODSUGAR;
    }

    @ArrayRes
    public int getExtremaArrayResourceId() {
        switch (this) {
            case BLOODSUGAR: return R.array.bloodsugar_extrema;
            case INSULIN: return R.array.insulin_extrema;
            case MEAL: return R.array.meal_extrema;
            case ACTIVITY: return R.array.activity_extrema;
            case HBA1C: return R.array.hba1c_extrema;
            case WEIGHT: return R.array.weight_extrema;
            case PULSE: return R.array.pulse_extrema;
            case PRESSURE: return R.array.pressure_extrema;
            case OXYGEN_SATURATION: return R.array.oxygen_saturation_extrema;
            default: throw new Resources.NotFoundException("Array of extrema missing for category: " + this);
        }
    }

    @ArrayRes
    public int getUnitNameArrayResourceId() {
        switch (this) {
            case BLOODSUGAR: return R.array.bloodsugar_units;
            case INSULIN: return R.array.insulin_units;
            case MEAL: return R.array.meal_units;
            case ACTIVITY: return R.array.activity_units;
            case HBA1C: return R.array.hba1c_units;
            case WEIGHT: return R.array.weight_units;
            case PULSE: return R.array.pulse_units;
            case PRESSURE: return R.array.pressure_units;
            case OXYGEN_SATURATION: return R.array.oxygen_saturation_units;
            default: throw new Resources.NotFoundException("Array of unit names missing for category: " + this);
        }
    }

    @ArrayRes
    public int getUnitNameAcronymArrayResourceId() {
        switch (this) {
            case INSULIN: return R.array.insulin_units_acronyms;
            case MEAL: return R.array.meal_units_acronyms;
            case ACTIVITY: return R.array.activity_units_acronyms;
            case HBA1C: return R.array.hba1c_units_acronyms;
            case WEIGHT: return R.array.weight_units_acronyms;
            case PULSE: return R.array.pulse_units_acronyms;
            case PRESSURE: return R.array.pressure_units_acronyms;
            case OXYGEN_SATURATION: return R.array.oxygen_saturation_units_acronyms;
            default: return 0;
        }
    }

    @ArrayRes
    public int getUnitValueArrayResourceId() {
        switch (this) {
            case BLOODSUGAR: return R.array.bloodsugar_units_values;
            case INSULIN: return R.array.insulin_units_values;
            case MEAL: return R.array.meal_units_values;
            case ACTIVITY: return R.array.activity_units_values;
            case HBA1C: return R.array.hba1c_units_values;
            case WEIGHT: return R.array.weight_units_values;
            case PULSE: return R.array.pulse_units_values;
            case PRESSURE: return R.array.pressure_units_values;
            case OXYGEN_SATURATION: return R.array.oxygen_saturation_units_values;
            default: throw new Resources.NotFoundException("Array of unit values missing for category: " + this);
        }
    }

    public static Category fromStableId(int stableId) {
        for (Category category : values()) {
            if (category.stableId == stableId) {
                return category;
            }
        }
        return null;
    }
}
