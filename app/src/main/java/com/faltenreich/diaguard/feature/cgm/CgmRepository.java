package com.faltenreich.diaguard.feature.cgm;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;

import com.faltenreich.diaguard.feature.notification.NotificationUtils;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.data.database.dao.EntryDao;
import com.faltenreich.diaguard.shared.data.database.dao.MeasurementDao;
import com.faltenreich.diaguard.shared.data.database.entity.BloodSugar;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Entry;
import com.faltenreich.diaguard.shared.data.primitive.FloatUtils;
import com.faltenreich.diaguard.shared.event.Events;
import com.faltenreich.diaguard.shared.event.data.EntryAddedEvent;
import com.faltenreich.diaguard.shared.image.BitmapUtils;

@SuppressWarnings({"rawtypes", "unchecked"})
public class CgmRepository {

   private final EntryDao entryDao = EntryDao.getInstance();
   private final MeasurementDao measurementDao =  MeasurementDao.getInstance(BloodSugar.class);
   private final PreferenceStore preferenceStore = PreferenceStore.getInstance();

   private static CgmRepository instance;

   public static CgmRepository getInstance() {
      if (instance == null) {
         instance = new CgmRepository();
      }
      return instance;
   }

   private CgmRepository() {}

   public void storeData(Context context, CgmData cgmData) {
      Entry entry = new Entry();
      entry.setDate(cgmData.getDateTime());
      entry.setSource(Entry.Source.CGM);
      entryDao.createOrUpdate(entry);

      BloodSugar bloodSugar = new BloodSugar();
      bloodSugar.setMgDl(cgmData.getGlucoseInMgDl());
      bloodSugar.setTrend(cgmData.getTrend());
      bloodSugar.setEntry(entry);
      measurementDao.createOrUpdate(bloodSugar);

      Events.post(new EntryAddedEvent(entry, null, null));

      showNotificationIfNeeded(context, bloodSugar);
   }

   public void invalidateNotification(Context context) {
      if (preferenceStore.showCgmNotification()) {
         // TODO: where source equals CGM
         Entry latestEntry = entryDao.getLatestWithMeasurement(BloodSugar.class);
         if (latestEntry != null) {
            BloodSugar bloodSugar = (BloodSugar) MeasurementDao.getInstance(BloodSugar.class).getMeasurement(latestEntry);
            if (bloodSugar != null) {
               showNotificationIfNeeded(context, bloodSugar);
            }
         }
      } else {
         hideNotification(context);
      }
   }

   public void showNotificationIfNeeded(Context context, BloodSugar bloodSugar) {
      if (preferenceStore.showCgmNotification()) {
         showNotification(context, bloodSugar);
      }
   }

   private void showNotification(Context context, BloodSugar bloodSugar) {
      float mgDl = bloodSugar.getMgDl();
      float mgDlNormalized = preferenceStore.formatDefaultToCustomUnit(Category.BLOODSUGAR, mgDl);
      String mgDlAsText = FloatUtils.parseFloat(mgDlNormalized);
      String title = bloodSugar.getTrend() != null
          ? String.format("%s %s", mgDlAsText, bloodSugar.getTrend().unicodeIcon)
          : mgDlAsText;

      // TODO: Check dimensions
      final int width = (int) context.getResources().getDimension(android.R.dimen.notification_large_icon_width);
      final int height = (int) context.getResources().getDimension(android.R.dimen.notification_large_icon_height);
      Bitmap icon = BitmapUtils.createBitmapFromText(mgDlAsText, width, Color.WHITE);
      NotificationUtils.updateOngoingNotification(context, title, icon);
   }

   private void hideNotification(Context context) {
      NotificationUtils.hideOngoingNotification(context);
   }
}
