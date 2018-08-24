package com.faltenreich.diaguard.data.dao;

import android.util.Log;

import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.data.DatabaseHelper;
import com.faltenreich.diaguard.data.entity.BaseEntity;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.TableUtils;

import org.joda.time.DateTime;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Faltenreich on 05.09.2015.
 */
public abstract class BaseDao <T extends BaseEntity> {

    private static final String TAG = BaseDao.class.getSimpleName();

    static final long PAGE_SIZE = 50;

    private DatabaseHelper databaseHelper;
    private Class<T> clazz;

    BaseDao(Class<T> clazz) {
        this.databaseHelper = OpenHelperManager.getHelper(DiaguardApplication.getContext(), DatabaseHelper.class);
        this.clazz = clazz;
    }

    protected Dao<T, Long> getDao() {
        try {
            return databaseHelper.getDao(clazz);
        } catch (SQLException exception) {
            Log.e(TAG, exception.getMessage());
            return null;
        }
    }

    QueryBuilder<T, Long> getQueryBuilder() {
        return getDao().queryBuilder();
    }

    Class<T> getClazz() {
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
            return new ArrayList<>();
        }
    }

    public long countAll() {
        try {
            return getDao().queryBuilder().countOf();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getMessage());
            return 0;
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
            Log.e(TAG, exception.getMessage());
            return null;
        }
    }

    public void bulkCreateOrUpdate(final List<T> objects) {
        if (objects != null && objects.size() > 0) {
            try {
                getDao().callBatchTasks(new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        for (T object : objects) {
                            createOrUpdate(object);
                        }
                        return null;
                    }
                });
            } catch (Exception exception) {
                Log.e(TAG, exception.getLocalizedMessage());
            }
        }
    }

    public void deleteAll() {
        try {
            TableUtils.clearTable(databaseHelper.getConnectionSource(), clazz);
        } catch (SQLException exception) {
            Log.e(TAG, exception.getMessage());
        }
    }

    public int delete(List<T> objects) {
        try {
            if (objects != null && objects.size() > 0) {
                return getDao().delete(objects);
            } else {
                return 0;
            }
        } catch (SQLException exception) {
            Log.e(TAG, exception.getMessage());
            return 0;
        }
    }

    public int delete(T object) {
        try {
            if (object != null) {
                return getDao().delete(object);
            } else {
                return 0;
            }
        } catch (SQLException exception) {
            Log.e(TAG, exception.getMessage());
            return 0;
        }
    }
}