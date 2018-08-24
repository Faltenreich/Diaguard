package com.faltenreich.diaguard.data.dao;

import android.util.Log;

import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.EntryTag;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EntryTagDao extends BaseDao<EntryTag> {

    private static final String TAG = EntryTagDao.class.getSimpleName();

    private static EntryTagDao instance;

    public static EntryTagDao getInstance() {
        if (instance == null) {
            instance = new EntryTagDao();
        }
        return instance;
    }

    private EntryTagDao() {
        super(EntryTag.class);
    }

    public List<EntryTag> getAll(Entry entry) {
        try {
            return getDao().queryBuilder()
                    .orderBy(EntryTag.Column.UPDATED_AT, false)
                    .where().eq(EntryTag.Column.ENTRY, entry)
                    .query();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getLocalizedMessage());
            return new ArrayList<>();
        }
    }
}
