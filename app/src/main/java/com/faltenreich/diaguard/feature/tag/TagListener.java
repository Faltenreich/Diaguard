package com.faltenreich.diaguard.feature.tag;

import android.view.View;

import com.faltenreich.diaguard.shared.data.database.entity.Tag;

public interface TagListener {
    void onTagDeleted(Tag tag, View view);
}