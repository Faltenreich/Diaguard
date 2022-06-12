package com.faltenreich.diaguard.feature.export.job.pdf.print;

import androidx.annotation.Nullable;

import com.faltenreich.diaguard.feature.datetime.DateTimeUtils;
import com.faltenreich.diaguard.feature.export.job.pdf.meta.PdfExportCache;
import com.faltenreich.diaguard.shared.data.repository.EntryRepository;

import org.joda.time.DateTime;
import org.joda.time.Interval;

public class PdfPageFactory {

    @Nullable
    public static PdfPage createPage(PdfExportCache cache) throws Exception {
        DateTime weekStart = DateTimeUtils.atStartOfWeek(cache.getDateTime());
        DateTime weekEnd = DateTimeUtils.atEndOfWeek(weekStart);
        Interval interval = new Interval(weekStart, weekEnd);
        boolean weekHasEntries = EntryRepository.getInstance().countBetween(interval) > 0;
        boolean exportWeek = weekHasEntries || !cache.getConfig().skipEmptyDays();
        return exportWeek ? new PdfPage(cache) : null;
    }
}
