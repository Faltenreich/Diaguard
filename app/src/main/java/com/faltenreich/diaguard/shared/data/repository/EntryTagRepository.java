package com.faltenreich.diaguard.shared.data.repository;

import com.faltenreich.diaguard.shared.data.database.Database;
import com.faltenreich.diaguard.shared.data.database.dao.EntryTagDao;
import com.faltenreich.diaguard.shared.data.database.entity.Entry;
import com.faltenreich.diaguard.shared.data.database.entity.EntryTag;
import com.faltenreich.diaguard.shared.data.database.entity.Tag;

import java.util.List;

public class EntryTagRepository {

   private static EntryTagRepository instance;

   public static EntryTagRepository getInstance() {
      if (instance == null) {
         instance = new EntryTagRepository();
      }
      return instance;
   }

   private final EntryTagDao entryTagDao = Database.getInstance().getDatabase().entryTagDao();

   public List<EntryTag> getByEntry(Entry entry) {
      return entryTagDao.getByEntry(entry);
   }

   public List<EntryTag> getByTag(Tag tag) {
      return entryTagDao.getByTag(tag);
   }

   public long countByTag(Tag tag) {
      return entryTagDao.countByTag(tag);
   }

   public EntryTag createOrUpdate(EntryTag entryTag) {
      if (entryTag.getId() <= 0) {
         long id = entryTagDao.create(entryTag);
         entryTag.setId(id);
      } else {
         entryTagDao.update(entryTag);
      }
      return entryTag;
   }

   public void createOrUpdate(List<EntryTag> entryTags) {
      for (EntryTag entryTag : entryTags) {
         createOrUpdate(entryTag);
      }
   }

   public void delete(List<EntryTag> entryTags) {
      entryTagDao.delete(entryTags);
   }

   public void deleteByEntry(Entry entry) {
      entryTagDao.deleteByEntry(entry);
   }
}
