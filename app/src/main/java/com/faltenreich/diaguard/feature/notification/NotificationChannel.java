package com.faltenreich.diaguard.feature.notification;

import android.os.Build;

import androidx.annotation.StringRes;
import androidx.core.app.NotificationManagerCompat;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.config.ApplicationConfig;

public enum NotificationChannel {

   ALARM("alarm", R.string.alarm, NotificationManagerCompat.IMPORTANCE_DEFAULT, false),
   CGM("cgm", R.string.cgm, NotificationManagerCompat.IMPORTANCE_LOW, true);

   private final String id;
   private final int labelRes;
   private final int importance;
   private final boolean isOngoing;

   NotificationChannel(
       String id,
       @StringRes int labelRes,
       int importance,
       boolean isOngoing
   ) {
      this.id = id;
      this.labelRes = labelRes;
      this.importance = importance;
      this.isOngoing = isOngoing;
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

   public boolean isOngoing() {
      return isOngoing;
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
