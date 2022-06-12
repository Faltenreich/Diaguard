package com.faltenreich.diaguard.shared.data.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import com.faltenreich.diaguard.feature.export.Backupable;
import com.faltenreich.diaguard.shared.data.database.Database;

@Entity(
    foreignKeys = {
        /*
        @ForeignKey(
            entity = Entry.class,
            parentColumns = {BaseEntity.Column.ID},
            childColumns = {EntryTag.Column.ENTRY_ID},
            onDelete = ForeignKey.CASCADE
        ),
         */
        @ForeignKey(
            entity = Tag.class,
            parentColumns = {BaseEntity.Column.ID},
            childColumns = {EntryTag.Column.TAG_ID},
            onDelete = ForeignKey.CASCADE
        )
    }
)
public class EntryTag extends BaseEntity implements Backupable {

    public static final String BACKUP_KEY = "entryTag";

    public class Column extends BaseEntity.Column {
        public static final String ENTRY = "entry";
        public static final String ENTRY_ID = "entryId";
        public static final String TAG = "tag";
        public static final String TAG_ID = "tagId";
    }

    @ColumnInfo(name = EntryTag.Column.ENTRY_ID)
    private long entryId;

    @ColumnInfo(name = Column.TAG_ID)
    private long tagId;

    public long getEntryId() {
        return entryId;
    }

    public void setEntryId(long entryId) {
        this.entryId = entryId;
    }

    @Deprecated
    public Entry getEntry() {
        throw new UnsupportedOperationException();
        // return Database.getInstance().getDatabase().entryDao().getById(entryId);
    }

    public long getTagId() {
        return tagId;
    }

    public void setTagId(long tagId) {
        this.tagId = tagId;
    }

    @Deprecated
    public Tag getTag() {
        return Database.getInstance().getDatabase().tagDao().getById(tagId);
    }

    @Override
    public String getKeyForBackup() {
        return BACKUP_KEY;
    }

    @Override
    public String[] getValuesForBackup() {
        throw new UnsupportedOperationException();
        // return new String[]{tag.getName()};
    }
}
