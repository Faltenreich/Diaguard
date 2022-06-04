package com.faltenreich.diaguard.shared.data.database.dao;

import android.util.Log;

import com.faltenreich.diaguard.shared.data.database.entity.Entry;
import com.faltenreich.diaguard.shared.data.database.entity.EntryTag;
import com.faltenreich.diaguard.shared.data.database.entity.Tag;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EntryTagOrmLiteDao extends BaseDao<EntryTag> implements EntryTagDao {

    private static final String TAG = EntryTagOrmLiteDao.class.getSimpleName();

    private static EntryTagOrmLiteDao instance;

    public static EntryTagOrmLiteDao getInstance() {
        if (instance == null) {
            instance = new EntryTagOrmLiteDao();
        }
        return instance;
    }

    private EntryTagOrmLiteDao() {
        super(EntryTag.class);
    }

    @Override
    public List<EntryTag> getByEntry(Entry entry) {
        try {
            return getQueryBuilder()
                .orderBy(EntryTag.Column.UPDATED_AT, false)
                .where().eq(EntryTag.Column.ENTRY, entry)
                .query();
        } catch (SQLException exception) {
            Log.e(TAG, exception.toString());
            return new ArrayList<>();
        }
    }

    @Override
    public int deleteByEntry(Entry entry) {
        try {
            DeleteBuilder<EntryTag, Long> builder = getDeleteBuilder();
            builder.where().eq(EntryTag.Column.ENTRY, entry);
            return builder.delete();
        } catch (SQLException exception) {
            Log.e(TAG, exception.toString());
            return 0;
        }
    }

    @Override
    public List<EntryTag> getByTag(Tag tag) {
        try {
            return getQueryBuilder()
                .orderBy(EntryTag.Column.UPDATED_AT, false)
                .where().eq(EntryTag.Column.TAG, tag)
                .query();
        } catch (SQLException exception) {
            Log.e(TAG, exception.toString());
            return new ArrayList<>();
        }
    }

    @Override
    public long count(Tag tag) {
        try {
            return getQueryBuilder()
                .orderBy(EntryTag.Column.UPDATED_AT, false)
                .where().eq(EntryTag.Column.TAG, tag)
                .countOf();
        } catch (SQLException exception) {
            Log.e(TAG, exception.toString());
            return 0;
        }
    }
}
