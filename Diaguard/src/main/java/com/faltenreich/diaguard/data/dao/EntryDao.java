package com.faltenreich.diaguard.data.dao;

import android.util.Log;

import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Faltenreich on 05.09.2015.
 */
public class EntryDao extends BaseDao<Entry> {

    private static final String TAG = EntryDao.class.getSimpleName();

    private static EntryDao instance;

    public static EntryDao getInstance() {
        if (instance == null) {
            instance = new EntryDao();
        }
        return instance;
    }

    private EntryDao() {
        super(Entry.class);
    }

    public int deleteMeasurements(Entry entry) {
        for (Measurement.Category category : Measurement.Category.values()) {

        }
        return 0;
    }

    public List<Measurement> getMeasurements(Entry entry, Measurement.Category[] categories) {
        List<Measurement> measurements = new ArrayList<>();
        for (Measurement.Category category : categories) {
            /*
            try {
                measurements.addAll(getDao().queryBuilder().where().eq(Measurement.ENTRY_ID, entry.getId()).query());
            } catch (SQLException exception) {
                Log.e(TAG, String.format("Could not fetch measurements of category '%s'", category.toString()));
            }
            */
        }
        return measurements;
    }

}