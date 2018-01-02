package com.faltenreich.diaguard.data.dao;

import com.faltenreich.diaguard.data.entity.Tag;

public class TagDao extends BaseDao<Tag> {

    private static TagDao instance;

    public static TagDao getInstance() {
        if (instance == null) {
            instance = new TagDao();
        }
        return instance;
    }

    private TagDao() {
        super(Tag.class);
    }
}
