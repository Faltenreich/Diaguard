package com.faltenreich.diaguard.shared.data.database.dao.room;

import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.faltenreich.diaguard.shared.data.database.dao.TagDao;
import com.faltenreich.diaguard.shared.data.database.entity.Tag;

import java.util.List;

@Dao
public interface TagRoomDao extends TagDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long createOrUpdate(Tag tag);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void createOrUpdate(List<Tag> tags);

    @Query("SELECT * FROM Tag ORDER BY updatedAt DESC")
    List<Tag> getAll();

    @Nullable
    @Query("SELECT * FROM Tag WHERE _id = :id")
    Tag getById(long id);

    @Nullable
    @Query("SELECT * FROM Tag WHERE name = :name")
    Tag getByName(String name);

    @Delete
    int delete(Tag tag);
}
