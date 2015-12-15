package com.faltenreich.diaguard.data.dao;

import android.util.Log;

import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.data.DatabaseHelper;
import com.faltenreich.diaguard.data.entity.BaseEntity;
import com.faltenreich.diaguard.data.entity.Meal;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;

import org.joda.time.DateTime;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Faltenreich on 05.09.2015.
 */
public abstract class BaseDao <T extends BaseEntity> {

    private static final String TAG = BaseDao.class.getSimpleName();

    private DatabaseHelper databaseHelper;
    private Class<T> clazz;

    protected BaseDao(Class<T> clazz) {
        this.databaseHelper = OpenHelperManager.getHelper(DiaguardApplication.getContext(), DatabaseHelper.class);
        this.clazz = clazz;
    }

    protected Dao<T, Long> getDao() {
        try {
            return databaseHelper.getDao(clazz);
        } catch (SQLException exception) {
            Log.e(TAG, String.format("Could not retrieve Dao of class %s", clazz.getSimpleName()));
            return null;
        }
    }

    protected Class<T> getClazz() {
        return clazz;
    }

    public T get(long id) {
        try {
            return getDao().queryBuilder().where().eq(BaseEntity.Column.ID, id).queryForFirst();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getMessage());
            return null;
        }
    }

    public List<T> getAll() {
        try {
            return getDao().queryBuilder().query();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getMessage());
            return null;
        }
    }

    public T createOrUpdate(T object) {
        try {
            DateTime now = DateTime.now();
            T existingObject = getDao().queryBuilder().where().eq(BaseEntity.Column.ID, object.getId()).queryForFirst();
            if (existingObject != null) {
                object.setUpdatedAt(now);
                getDao().update(object);
            } else {
                object.setCreatedAt(now);
                object.setUpdatedAt(now);
                getDao().create(object);
            }
            return object;
        } catch (SQLException exception) {
            Log.e(TAG, String.format("Could not createOrUpdate %s", clazz.getSimpleName()));
            return null;
        }
    }

    public int delete(List<T> objects) {
        try {
            return getDao().delete(objects);
        } catch (SQLException exception) {
            Log.e(TAG, "Could not delete list of objects");
            return 0;
        }
    }

    public int delete(T object) {
        try {
            return getDao().delete(object);
        } catch (SQLException exception) {
            Log.e(TAG, String.format("Could not delete %s with id %d", clazz.getSimpleName(), object.getId()));
            return 0;
        }
    }
}