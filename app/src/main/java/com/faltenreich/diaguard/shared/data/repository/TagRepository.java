package com.faltenreich.diaguard.shared.data.repository;

import androidx.annotation.Nullable;

import com.faltenreich.diaguard.shared.data.database.dao.TagDao;
import com.faltenreich.diaguard.shared.data.database.dao.TagOrmLiteDao;
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

   private final TagDao tagDao = TagOrmLiteDao.getInstance();

   public List<Tag> getAll() {
      return tagDao.getAll();
   }

   @Nullable
   public Tag getById(long id) {
      return tagDao.getById(id);
   }

   @Nullable
   public Tag getByName(String name) {
      return tagDao.getByName(name);
   }

   public Tag createOrUpdate(Tag tag) {
      if (tag.getId() <= 0) {
         long id = tagDao.create(tag);
         tag.setId(id);
      } else {
         tagDao.update(tag);
      }
      return tag;
   }

   public void bulkCreateOrUpdate(List<Tag> tags) {
      // TODO: Use @Insert with replacement strategy instead
      for (Tag tag : tags) {
         createOrUpdate(tag);
      }
   }

   public int delete(Tag tag) {
      return tagDao.delete(tag);
   }

   public void deleteAll() {
      tagDao.deleteAll();
   }
}
