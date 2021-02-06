package com.faltenreich.diaguard.shared.data.database.factory;

import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Measurement;

import java.lang.reflect.Constructor;

public class MeasurementFactory {

    public static <MEASUREMENT extends Measurement> MEASUREMENT createFromCategory(Category category) {
        try {
            Class<MEASUREMENT> clazz = category.toClass();
            Constructor<MEASUREMENT> constructor = clazz.getConstructor();
            return constructor.newInstance();
        } catch (Exception exception) {
            throw new IllegalArgumentException("Failed to instantiate measurement for category " + category.toString());
        }
    }
}
