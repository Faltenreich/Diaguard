package com.faltenreich.diaguard.shared.data.repository;

import com.faltenreich.diaguard.shared.data.database.entity.BaseEntity;

import org.joda.time.DateTime;

import java.util.Collection;

public interface DateTimeValidator <T extends BaseEntity> {

   default void invalidateDateTime(T entity) {
      DateTime dateTime = DateTime.now();
      if (!entity.isPersisted()) {
         entity.setCreatedAt(dateTime);
      }
      entity.setUpdatedAt(dateTime);
   }

   default void invalidateDateTime(Collection<T> entities) {
      for (T entity : entities) {
         invalidateDateTime(entity);
      }
   }
}
