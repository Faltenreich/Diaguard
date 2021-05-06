package com.faltenreich.diaguard.shared.data.database.dao;

import android.util.Log;

import androidx.annotation.Nullable;

import com.faltenreich.diaguard.shared.data.database.entity.BaseEntity;
import com.faltenreich.diaguard.shared.data.database.entity.Tag;

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

    @Override
    public List<Tag> getAll() {
        try {
            return getQueryBuilder().orderBy(BaseEntity.Column.UPDATED_AT, false).query();
        } catch (SQLException exception) {
            Log.e(TAG, exception.toString());
            return new ArrayList<>();
        }
    }

    @Nullable
    public Tag getByName(String name) {
        try {
            return getQueryBuilder().where().eq(Tag.Column.NAME, name).queryForFirst();
        } catch (SQLException exception) {
            Log.e(TAG, exception.toString());
            return null;
        }
    }
}
