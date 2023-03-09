package com.faltenreich.diaguard.feature.cgm;

import com.faltenreich.diaguard.shared.data.database.dao.EntryDao;
import com.faltenreich.diaguard.shared.data.database.dao.MeasurementDao;
import com.faltenreich.diaguard.shared.data.database.entity.BloodSugar;
import com.faltenreich.diaguard.shared.data.database.entity.Entry;
import com.faltenreich.diaguard.shared.event.Events;
import com.faltenreich.diaguard.shared.event.data.EntryAddedEvent;

@SuppressWarnings({"rawtypes", "unchecked"})
public class CgmRepository {

   private final EntryDao entryDao = EntryDao.getInstance();
   private final MeasurementDao measurementDao =  MeasurementDao.getInstance(BloodSugar.class);
   private final CgmMapper mapper = new CgmMapper();

   public void store(CgmData cgmData) {
      Entry entry = mapper.mapEntry(cgmData);
      entryDao.createOrUpdate(entry);

      BloodSugar bloodSugar = mapper.mapBloodSugar(cgmData);
      bloodSugar.setEntry(entry);
      measurementDao.createOrUpdate(bloodSugar);

      Events.post(new EntryAddedEvent(entry, null, null));
   }
}
