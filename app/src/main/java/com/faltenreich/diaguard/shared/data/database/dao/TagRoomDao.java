package com.faltenreich.diaguard.shared.data.database.dao;

import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.faltenreich.diaguard.shared.data.database.entity.Tag;

import java.util.List;

@Dao
public interface TagRoomDao extends TagDao {

   @Override
   @Insert
   Long create(Tag tag);

   @Override
   @Update
   void update(Tag tag);

   @Query("SELECT * FROM Tag")
   List<Tag> getAll();

   @Nullable
   @Query("SELECT * FROM Tag WHERE name = :name")
   Tag getByName(String name);

   @Nullable
   @Query("SELECT * FROM Tag WHERE _id = :id")
   Tag getById(long id);

   @Delete
   int delete(Tag tag);

   @Query("DELETE FROM Tag")
   void deleteAll();
}
