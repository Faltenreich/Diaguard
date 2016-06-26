package com.faltenreich.diaguard.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.entity.BloodSugar;
import com.faltenreich.diaguard.data.entity.Entry;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Hours;
import org.joda.time.Interval;
import org.joda.time.Minutes;

/**
 * Created by Faltenreich on 26.06.2016.
 */
public class AlarmUtils {

    private static final int ALARM_ID = 34248273;
    private static final int VIBRATION_DURATION_IN_MILLIS = DateTimeConstants.MILLIS_PER_SECOND;

    private static Context getContext() {
        return DiaguardApplication.getContext();
    }

    private static AlarmManager getAlarmManager() {
        return (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
    }

    private static PendingIntent getPendingIntent() {
        Intent intent = new Intent(getContext(), AlarmBroadcastReceiver.class);
        return PendingIntent.getBroadcast(getContext(), ALARM_ID, intent, 0);
    }

    public static void setAlarm(long intervalInMillis) {
        long alarmStartInMillis = System.currentTimeMillis() + intervalInMillis;
        PreferenceHelper.getInstance().setAlarmStartInMillis(alarmStartInMillis);
        getAlarmManager().set(AlarmManager.RTC_WAKEUP,
                alarmStartInMillis,
                getPendingIntent());
    }

    public static long getAlarmInMillis() {
        return PreferenceHelper.getInstance().getAlarmStartInMillis();
    }

    public static void stopAlarm() {
        PreferenceHelper.getInstance().setAlarmStartInMillis(-1);
        getAlarmManager().cancel(getPendingIntent());
    }

    public static boolean isAlarmSet() {
        return new DateTime(PreferenceHelper.getInstance().getAlarmStartInMillis()).isAfterNow();
    }

    public static void executeAlarm() {
        PreferenceHelper.getInstance().setAlarmStartInMillis(-1);
        SystemUtils.showNotification(R.string.alarm, getMessageForMeasurement());

        if (PreferenceHelper.getInstance().isSoundAllowed()) {
            SystemUtils.playSound();
        }

        if (PreferenceHelper.getInstance().isVibrationAllowed()) {
            SystemUtils.vibrate(VIBRATION_DURATION_IN_MILLIS);
        }
    }

    private static String getMessageForMeasurement() {
        Context context = DiaguardApplication.getContext();
        Entry lastMeasurement = EntryDao.getInstance().getLatestWithMeasurement(BloodSugar.class);
        String message = context.getString(R.string.alarm_desc_first);
        if (lastMeasurement != null) {
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
        return message;
    }
}
