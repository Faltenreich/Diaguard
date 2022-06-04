package com.faltenreich.diaguard.shared.data.repository;

import androidx.annotation.Nullable;

import com.faltenreich.diaguard.shared.data.database.Database;
import com.faltenreich.diaguard.shared.data.database.dao.TagDao;
import com.faltenreich.diaguard.shared.data.database.entity.Tag;

import java.util.List;

public class TagRepository {

   private static TagRepository instance;

   public static TagRepository getInstance() {
      if (instance == null) {
         instance = new TagRepository();
      }
      return instance;
   }

   private final TagDao dao = Database.getInstance().getDatabase().tagDao();

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

   public Tag createOrUpdate(Tag tag) {
      if (tag.getId() <= 0) {
         long id = dao.create(tag);
         tag.setId(id);
      } else {
         dao.update(tag);
      }
      return tag;
   }

   public void createOrUpdate(List<Tag> tags) {
      for (Tag tag : tags) {
         createOrUpdate(tag);
      }
   }

   public int delete(Tag tag) {
      return dao.delete(tag);
   }
}
