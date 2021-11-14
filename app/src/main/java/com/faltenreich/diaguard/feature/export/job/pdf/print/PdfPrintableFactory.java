package com.faltenreich.diaguard.feature.export.job.pdf.print;

import androidx.annotation.Nullable;

import com.faltenreich.diaguard.feature.export.job.pdf.meta.PdfExportCache;
import com.faltenreich.diaguard.feature.export.job.pdf.meta.PdfExportStyle;
import com.faltenreich.diaguard.shared.data.database.dao.EntryDao;
import com.faltenreich.diaguard.shared.data.database.entity.Entry;

import java.util.List;

public class PdfPrintableFactory {

    @Nullable
    public static PdfPrintable createPrintable(PdfExportCache cache) {
        List<Entry> entriesOfDay = EntryDao.getInstance().getEntriesOfDay(cache.getDateTime());
        boolean exportDay = !entriesOfDay.isEmpty() || cache.getConfig().isExportEmptyDays();
        if (exportDay) {
            PdfExportStyle style = cache.getConfig().getStyle();
            switch (style) {
                case TABLE:
                    return new PdfTable(cache, entriesOfDay);
                case LOG:
                    return new PdfLog(cache, entriesOfDay);
                case TIMELINE:
                    return new PdfTimeline(cache, entriesOfDay);
                default:
                    throw new IllegalArgumentException("Unsupported style: " + style);
            }
        }
        return null;
    }
}
