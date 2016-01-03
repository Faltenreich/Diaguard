package com.faltenreich.diaguard.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.dao.MeasurementDao;
import com.faltenreich.diaguard.data.entity.BloodSugar;
import com.faltenreich.diaguard.data.entity.Entry;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.Interval;
import org.joda.time.Minutes;

/**
 * Created by Filip on 22.12.2014.
 */
public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

    public static final int ALARM_ID = 1337;
    public static final int VIBRATION_TIME_IN_SECONDS = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        // Get date of latest blood sugar measurement
        Entry lastMeasurement = EntryDao.getInstance().getLatestWithMeasurement(BloodSugar.class);

        String message = context.getString(R.string.alarm_desc_first);
        if(lastMeasurement != null) {
            // Calculate how long the last measurement has been ago
            Interval interval = new Interval(lastMeasurement.getDate(), DateTime.now());
            if (Minutes.minutesIn(interval).getMinutes() < 120) {
                message = String.format(
                        context.getString(R.string.alarm_desc),
                        Integer.toString(Minutes.minutesIn(interval).getMinutes()) + " " +
                                context.getString(R.string.minutes));
            } else {
                message = String.format(
                        context.getString(R.string.alarm_desc),
                        Integer.toString(Hours.hoursIn(interval).getHours()) + " " +
                                context.getString(R.string.hours));
            }
        }
        Helper.notify(context, context.getString(R.string.alarm), message);

        if(PreferenceHelper.getInstance().isSoundAllowed()) {
            Helper.playSound(context);
        }
        if(PreferenceHelper.getInstance().isVibrationAllowed()) {
            Helper.vibrate(context, 1000 * VIBRATION_TIME_IN_SECONDS);
        }
    }
}