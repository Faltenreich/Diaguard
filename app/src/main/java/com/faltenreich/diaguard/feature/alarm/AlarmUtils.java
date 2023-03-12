package com.faltenreich.diaguard.feature.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.notification.NotificationUtils;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.data.database.dao.EntryDao;
import com.faltenreich.diaguard.shared.data.database.entity.BloodSugar;
import com.faltenreich.diaguard.shared.data.database.entity.Entry;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.Interval;
import org.joda.time.Minutes;

/**
 * Created by Faltenreich on 26.06.2016.
 */
public class AlarmUtils {

    private static final int ALARM_ID = 34248273;

    private static Context getContext() {
        return DiaguardApplication.getContext();
    }

    private static AlarmManager getAlarmManager() {
        return (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
    }

    private static PendingIntent getPendingIntent() {
        Intent intent = new Intent(getContext(), AlarmBroadcastReceiver.class);
        int flags = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
            ? PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            : PendingIntent.FLAG_UPDATE_CURRENT;
        return PendingIntent.getBroadcast(getContext(), ALARM_ID, intent, flags);
    }

    public static void setAlarm(long intervalInMillis) {
        long alarmStartInMillis = System.currentTimeMillis() + intervalInMillis;
        PreferenceStore.getInstance().setAlarmStartInMillis(alarmStartInMillis);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getAlarmManager().setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    alarmStartInMillis,
                    getPendingIntent());
        } else {
            getAlarmManager().set(AlarmManager.RTC_WAKEUP,
                    alarmStartInMillis,
                    getPendingIntent());
        }
    }

    public static long getAlarmInMillis() {
        return PreferenceStore.getInstance().getAlarmStartInMillis();
    }

    public static void stopAlarm() {
        PreferenceStore.getInstance().setAlarmStartInMillis(-1);
        getAlarmManager().cancel(getPendingIntent());
    }

    public static boolean isAlarmSet() {
        return new DateTime(PreferenceStore.getInstance().getAlarmStartInMillis()).isAfterNow();
    }

    static void executeAlarm(Context context) {
        PreferenceStore.getInstance().setAlarmStartInMillis(-1);
        NotificationUtils.showNotification(context, R.string.alarm, getMessageForMeasurement());
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
