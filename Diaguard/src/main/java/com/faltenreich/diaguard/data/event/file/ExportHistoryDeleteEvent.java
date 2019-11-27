package com.faltenreich.diaguard.data.event.file;

import com.faltenreich.diaguard.data.event.BaseContextEvent;
import com.faltenreich.diaguard.ui.list.item.ListItemExportHistory;

public class ExportHistoryDeleteEvent extends BaseContextEvent<ListItemExportHistory> {

    public ExportHistoryDeleteEvent(ListItemExportHistory item) {
        super(item);
    }
}
