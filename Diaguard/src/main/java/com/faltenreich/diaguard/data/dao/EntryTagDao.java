package com.faltenreich.diaguard.data.dao;

import com.faltenreich.diaguard.data.entity.EntryTag;

public class EntryTagDao extends BaseDao<EntryTag> {

    private static EntryTagDao instance;

    public static EntryTagDao getInstance() {
        if (instance == null) {
            instance = new EntryTagDao();
        }
        return instance;
    }

    private EntryTagDao() {
        super(EntryTag.class);
    }
}
