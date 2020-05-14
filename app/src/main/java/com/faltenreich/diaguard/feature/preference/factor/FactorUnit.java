package com.faltenreich.diaguard.feature.preference.factor;

import androidx.annotation.StringRes;

import com.faltenreich.diaguard.R;

public enum FactorUnit {

    CARBOHYDRATES_UNIT(0, R.string.unit_factor_carbohydrates_unit, .1f),
    BREAD_UNITS(1, R.string.unit_factor_bread_unit, .0833f);

    public int index;
    @StringRes public int titleResId;
    public float factor;

    FactorUnit(int index, @StringRes int titleResId, float factor) {
        this.index = index;
        this.titleResId = titleResId;
        this.factor = factor;
    }
}
