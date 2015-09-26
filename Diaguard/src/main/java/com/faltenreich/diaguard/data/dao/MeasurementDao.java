package com.faltenreich.diaguard.data.dao;

import android.util.Log;

import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.Measurement;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Faltenreich on 06.09.2015.
 */
public class MeasurementDao <M extends Measurement> extends BaseDao<M> {

    private static HashMap<Class, MeasurementDao> instances;

    public static <M extends Measurement> MeasurementDao getInstance(Class<M> clazz) {
        if (instances == null) {
            instances = new HashMap<>();
        }
        MeasurementDao measurementDao = instances.get(clazz);
        if (measurementDao == null) {
            instances.put(clazz, new MeasurementDao<>(clazz));
            measurementDao = instances.get(clazz);
        }
        return measurementDao;
    }

    private MeasurementDao(Class<M> clazz) {
        super(clazz);
    }
}
