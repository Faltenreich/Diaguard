package com.faltenreich.diaguard.event.data;

import com.faltenreich.diaguard.data.entity.BaseEntity;
import com.faltenreich.diaguard.event.BaseContextEvent;

/**
 * Created by Faltenreich on 23.03.2016.
 */
public abstract class BaseDataEvent <T extends BaseEntity> extends BaseContextEvent <T> {

    public BaseDataEvent(T entity) {
        super(entity);
    }
}
