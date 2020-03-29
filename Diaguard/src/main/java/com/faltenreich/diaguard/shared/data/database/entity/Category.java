package com.faltenreich.diaguard.shared.data.database.entity;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import com.faltenreich.diaguard.R;

public enum Category {

    BLOODSUGAR(BloodSugar.class, 1, R.string.bloodsugar, R.drawable.ic_category_bloodsugar, R.drawable.ic_showcase_bloodsugar),
    INSULIN(Insulin.class, 2, R.string.insulin, R.drawable.ic_category_insulin, R.drawable.ic_showcase_insulin),
    MEAL(Meal.class, 3, R.string.meal, R.drawable.ic_category_meal, R.drawable.ic_showcase_meal),
    ACTIVITY(Activity.class, 4, R.string.activity, R.drawable.ic_category_activity, R.drawable.ic_showcase_activity),
    HBA1C(HbA1c.class, 5, R.string.hba1c, R.drawable.ic_category_hba1c, R.drawable.ic_showcase_hba1c),
    WEIGHT(Weight.class, 6, R.string.weight, R.drawable.ic_category_weight, R.drawable.ic_showcase_weight),
    PULSE(Pulse.class, 7, R.string.pulse, R.drawable.ic_category_pulse, R.drawable.ic_showcase_pulse),
    PRESSURE(Pressure.class, 8, R.string.pressure, R.string.pressure_acronym, R.drawable.ic_category_pressure, R.drawable.ic_showcase_pressure),
    OXYGEN_SATURATION(OxygenSaturation.class, 9, R.string.oxygen_saturation, R.string.oxygen_saturation_acronym, R.drawable.ic_category_oxygen_saturation, R.drawable.ic_showcase_oxygen_saturation);

    private Class clazz;
    private int stableId;
    private int stringResId;
    private int stringAcronymResId;
    private int iconImageResId;
    private int showcaseImageResId;

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

    public static Category fromStableId(int stableId) {
        for (Category category : values()) {
            if (category.stableId == stableId) {
                return category;
            }
        }
        return null;
    }
}
