package com.faltenreich.diaguard.util;

import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.R;

/**
 * Created by Faltenreich on 22.03.2016.
 */
public enum TimeSpan {
    WEEK,
    TWO_WEEKS,
    MONTH,
    YEAR;

    @Override
    public String toString() {
        switch (this) {
            case WEEK:
                return DiaguardApplication.getContext().getString(R.string.week);
            case TWO_WEEKS:
                return DiaguardApplication.getContext().getString(R.string.week_two);
            case MONTH:
                return DiaguardApplication.getContext().getString(R.string.month);
            case YEAR:
                return DiaguardApplication.getContext().getString(R.string.year);
            default:
                return super.toString();
        }
    }
}
