package com.faltenreich.diaguard.helpers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.NewEventActivity;
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
    public static final String DB_FORMAT_DATE_DB = "yyyy-MM-dd HH:mm:ss";
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

    public static DateTimeFormatter getDateDatabaseFormat() {
        return DateTimeFormat.forPattern(DB_FORMAT_DATE_DB);
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

    public static float getDPI(Context context, float pixels) {
        return pixels * (context.getResources().getDisplayMetrics().densityDpi / 160);
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

    public static String toStringDelimited(String[] array, char delimiter) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : array) {
            stringBuilder.append(string);
            stringBuilder.append(delimiter);
        }
        stringBuilder.deleteCharAt(stringBuilder.length()-1);
        return stringBuilder.toString();
    }

    public static void vibrate(Context context, int timeInMilliseconds) {
        Vibrator vibrator = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
        vibrator.vibrate(timeInMilliseconds);
    }

    public static void playSound(Context context, int resourceId) {
        try {
            MediaPlayer mediaPlayer = MediaPlayer.create(context.getApplicationContext(), resourceId);
            if(mediaPlayer != null) {
                mediaPlayer.setLooping(false);
                mediaPlayer.start();
            }
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }
        catch (IllegalStateException e)
        {
            e.printStackTrace();
        }
    }

    public static void notify(Context context, int resourceId) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(context.getString(R.string.app_name))
                        .setContentText(context.getString(resourceId));
        Intent resultIntent = new Intent(context, NewEventActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(NewEventActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // TODO: Set ID for every alarm?
        mNotificationManager.notify(0, mBuilder.build());
    }
}
