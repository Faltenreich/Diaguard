package com.faltenreich.diaguard.feature.export.job.pdf.print;

import androidx.annotation.Nullable;

import com.faltenreich.diaguard.feature.datetime.DateTimeUtils;
import com.faltenreich.diaguard.feature.export.job.pdf.meta.PdfExportCache;
import com.faltenreich.diaguard.shared.data.database.dao.EntryDao;

import org.joda.time.DateTime;

public class PdfPageFactory {

    @Nullable
    public static PdfPage createPage(PdfExportCache cache) throws Exception {
        DateTime weekStart = DateTimeUtils.atStartOfDay(cache.getDateTime());
        DateTime weekEnd = DateTimeUtils.atEndOfWeek(weekStart);
        boolean weekHasEntries = EntryDao.getInstance().count(weekStart, weekEnd) > 0;
        boolean exportWeek = weekHasEntries || !cache.getConfig().skipEmptyDays();
        return exportWeek ? new PdfPage(cache) : null;
    }
}
