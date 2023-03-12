package com.faltenreich.diaguard.feature.cgm.xdrip;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.faltenreich.diaguard.feature.cgm.CgmData;
import com.faltenreich.diaguard.feature.cgm.CgmAlarm;
import com.faltenreich.diaguard.shared.data.database.entity.BloodSugar;

import org.joda.time.DateTime;

class XDripMapper {

   private static final String TAG = XDripMapper.class.getSimpleName();

   private static final String ACTION = "glucodata.Minute";
   private static final String ALARM_BIT_MASK = "glucodata.Minute.Alarm";
   private static final String GLUCOSE_IN_MG_DL = "glucodata.Minute.mgdl";
   private static final String RATE_OF_CHANGE = "glucodata.Minute.Rate";
   private static final String SENSOR_SERIAL_NUMBER = "glucodata.Minute.SerialNumber";
   private static final String TIME_IN_MILLIS = "glucodata.Minute.Time";

   @Nullable
   CgmData mapData(Intent intent) {
      String action = intent.getAction();
      if (action == null || !action.equals(ACTION) || intent.getExtras() == null) {
         Log.e(TAG, "Ignoring action: " + action);
         return null;
      }
      Bundle extras = intent.getExtras();

      String sensorSerialNumber = extras.getString(SENSOR_SERIAL_NUMBER);
      long timeInMillis = extras.getLong(TIME_IN_MILLIS);
      if (sensorSerialNumber == null || timeInMillis == 0) {
         return null;
      }
      DateTime dateTime = new DateTime(timeInMillis);
      int glucoseInMgDl = extras.getInt(GLUCOSE_IN_MG_DL);
      BloodSugar.Trend trend = mapTrend(extras.getFloat(RATE_OF_CHANGE));
      CgmAlarm alarmIndicator = mapAlarm(extras.getInt(ALARM_BIT_MASK));

      return new CgmData(
          sensorSerialNumber,
          dateTime,
          glucoseInMgDl,
          trend,
          alarmIndicator
      );
   }

   @Nullable
   private BloodSugar.Trend mapTrend(float rateOfChange) {
      if (rateOfChange <= -2.0f) {
         return BloodSugar.Trend.FALLING_QUICKLY;
      }
      if (rateOfChange <= -1.0f) {
         return BloodSugar.Trend.FALLING;
      }
      if (rateOfChange <= 1.0f) {
         return BloodSugar.Trend.STEADY;
      }
      if (rateOfChange <= 2.0f) {
         return BloodSugar.Trend.RISING;
      }
      if (Float.isNaN(rateOfChange)) {
         return null;
      }
      return BloodSugar.Trend.RISING_QUICKLY;
   }

   @Nullable
   private CgmAlarm mapAlarm(int alarmBitMask) {
      boolean showAlarm = (alarmBitMask & 8) != 0;
      switch (alarmBitMask & 7) {
         case 4: return CgmAlarm.HIGHEST;
         case 5: return CgmAlarm.LOWEST;
         case 6: return CgmAlarm.TOO_HIGH;
         case 7: return CgmAlarm.TOO_LOW;
         default: return null;
      }
   }
}
