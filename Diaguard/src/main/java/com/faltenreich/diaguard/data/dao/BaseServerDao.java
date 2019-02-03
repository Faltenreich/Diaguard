package com.faltenreich.diaguard.data.dao;

import android.util.Log;

import com.faltenreich.diaguard.data.entity.BaseEntity;
import com.faltenreich.diaguard.data.entity.BaseServerEntity;

import org.joda.time.DateTime;

import java.sql.SQLException;

import static android.R.attr.id;

/**
 * Created by Faltenreich on 25.09.2016.
 */

class BaseServerDao <T extends BaseServerEntity> extends BaseDao<T> {

    private static final String TAG = BaseServerDao.class.getSimpleName();

    BaseServerDao(Class<T> clazz) {
        super(clazz);
    }

    T getByServerId(String serverId) {
        try {
            return getDao().queryBuilder().where().eq(BaseServerEntity.Column.SERVER_ID, serverId).queryForFirst();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getMessage());
            return null;
        }
    }

    @Override
    public T get(T object) {
        return object.getServerId() != null ? getByServerId(object.getServerId()) : super.get(object);
    }

    public void softDelete(T entity) {
        entity.setDeletedAt(DateTime.now());
        createOrUpdate(entity);
    }
}
