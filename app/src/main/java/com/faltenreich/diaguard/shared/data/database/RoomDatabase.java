package com.faltenreich.diaguard.shared.data.database;

import static com.faltenreich.diaguard.shared.data.database.RoomDatabase.DATABASE_VERSION;

import androidx.room.Database;
import androidx.room.TypeConverters;

import com.faltenreich.diaguard.shared.data.database.converter.DateTimeConverter;
import com.faltenreich.diaguard.shared.data.database.dao.room.EntryTagRoomDao;
import com.faltenreich.diaguard.shared.data.database.dao.room.FoodRoomDao;
import com.faltenreich.diaguard.shared.data.database.dao.room.TagRoomDao;
import com.faltenreich.diaguard.shared.data.database.entity.EntryTag;
import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.data.database.entity.Tag;

@Database(
    entities = {
        Food.class,
        Tag.class,
        EntryTag.class
    },
    version = DATABASE_VERSION
)
@TypeConverters({DateTimeConverter.class})
public abstract class RoomDatabase extends androidx.room.RoomDatabase {

    public static final String DATABASE_NAME = "diaguard.room.db";
    public static final int DATABASE_VERSION = DatabaseVersion.v4_0;

    public abstract FoodRoomDao foodDao();

    public abstract TagRoomDao tagDao();

    public abstract EntryTagRoomDao entryTagDao();
}
