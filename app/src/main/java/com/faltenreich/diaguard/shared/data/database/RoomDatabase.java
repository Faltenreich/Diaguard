package com.faltenreich.diaguard.shared.data.database;

import androidx.room.Database;
import androidx.room.TypeConverters;

import com.faltenreich.diaguard.shared.data.database.converter.DateTimeConverter;
import com.faltenreich.diaguard.shared.data.database.dao.TagRoomDao;
import com.faltenreich.diaguard.shared.data.database.entity.Tag;

@Database(entities = {Tag.class}, version = com.faltenreich.diaguard.shared.data.database.Database.DATABASE_VERSION)
@TypeConverters({DateTimeConverter.class})
public abstract class RoomDatabase extends androidx.room.RoomDatabase {

    public abstract TagRoomDao tagDao();
}
