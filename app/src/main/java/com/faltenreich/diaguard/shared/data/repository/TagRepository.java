package com.faltenreich.diaguard.shared.data.repository;

import androidx.annotation.Nullable;

import com.faltenreich.diaguard.shared.data.database.Database;
import com.faltenreich.diaguard.shared.data.database.dao.TagDao;
import com.faltenreich.diaguard.shared.data.database.entity.Tag;

import java.util.List;

public class TagRepository implements DateTimeValidator<Tag> {

   private static TagRepository instance;

   public static TagRepository getInstance() {
      if (instance == null) {
         instance = new TagRepository();
      }
      return instance;
   }

   private final TagDao dao = Database.getInstance().getDatabase().tagDao();

   public Tag createOrUpdate(Tag tag) {
      invalidateDateTime(tag);
      long id = dao.createOrUpdate(tag);
      tag.setId(id);
      return tag;
   }

   public void createOrUpdate(List<Tag> tags) {
      invalidateDateTime(tags);
      dao.createOrUpdate(tags);
   }

   public List<Tag> getAll() {
      return dao.getAll();
   }

   @Nullable
   public Tag getById(long id) {
      return dao.getById(id);
   }

   @Nullable
   public Tag getByName(String name) {
      return dao.getByName(name);
   }

   public int delete(Tag tag) {
      return dao.delete(tag);
   }
}
