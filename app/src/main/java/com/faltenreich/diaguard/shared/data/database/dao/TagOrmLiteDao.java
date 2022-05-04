package com.faltenreich.diaguard.shared.data.database.dao;

import android.util.Log;

import androidx.annotation.Nullable;

import com.faltenreich.diaguard.shared.data.database.entity.BaseEntity;
import com.faltenreich.diaguard.shared.data.database.entity.Tag;
import com.j256.ormlite.stmt.SelectArg;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TagOrmLiteDao extends BaseDao<Tag> implements TagDao {

    private static final String TAG = TagOrmLiteDao.class.getSimpleName();

    private static TagOrmLiteDao instance;

    public static TagOrmLiteDao getInstance() {
        if (instance == null) {
            instance = new TagOrmLiteDao();
        }
        return instance;
    }

    private TagOrmLiteDao() {
        super(Tag.class);
    }

    @Override
    public List<Tag> getAll() {
        try {
            return getQueryBuilder()
                .orderBy(BaseEntity.Column.UPDATED_AT, false)
                .query();
        } catch (SQLException exception) {
            Log.e(TAG, exception.toString());
            return new ArrayList<>();
        }
    }

    @Nullable
    public Tag getByName(String name) {
        try {
            return getQueryBuilder().where()
                .eq(Tag.Column.NAME, new SelectArg(name))
                .queryForFirst();
        } catch (SQLException exception) {
            Log.e(TAG, exception.toString());
            return null;
        }
    }
}
