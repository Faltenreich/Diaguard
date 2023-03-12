package com.faltenreich.diaguard.feature.cgm;

import android.content.Context;

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
      float value = preferenceStore.formatDefaultToCustomUnit(Category.BLOODSUGAR, bloodSugar.getMgDl());
      String title = String.format("%s %s %s",
          FloatUtils.parseFloat(value),
          preferenceStore.getUnitAcronym(Category.BLOODSUGAR),
          bloodSugar.getTrend() != null ? bloodSugar.getTrend().unicodeIcon : ""
      );
      NotificationUtils.updateOngoingNotification(context, title);
   }
}
