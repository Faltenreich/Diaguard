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

   private final EntryTagDao dao = Database.getInstance().getDatabase().entryTagDao();

   public List<EntryTag> getByEntry(Entry entry) {
      return dao.getByEntry(entry);
   }

   public List<EntryTag> getByTag(Tag tag) {
      return dao.getByTag(tag);
   }

   public long countByTag(Tag tag) {
      return dao.countByTag(tag);
   }

   public EntryTag createOrUpdate(EntryTag entryTag) {
      long id = dao.createOrUpdate(entryTag);
      entryTag.setId(id);
      return entryTag;
   }

   public void createOrUpdate(List<EntryTag> entryTags) {
      dao.createOrUpdate(entryTags);
   }

   public void delete(List<EntryTag> entryTags) {
      dao.delete(entryTags);
   }

   public void deleteByEntry(Entry entry) {
      dao.deleteByEntry(entry);
   }
}
