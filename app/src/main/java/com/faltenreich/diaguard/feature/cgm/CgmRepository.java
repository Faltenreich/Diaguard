package com.faltenreich.diaguard.feature.cgm;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

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

@SuppressWarnings({"rawtypes", "unchecked"})
public class CgmRepository {

   private final EntryDao entryDao = EntryDao.getInstance();
   private final MeasurementDao measurementDao =  MeasurementDao.getInstance(BloodSugar.class);
   private final PreferenceStore preferenceStore = PreferenceStore.getInstance();

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

      updateNotification(context, bloodSugar);
   }

   public void updateNotification(Context context, BloodSugar bloodSugar) {
      float mgDl = bloodSugar.getMgDl();
      float mgDlNormalized = preferenceStore.formatDefaultToCustomUnit(Category.BLOODSUGAR, mgDl);
      String mgDlAsText = FloatUtils.parseFloat(mgDlNormalized);
      String title = String.format("%s %s %s",
          mgDlAsText,
          preferenceStore.getUnitAcronym(Category.BLOODSUGAR),
          bloodSugar.getTrend() != null ? bloodSugar.getTrend().unicodeIcon : ""
      );

      // TODO: Check dimensions
      final int width = (int) context.getResources().getDimension(android.R.dimen.notification_large_icon_width);
      final int height = (int) context.getResources().getDimension(android.R.dimen.notification_large_icon_height);
      Bitmap icon = textAsBitmap(mgDlAsText, width, Color.WHITE);
      NotificationUtils.updateOngoingNotification(context, title, icon);
   }

   public Bitmap textAsBitmap(String text, float textSize, int textColor) {
      Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
      paint.setTextSize(textSize);
      paint.setColor(textColor);
      paint.setTextAlign(Paint.Align.LEFT);
      float baseline = -paint.ascent(); // ascent() is negative
      int width = (int) (paint.measureText(text) + 0.5f); // round
      int height = (int) (baseline + paint.descent() + 0.5f);
      Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
      Canvas canvas = new Canvas(image);
      canvas.drawText(text, 0, baseline, paint);
      return image;
   }
}
