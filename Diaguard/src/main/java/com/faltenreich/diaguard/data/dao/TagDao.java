package com.faltenreich.diaguard.data.dao;

import com.faltenreich.diaguard.data.entity.Tag;
import com.faltenreich.diaguard.util.StringUtils;

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

    @Override
    public Tag createOrUpdate(Tag tag) {
        if (StringUtils.isBlank(tag.getKey())) {
            tag.setKey(tag.getName());
        }
        return super.createOrUpdate(tag);
    }
}
