package com.faltenreich.diaguard.feature.export.job.pdf.print;

import androidx.annotation.NonNull;

import com.faltenreich.diaguard.feature.export.job.pdf.meta.PdfExportCache;

public class PdfPrintableFactory {

    @NonNull
    public static PdfPrintable createPrintable(PdfExportCache cache, PdfCellFactory cellFactory) {
        switch (cache.getConfig().getStyle()) {
            case TABLE: return new PdfTable(cache, cellFactory);
            case LOG: return new PdfLog(cache, cellFactory);
            case TIMELINE: return new PdfTimeline(cache, cellFactory);
            default: throw new IllegalArgumentException();
        }
    }
}
