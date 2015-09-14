package com.faltenreich.diaguard.data.dao;

import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.j256.ormlite.dao.Dao;

/**
 * Created by Faltenreich on 05.09.2015.
 */
public class EntryDao extends BaseDao<Entry> {

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

}