package com.faltenreich.diaguard.feature.dashboard.value;

import androidx.annotation.Nullable;

import com.faltenreich.diaguard.shared.data.database.entity.Entry;

public interface DashboardValue {

    String getKey();
    String getValue();
    @Nullable default Entry getEntry() { return null; }
}
