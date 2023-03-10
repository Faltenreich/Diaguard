package com.faltenreich.diaguard.feature.cgm;

import android.content.Context;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.shared.data.database.dao.EntryDao;
import com.faltenreich.diaguard.shared.data.database.dao.EntryTagDao;
import com.faltenreich.diaguard.shared.data.database.dao.MeasurementDao;
import com.faltenreich.diaguard.shared.data.database.dao.TagDao;
import com.faltenreich.diaguard.shared.data.database.entity.BloodSugar;
import com.faltenreich.diaguard.shared.data.database.entity.Entry;
import com.faltenreich.diaguard.shared.data.database.entity.EntryTag;
import com.faltenreich.diaguard.shared.data.database.entity.Tag;
import com.faltenreich.diaguard.shared.event.Events;
import com.faltenreich.diaguard.shared.event.data.EntryAddedEvent;

import java.util.Collections;

@SuppressWarnings({"rawtypes", "unchecked"})
public class CgmRepository {

   private final EntryDao entryDao = EntryDao.getInstance();
   private final MeasurementDao measurementDao =  MeasurementDao.getInstance(BloodSugar.class);
   private final TagDao tagDao = TagDao.getInstance();
   private final EntryTagDao entryTagDao = EntryTagDao.getInstance();

   public void store(Context context, CgmData cgmData) {
      Entry entry = new Entry();
      entry.setDate(cgmData.getDateTime());
      entryDao.createOrUpdate(entry);

      BloodSugar bloodSugar = new BloodSugar();
      bloodSugar.setMgDl(cgmData.getGlucoseInMgDl());
      bloodSugar.setEntry(entry);
      measurementDao.createOrUpdate(bloodSugar);

      String tagName = context.getString(R.string.cgm);
      Tag tag = tagDao.getByName(tagName);
      if (tag == null) {
         tag = new Tag();
         tag.setName(tagName);
         tagDao.createOrUpdate(tag);
      }

      EntryTag entryTag = new EntryTag();
      entryTag.setEntry(entry);
      entryTag.setTag(tag);
      entryTagDao.createOrUpdate(entryTag);

      Events.post(new EntryAddedEvent(entry, Collections.singletonList(entryTag), null));
   }
}
