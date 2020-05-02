package com.faltenreich.diaguard.shared.event.data;

import com.faltenreich.diaguard.shared.data.database.entity.BaseEntity;
import com.faltenreich.diaguard.shared.event.BaseContextEvent;

/**
 * Created by Faltenreich on 23.03.2016.
 */
public abstract class BaseDataEvent <T extends BaseEntity> extends BaseContextEvent <T> {

    public BaseDataEvent(T entity) {
        super(entity);
    }
}
