package com.faltenreich.diaguard.util;

import android.support.annotation.StringRes;

import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.R;

import org.joda.time.DateTime;

/**
 * Created by Faltenreich on 22.03.2016.
 */
public enum TimeSpan {

    WEEK(R.string.week),
    TWO_WEEKS(R.string.week_two),
    MONTH(R.string.month),
    YEAR(R.string.year);

    private int stringRes;

    TimeSpan(@StringRes int stringRes) {
        this.stringRes = stringRes;
    }

    public String toLocalizedString() {
        return DiaguardApplication.getContext().getString(stringRes);
    }

    public DateTime getPreviousInterval(DateTime dateTime, int step) {
        switch (this) {
            case WEEK:
                return dateTime.minusDays(step);
            case TWO_WEEKS:
                return dateTime.minusDays(step * 2);
            case MONTH:
                return dateTime.minusWeeks(step);
            case YEAR:
                return dateTime.minusMonths(step);
            default:
                return dateTime.minusDays(step);
        }
    }

    public DateTime getNextInterval(DateTime dateTime, int step) {
        switch (this) {
            case WEEK:
                return dateTime.plusDays(step);
            case TWO_WEEKS:
                return dateTime.plusDays(step * 2);
            case MONTH:
                return dateTime.plusWeeks(step);
            case YEAR:
                return dateTime.plusMonths(step);
            default:
                return dateTime.plusDays(step);
        }
    }
}
