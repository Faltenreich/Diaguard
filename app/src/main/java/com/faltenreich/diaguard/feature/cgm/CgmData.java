package com.faltenreich.diaguard.feature.cgm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.faltenreich.diaguard.shared.Helper;

import org.joda.time.DateTime;

public class CgmData {

   @NonNull private final String sensorSerialNumber;
   @NonNull private final DateTime dateTime;
   private final int glucoseInMgDl;
   @Nullable private final CgmTrend trend;
   @Nullable private final CgmAlarm alarm;

   public CgmData(
       @NonNull String sensorSerialNumber,
       @NonNull DateTime dateTime,
       int glucoseInMgDl,
       @Nullable CgmTrend trend,
       @Nullable CgmAlarm alarm
    ) {
      this.sensorSerialNumber = sensorSerialNumber;
      this.dateTime = dateTime;
      this.glucoseInMgDl = glucoseInMgDl;
      this.trend = trend;
      this.alarm = alarm;
   }

   @NonNull
   public String getSensorSerialNumber() {
      return sensorSerialNumber;
   }

   @NonNull
   public DateTime getDateTime() {
      return dateTime;
   }

   public int getGlucoseInMgDl() {
      return glucoseInMgDl;
   }

   @Nullable
   public CgmTrend getTrend() {
      return trend;
   }

   @Nullable
   public CgmAlarm getAlarm() {
      return alarm;
   }

   @NonNull
   @Override
   public String toString() {
      return String.format(
          Helper.getLocale(),
          "%d mg/dL (%s)",
          glucoseInMgDl,
          getTrend()
      );
   }
}
