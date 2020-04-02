package com.faltenreich.diaguard.shared.event.file;

import com.faltenreich.diaguard.shared.event.BaseContextEvent;
import com.faltenreich.diaguard.feature.export.history.ExportHistoryListItem;

public class ExportHistoryDeleteEvent extends BaseContextEvent<ExportHistoryListItem> {

    public ExportHistoryDeleteEvent(ExportHistoryListItem item) {
        super(item);
    }
}
