package com.faltenreich.diaguard.util;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.ui.activity.NewEventActivity;
import com.faltenreich.diaguard.R;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Format;
import java.text.SimpleDateFormat;

/**
 * Created by Filip on 10.12.13.
 */
public class Helper {

    public static final String DB_FORMAT_TIME = "HH:mm";
    public static final String PLACEHOLDER = "-";

    public static DecimalFormat getDecimalFormat() {
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        decimalFormat.setDecimalFormatSymbols(symbols);
        return decimalFormat;
    }

    public static DecimalFormat getRationalFormat() {
        return new DecimalFormat("#");
    }

    private static String getTimePattern() {
        return DB_FORMAT_TIME;
    }

    private static String getDatePattern() {
        Format dateFormat = android.text.format.DateFormat.getDateFormat(DiaguardApplication.getContext().getApplicationContext());
        return ((SimpleDateFormat)dateFormat).toLocalizedPattern();
    }

    public static DateTimeFormatter getDateFormat() {
        return DateTimeFormat.forPattern(getDatePattern());
    }

    public static DateTimeFormatter getTimeFormat() {
        return DateTimeFormat.forPattern(getTimePattern());
    }

    public static float formatCalendarToHourMinutes(DateTime time) {
        int hour = time.getHourOfDay();
        int minute = time.getMinuteOfHour();
        return hour + ((float)minute / 60.0f);
    }

    public static float getDPI(float pixels) {
        return pixels * (DiaguardApplication.getContext().getResources().getDisplayMetrics().densityDpi / 160);
    }

    public static String getTextAgo(Context context, int differenceInMinutes) {
        if(differenceInMinutes < 2) {
            return context.getString(R.string.latest_moments);
        }

        String textAgo = context.getString(R.string.latest);

        if(differenceInMinutes > 2879) {
            differenceInMinutes = differenceInMinutes / 60 / 24;
            textAgo = textAgo.replace("[unit]", context.getString(R.string.days));
        }
        else if(differenceInMinutes > 119) {
            differenceInMinutes = differenceInMinutes / 60;
            textAgo = textAgo.replace("[unit]", context.getString(R.string.hours));
        }
        else {
            textAgo = textAgo.replace("[unit]", context.getString(R.string.minutes));
        }

        return  textAgo.replace("[value]", Integer.toString(differenceInMinutes));
    }

    public static int colorBrighten(int color, float percent) {
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        return Color.rgb((int) (r * percent), (int) (g * percent), (int) (b * percent));
    }

    public static String toStringDelimited(String[] array, char delimiter) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : array) {
            stringBuilder.append(string);
            stringBuilder.append(delimiter);
        }
        stringBuilder.deleteCharAt(stringBuilder.length()-1);
        return stringBuilder.toString();
    }

    public static void setAlarm(Context context, int intervalInMinutes) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, AlarmManagerBroadcastReceiver.ALARM_ID, intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + 1000 * 60 * intervalInMinutes,
                pendingIntent);
    }

    public static void stopAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, AlarmManagerBroadcastReceiver.ALARM_ID, intent, 0);
        alarmManager.cancel(pendingIntent);
    }

    public static void vibrate(Context context, int timeInMilliseconds) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(timeInMilliseconds);
    }

    public static void playSound(Context context) {
        try {
            MediaPlayer mediaPlayer = MediaPlayer.create(
                    context.getApplicationContext(),
                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
            if(mediaPlayer != null) {
                mediaPlayer.setLooping(false);
                mediaPlayer.start();
            }
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }
    }

    public static void notify(Context context, String title, String message) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setTicker(title)
                        .setWhen(1000);
        Intent resultIntent = new Intent(context, NewEventActivity.class);

        // Put target activity on back stack on top of its parent to guarantee correct back navigation
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(NewEventActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        // Notification will dismiss when be clicked on
        Notification notification = builder.build();
        notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL | Notification.FLAG_ONLY_ALERT_ONCE;

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(AlarmManagerBroadcastReceiver.ALARM_ID, notification);
    }
}
