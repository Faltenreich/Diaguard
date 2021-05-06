package com.faltenreich.diaguard.feature.datetime;

import android.content.Context;

import androidx.annotation.StringRes;

import com.faltenreich.diaguard.R;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Interval;

/**
 * Created by Faltenreich on 22.03.2016.
 */
public enum TimeSpan {

    WEEK(R.string.week, DateTimeConstants.DAYS_PER_WEEK),
    MONTH(R.string.month, 4),
    QUARTER(R.string.quarter, 3),
    YEAR(R.string.year, 12, 6);

    private final int intervalStringResId;
    public final int stepsPerInterval;
    public final int stepsPerIntervalForUi;

    TimeSpan(@StringRes int intervalStringResId, int stepsPerInterval, int stepsPerIntervalForUi) {
        this.intervalStringResId = intervalStringResId;
        this.stepsPerInterval = stepsPerInterval;
        this.stepsPerIntervalForUi = stepsPerIntervalForUi;
    }

    TimeSpan(@StringRes int intervalStringResId, int stepsPerInterval) {
        this(intervalStringResId, stepsPerInterval, stepsPerInterval);
    }

    public String toIntervalLabel(Context context) {
        return context.getString(intervalStringResId);
    }

    public Interval getInterval(DateTime dateTime, int add) {
        switch (this) {
            case WEEK:
                return new Interval(dateTime.plusWeeks(add), dateTime);
            case MONTH:
                return new Interval(dateTime.plusMonths(add), dateTime);
            case QUARTER:
                return new Interval(dateTime.plusMonths(add * 3), dateTime);
            case YEAR:
                return new Interval(dateTime.plusYears(add), dateTime);
            default:
                throw new IllegalArgumentException("Unknown enum value: " + this.toString());
        }
    }

    public DateTime getStep(DateTime dateTime, int add) {
        switch (this) {
            case WEEK:
                return dateTime.plusDays(add);
            case MONTH:
                return dateTime.plusWeeks(add);
            case QUARTER:
            case YEAR:
                return dateTime.plusMonths(add);
            default:
                throw new IllegalArgumentException("Unknown enum value: " + this.toString());
        }
    }

    public String getLabel(DateTime dateTime) {
        switch (this) {
            case WEEK:
                return DateTimeUtils.toWeekDayShort(dateTime);
            case MONTH:
                return dateTime.toString("w");
            case QUARTER:
            case YEAR:
                return dateTime.toString("MMM");
            default:
                return dateTime.toString();
        }
    }
}
