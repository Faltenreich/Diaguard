package com.faltenreich.diaguard.shared.data.database.entity;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class BloodSugar extends Measurement {

    public class Column extends Measurement.Column {
        public static final String MGDL = "mgDl";
        public static final String TREND = "trend";
    }

    public enum Trend {
        FALLING_QUICKLY(R.string.trend_falling_quickly, "↓", R.drawable.ic_arrow_down),
        FALLING(R.string.trend_falling, "↘", R.drawable.ic_arrow_down_right),
        STEADY(R.string.trend_steady, "→", R.drawable.ic_arrow_forward),
        RISING(R.string.trend_rising, "↗", R.drawable.ic_arrow_up_right),
        RISING_QUICKLY(R.string.trend_rising_quickly, "↑", R.drawable.ic_arrow_up);

        @StringRes public final int titleRes;
        public final String unicodeIcon;
        @DrawableRes public final int iconRes;

        Trend(
            @StringRes int titleRes,
            String unicodeIcon,
            @DrawableRes int iconRes
        ) {
            this.titleRes = titleRes;
            this.unicodeIcon = unicodeIcon;
            this.iconRes = iconRes;
        }
    }

    @DatabaseField(columnName = Column.MGDL)
    private float mgDl;

    @DatabaseField(columnName = Column.TREND, dataType = DataType.ENUM_STRING)
    private Trend trend;

    public float getMgDl() {
        return mgDl;
    }

    public void setMgDl(float mgDl) {
        this.mgDl = mgDl;
    }

    @Nullable
    public Trend getTrend() {
        return trend;
    }

    public void setTrend(@Nullable Trend trend) {
        this.trend = trend;
    }

    @Override
    public Category getCategory() {
        return Category.BLOODSUGAR;
    }

    @Override
    public float[] getValues() {
        return new float[] { mgDl };
    }

    @Override
    public void setValues(float... values) {
        if (values.length > 0) {
            mgDl = values[0];
        }
    }

    @NonNull
    @Override
    public String toString() {
        return PreferenceStore.getInstance().getMeasurementForUi(getCategory(), mgDl);
    }
}
