package com.faltenreich.diaguard.shared.data.database.dao;

import androidx.annotation.Nullable;

import com.faltenreich.diaguard.shared.data.database.entity.Tag;

import java.util.List;

public interface TagDao {

    Long create(Tag tag);

    void update(Tag tag);

    List<Tag> getAll();

    @Nullable
    Tag getById(long id);

    @Nullable
    Tag getByName(String name);

    int delete(Tag tag);

    void deleteAll();
}
