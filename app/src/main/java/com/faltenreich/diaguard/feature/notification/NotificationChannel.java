package com.faltenreich.diaguard.feature.notification;

import android.os.Build;

import androidx.annotation.StringRes;
import androidx.core.app.NotificationManagerCompat;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.config.ApplicationConfig;

public enum NotificationChannel {

   ALARM("alarm", R.string.alarm, NotificationManagerCompat.IMPORTANCE_DEFAULT, true),
   CGM("cgm", R.string.cgm, NotificationManagerCompat.IMPORTANCE_MIN, false);

   private final String id;
   private final int labelRes;
   private final int importance;
   private final boolean enableVibration;

   NotificationChannel(
       String id,
       @StringRes int labelRes,
       int importance,
       boolean enableVibration
   ) {
      this.id = id;
      this.labelRes = labelRes;
      this.importance = importance;
      this.enableVibration = enableVibration;
   }

   public String getId() {
      return id;
   }

   @StringRes
   public int getLabelRes() {
      return labelRes;
   }

   public int getImportance() {
      return importance;
   }

   public boolean enableVibration() {
      return enableVibration;
   }

   public boolean isActive() {
      if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
         return false;
      }
      switch (this) {
         case ALARM: return true;
         case CGM: return ApplicationConfig.isCgmSupported();
         default: throw new IllegalArgumentException();
      }
   }
}
