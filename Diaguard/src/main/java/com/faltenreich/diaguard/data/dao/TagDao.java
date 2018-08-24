package com.faltenreich.diaguard.data.dao;

import android.support.annotation.Nullable;
import android.util.Log;

import com.faltenreich.diaguard.data.entity.BaseEntity;
import com.faltenreich.diaguard.data.entity.Tag;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TagDao extends BaseDao<Tag> {

    private static final String TAG = TagDao.class.getSimpleName();

    private static TagDao instance;

    public static TagDao getInstance() {
        if (instance == null) {
            instance = new TagDao();
        }
        return instance;
    }

    private TagDao() {
        super(Tag.class);
    }

    public List<Tag> getRecent() {
        try {
            return getDao().queryBuilder().orderBy(BaseEntity.Column.UPDATED_AT, false).limit(20L).query();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getMessage());
            return new ArrayList<>();
        }
    }

    @Nullable
    public Tag getByName(String name) {
        try {
            return getDao().queryBuilder().where().eq(Tag.Column.NAME, name).queryForFirst();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getMessage());
            return null;
        }
    }
}
