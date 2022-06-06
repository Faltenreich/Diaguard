package com.faltenreich.diaguard.shared.data.database.dao;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.faltenreich.diaguard.shared.data.database.Database;
import com.faltenreich.diaguard.shared.data.database.entity.BaseEntity;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.DeleteBuilder;
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
public abstract class BaseDao<T extends BaseEntity> {

    private static final String TAG = BaseDao.class.getSimpleName();

    public static final long PAGE_SIZE = 50;

    private final Class<T> clazz;

    public BaseDao(Class<T> clazz) {
        this.clazz = clazz;
    }

    public Class<T> getClazz() {
        return clazz;
    }

    @NonNull
    private com.j256.ormlite.dao.Dao<T, Long> getDao() {
        try {
            return Database.getInstance().getDatabaseHelper().getDao(clazz);
        } catch (SQLException exception) {
            throw new IllegalStateException(exception.getMessage());
        }
    }

    @NonNull
    public QueryBuilder<T, Long> getQueryBuilder() {
        return getDao().queryBuilder();
    }

    @NonNull
    public DeleteBuilder<T, Long> getDeleteBuilder() {
        return getDao().deleteBuilder();
    }

    @Nullable
    List<String[]> queryRaw(String query) {
        try {
            GenericRawResults<String[]> rawResults = getDao().queryRaw(query);
            return rawResults.getResults();
        } catch (SQLException exception) {
            Log.e(TAG, exception.toString());
            return null;
        }
    }

    public T getById(long id) {
        try {
            return getQueryBuilder().where().eq(BaseEntity.Column.ID, id).queryForFirst();
        } catch (SQLException exception) {
            Log.e(TAG, exception.toString());
            return null;
        }
    }

    public T get(T object) {
        return getById(object.getId());
    }

    public List<T> getAll() {
        try {
            return getQueryBuilder().query();
        } catch (SQLException exception) {
            Log.e(TAG, exception.toString());
            return new ArrayList<>();
        }
    }

    public Long create(T object) {
        try {
            int id = getDao().create(object);
            object.setId(id);
            return (long) id;
        } catch (SQLException exception) {
            Log.e(TAG, exception.toString());
            return null;
        }
    }

    public void update(T object) {
        try {
            getDao().update(object);
        } catch (SQLException exception) {
            Log.e(TAG, exception.toString());
        }
    }

    public T createOrUpdate(T object) {
        DateTime now = DateTime.now();
        T existingObject = get(object);
        if (existingObject != null) {
            object.setUpdatedAt(now);
            update(object);
        } else {
            object.setCreatedAt(now);
            object.setUpdatedAt(now);
            create(object);
        }
        return object;
    }

    public void createOrUpdate(final List<T> objects) {
        if (objects != null && objects.size() > 0) {
            try {
                getDao().callBatchTasks((Callable<Void>) () -> {
                    for (T object : objects) {
                        createOrUpdate(object);
                    }
                    return null;
                });
            } catch (Exception exception) {
                Log.e(TAG, exception.toString());
            }
        }
    }

    public void deleteAll() {
        try {
            TableUtils.clearTable(Database.getInstance().getDatabaseHelper().getConnectionSource(), clazz);
        } catch (SQLException exception) {
            Log.e(TAG, exception.toString());
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
            Log.e(TAG, exception.toString());
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
            Log.e(TAG, exception.toString());
            return 0;
        }
    }
}